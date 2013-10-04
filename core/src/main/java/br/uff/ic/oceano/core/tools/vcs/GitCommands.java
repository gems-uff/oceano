/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.vcs;

import br.uff.ic.oceano.core.tools.vcs.PathModel.PathChangeModel;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.UnmergedPathException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

/**
 *
 * @author Gleiph, João Felipe
 */
public class GitCommands {

    private FileRepositoryBuilder builder;
    private Repository repository;
    private Git git;

    public GitCommands(String path) throws IOException {
        builder = new FileRepositoryBuilder();
        repository = builder.setGitDir(new File(path + "/.git")).readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();
        git = new Git(repository);
    }

    public void create() throws IOException {
        repository.create();
    }

    public void add(List<String> files) throws NoFilepatternException {

        AddCommand add = git.add();

        for (String file : files) {
            add.addFilepattern(file);
        }
        add.call();
    }

    public void commit(String message)
            throws NoHeadException, NoMessageException, UnmergedPathException, ConcurrentRefUpdateException, JGitInternalException,
            WrongRepositoryStateException {
        CommitCommand commit = git.commit();
        RevCommit revision = commit.setMessage(message).call();
        System.out.println(revision.getShortMessage());
    }

    public void addingCommit(String message)
            throws NoFilepatternException, NoHeadException, NoMessageException, UnmergedPathException, ConcurrentRefUpdateException,
            JGitInternalException, WrongRepositoryStateException {
        add(Arrays.asList("."));
        commit(message);
    }

    public void checkout(String developmentLine, boolean branch) throws JGitInternalException, InvalidRefNameException, RefAlreadyExistsException, RefNotFoundException {
        CheckoutCommand co = git.checkout();
        co.setName(developmentLine);        
        co.setCreateBranch(branch);
        co.call();

    }

    public void checkoutRevision(String revision) throws JGitInternalException, InvalidRefNameException, RefAlreadyExistsException, RefNotFoundException {
        checkout(revision, false);
    }

    public void checkoutDevelopmentLine(String developmentLine) throws JGitInternalException, InvalidRefNameException, RefAlreadyExistsException, RefNotFoundException {
        checkout(developmentLine, false);
    }

    public void branch(String name) throws JGitInternalException, InvalidRefNameException, RefAlreadyExistsException, RefNotFoundException {
        checkout(name, true);
    }

    //Missing commands:
    // *Push
    // *Pull
    // *Clone
    
    public void pull(String username, String password) 
            throws WrongRepositoryStateException, InvalidConfigurationException, DetachedHeadException, InvalidRemoteException, 
            CanceledException, RefNotFoundException, NoHeadException {

        CredentialsProvider authentication = new UsernamePasswordCredentialsProvider(username, password);
        
        PullCommand pull = git.pull();
//        pull.setCredentialsProvider(authentication);

        pull.call();
        


    }
    
    /**
	 * Returns a list of references in the repository matching "refs". If the
	 * repository is null or empty, an empty list is returned.
	 * 
	 * @param repository
	 * @param refs
	 *            if unspecified, all refs are returned
	 * @param fullName
	 *            if true, /refs/something/yadayadayada is returned. If false,
	 *            yadayadayada is returned.
	 * @param maxCount
	 *            if < 0, all references are returned
	 * @return list of references
	 */
	private List<RefModel> getRefs(Repository repository, String refs, boolean fullName,
			int maxCount) {
		List<RefModel> list = new ArrayList<RefModel>();
		if (maxCount == 0) {
			return list;
		}
		if (!hasCommits(repository)) {
			return list;
		}
		try {
			Map<String, Ref> map = repository.getRefDatabase().getRefs(refs);
			RevWalk rw = new RevWalk(repository);
			for (Entry<String, Ref> entry : map.entrySet()) {
				Ref ref = entry.getValue();
				RevObject object = rw.parseAny(ref.getObjectId());
				String name = entry.getKey();
				if (fullName && !refs.equals("")) {
					name = refs + name;
				}
				list.add(new RefModel(name, ref, object));
			}
			rw.dispose();
			Collections.sort(list);
			Collections.reverse(list);
			if (maxCount > 0 && list.size() > maxCount) {
				list = new ArrayList<RefModel>(list.subList(0, maxCount));
			}
		} catch (IOException e) {
			System.err.println("Repository {0} failed to retrieve {1}" + refs);
		}
		return list;
	}
    
    /**
	 * Returns the list of local branches in the repository. If repository does
	 * not exist or is empty, an empty list is returned.
	 * 
	 * @param repository
	 * @param fullName
	 *            if true, /refs/heads/yadayadayada is returned. If false,
	 *            yadayadayada is returned.
	 * @param maxCount
	 *            if < 0, all local branches are returned
	 * @return list of local branches
	 */
	private List<RefModel> getLocalBranches(Repository repository, boolean fullName,
			int maxCount) {
		return getRefs(repository, Constants.R_HEADS, fullName, maxCount);
	}
    
    /**
	 * Returns the default branch to use for a repository. Normally returns
	 * whatever branch HEAD points to, but if HEAD points to nothing it returns
	 * the most recently updated branch.
	 * 
	 * @param repository
	 * @return the objectid of a branch
	 * @throws Exception
	 */
	private ObjectId getDefaultBranch(Repository repository) throws Exception {
		ObjectId object = repository.resolve(Constants.HEAD);
		if (object == null) {
			// no HEAD
			// perhaps non-standard repository, try local branches
			List<RefModel> branchModels = getLocalBranches(repository, true, -1);
			if (branchModels.size() > 0) {
				// use most recently updated branch
				RefModel branch = null;
				Date lastDate = new Date(0);
				for (RefModel branchModel : branchModels) {
					if (branchModel.getDate().after(lastDate)) {
						branch = branchModel;
						lastDate = branch.getDate();
					}
				}
				object = branch.getReferencedObjectId();
			}
		}
		return object;
	}
    
    private boolean hasCommits(Repository repository) {
            if (repository != null && repository.getDirectory().exists()) {
                    return (new File(repository.getDirectory(), "objects").list().length > 2)
                                    || (new File(repository.getDirectory(), "objects/pack").list().length > 0);
            }
            return false;
    }
    
    /**
	 * Returns the list of files changed in a specified commit. If the
	 * repository does not exist or is empty, an empty list is returned.
	 * 
	 * @param repository
	 * @param commit
	 *            if null, HEAD is assumed.
	 * @return list of files changed in a commit
	 */
	public List<PathChangeModel> getFilesInCommit(RevCommit commit) {
		List<PathChangeModel> list = new ArrayList<PathChangeModel>();
		if (!hasCommits(repository)) {
			return list;
		}
		RevWalk rw = new RevWalk(repository);
		try {
			if (commit == null) {
				ObjectId object = getDefaultBranch(repository);
				commit = rw.parseCommit(object);
			}

			if (commit.getParentCount() == 0) {
				TreeWalk tw = new TreeWalk(repository);
				tw.reset();
				tw.setRecursive(true);
				tw.addTree(commit.getTree());
				while (tw.next()) {
					list.add(new PathChangeModel(tw.getPathString(), tw.getPathString(), 0, tw
							.getRawMode(0), commit.getId().getName(), ChangeType.ADD));
				}
				tw.release();
			} else {
				RevCommit parent = rw.parseCommit(commit.getParent(0).getId());
				DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
				df.setRepository(repository);
				df.setDiffComparator(RawTextComparator.DEFAULT);
				df.setDetectRenames(true);
				List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
				for (DiffEntry diff : diffs) {
					if (diff.getChangeType().equals(ChangeType.DELETE)) {
						list.add(new PathChangeModel(diff.getOldPath(), diff.getOldPath(), 0, diff
								.getNewMode().getBits(), commit.getId().getName(), diff
								.getChangeType()));
					} else if (diff.getChangeType().equals(ChangeType.RENAME)) {
						list.add(new PathChangeModel(diff.getOldPath(), diff.getNewPath(), 0, diff
								.getNewMode().getBits(), commit.getId().getName(), diff
								.getChangeType()));
					} else {
						list.add(new PathChangeModel(diff.getNewPath(), diff.getNewPath(), 0, diff
								.getNewMode().getBits(), commit.getId().getName(), diff
								.getChangeType()));
					}
				}
			}
		} catch (Throwable t) {
			System.err.println("Repository failed to determine files in commit!");
		} finally {
			rw.dispose();
		}
		return list;
	}    
    
    public Iterator<RevCommit> log() throws NoHeadException, MissingObjectException, IncorrectObjectTypeException, IOException
    {
        Iterable<RevCommit> log = git.log().call();
        return log.iterator();
    }
    
}

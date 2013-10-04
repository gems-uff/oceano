/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service.traceability;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.core.service.vcs.VCSService;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import com.google.common.collect.SortedArraySet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author DanCastellani
 */
public class TraceabilityService implements PersistenceService {
    //CONSTANTS

    private static final String[] REFERENCE_TAGS = {"REFS"};
    private static final String[] CLOSE_TAGS = {"CLOSES"};
    private static final List<String> ALL_TAGS = new ArrayList<String>();
    private static final String TICKET_NUMBER_REFERENCE = "#";
    private static final String HAS_SYMBOL = "x";
    //Control
    private VCSService vcsService;
    private Set<String> tickets;
    private Set<String> files;
    private Map<String, List<String>> ticketFilesMap;

    public void setup() {
        vcsService = ObjectFactory.getObjectWithDataBaseDependencies(VCSService.class);
    }

    public TraceabilityService() {
    }

    private void init() {
        tickets = new SortedArraySet<String>();
        files = new SortedArraySet<String>();
        ticketFilesMap = new HashMap<String, List<String>>();

        ALL_TAGS.addAll(Arrays.asList(CLOSE_TAGS));
        ALL_TAGS.addAll(Arrays.asList(REFERENCE_TAGS));

    }

    public String[][] buildTraceabilityMatrix(SoftwareProject project, ProjectUser pu) throws ServiceException {
        init();
        Set<Revision> revisions;
        try {
            System.out.println("Recovering revisions...");
            revisions = vcsService.getRevisions(project, pu);
            System.out.println("ok");
        } catch (VCSException ex) {
            System.out.println("error");
            throw new ServiceException(ex);
        }

        System.out.println("Processing revisions");
        for (Revision revision : revisions) {
            System.out.println("revision = " + revision);

            final Collection<String> referedTickets = getReferedTickets(revision.getMessage());
            if (referedTickets.size() > 0) {
                List<String> changedFiles = new LinkedList<String>();
                for (VersionedItem versionedItem : revision.getChangedFilesAsList()) {
                    changedFiles.add(versionedItem.getItem().getPath());
                }
                addTicketsAndFilesToMaps(referedTickets, changedFiles);
            }
        }

        System.out.println("Building matrix");
        String[][] traceabilityMatrix = new String[files.size() + 1][tickets.size() + 1];
        Map<String, Integer> ticketIndices = new HashMap<String, Integer>();
        Map<String, Integer> fileIndices = new HashMap<String, Integer>();

        initMatrixHeaders(traceabilityMatrix, ticketIndices, fileIndices);
        if (ticketFilesMap != null && !ticketFilesMap.isEmpty()) {
            for (String ticket : ticketFilesMap.keySet()) {
                int ticketIndice = ticketIndices.get(ticket);

                if (ticketFilesMap.get(ticket) != null && !ticketFilesMap.get(ticket).isEmpty()) {
                    for (String file : ticketFilesMap.get(ticket)) {
                        int fileIndex = fileIndices.get(file);

                        if (traceabilityMatrix[fileIndex][ticketIndice] == null) {
                            traceabilityMatrix[fileIndex][ticketIndice] = HAS_SYMBOL;
                        }
                    }
                }
            }
        }
        System.out.println("done");
        return traceabilityMatrix;
    }

    private boolean containsIgnoringCase(String word, List<String> words) {
        for (String string : words) {
            if (string.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }

    private Collection<String> getReferedTickets(final String message) throws ServiceException {
        try {
            Set<String> referedTickets = new HashSet<String>();

            String[] messageWords = message.split(" ");

            for (int i = 0; i < messageWords.length;) {
                String word = messageWords[i++];

                if (containsIgnoringCase(word, ALL_TAGS)) {
                    word = messageWords[i++];

                    if (word.contains(TICKET_NUMBER_REFERENCE)) {
                        if (word.length() == 1) {
                            word = messageWords[i++];
                        } else {
                            word = word.substring(1);
                        }
                        String number = "";
                        do {
                            number += word.charAt(0);
                            word = word.substring(1);
                        } while (number.matches("\\d*") && !word.isEmpty());

                        // if it is a number, we dont have to take out the last char
                        if (!number.matches("\\d*")) {
                            number = number.substring(0, number.length() - 1);
                        }

                        String ticket = TICKET_NUMBER_REFERENCE + number;
                        verifyTicket(ticket);
                        System.out.println("    found ticket: " + ticket);
                        referedTickets.add(ticket);
                    }
                }
            }
            return referedTickets;
        } catch (ServiceException ex) {
            throw new ServiceException("Malformed reference message: <" + message + ">");
        }
    }

    private void verifyTicket(String word) throws ServiceException {
        try {
            Integer.parseInt(word.substring(1));
        } catch (NumberFormatException ex) {
            throw new ServiceException();
        }
    }

    private void addTicketsAndFilesToMaps(Collection<String> referedTickets, List<String> referedFiles) {
        //add tickets and related files
        for (String ticket : referedTickets) {
            if (tickets.contains(ticket)) {
                final List<String> listOfFiles = ticketFilesMap.get(ticket);
                listOfFiles.addAll(referedFiles);
                ticketFilesMap.put(ticket, listOfFiles);
            } else {
                tickets.add(ticket);
                ticketFilesMap.put(ticket, referedFiles);
            }
        }
        //add files
        for (String file : referedFiles) {
            if (!files.contains(file)) {
                files.add(file);
            }
        }
    }

    private void initMatrixHeaders(String[][] traceabilityMatrix, Map<String, Integer> ticketIndices, Map<String, Integer> fileIndices) {
        int i = 1;
        for (String file : files) {
            traceabilityMatrix[i][0] = file;
            fileIndices.put(file, i++);
        }
        int j = 1;
        for (String ticket : tickets) {
            traceabilityMatrix[0][j] = ticket;
            ticketIndices.put(ticket, j++);
        }
    }
}

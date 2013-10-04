package br.uff.ic.oceano.util.test;

import br.uff.ic.oceano.util.file.FileUtils;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Daniel
 */
public class DiffUtil {

    public static boolean hasDiff(Set<String> leftFiles, Set<String> rightFiles) throws IOException {
        Iterator<String> itLeft = leftFiles.iterator();
        Iterator<String> itRight = rightFiles.iterator();

        while (itLeft.hasNext()) {
            if (hasDiff(itLeft.next(), itRight.next())) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasDiff(String leftFile, String rightFile) throws IOException {
        List<String> leftLines = FileUtils.getLines(leftFile);
        List<String> righLines = FileUtils.getLines(rightFile);
        if (!leftLines.containsAll(righLines) || !righLines.containsAll(leftLines)) {
            return false;
        }

        Patch patch = DiffUtils.diff(leftLines, righLines);
        List<Delta> deltas = patch.getDeltas();
        return deltas != null ? !deltas.isEmpty() : false;
    }
}

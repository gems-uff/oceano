package br.uff.ic.oceano.core.tools.metrics.extractors.cpp.easycount;

import br.uff.ic.oceano.util.file.FileUtils;

/**
 * Converted to Java from http://www.monperrus.net/martin/easysloc.c.txt original c version
 * @author Daniel
 */
public class EasyCountService {

    //TODO Fix class to extract this metrics
    //TLOC: Total lines of code that will count non-blank and non-comment lines in a compilation unit.
    //MLOC: Method lines of code will counts and sum non-blank and non-comment lines inside method bodies.
    //LOC: Total lines of code that will counts non-blank lines in a compilation unit.


    /**
     * Total of logical lines of code plus comments
     * @param path Path to a file
     * @return
     * @throws EasyCountServiceException
     */
    public long loc(String path) throws EasyCountServiceException{

        final int PREPROCESSING = 8;
	final int CHAR = 9;
	final int COMMENT_LINE = 3;
	final int COMMENT_STAR_OUT = 5;
	final int STRING_SPECIAL = 7;
	final int COMMENT_STAR = 4;
	final int CHAR_SPECIAL = 10;
	final int COMMENT = 1;
	final int NORMAL = 0;
	final int STRING = 6;
	final int COMMENT_IN = 2;

        int state = NORMAL;
        long semicolon = 0;
        long brace = 0;
        long comment_logical = 0;
        long comment_physical = 0;
        long parenthesis_opened = 0;
        long preprocessing = 0;

        String fileText;
        try {
            fileText = FileUtils.readFile(path);
        } catch (Exception ex) {
            throw new EasyCountServiceException(ex);
        }
        //Empty file
        if(fileText== null || fileText.isEmpty()){
            return 0;
        }

        int position = 0;
        do {
            char message = fileText.charAt(position);

            if ((state != STRING) && (state != CHAR)
                    && (state != STRING_SPECIAL) && (state != CHAR_SPECIAL)
                    && (state != PREPROCESSING)
                    && (state != COMMENT_LINE) && (state != COMMENT_STAR)) {
                if (message == ';') {
                    semicolon++;
                }
                if (message == '{') {
                    brace++;
                }
                if (message == '(') {
                    parenthesis_opened++;
                }
            }

            switch (state) {

                case NORMAL:
                    switch (message) {
                        case '/':
                            state = COMMENT_IN;
                            break;
                        case '"':
                            state = STRING;
                            break;
                        case '#':
                            preprocessing++;
                            state = PREPROCESSING;
                            break;
                        case '\'':
                            state = CHAR;
                            break;
                        default:
                            state = NORMAL;
                    }
                    break;

                case PREPROCESSING:
                    switch (message) {
                        case '\n':
                            state = NORMAL;
                            break;
                        default:
                            state = PREPROCESSING;
                    }
                    break;

                case COMMENT_IN:
                    switch (message) {
                        case '/':
                            state = COMMENT_LINE;
                            break;
                        case '*':
                            state = COMMENT_STAR;
                            break;
                        default:
                            state = NORMAL;
                    }
                    break;

                case COMMENT_LINE:
                    switch (message) {
                        case '\n':
                            comment_logical++;
                            comment_physical++;
                            state = NORMAL;
                            break;
                        default:
                            state = COMMENT_LINE;
                    }
                    break;

                case COMMENT_STAR:
                    switch (message) {
                        case '*':
                            state = COMMENT_STAR_OUT;
                            break;
                        case '\n':
                            comment_physical++;
                            break;
                        default:
                            state = COMMENT_STAR;
                    }
                    break;

                case COMMENT_STAR_OUT:
                    switch (message) {
                        case '/':
                            comment_logical++;
                            comment_physical++;
                            state = NORMAL;
                            break;
                        case '\n':
                            comment_physical++;
                            break;
                        case '*':
                            state = COMMENT_STAR_OUT;
                            break;
                        default:
                            state = COMMENT_STAR;
                    }
                    break;


                case STRING:
                    switch (message) {
                        case '\\':
                            state = STRING_SPECIAL;
                            break;
                        case '"':
                            state = NORMAL;
                            break;
                        default:
                            state = STRING;
                    }
                    break;


                case STRING_SPECIAL:
                    switch (message) {
                        default:
                            state = STRING;
                    }
                    break;

                case CHAR:
                    switch (message) {
                        case '\\':
                            state = CHAR_SPECIAL;
                            break;
                        case '\'':
                            state = NORMAL;
                            break;
                        default:
                            state = CHAR;
                    }
                    break;

                case CHAR_SPECIAL:
                    switch (message) {
                        default:
                            state = CHAR;
                    }
                    break;

                default:
                    break;
            }
            //next char position
            position++;
        } while (position != fileText.length());

        return semicolon + comment_physical;
    }    
}

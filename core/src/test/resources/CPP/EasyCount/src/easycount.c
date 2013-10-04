/* easysloc is a C/C++ logical lines of code counter (LLOC).
We chose an intuitive and clear definition of a logical line of code for C/C++ :
the number of instructions with the semantic delimiter ";" (see Fenton 1991, p246-253)

Additionnally easysloc gives you the number of comments.
     &#47;&#47; is one comment
     /* &#42;foo&#42; *&#47; is one logical and physical comment
     /* this is
        another comment *&#47; is one logical comment and two physical comments

 **[[http://www.monperrus.net/martin/easysloc.c.txt|download easysloc]]**


=====Related tools=====
CodeCount / CCCC / LOCC / SLOCCount

Compared to these tools, easysloc satisfies all the following conditions:
 * easysloc gives the number of _logical lines of code_;
 * easysloc provides the definition used for a logical line of code;
 * easysloc's source code can be easily reviewed;
 * easysloc is designed to be integrated via command line interface, see below.

easysloc is able to measure the linux kernel:
 * linux kernel 2.0.27 :   206531  logical lines of code ([[http://www.kernel.org/pub/linux/kernel/v2.0/linux-2.0.27.tar.bz2]])
 * linux kernel 2.6.19 : 1987933  logical lines of code (x 9.6 !)

=====Usage=====

Compilation :
$ gcc -o easysloc easysloc.c

$ cat myfile.c | easysloc

$ cat myfile.cpp | easysloc

$ cat myfile1.c myfile2.c myfile3.c| easysloc

$ for i in *.cpp; do echo -n "$i "; cat $i | easysloc ; done

$ for i in `find . -iname "*.c"`; do echo -n "$i "; cat $i | easysloc ; done

Warning: you should not use these two below because a syntactic error in one file could have hard consequences on others, please use the command line above instead.
$ cat *.cpp | easysloc
$ cat `find . -iname "*c"` | easysloc

= A powerful usage =

# this prints the result for each file and computes the total
$ for i in `find . -iname "*.c"`;
do
  echo -n "$i ";
  cat $i | easysloc ;
done \
| awk 'BEGIN{n1=0;n2=0;} {print $0;n1=n1+$2; n2=n2+$3; } END{print "Total",n1,n2;}' \
| sort -n -k 2

Tailed output for coreutils-6.7:

./src/stty.c 464 151
./src/csplit.c 511 221
./lib/fts.c 516 420
./src/tail.c 530 262
./src/tr.c 621 375
./src/od.c 667 273
./src/ptx.c 670 594
./lib/regex_internal.c 671 181
./src/pr.c 835 850
./src/sort.c 929 432
./lib/getdate.c 973 568
./src/ls.c 1346 723
./lib/regcomp.c 1378 511
./lib/regexec.c 1580 541
Total 31822 21301

Coreutils-6.7 has 31822 logical lines of code and 21301 comments.

(C) 2006-2007 Martin Monperrus
Don't hesitate to contact me
 */
#include <stdio.h>
#include <stddef.h>

#define NORMAL 0
#define COMMENT 1
#define COMMENT_IN 2
#define COMMENT_LINE 3
#define COMMENT_STAR 4
#define COMMENT_STAR_OUT 5
#define STRING 6
#define STRING_SPECIAL 7
#define PREPROCESSING 8
#define CHAR 9
#define CHAR_SPECIAL 10

int main(int argc, char *argv[]) {

    int state = NORMAL;
    long semicolon = 0;
    long brace = 0;
    long comment_logical = 0;
    long comment_physical = 0;
    long parenthesis_opened = 0;
    long preprocessing = 0;
    char message;

    do {
        message = getchar();


        if ((state != STRING) && (state != CHAR)
                && (state != STRING_SPECIAL) && (state != CHAR_SPECIAL)
                && (state != PREPROCESSING)
                && (state != COMMENT_LINE) && (state != COMMENT_STAR)) {
            if (message == ';') semicolon++;
            //if (message==':') semicolon++;
            if (message == '{') brace++;
            if (message == '(') parenthesis_opened++;
        }

        switch (state) {

            case NORMAL:
                switch (message) {
                    case '/': state = COMMENT_IN;
                        break;
                    case '"': state = STRING;
                        break;
                    case '#': preprocessing++;
                        state = PREPROCESSING;
                        break;
                    case '\'': state = CHAR;
                        break;
                    default: state = NORMAL;
                }
                break;

            case PREPROCESSING:
                switch (message) {
                    case '\n': state = NORMAL;
                        break;
                    default: state = PREPROCESSING;
                }
                break;

            case COMMENT_IN:
                switch (message) {
                    case '/': state = COMMENT_LINE;
                        break;
                    case '*': state = COMMENT_STAR;
                        break;
                    default: state = NORMAL;
                }
                break;

            case COMMENT_LINE:
                switch (message) {
                    case '\n': comment_logical++;
                        comment_physical++;
                        state = NORMAL;
                        break;
                    default: state = COMMENT_LINE;
                }
                break;

            case COMMENT_STAR:
                switch (message) {
                    case '*': state = COMMENT_STAR_OUT;
                        break;
                    case '\n': comment_physical++;
                        break;
                    default: state = COMMENT_STAR;
                }
                break;

            case COMMENT_STAR_OUT:
                switch (message) {
                    case '/': comment_logical++;
                        comment_physical++;
                        state = NORMAL;
                        break;
                    case '\n': comment_physical++;
                        break;
                    case '*': state = COMMENT_STAR_OUT;
                        break;
                    default: state = COMMENT_STAR;
                }
                break;


            case STRING:
                switch (message) {
                    case '\\': state = STRING_SPECIAL;
                        break;
                    case '"': state = NORMAL;
                        break;
                    default: state = STRING;
                }
                break;


            case STRING_SPECIAL:
                switch (message) {
                    default: state = STRING;
                }
                break;

            case CHAR:
                switch (message) {
                    case '\\': state = CHAR_SPECIAL;
                        break;
                    case '\'': state = NORMAL;
                        break;
                    default: state = CHAR;
                }
                break;

            case CHAR_SPECIAL:
                switch (message) {
                    default: state = CHAR;
                }
                break;

            default: break;
        }
    } while (message != EOF);

    printf("%ld %ld\n", semicolon, comment_physical);

    return 0;
}

/**
 * Adapted from http://www.mrx.net/c/source.html original code.
 * Author: Daniel
 *
 */
char* fread(char* filePath) {

    //Open file stream
    FILE *file = fopen(filePath, "rt");

    //File size
    fseek(file, 0L, SEEK_END); /* Position to end of file */
    long lFileLen = ftell(file); /* Get file length */
    rewind(file); /* Back to start of file */

    //Alloc mem
    char *cFile = calloc(lFileLen + 1, sizeof (char));
    if (cFile == NULL) {
        return NULL;
    }

    //Read file
    fread(cFile, lFileLen, 1, file); /* Read the entire file into cFile */

    //close file stream
    fclose(file);

    return cFile;
}

int loc(int argc, char *argv[]) {

    int state = NORMAL;
    long semicolon = 0;
    long brace = 0;
    long comment_logical = 0;
    long comment_physical = 0;
    long parenthesis_opened = 0;
    long preprocessing = 0;
    char message;

    do {
        message = getchar();


        if ((state != STRING) && (state != CHAR)
                && (state != STRING_SPECIAL) && (state != CHAR_SPECIAL)
                && (state != PREPROCESSING)
                && (state != COMMENT_LINE) && (state != COMMENT_STAR)) {
            if (message == ';') semicolon++;
            //if (message==':') semicolon++;
            if (message == '{') brace++;
            if (message == '(') parenthesis_opened++;
        }

        switch (state) {

            case NORMAL:
                switch (message) {
                    case '/': state = COMMENT_IN;
                        break;
                    case '"': state = STRING;
                        break;
                    case '#': preprocessing++;
                        state = PREPROCESSING;
                        break;
                    case '\'': state = CHAR;
                        break;
                    default: state = NORMAL;
                }
                break;

            case PREPROCESSING:
                switch (message) {
                    case '\n': state = NORMAL;
                        break;
                    default: state = PREPROCESSING;
                }
                break;

            case COMMENT_IN:
                switch (message) {
                    case '/': state = COMMENT_LINE;
                        break;
                    case '*': state = COMMENT_STAR;
                        break;
                    default: state = NORMAL;
                }
                break;

            case COMMENT_LINE:
                switch (message) {
                    case '\n': comment_logical++;
                        comment_physical++;
                        state = NORMAL;
                        break;
                    default: state = COMMENT_LINE;
                }
                break;

            case COMMENT_STAR:
                switch (message) {
                    case '*': state = COMMENT_STAR_OUT;
                        break;
                    case '\n': comment_physical++;
                        break;
                    default: state = COMMENT_STAR;
                }
                break;

            case COMMENT_STAR_OUT:
                switch (message) {
                    case '/': comment_logical++;
                        comment_physical++;
                        state = NORMAL;
                        break;
                    case '\n': comment_physical++;
                        break;
                    case '*': state = COMMENT_STAR_OUT;
                        break;
                    default: state = COMMENT_STAR;
                }
                break;


            case STRING:
                switch (message) {
                    case '\\': state = STRING_SPECIAL;
                        break;
                    case '"': state = NORMAL;
                        break;
                    default: state = STRING;
                }
                break;


            case STRING_SPECIAL:
                switch (message) {
                    default: state = STRING;
                }
                break;

            case CHAR:
                switch (message) {
                    case '\\': state = CHAR_SPECIAL;
                        break;
                    case '\'': state = NORMAL;
                        break;
                    default: state = CHAR;
                }
                break;

            case CHAR_SPECIAL:
                switch (message) {
                    default: state = CHAR;
                }
                break;

            default: break;
        }
    } while (message != EOF);

    printf("%ld %ld\n", semicolon, comment_physical);

    return 0;
}
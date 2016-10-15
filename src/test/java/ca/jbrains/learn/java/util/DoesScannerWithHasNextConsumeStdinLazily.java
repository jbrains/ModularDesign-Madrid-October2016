package ca.jbrains.learn.java.util;

import java.util.Scanner;

/*
* This program provides a manual test for consuming input
* line by line "in real time". Run the program, type some
* text, press ENTER, and it does not necessarily echo
* the input back to `stdout` in real time. In particular,
* it seems to "batch" empty lines and echo them only after
* you type some text on a line.
*
* Try this: run the program, then press ENTER by itself
* a few times, waiting a few seconds after each press.
* Next, type a line of text, then press ENTER once more.
* The program should echo all the empty
* lines (plus the non-empty line of text) at once, rather
* than echoing the empty lines as you go.
*
* By comparison, Scanner.hasNextLine() and BufferedReader
* echo the empty lines "in real time" -- or, at least, in
* close-enough-to-real-time-for-a-human-to-notice.
*
* This makes Scanner.hasNext() UNSUITABLE to use in a
* command-line interface where the user expects a reply
* immediately after typing a command. Even if this approach
* only seems to delay processing empty lines, I see nothing
* that guarantees when it would process an arbitrary line,
* and BufferedReader.lines() appears to offer a similarly-
* convenient API, so I see no obvious benefit in taking the
* risk of implementing a CLI command consumer with
* Scanner.hasNext().
 */

public class DoesScannerWithHasNextConsumeStdinLazily {
    public static void main(String[] args) {
        final Scanner source = new Scanner(System.in);
        while (source.hasNext())
            System.out.println(source.nextLine());
    }
}

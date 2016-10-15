package ca.jbrains.learn.java.util;

import java.util.Scanner;

/*
* This program provides a manual test for consuming input
* line by line "in real time". Run the program, type some
* text, press ENTER, and it should echo the input to
* `stdout` immediately. Type some blank lines (press ENTER
* a few times in a row) and -- as long as you don't go too
* fast -- the program echoes those lines back to you
* right away. This makes Scanner.hasNextLine() suitable
* for a command-line interface where the user expects to
* see an immediate reply to typing a text command.
*
* By comparison, Scanner.hasNext() does not behave this way.
 */
public class DoesScannerWithHasNextLineConsumeStdinLazily {
    public static void main(String[] args) {
        final Scanner source = new Scanner(System.in);
        while (source.hasNextLine())
            System.out.println(source.nextLine());
    }
}

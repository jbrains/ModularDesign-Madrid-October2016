package ca.jbrains.learn.java.util;

import java.util.Scanner;

public class DoesScannerWithHasNextLineConsumeStdinLazily {
    public static void main(String[] args) {
        final Scanner source = new Scanner(System.in);
        while (source.hasNextLine())
            System.out.println(source.nextLine());
    }
}

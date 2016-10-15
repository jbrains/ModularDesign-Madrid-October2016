package ca.jbrains.learn.java.util;

import java.util.Scanner;

public class DoesScannerConsumeStdinLazily {
    public static void main(String[] args) {
        final Scanner source = new Scanner(System.in);
        while (source.hasNext())
            System.out.println(source.nextLine());
    }
}

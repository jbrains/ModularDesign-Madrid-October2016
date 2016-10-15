package ca.jbrains.learn.java.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DoesBufferedReaderConsumeStdinLazily {
    public static void main(String[] args) throws IOException {
        final BufferedReader source = new BufferedReader(
                new InputStreamReader(System.in)
        );
        while (true) System.out.println(source.readLine());
    }
}

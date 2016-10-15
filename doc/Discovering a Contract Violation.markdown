---
title: "Discovering a Contract Violation"
date: 2016-10-15
---

I had am embarrassing situation happen in a training course: I put everything together and It **Didn't** Just Work. I couldn't figure out on stage what had happened, so I'm writing this article to document how I explored the problem.

# The Problem

I test-drove all the pieces of my Point of Sale application's Sell One Item feature, including the component that consumes input from `stdin` and dispatches commands to the Sell One Item Controller, which then sends output to `stdout`. I did all this in Java 8. When I assembled the production application, however, the output did not appear to work… at least not right away.

I tried to scan a product, but the scanner failed. I didn't spend much time investigating the failure; instead, I switched to using the keyboard. I typed a barcode, pressed `ENTER`, then saw no output. I hit the `ENTER` key a few times, then saw no output. I pounded the keyboard to produce an obviously unknown barcode, pressed `ENTER`, then saw a few bits of output all strung together. I guessed immediately that I'd implemented the `Display` interface incorrectly and that it wasn't flushing its output. Sure enough, I had used `System.out.printf()` instead of `System.out.println()` and so I _assumed_ that `println()` flushed the stream after each invocation, whereas `printf()` did not. I quickly tried switching the code to `println()` and that did not appear to change the behavior. I made a lame joke in a pathetic attempt to save face with the audience: _Notice that I'm having problems with the only part that I didn't test-drive!_

# A Possible Cause

My mind kept working on the problem. What did I do differently from previous demonstrations? One difference popped into my head: when consuming input from `stdin`, this code uses `java.util.Scanner` rather than `java.io.BufferedReader`, which I'd typically used in the past. Could it be, then, that `Scanner` and `BufferedReader` don't respect exactly the same contract with respect to the timing of their execution? Could `Scanner` behave more lazily than `BufferedReader`? That would account for the difference in behavior that I saw. With this idea, I had something I could try to test.

# A Minimal Learning Test

I thought about a minimal learning test that would help me explore potential differences in the contract between `Scanner` and `BufferedReader`. I could remove all the behavior in between consuming `stdin` and writing the `stdout` by building a simple Echo program. I would type input on the keyboard, press `ENTER`, then expect to see that input echoed to `stdout`. If `Scanner` behaved lazily compared to `BufferedReader`, then this small program might already expose that difference.

I don't know yet whether automating the test would show the problem, because presumably if we pipe `stdout` to a file, then the operating system would flush the file, and we wouldn't be able to detect this timing difference. We might be forced to run the test manually in order to detect the problem. Moreover, I wonder whether the behavior changes between running the application inside the IDE and as a standalone process? This gives us several things to try.

<aside markdown="1">

Inbox

+   Run Echo programs manually; do we notice a difference?
+   If we pipe `stdout` to a file, do we notice a difference?
+   Can we simulate running the program and typing on the keyboard, so that we can automate this part? Can we examine the output to reproduce the problem?
+   If we run the Echo programs outside IDEA, do they behave differently?

</aside>

## The First Test

I wrote two Echo programs, one using `Scanner` and the other using `BufferedReader`, and then I ran them to look for differences in the behavior.

As I type the first program, I notice that `Scanner` has two methods: `hasNext()` and `hasNextLine()`. I used `hasNext()` during my demo. Could it be that `hasNextLine()` behaves slightly differently?

<aside markdown="1">

Inbox

-   Run Echo programs manually; do we notice a difference?
    -   Use `Scanner` with `hasNext()` and `hasNextLine()`; do they behave differently?
-   If we pipe `stdout` to a file, do we notice a difference?
-   Can we simulate running the program and typing on the keyboard, so that we can automate this part? Can we examine the output to reproduce the problem?
-   If we run the Echo programs outside IDEA, do they behave differently?

</aside>

I wrote the `Scanner` version and the `BufferedReader` version of the Echo program and noticed one difference in the behavior: with the `Scanner` version, empty lines did not echo right away, but rather were echoed only when I typed in a non-empty line. For example, when I typed three empty lines (hit `ENTER` three times in a row), then typed some text on the fourth line, after hitting `ENTER` on the fourth line, the program echoed the three empty lines followed by the fourth, non-empty, line. When I ran the `BufferedReader` version of the Echo program, it behaves the way I expected: it echoed each empty line immediately.

I did use the older `BufferedReader` API to write this version of the program. Since then, someone has added `lines()` which creates a Stream of lines from the `BufferedReader`. I wonder whether this echoes empty lines lazily, so I put that in the inbox.

<aside markdown="1">

Inbox

-   Run Echo programs manually; do we notice a difference?
    -   Use `Scanner` with `hasNext()` and `hasNextLine()`; do they behave differently?
    -   Use `BufferedReader` with `lines()`; does it behave differently?
-   If we pipe `stdout` to a file, do we notice a difference?
-   Can we simulate running the program and typing on the keyboard, so that we can automate this part? Can we examine the output to reproduce the problem?
-   If we run the Echo programs outside IDEA, do they behave differently?

</aside>


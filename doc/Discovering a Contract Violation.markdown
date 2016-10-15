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

## RTFM!

Before proceeding, I thought I should read the documentation for `Scanner` to see whether I should have tried to use it at all for this purpose. The Javadoc clearly reads:

>   A `Scanner` is not safe for multithreaded use without external synchronization.

This seems like a pretty good clue that `Scanner` makes no guarantees about the sequence of its operations, and that we can't justify using it to consume input for an application that cares about the timing of input and output. **It appears that we made an incorrect assumption about the contract of `Scanner`.** Oops. This happens.

Sadly, it never occurred to me that `Scanner` would not work the way I expected, and so it did not occur to me to write a test — nor even read the documentation! — to verify my assumptions. **I ignored a risk and it became a problem.** I made a mistake. Fortunately, wanting to demonstrate the system to The Customer involves integrating everything, and that led to discovering the problem.

## Aha! We _Do_ Need Integrated Tests!

Yes and no: I might have needed an integrated test to discover the problem, but I certainly didn't need to integrate _our application_ to discover the problem. In this case, I took a risk and happened to discover the problem after integrating our application, but that doesn't mean that I needed to integrate our application to discover the problem; it merely means that I took a shortcut, using a class before I understood how it worked, and that led to an integration mistake. I could have discovered the problem without integrating the whole system and in this case I could probably even have discovered the problem without writing even the smallest integrated test. I could have read the manual.

Even if I had read the Javadoc for `Scanner`, however, I might not have noticed this statement about the `Scanner`. Even if I had read that statement, I might not have realized its significance in the context of building this application. Even if I had noticed this statement _and_ realized its significance, I might have needed an integrated test to confirm my suspicion that `Scanner` would not do what we need. This makes sense to me, because it involves the outermost layer of our code integrating with third-party software. The few integrated tests that I write, I write to check exactly and only the last layer of my code that integrates with their code. In this case, an integrated test makes perfect sense to me, and since I don't see how to avoid an integrated test in this case, at least I feel forced to write one where I also consider it reasonable to write one. It could be worse.

## Abandoning `Scanner`

As I read more of the documentation for `Scanner`, I start feeling that I made a poor choice from the beginning. This class seems better suited to parse text and not to chop `stdin` into lines for a command line interface. It really seems like `BufferedReader` fits that job better.

I wanted to use `Scanner` because of the convenient `hasNext()`/`nextLine()` API. Fortunately, `BufferedReader` exposes the lines as a `Stream`, which gives me an API of similar convenience, so I feel satisfied abandoning `Scanner` for this purpose.

## Finishing the Learning Tests

I might as well run the remaining learning tests so that I feel more confident about my now-current understanding of how things work.

First, I try `Scanner` with `hasNextLine()` to see whether that changes the behavior. I'm glad I did this, because `Scanner` with `hasNextLine()` appears to behave exactly as I expect: this version of the program echoes the empty lines right away. In reading the Javadoc for both methods, I don't immediately see a difference; however, `hasNext()` appears to check for "is there another token to consume?" while `hasNextLine()` checks for "is there another line to consume?" Since `Scanner` consumes tokens delimited by whitespace (by default), I surmise — and I'm not going to bother to confirm this now — that `hasNext()` causes the `Scanner` to scan _up to but excluding the newline character_, which results coincidentally (or merely in some way that I don't yet understand) in not quite consuming an empty line. This doesn't adequately explain (to me) the difference in behavior between using `hasNext()` and `hasNextLine()`, but it gives me enough to understand that a difference might exist, and that therefore **I probably never want to use `Scanner.hasNext()` to decide whether there are more lines of input to consume from an input stream.** For now, it suffices to conclude this and go on my way. It means that I _could_ use `Scanner` as long as I remember to use `hasNextLine()`. Since I don't want to have to remember this level of detail to get the desired behavior, I hope that `BufferedReader.lines()` works the way I need it to work for this application! This now becomes the most urgent task.

<aside markdown="1">

Inbox

-   Run Echo programs manually; do we notice a difference?
    -   Use `BufferedReader` with `lines()`; does it behave differently?
-   If we pipe `stdout` to a file, do we notice a difference?
-   Can we simulate running the program and typing on the keyboard, so that we can automate this part? Can we examine the output to reproduce the problem?
-   If we run the Echo programs outside IDEA, do they behave differently?

</aside>

When I run the Echo program with `BufferedReader` using `lines()`, it behaves the way I expect. Sadly, I can't think of how to write a contract test for this behavior, so for now, I have to document my understanding of the contract carefully. I will write explanatory comments in the code until I can figure out how to write an automated test that expresses the notion that `Scanner.hasNext()` might delay consuming certain lines of text, and therefore I can't use it to integrate with an input stream in situations where I need to predict the timing of consuming the text line by line.


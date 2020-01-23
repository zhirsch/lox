package com.zacharyhirsch.lox;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.zacharyhirsch.lox.LineReadingIterator.readLines;

final class Lox {

  static boolean hadError = false;

  public static void main(String[] args) throws IOException {
    if (args.length > 1) {
      System.out.println("Usage: jlox [script]");
      System.exit(64);
    } else if (args.length == 1) {
      runFile(args[0]);
    } else {
      runPrompt();
    }
  }

  private static void runFile(String path) throws IOException {
    run(Files.readString(Paths.get(path), Charset.defaultCharset()));
    if (hadError) {
      System.exit(65);
    }
  }

  private static void runPrompt() {
    for (String line : readLines(System.in)) {
      run(line);
      hadError = false;
    }
  }

  private static void run(String source) {
    for (Token token : new Scanner(source).scanTokens()) {
      System.out.println(token);
    }
  }

  static void error(int line, String message) {
    report(line, "", message);
  }

  private static void report(int line, String where, String message) {
    System.err.println("[line " + line + "] Error" + where + ": " + message);
    hadError = true;
  }
}

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
    Scanner scanner = new Scanner(source);
    Parser parser = new Parser(scanner.scanTokens());
    Expr expression = parser.parse();
    if (hadError) {
      return;
    }
    System.out.println(new AstPrinter().print(expression));
  }

  static void error(int line, String message) {
    report(line, "", message);
  }

  private static void report(int line, String where, String message) {
    System.err.println("[line " + line + "] Error" + where + ": " + message);
    hadError = true;
  }

  static void error(Token token, String message) {
    if (token.type == TokenType.EOF) {
      report(token.line, " at end", message);
    } else {
      report(token.line, " at '" + token.lexeme + "'", message);
    }
  }
}

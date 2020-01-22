package com.zacharyhirsch.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

final class LineReadingIterator implements Iterator<String> {

  private final BufferedReader reader;
  private String line;

  static Iterable<String> readLines(InputStream in) {
    return () -> new LineReadingIterator(new BufferedReader(new InputStreamReader(in)));
  }

  public LineReadingIterator(BufferedReader reader) {
    this.reader = reader;
    this.line = null;
  }

  @Override
  public boolean hasNext() {
    line = readLine();
    return line != null;
  }

  @Override
  public String next() {
    return line;
  }

  private String readLine() {
    System.out.print("> ");
    try {
      return reader.readLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

package com.zacharyhirsch.lox;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

final class Scanner {

  private final String source;
  private final List<Token> tokens = new ArrayList<>();

  public Scanner(String source) {
    this.source = source;
  }

  public ImmutableList<Token> scanTokens() {
    return null;
  }
}

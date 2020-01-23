package com.zacharyhirsch.lox;

import java.util.List;

final class Parser {

  private final List<Token> tokens;
  private int current;

  Parser(List<Token> tokens) {
    this.tokens = tokens;
    this.current = 0;
  }

  private Expr expression() {
    return equality();
  }
}

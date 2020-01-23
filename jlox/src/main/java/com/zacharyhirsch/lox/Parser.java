package com.zacharyhirsch.lox;

import java.util.List;

import static com.zacharyhirsch.lox.TokenType.BANG;
import static com.zacharyhirsch.lox.TokenType.BANG_EQUAL;
import static com.zacharyhirsch.lox.TokenType.EOF;
import static com.zacharyhirsch.lox.TokenType.EQUAL_EQUAL;
import static com.zacharyhirsch.lox.TokenType.GREATER;
import static com.zacharyhirsch.lox.TokenType.GREATER_EQUAL;
import static com.zacharyhirsch.lox.TokenType.LESS;
import static com.zacharyhirsch.lox.TokenType.LESS_EQUAL;
import static com.zacharyhirsch.lox.TokenType.MINUS;
import static com.zacharyhirsch.lox.TokenType.PLUS;
import static com.zacharyhirsch.lox.TokenType.SLASH;
import static com.zacharyhirsch.lox.TokenType.STAR;

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

  private Expr equality() {
    Expr expr = comparison();
    while (match(BANG_EQUAL, EQUAL_EQUAL)) {
      Token operator = previous();
      Expr right = comparison();
      expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
  }

  private Expr comparison() {
    Expr expr = addition();
    while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
      Token operator = previous();
      Expr right = addition();
      expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
  }

  private Expr addition() {
    Expr expr = multiplication();
    while (match(MINUS, PLUS)) {
      Token operator = previous();
      Expr right = multiplication();
      expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
  }

  private Expr multiplication() {
    Expr expr = unary();
    while (match(SLASH, STAR)) {
      Token operator = previous();
      Expr right = unary();
      expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
  }

  private Expr unary() {
    if (match(BANG, MINUS)) {
      Token operator = previous();
      Expr right = unary();
      return new Expr.Unary(operator, right);
    }
    return primary();
  }

  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }
    return false;
  }

  private boolean check(TokenType type) {
    if (isAtEnd()) {
      return false;
    }
    return peek().type == type;
  }

  private Token advance() {
    if (!isAtEnd()) {
      current++;
    }
    return previous();
  }

  private boolean isAtEnd() {
    return peek().type == EOF;
  }

  private Token peek() {
    return tokens.get(current);
  }

  private Token previous() {
    return tokens.get(current - 1);
  }
}

package com.zacharyhirsch.lox;

import java.util.ArrayList;
import java.util.List;

import static com.zacharyhirsch.lox.TokenType.COMMA;
import static com.zacharyhirsch.lox.TokenType.DOT;
import static com.zacharyhirsch.lox.TokenType.EOF;
import static com.zacharyhirsch.lox.TokenType.LEFT_BRACE;
import static com.zacharyhirsch.lox.TokenType.LEFT_PAREN;
import static com.zacharyhirsch.lox.TokenType.MINUS;
import static com.zacharyhirsch.lox.TokenType.PLUS;
import static com.zacharyhirsch.lox.TokenType.RIGHT_BRACE;
import static com.zacharyhirsch.lox.TokenType.RIGHT_PAREN;
import static com.zacharyhirsch.lox.TokenType.SEMICOLON;
import static com.zacharyhirsch.lox.TokenType.STAR;

final class Scanner {

  private final String source;
  private final List<Token> tokens = new ArrayList<>();

  private int start = 0;
  private int current = 0;
  private int line = 1;

  public Scanner(String source) {
    this.source = source;
  }

  List<Token> scanTokens() {
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }

    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      case '(': addToken(LEFT_PAREN); break;
      case ')': addToken(RIGHT_PAREN); break;
      case '{': addToken(LEFT_BRACE); break;
      case '}': addToken(RIGHT_BRACE); break;
      case ',': addToken(COMMA); break;
      case '.': addToken(DOT); break;
      case '-': addToken(MINUS); break;
      case '+': addToken(PLUS); break;
      case ';': addToken(SEMICOLON); break;
      case '*': addToken(STAR); break;
    }
  }

  private char advance() {
    current++;
    return source.charAt(current - 1);
  }

  private void addToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }

  private boolean isAtEnd() {
    return current >= source.length();
  }
}

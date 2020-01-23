package com.zacharyhirsch.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zacharyhirsch.lox.TokenType.AND;
import static com.zacharyhirsch.lox.TokenType.BANG;
import static com.zacharyhirsch.lox.TokenType.BANG_EQUAL;
import static com.zacharyhirsch.lox.TokenType.CLASS;
import static com.zacharyhirsch.lox.TokenType.COMMA;
import static com.zacharyhirsch.lox.TokenType.DOT;
import static com.zacharyhirsch.lox.TokenType.ELSE;
import static com.zacharyhirsch.lox.TokenType.EOF;
import static com.zacharyhirsch.lox.TokenType.EQUAL;
import static com.zacharyhirsch.lox.TokenType.EQUAL_EQUAL;
import static com.zacharyhirsch.lox.TokenType.FALSE;
import static com.zacharyhirsch.lox.TokenType.FOR;
import static com.zacharyhirsch.lox.TokenType.FUN;
import static com.zacharyhirsch.lox.TokenType.GREATER;
import static com.zacharyhirsch.lox.TokenType.GREATER_EQUAL;
import static com.zacharyhirsch.lox.TokenType.IDENTIFIER;
import static com.zacharyhirsch.lox.TokenType.IF;
import static com.zacharyhirsch.lox.TokenType.LEFT_BRACE;
import static com.zacharyhirsch.lox.TokenType.LEFT_PAREN;
import static com.zacharyhirsch.lox.TokenType.LESS;
import static com.zacharyhirsch.lox.TokenType.LESS_EQUAL;
import static com.zacharyhirsch.lox.TokenType.MINUS;
import static com.zacharyhirsch.lox.TokenType.NIL;
import static com.zacharyhirsch.lox.TokenType.NUMBER;
import static com.zacharyhirsch.lox.TokenType.OR;
import static com.zacharyhirsch.lox.TokenType.PLUS;
import static com.zacharyhirsch.lox.TokenType.PRINT;
import static com.zacharyhirsch.lox.TokenType.QUESTION;
import static com.zacharyhirsch.lox.TokenType.RETURN;
import static com.zacharyhirsch.lox.TokenType.RIGHT_BRACE;
import static com.zacharyhirsch.lox.TokenType.RIGHT_PAREN;
import static com.zacharyhirsch.lox.TokenType.SEMICOLON;
import static com.zacharyhirsch.lox.TokenType.SLASH;
import static com.zacharyhirsch.lox.TokenType.STAR;
import static com.zacharyhirsch.lox.TokenType.STRING;
import static com.zacharyhirsch.lox.TokenType.SUPER;
import static com.zacharyhirsch.lox.TokenType.THIS;
import static com.zacharyhirsch.lox.TokenType.TRUE;
import static com.zacharyhirsch.lox.TokenType.VAR;
import static com.zacharyhirsch.lox.TokenType.WHILE;

final class Scanner {

  private static final Map<String, TokenType> KEYWORDS;

  static {
    KEYWORDS = new HashMap<>();
    KEYWORDS.put("and", AND);
    KEYWORDS.put("class", CLASS);
    KEYWORDS.put("else", ELSE);
    KEYWORDS.put("false", FALSE);
    KEYWORDS.put("for", FOR);
    KEYWORDS.put("fun", FUN);
    KEYWORDS.put("if", IF);
    KEYWORDS.put("nil", NIL);
    KEYWORDS.put("or", OR);
    KEYWORDS.put("print", PRINT);
    KEYWORDS.put("return", RETURN);
    KEYWORDS.put("super", SUPER);
    KEYWORDS.put("this", THIS);
    KEYWORDS.put("true", TRUE);
    KEYWORDS.put("var", VAR);
    KEYWORDS.put("while", WHILE);
  }

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
      case '(':
        addToken(LEFT_PAREN);
        break;
      case ')':
        addToken(RIGHT_PAREN);
        break;
      case '{':
        addToken(LEFT_BRACE);
        break;
      case '}':
        addToken(RIGHT_BRACE);
        break;
      case ',':
        addToken(COMMA);
        break;
      case '.':
        addToken(DOT);
        break;
      case '-':
        addToken(MINUS);
        break;
      case '+':
        addToken(PLUS);
        break;
      case ';':
        addToken(SEMICOLON);
        break;
      case '*':
        addToken(STAR);
        break;
      case '?':
        addToken(QUESTION);
        break;
      case '!':
        addToken(match('=') ? BANG_EQUAL : BANG);
        break;
      case '=':
        addToken(match('=') ? EQUAL_EQUAL : EQUAL);
        break;
      case '<':
        addToken(match('=') ? LESS_EQUAL : LESS);
        break;
      case '>':
        addToken(match('=') ? GREATER_EQUAL : GREATER);
        break;
      case '/':
        if (match('/')) {
          while (peek() != '\n' && !isAtEnd()) {
            advance();
          }
        } else {
          addToken(SLASH);
        }
        break;

      case ' ':
      case '\r':
      case '\t':
        break;

      case '\n':
        line++;
        break;

      case '"':
        string();
        break;

      default:
        if (isDigit(c)) {
          number();
        } else if (isAlpha(c)) {
          identifier();
        } else {
          Lox.error(line, "Unexpected character.");
        }
        break;
    }
  }

  private void identifier() {
    while (isAlphaNumeric(peek())) {
      advance();
    }
    String text = source.substring(start, current);
    TokenType type = KEYWORDS.get(text);
    if (type == null) {
      type = IDENTIFIER;
    }
    addToken(type);
  }

  private void number() {
    while (isDigit((peek()))) {
      advance();
    }

    if (peek() == '.' && isDigit(peekNext())) {
      advance();
      while (isDigit(peek())) {
        advance();
      }
    }

    double value = Double.parseDouble(source.substring(start, current));
    addToken(NUMBER, value);
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n') {
        line++;
      }
      advance();
    }

    if (isAtEnd()) {
      Lox.error(line, "Unterminated string.");
      return;
    }

    advance();

    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
  }

  private boolean match(char expected) {
    if (isAtEnd()) {
      return false;
    }
    if (source.charAt(current) != expected) {
      return false;
    }
    current++;
    return true;
  }

  private char peek() {
    if (isAtEnd()) {
      return '\0';
    }
    return source.charAt(current);
  }

  private char peekNext() {
    if (current + 1 >= source.length()) {
      return '\0';
    }
    return source.charAt(current + 1);
  }

  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
  }

  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
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

package com.zacharyhirsch.lox;

public class Interpreter implements Expr.Visitor<Object> {

  @Override
  public Object visitGroupingExpr(Expr.Grouping expr) {
    return evaluate(expr.expression);
  }

  @Override
  public Object visitLiteralExpr(Expr.Literal expr) {
    return expr.value;
  }
}

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

  @Override
  public Object visitUnaryExpr(Expr.Unary expr) {
    Object right = evaluate(expr.right);
    if (expr.operator.type == TokenType.MINUS) {
      return -(double) right;
    }
    return null;
  }

  private Object evaluate(Expr expr) {
    return expr.accept(this);
  }
}

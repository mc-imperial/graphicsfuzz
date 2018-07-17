// Copyright (c) 2018 Imperial College London
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.graphicsfuzz.common.ast.stmt;

import com.graphicsfuzz.common.ast.IAstNode;
import com.graphicsfuzz.common.ast.expr.Expr;
import com.graphicsfuzz.common.ast.visitors.IAstVisitor;

public class ReturnStmt extends Stmt {

  private Expr expr;

  public ReturnStmt(Expr expr) {
    this.expr = expr;
  }

  public ReturnStmt() {
    this(null);
  }

  public Expr getExpr() {
    return expr;
  }

  public boolean hasExpr() {
    return getExpr() != null;
  }

  @Override
  public void replaceChild(IAstNode child, IAstNode newChild) {
    assert child == expr;
    assert newChild instanceof Expr;
    this.expr = (Expr) newChild;
  }

  @Override
  public boolean hasChild(IAstNode candidateChild) {
    return candidateChild == expr;
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitReturnStmt(this);
  }

  @Override
  public ReturnStmt clone() {
    if (expr == null) {
      return new ReturnStmt();
    }
    return new ReturnStmt(expr.clone());
  }

}

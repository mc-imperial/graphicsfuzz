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

public class IfStmt extends Stmt {

  private Expr condition;
  private Stmt thenStmt;
  private Stmt elseStmt;

  public IfStmt(Expr condition, Stmt thenStmt, Stmt elseStmt) {
    this.condition = condition;
    this.thenStmt = thenStmt;
    this.elseStmt = elseStmt;
  }

  public Expr getCondition() {
    return condition;
  }

  public Stmt getThenStmt() {
    return thenStmt;
  }

  public Stmt getElseStmt() {
    return elseStmt;
  }

  public boolean hasElseStmt() {
    return elseStmt != null;
  }

  public void setThenStmt(Stmt thenStmt) {
    this.thenStmt = thenStmt;
  }

  public void setElseStmt(Stmt elseStmt) {
    this.elseStmt = elseStmt;
  }

  public void setCondition(Expr condition) {
    this.condition = condition;
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitIfStmt(this);
  }

  @Override
  public void replaceChild(IAstNode child, IAstNode newChild) {
    if (child == condition && newChild instanceof Expr) {
      setCondition((Expr) newChild);
      return;
    }
    if (child == thenStmt && newChild instanceof Stmt) {
      setThenStmt((Stmt) newChild);
      return;
    }
    if (child == elseStmt && newChild instanceof Stmt) {
      setElseStmt((Stmt) newChild);
      return;
    }
    throw new IllegalArgumentException();
  }

  @Override
  public boolean hasChild(IAstNode candidateChild) {
    assert candidateChild != null;
    return condition == candidateChild
          || thenStmt == candidateChild
          || elseStmt == candidateChild;
  }

  @Override
  public IfStmt clone() {
    return new IfStmt(condition.clone(), thenStmt.clone(),
        elseStmt == null ? null : elseStmt.clone());
  }

}

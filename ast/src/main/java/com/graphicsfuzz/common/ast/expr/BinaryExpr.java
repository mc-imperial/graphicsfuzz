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

package com.graphicsfuzz.common.ast.expr;

import com.graphicsfuzz.common.ast.IAstNode;
import com.graphicsfuzz.common.ast.visitors.IAstVisitor;

public class BinaryExpr extends Expr {

  private Expr lhs;
  private Expr rhs;
  private BinOp op;

  /**
   * Makes a binary expression from the given expressions and operator.
   *
   * @param lhs Left hand sub-expression
   * @param rhs Right had sub-expression
   * @param op Operator
   */
  public BinaryExpr(Expr lhs, Expr rhs, BinOp op) {
    assert lhs != null;
    assert rhs != null;
    this.lhs = lhs;
    this.rhs = rhs;
    this.op = op;
  }

  public Expr getLhs() {
    return lhs;
  }

  public void setLhs(Expr lhs) {
    this.lhs = lhs;
  }

  public Expr getRhs() {
    return rhs;
  }

  public void setRhs(Expr rhs) {
    this.rhs = rhs;
  }

  public BinOp getOp() {
    return op;
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitBinaryExpr(this);
  }

  @Override
  public BinaryExpr clone() {
    return new BinaryExpr(lhs.clone(), rhs.clone(), op);
  }

  @Override
  public boolean hasChild(IAstNode candidateChild) {
    return lhs == candidateChild || rhs == candidateChild;
  }

  @Override
  public Expr getChild(int index) {
    if (index == 0) {
      return getLhs();
    }
    if (index == 1) {
      return getRhs();
    }
    throw new IndexOutOfBoundsException("Index for BinaryExpr must be 0 or 1");
  }

  @Override
  public void setChild(int index, Expr expr) {
    if (index == 0) {
      lhs = expr;
      return;
    }
    if (index == 1) {
      rhs = expr;
      return;
    }
    throw new IndexOutOfBoundsException("Index for BinaryExpr must be 0 or 1");
  }

  @Override
  public int getNumChildren() {
    return 2;
  }

}

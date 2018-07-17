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

public class ParenExpr extends Expr {

  private Expr expr;

  public ParenExpr(Expr expr) {
    this.expr = expr;
  }

  public Expr getExpr() {
    return expr;
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitParenExpr(this);
  }

  @Override
  public ParenExpr clone() {
    return new ParenExpr(expr.clone());
  }

  @Override
  public boolean hasChild(IAstNode candidateChild) {
    return candidateChild == expr;
  }

  @Override
  public Expr getChild(int index) {
    if (index == 0) {
      return expr;
    }
    throw new IndexOutOfBoundsException("Index for ParenExpr must be 0");
  }

  @Override
  public void setChild(int index, Expr expr) {
    if (index == 0) {
      this.expr = expr;
      return;
    }
    throw new IndexOutOfBoundsException("Index for ParenExpr must be 0");
  }

  @Override
  public int getNumChildren() {
    return 1;
  }

}

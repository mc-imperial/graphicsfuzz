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

public class TernaryExpr extends Expr {

  private Expr test;
  private Expr thenExpr;
  private Expr elseExpr;

  /**
   * Makes a ternary expression from the given test and options.
   * @param test Boolean to be tested
   * @param thenExpr Result if the boolean is true
   * @param elseExpr Result if the boolean is false
   */
  public TernaryExpr(Expr test, Expr thenExpr, Expr elseExpr) {
    this.test = test;
    this.thenExpr = thenExpr;
    this.elseExpr = elseExpr;
  }

  public Expr getTest() {
    return test;
  }

  public Expr getThenExpr() {
    return thenExpr;
  }

  public Expr getElseExpr() {
    return elseExpr;
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitTernaryExpr(this);
  }

  @Override
  public TernaryExpr clone() {
    return new TernaryExpr(test.clone(), thenExpr.clone(), elseExpr.clone());
  }

  @Override
  public boolean hasChild(IAstNode candidateChild) {
    return candidateChild == test || candidateChild == thenExpr || candidateChild == elseExpr;
  }

  @Override
  public Expr getChild(int index) {
    if (index == 0) {
      return test;
    }
    if (index == 1) {
      return thenExpr;
    }
    if (index == 2) {
      return elseExpr;
    }
    throw new IndexOutOfBoundsException("Index for TernaryExpr must be 0, 1 or 2");
  }

  @Override
  public void setChild(int index, Expr expr) {
    if (index == 0) {
      test = expr;
      return;
    }
    if (index == 1) {
      thenExpr = expr;
      return;
    }
    if (index == 2) {
      elseExpr = expr;
      return;
    }
    throw new IndexOutOfBoundsException("Index for TernaryExpr must be 0, 1 or 2");
  }

  @Override
  public int getNumChildren() {
    return 3;
  }

}

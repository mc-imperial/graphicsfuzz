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

public class ForStmt extends LoopStmt {

  private Stmt init;
  private Expr increment;

  public ForStmt(Stmt init, Expr condition, Expr increment, Stmt body) {
    super(condition, body);
    this.init = init;
    this.increment = increment;
  }

  public Stmt getInit() {
    return init;
  }

  public Expr getIncrement() {
    return increment;
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitForStmt(this);
  }

  @Override
  public ForStmt clone() {
    return new ForStmt(init.clone(), getCondition().clone(), increment.clone(), getBody().clone());
  }

  @Override
  public void replaceChild(IAstNode child, IAstNode newChild) {
    if (child == init) {
      init = (Stmt) newChild;
    } else if (child == increment) {
      increment = (Expr) newChild;
    } else {
      super.replaceChild(child, newChild);
    }
  }

  @Override
  public boolean hasChild(IAstNode candidateChild) {
    return candidateChild == init
          || candidateChild == increment
          || super.hasChild(candidateChild);
  }

}

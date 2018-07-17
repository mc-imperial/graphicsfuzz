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
import com.graphicsfuzz.common.ast.visitors.StandardVisitor;

public abstract class LoopStmt extends Stmt {

  private Expr condition;
  private Stmt body;

  LoopStmt(Expr condition, Stmt body) {
    this.condition = condition;
    this.body = body;
  }

  public final Stmt getBody() {
    return body;
  }

  public final void setBody(Stmt body) {
    this.body = body;
  }

  public final Expr getCondition() {
    return condition;
  }

  public final void setCondition(Expr condition) {
    this.condition = condition;
  }

  @Override
  public void replaceChild(IAstNode child, IAstNode newChild) {
    if (child == body) {
      setBody((Stmt) newChild);
    } else if (child == condition) {
      setCondition((Expr) newChild);
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public boolean hasChild(IAstNode candidateChild) {
    return candidateChild == body
          || candidateChild == condition;
  }

  /**
   * Determines whether the loop's body contains any break or continue statements
   * that are not nested in inner loops.
   */
  public boolean containsDirectBreakOrContinueStmt() {
    return new ContainsDirectBreakOrContinueStmt().check();
  }

  private class ContainsDirectBreakOrContinueStmt extends StandardVisitor {

    private class FoundBreakOrContinueStmtException extends RuntimeException {

    }

    private int nestingDepth = 0;

    public boolean check() {
      try {
        visit(body);
        return false;
      } catch (FoundBreakOrContinueStmtException exception) {
        return true;
      }
    }

    @Override
    public void visit(IAstNode node) {
      if (node instanceof LoopStmt) {
        nestingDepth++;
      }
      super.visit(node);
      if (node instanceof LoopStmt) {
        nestingDepth--;
      }
    }

    @Override
    public void visitBreakStmt(BreakStmt breakStmt) {
      if (nestingDepth == 0) {
        throw new FoundBreakOrContinueStmtException();
      }
    }

    @Override
    public void visitContinueStmt(ContinueStmt continueStmt) {
      if (nestingDepth == 0) {
        throw new FoundBreakOrContinueStmtException();
      }
    }

  }

}

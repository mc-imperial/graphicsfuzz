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

import com.graphicsfuzz.common.ast.ChildDoesNotExistException;
import com.graphicsfuzz.common.ast.IAstNode;
import com.graphicsfuzz.common.ast.visitors.IAstVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BlockStmt extends Stmt {

  private List<Stmt> stmts;

  // A procedure body, for body or while body
  // does not introduce a new scope: the procedure
  // header, for header or while header does this
  private boolean introducesNewScope;

  /**
   * Creates a block statement from the given list of statements.
   * @param stmts Initial statements for the block
   * @param introducesNewScope Determines whether the block introduces a new lexical scope.  This
   *                           should be false in the case of a function body, as this has the same
   *                           lexical scope as the function parameters.
   */
  public BlockStmt(List<Stmt> stmts, boolean introducesNewScope) {
    setStmts(stmts);
    this.introducesNewScope = introducesNewScope;
  }

  public List<Stmt> getStmts() {
    return Collections.unmodifiableList(stmts);
  }

  public void setStmts(List<Stmt> stmts) {
    this.stmts = new ArrayList<>();
    this.stmts.addAll(stmts);
  }

  public int getNumStmts() {
    return stmts.size();
  }

  public boolean introducesNewScope() {
    return introducesNewScope;
  }

  public void setIntroducesNewScope(boolean introducesNewScope) {
    this.introducesNewScope = introducesNewScope;
  }

  public void insertStmt(int index, Stmt stmt) {
    stmts.add(index, stmt);
  }

  public void setStmt(int index, Stmt stmt) {
    stmts.set(index, stmt);
  }

  public Stmt getStmt(int index) {
    return stmts.get(index);
  }

  public void removeStmt(int index) {
    stmts.remove(index);
  }

  /**
   * Removes the given child statement from the block, throwing an exception if not present.
   * @param child Statement to be removed
   */
  public void removeStmt(Stmt child) {
    if (!stmts.contains(child)) {
      throw new IllegalArgumentException("Block does not contain given statement.");
    }
    stmts.remove(child);
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitBlockStmt(this);
  }

  @Override
  public void replaceChild(IAstNode child, IAstNode newChild) {
    if (!hasChild(child)) {
      throw new ChildDoesNotExistException(child, this);
    }
    if (!(newChild instanceof Stmt)) {
      throw new IllegalArgumentException(
          "Attempt to replace child of block statement with a non-statement: "
              + newChild);
    }
    for (int i = 0; i < stmts.size(); i++) {
      if (child == getStmt(i)) {
        setStmt(i, (Stmt) newChild);
        return;
      }
    }
    throw new IllegalArgumentException("Should be unreachable.");
  }

  @Override
  public boolean hasChild(IAstNode candidateChild) {
    return stmts.contains(candidateChild);
  }

  /**
   * Inserts the second statement right after the first statement, which must appear in the block.
   *
   * @param originalStmt A statement that must be present in the block
   * @param insertedStmt A statement to be inserted right after originalStmt
   */
  public void insertAfter(Stmt originalStmt, Stmt insertedStmt) {
    for (int i = 0; i < stmts.size(); i++) {
      if (getStmt(i) == originalStmt) {
        insertStmt(i + 1, insertedStmt);
        return;
      }
    }
    throw new IllegalArgumentException("Should be unreachable.");
  }

  /**
   * Inserts the second statement right before the first statement, which must appear in the block.
   *
   * @param originalStmt A statement that must be present in the block
   * @param insertedStmt A statement to be inserted right before originalStmt
   */
  public void insertBefore(Stmt originalStmt, Stmt insertedStmt) {
    for (int i = 0; i < stmts.size(); i++) {
      if (getStmt(i) == originalStmt) {
        insertStmt(i, insertedStmt);
        return;
      }
    }
    throw new IllegalArgumentException("Should be unreachable.");
  }

  public void addStmt(Stmt stmt) {
    stmts.add(stmt);
  }

  @Override
  public BlockStmt clone() {
    return new BlockStmt(stmts.stream().map(x -> x.clone()).collect(Collectors.toList()),
        introducesNewScope);
  }

}

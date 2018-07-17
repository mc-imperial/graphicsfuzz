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

package com.graphicsfuzz.common.typing;

import com.graphicsfuzz.common.ast.decl.FunctionDefinition;
import com.graphicsfuzz.common.ast.decl.FunctionPrototype;
import com.graphicsfuzz.common.ast.decl.ParameterDecl;
import com.graphicsfuzz.common.ast.decl.VariableDeclInfo;
import com.graphicsfuzz.common.ast.decl.VariablesDeclaration;
import com.graphicsfuzz.common.ast.stmt.BlockStmt;
import com.graphicsfuzz.common.ast.stmt.ForStmt;
import com.graphicsfuzz.common.ast.stmt.WhileStmt;
import com.graphicsfuzz.common.ast.type.ArrayType;
import com.graphicsfuzz.common.ast.visitors.StandardVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class ScopeTreeBuilder extends StandardVisitor {

  protected Scope currentScope;
  private Deque<BlockStmt> enclosingBlocks;
  private boolean addEncounteredParametersToScope;
  protected FunctionDefinition enclosingFunction;
  private List<FunctionPrototype> encounteredFunctionPrototypes;

  protected ScopeTreeBuilder() {
    this.currentScope = new Scope(null);
    this.enclosingBlocks = new LinkedList<>();
    this.addEncounteredParametersToScope = false;
    this.enclosingFunction = null;
    this.encounteredFunctionPrototypes = new ArrayList<>();
  }

  @Override
  public void visitBlockStmt(BlockStmt stmt) {
    enterBlockStmt(stmt);
    super.visitBlockStmt(stmt);
    leaveBlockStmt(stmt);
  }

  protected void enterBlockStmt(BlockStmt stmt) {
    enclosingBlocks.addFirst(stmt);
    if (stmt.introducesNewScope()) {
      pushScope();
    }
  }

  protected void leaveBlockStmt(BlockStmt stmt) {
    if (stmt.introducesNewScope()) {
      popScope();
    }
    enclosingBlocks.removeFirst();
  }

  /**
   * Returns the closest block enclosing the current point of visitation.
   *
   * @return The closest block
   */
  protected BlockStmt currentBlock() {
    return enclosingBlocks.peekFirst();
  }

  /**
   * Returns true if and only if visitatin is in some block.
   *
   * @return Whether visitation is in a block
   */
  protected boolean inBlock() {
    return !enclosingBlocks.isEmpty();
  }

  @Override
  public void visitForStmt(ForStmt forStmt) {
    pushScope();
    super.visitForStmt(forStmt);
    popScope();
  }

  /**
   * Because WebGL places limits on for loops it can be convenient to only visit the body of a
   * for loop, skipping the header.  Subclasses can call this method to invoke this behaviour
   * during visitation, instead of calling super.visitForStmt(...).
   *
   * @param forStmt For statement whose body should be visited
   */
  protected void visitForStmtBodyOnly(ForStmt forStmt) {
    pushScope();
    visit(forStmt.getBody());
    popScope();
  }


  @Override
  public void visitWhileStmt(WhileStmt whileStmt) {
    pushScope();
    super.visitWhileStmt(whileStmt);
    popScope();
  }

  @Override
  public void visitFunctionDefinition(FunctionDefinition functionDefinition) {
    assert enclosingFunction == null;
    enclosingFunction = functionDefinition;
    pushScope();

    addEncounteredParametersToScope = true;
    visitFunctionPrototype(functionDefinition.getPrototype());
    addEncounteredParametersToScope = false;
    visit(functionDefinition.getBody());
    popScope();
    enclosingFunction = null;
  }

  @Override
  public void visitFunctionPrototype(FunctionPrototype functionPrototype) {
    encounteredFunctionPrototypes.add(functionPrototype);
    for (ParameterDecl p : functionPrototype.getParameters()) {
      visitParameterDecl(p);
      if (addEncounteredParametersToScope) {
        if (p.getName() == null) {
          continue;
        }
        currentScope.add(p.getName(),
              p.getArrayInfo() == null ? p.getType() : new ArrayType(p.getType(), p.getArrayInfo()),
              Optional.of(p));
      }
    }
  }

  @Override
  public void visitVariablesDeclaration(VariablesDeclaration variablesDeclaration) {
    List<VariableDeclInfo> children = new ArrayList<>();
    children.addAll(variablesDeclaration.getDeclInfos());
    for (VariableDeclInfo declInfo : children) {
      visitVariableDeclInfo(declInfo);
      currentScope.add(declInfo.getName(),
          declInfo.getArrayInfo() == null
              ? variablesDeclaration.getBaseType()
              : new ArrayType(variablesDeclaration.getBaseType(), declInfo.getArrayInfo()),
          Optional.empty(),
          declInfo, variablesDeclaration);
      visitVariableDeclInfoAfterAddedToScope(declInfo);
    }
  }

  /**
   * This is a hook for subclasses that need to perform an action after a declaration has been added
   * to the current scope.
   *
   * @param declInfo The declaration info that was just added to the current scope
   */
  protected void visitVariableDeclInfoAfterAddedToScope(VariableDeclInfo declInfo) {
    // Deliberately empty - to be optionally overridden by subclasses.
  }

  protected void popScope() {
    currentScope = currentScope.getParent();
  }

  protected void pushScope() {
    Scope newScope = new Scope(currentScope);
    currentScope = newScope;
  }

  protected List<FunctionPrototype> getEncounteredFunctionPrototypes() {
    return Collections.unmodifiableList(encounteredFunctionPrototypes);
  }

  protected boolean atGlobalScope() {
    return !currentScope.hasParent();
  }


}

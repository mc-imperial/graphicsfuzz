// Copyright (c) 2018 Imperial College London
//
// All rights reserved

package com.graphicsfuzz.common.util;

import com.graphicsfuzz.common.ast.IParentMap;
import com.graphicsfuzz.common.ast.TranslationUnit;
import com.graphicsfuzz.common.ast.decl.ScalarInitializer;
import com.graphicsfuzz.common.ast.decl.VariableDeclInfo;
import com.graphicsfuzz.common.ast.decl.VariablesDeclaration;
import com.graphicsfuzz.common.ast.expr.BinOp;
import com.graphicsfuzz.common.ast.expr.BinaryExpr;
import com.graphicsfuzz.common.ast.expr.IntConstantExpr;
import com.graphicsfuzz.common.ast.expr.UnOp;
import com.graphicsfuzz.common.ast.expr.UnaryExpr;
import com.graphicsfuzz.common.ast.expr.VariableIdentifierExpr;
import com.graphicsfuzz.common.ast.stmt.BlockStmt;
import com.graphicsfuzz.common.ast.stmt.BreakStmt;
import com.graphicsfuzz.common.ast.stmt.DeclarationStmt;
import com.graphicsfuzz.common.ast.stmt.DoStmt;
import com.graphicsfuzz.common.ast.stmt.ExprStmt;
import com.graphicsfuzz.common.ast.stmt.ForStmt;
import com.graphicsfuzz.common.ast.stmt.IfStmt;
import com.graphicsfuzz.common.ast.stmt.LoopStmt;
import com.graphicsfuzz.common.ast.stmt.Stmt;
import com.graphicsfuzz.common.ast.stmt.WhileStmt;
import com.graphicsfuzz.common.ast.type.BasicType;
import com.graphicsfuzz.common.ast.visitors.StandardVisitor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TruncateLoops extends StandardVisitor {

  private final int limit;
  private final TranslationUnit tu;
  private final String prefix;
  private int counter;

  public TruncateLoops(int limit, String prefix, TranslationUnit tu) {
    this.limit = limit;
    this.tu = tu;
    this.prefix = prefix;
    counter = 0;
    visit(tu);
  }

  @Override
  public void visitForStmt(ForStmt forStmt) {
    super.visitForStmt(forStmt);
    handleLoop(forStmt);
  }

  @Override
  public void visitWhileStmt(WhileStmt whileStmt) {
    super.visitWhileStmt(whileStmt);
    handleLoop(whileStmt);
  }

  @Override
  public void visitDoStmt(DoStmt doStmt) {
    super.visitDoStmt(doStmt);
    handleLoop(doStmt);
  }

  private void handleLoop(LoopStmt loopStmt) {
    final IParentMap parentMap = IParentMap.createParentMap(tu);
    final String limiterName = prefix + "_looplimiter" + counter;
    counter++;

    final DeclarationStmt limiterDeclaration = new DeclarationStmt(
          new VariablesDeclaration(BasicType.INT,
                new VariableDeclInfo(limiterName, null,
                      new ScalarInitializer(new IntConstantExpr("0")))));

    final List<Stmt> limitCheckAndIncrement = Arrays.asList(
          new IfStmt(
            new BinaryExpr(
                  new VariableIdentifierExpr(limiterName),
                  new IntConstantExpr(String.valueOf(limit)),
                  BinOp.GE),
            new BlockStmt(Arrays.asList(BreakStmt.INSTANCE), true),
            null),
          new ExprStmt(new UnaryExpr(
            new VariableIdentifierExpr(limiterName),
            UnOp.POST_INC)));

    if (loopStmt.getBody() instanceof BlockStmt) {
      for (int i = limitCheckAndIncrement.size() - 1; i >= 0; i--) {
        ((BlockStmt) loopStmt.getBody()).insertStmt(0, limitCheckAndIncrement.get(i));
      }
    } else {
      final List<Stmt> newStmts = new ArrayList<>();
      newStmts.addAll(limitCheckAndIncrement);
      newStmts.add(loopStmt.getBody());
      loopStmt.setBody(new BlockStmt(newStmts, loopStmt instanceof DoStmt));
    }

    final BlockStmt replacementBlock = new BlockStmt(
          Arrays.asList(limiterDeclaration, loopStmt), true);
    parentMap.getParent(loopStmt).replaceChild(loopStmt, replacementBlock);
  }

}

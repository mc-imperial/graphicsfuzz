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
    if(analyseForStmt(forStmt)) {
      handleLoop(forStmt);
    }
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

  private boolean analyseForStmt(ForStmt forStmt) {

    String loopCounterName;
    int initValue;
    BinOp condTestType;
    int condValue;
    int incrementValue;

    if (forStmt.getInit() instanceof ExprStmt
        && ((ExprStmt) forStmt.getInit()).getExpr() instanceof BinaryExpr
        && ((BinaryExpr)(((ExprStmt) forStmt.getInit()).getExpr())).getOp() == BinOp.ASSIGN
        && ((BinaryExpr)(((ExprStmt) forStmt.getInit()).getExpr())).getRhs()
        instanceof IntConstantExpr
        && ((BinaryExpr)(((ExprStmt) forStmt.getInit()).getExpr())).getLhs()
        instanceof VariableIdentifierExpr) {
      initValue = Integer.valueOf(
          ((IntConstantExpr)((BinaryExpr)(((ExprStmt) forStmt.getInit()).getExpr())).getRhs())
              .getValue());
      loopCounterName = ((VariableIdentifierExpr) (((BinaryExpr)(((ExprStmt) forStmt.getInit())
          .getExpr())).getLhs())).getName();
    } else if(forStmt.getInit() instanceof DeclarationStmt
        && ((DeclarationStmt) forStmt.getInit()).getVariablesDeclaration().getNumDecls() == 1
        && ((DeclarationStmt) forStmt.getInit()).getVariablesDeclaration().getDeclInfo(0)
        .getInitializer() instanceof ScalarInitializer
        && ((ScalarInitializer)((DeclarationStmt) forStmt.getInit()).getVariablesDeclaration()
        .getDeclInfo(0).getInitializer()).getExpr() instanceof IntConstantExpr) {
      initValue = Integer.valueOf(
          ((IntConstantExpr)((ScalarInitializer)((DeclarationStmt) forStmt.getInit())
              .getVariablesDeclaration().getDeclInfo(0)
              .getInitializer()).getExpr()).getValue());
      loopCounterName = ((DeclarationStmt) forStmt.getInit()).getVariablesDeclaration()
          .getDeclInfo(0).getName();
    } else {
      return false;
    }

    if (forStmt.getCondition() instanceof BinaryExpr
        && ((BinaryExpr) forStmt.getCondition()).getRhs() instanceof IntConstantExpr // Add Unary Expression case
        && ((BinaryExpr) forStmt.getCondition()).getLhs() instanceof VariableIdentifierExpr
        && ((VariableIdentifierExpr) (((BinaryExpr) forStmt.getCondition()).getLhs())).getName()
        .equals(loopCounterName)) {
        condValue = Integer.valueOf(
            ((IntConstantExpr)((BinaryExpr) forStmt.getCondition()).getRhs()).getValue());
        condTestType = ((BinaryExpr) forStmt.getCondition()).getOp();
    } else if (forStmt.getCondition() instanceof BinaryExpr
        && ((BinaryExpr) forStmt.getCondition()).getLhs() instanceof IntConstantExpr
        && ((BinaryExpr) forStmt.getCondition()).getRhs() instanceof VariableIdentifierExpr
        && ((VariableIdentifierExpr) (((BinaryExpr) forStmt.getCondition()).getRhs())).getName()
        .equals(loopCounterName)) {
        condValue = Integer.valueOf(
            ((IntConstantExpr)((BinaryExpr) forStmt.getCondition()).getLhs()).getValue());
        condTestType = switchCondTestType(((BinaryExpr) forStmt.getCondition()).getOp());
    } else {
      return false;
    }

    if(condTestType.isSideEffecting()) {
      return false;
    }

    if (forStmt.getIncrement() instanceof UnaryExpr
        && ((UnaryExpr) forStmt.getIncrement()).getExpr() instanceof VariableIdentifierExpr
        && ((VariableIdentifierExpr) (((UnaryExpr) forStmt.getIncrement()).getExpr())).getName()
        .equals(loopCounterName)) {
      final UnOp operator = ((UnaryExpr) forStmt.getIncrement()).getOp();
      if (operator == UnOp.POST_INC || operator == UnOp.PRE_INC) {
        incrementValue = 1;
      } else if (operator == UnOp.POST_DEC || operator == UnOp.PRE_DEC) {
        incrementValue = -1;
      } else {
        return false;
      }
    } else if (forStmt.getIncrement() instanceof BinaryExpr
        && ((BinaryExpr) forStmt.getIncrement()).getRhs() instanceof IntConstantExpr
        && ((BinaryExpr) forStmt.getIncrement()).getLhs() instanceof VariableIdentifierExpr
        && ((VariableIdentifierExpr)(((BinaryExpr) forStmt.getIncrement()).getLhs())).getName()
        .equals(loopCounterName)){
      final BinaryExpr binaryExpr = (BinaryExpr) forStmt.getIncrement();
      if(binaryExpr.getOp() == BinOp.ADD_ASSIGN) {
        incrementValue = Integer.valueOf(((IntConstantExpr) binaryExpr.getRhs()).getValue());
      } else if(binaryExpr.getOp() == BinOp.SUB_ASSIGN) {
        incrementValue = -Integer.valueOf(((IntConstantExpr) binaryExpr.getRhs()).getValue());
      } else {
        return false;
      }
    } else {
      return false;
    }

    return (((condTestType == BinOp.LT || condTestType == BinOp.LE) && incrementValue <= 0)
        || ((condTestType == BinOp.GT || condTestType == BinOp.GE) && incrementValue >= 0)
        || ((condValue - initValue) / incrementValue > limit));
  }

  private BinOp switchCondTestType(BinOp binOp) {
    switch (binOp) {
      case LT:
        return BinOp.GT;
      case GT:
        return BinOp.LT;
      case LE:
        return BinOp.GE;
      case GE:
        return BinOp.LE;
      default:
        return binOp;
    }
  }

}

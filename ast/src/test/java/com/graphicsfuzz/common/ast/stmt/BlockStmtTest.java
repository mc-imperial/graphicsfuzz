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

import static org.junit.Assert.assertEquals;

import com.graphicsfuzz.common.ast.expr.BoolConstantExpr;
import java.util.Arrays;
import org.junit.Test;

public class BlockStmtTest {

  @Test
  public void testInsertStmt() {
    BlockStmt b = new BlockStmt(Arrays.asList(NullStmt.INSTANCE), true);
    assertEquals(1, b.getNumStmts());
    b.insertStmt(0, new ExprStmt(BoolConstantExpr.TRUE));
    assertEquals(2, b.getNumStmts());
    assertEquals(BoolConstantExpr.TRUE, ((ExprStmt)b.getStmt(0)).getExpr());
    assertEquals(NullStmt.INSTANCE, b.getStmt(1));
  }

  @Test
  public void testInsertBefore() {
    ExprStmt stmt = new ExprStmt(BoolConstantExpr.TRUE);
    BlockStmt b = new BlockStmt(Arrays.asList(stmt), true);
    assertEquals(1, b.getNumStmts());
    b.insertBefore(stmt, NullStmt.INSTANCE);
    assertEquals(2, b.getNumStmts());
    assertEquals(NullStmt.INSTANCE, b.getStmt(0));
    assertEquals(stmt, b.getStmt(1));
  }

  @Test
  public void testInsertAfter() {
    ExprStmt stmt1 = new ExprStmt(BoolConstantExpr.TRUE);
    ExprStmt stmt2 = new ExprStmt(BoolConstantExpr.FALSE);
    BlockStmt b = new BlockStmt(Arrays.asList(stmt1, stmt2), true);
    assertEquals(2, b.getNumStmts());
    b.insertAfter(stmt1, NullStmt.INSTANCE);
    assertEquals(3, b.getNumStmts());
    b.insertAfter(stmt2, NullStmt.INSTANCE);
    assertEquals(4, b.getNumStmts());
    assertEquals(stmt1, b.getStmt(0));
    assertEquals(NullStmt.INSTANCE, b.getStmt(1));
    assertEquals(stmt2, b.getStmt(2));
    assertEquals(NullStmt.INSTANCE, b.getStmt(3));
  }

}
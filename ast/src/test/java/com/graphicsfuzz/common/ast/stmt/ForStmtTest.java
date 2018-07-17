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

import static org.junit.Assert.assertTrue;

import com.graphicsfuzz.common.ast.expr.IntConstantExpr;
import java.util.ArrayList;
import org.junit.Test;

public class ForStmtTest {

  @Test
  public void replaceChild() {
    final ExprStmt init = new ExprStmt(new IntConstantExpr("0"));
    final IntConstantExpr condition = new IntConstantExpr("1");
    final IntConstantExpr increment = new IntConstantExpr("2");
    final BlockStmt body = new BlockStmt(new ArrayList<>(), false);
    final ForStmt forStmt = new ForStmt(
        init,
        condition,
        increment,
        body);
    final ExprStmt newInit = new ExprStmt(new IntConstantExpr("3"));
    final IntConstantExpr newCondition = new IntConstantExpr("4");
    final IntConstantExpr newIncrement = new IntConstantExpr("5");
    final BlockStmt newBody = new BlockStmt(new ArrayList<>(), false);

    forStmt.replaceChild(init, newInit);
    forStmt.replaceChild(condition, newCondition);
    forStmt.replaceChild(increment, newIncrement);
    forStmt.replaceChild(body, newBody);

    assertTrue(newInit == forStmt.getInit());
    assertTrue(newCondition == forStmt.getCondition());
    assertTrue(newIncrement == forStmt.getIncrement());
    assertTrue(newBody == forStmt.getBody());

  }

}
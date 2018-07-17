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

import static org.junit.Assert.assertEquals;

import com.graphicsfuzz.common.ast.visitors.StandardVisitor;
import org.junit.Test;

public class IntConstantExprTest {

  @Test
  public void getValue() throws Exception {
    IntConstantExpr ec = new IntConstantExpr("-1");
    assertEquals("-1", ec.getValue());
  }

  @Test
  public void accept() throws Exception {
    new StandardVisitor() {
      @Override
      public void visitIntConstantExpr(IntConstantExpr intConstantExpr) {
        super.visitIntConstantExpr(intConstantExpr);
        assertEquals("42", intConstantExpr.getValue());
      }
    }.visit(new IntConstantExpr("42"));
  }

  @Test
  public void testClone() throws Exception {
    IntConstantExpr ec1 = new IntConstantExpr("10");
    IntConstantExpr ec2 = ec1.clone();
    assertEquals(ec1.getValue(), ec2.getValue());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getChild() throws Exception {
    new IntConstantExpr("2").getChild(0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void setChild() throws Exception {
    new IntConstantExpr("2").setChild(0,
        new IntConstantExpr("3"));
  }

  @Test
  public void getNumChildren() throws Exception {
    assertEquals(0, new IntConstantExpr("2").getNumChildren());
  }

}
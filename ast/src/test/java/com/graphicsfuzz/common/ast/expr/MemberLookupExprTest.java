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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.graphicsfuzz.common.ast.visitors.StandardVisitor;
import org.junit.Test;

public class MemberLookupExprTest {

  @Test
  public void getStructure() throws Exception {
    VariableIdentifierExpr v = new VariableIdentifierExpr("v");
    MemberLookupExpr mle = new MemberLookupExpr(
        v,
        "foo");
    assertEquals(v, mle.getStructure());
  }

  @Test
  public void setStructure() throws Exception {
    VariableIdentifierExpr v = new VariableIdentifierExpr("v");
    VariableIdentifierExpr w = new VariableIdentifierExpr("w");
    MemberLookupExpr mle = new MemberLookupExpr(
        v,
        "foo");
    mle.setStructure(w);
    assertEquals(w, mle.getStructure());
  }

  @Test
  public void getMember() throws Exception {
    VariableIdentifierExpr v = new VariableIdentifierExpr("v");
    MemberLookupExpr mle = new MemberLookupExpr(
        v,
        "foo");
    assertEquals("foo", mle.getMember());
  }

  @Test
  public void setMember() throws Exception {
    VariableIdentifierExpr v = new VariableIdentifierExpr("v");
    MemberLookupExpr mle = new MemberLookupExpr(
        v,
        "foo");
    assertEquals("foo", mle.getMember());
    mle.setMember("bar");
    assertEquals("bar", mle.getMember());
  }

  @Test
  public void accept() throws Exception {
    new StandardVisitor() {
      @Override
      public void visitMemberLookupExpr(MemberLookupExpr memberLookupExpr) {
        super.visitMemberLookupExpr(memberLookupExpr);
        assertEquals("foo", memberLookupExpr.getMember());
        assertTrue(memberLookupExpr.getStructure() instanceof VariableIdentifierExpr);
        assertEquals("v", ((VariableIdentifierExpr) memberLookupExpr.getStructure()).getName());
      }
    }.visit(new MemberLookupExpr(new VariableIdentifierExpr("v"), "foo"));
  }

  @Test
  public void testClone() throws Exception {
    VariableIdentifierExpr v = new VariableIdentifierExpr("v");
    MemberLookupExpr mle = new MemberLookupExpr(
        v,
        "foo");
    MemberLookupExpr mle2 = mle.clone();
    assertFalse(mle == mle2);
    assertEquals(mle.getMember(), mle2.getMember());
    assertEquals(((VariableIdentifierExpr) mle.getStructure()).getName(),
        ((VariableIdentifierExpr) mle2.getStructure()).getName());
    assertFalse(mle.getStructure() == mle2.getStructure());
  }

  @Test
  public void getChild() throws Exception {
    VariableIdentifierExpr v = new VariableIdentifierExpr("v");
    MemberLookupExpr mle = new MemberLookupExpr(
        v,
        "foo");
    assertEquals(v, mle.getChild(0));
    assertEquals(mle.getStructure(), mle.getChild(0));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getChildException() throws Exception {
    new MemberLookupExpr(new VariableIdentifierExpr("v"), "foo").getChild(1);
  }

  @Test
  public void setChild() throws Exception {
    VariableIdentifierExpr v = new VariableIdentifierExpr("v");
    VariableIdentifierExpr w = new VariableIdentifierExpr("w");
    MemberLookupExpr mle = new MemberLookupExpr(
        v,
        "foo");
    mle.setChild(0, w);
    assertEquals(w, mle.getStructure());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void setChildException() throws Exception {
    new MemberLookupExpr(new VariableIdentifierExpr("v"), "foo")
        .setChild(1, new VariableIdentifierExpr("w"));
  }

  @Test
  public void getNumChildren() throws Exception {
    VariableIdentifierExpr v = new VariableIdentifierExpr("v");
    MemberLookupExpr mle = new MemberLookupExpr(
        v,
        "foo");
    assertEquals(1, mle.getNumChildren());
  }

}
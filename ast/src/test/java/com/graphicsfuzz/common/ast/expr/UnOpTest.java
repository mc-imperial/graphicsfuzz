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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.graphicsfuzz.common.ast.CompareAstsDuplicate;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class UnOpTest {

  @Test
  public void getText() throws Exception {
    String programUsingGetText = getOpTesterProgram(true);
    String programUsingStrings = getOpTesterProgram(true);

    CompareAstsDuplicate.assertEqualAsts(programUsingGetText, programUsingStrings);

  }

  @Test
  public void isSideEffecting() throws Exception {
    List<UnOp> shouldBeSideEffecting = Arrays.asList(
        UnOp.POST_DEC,
        UnOp.PRE_DEC,
        UnOp.POST_INC,
        UnOp.PRE_INC);

    for (UnOp unop : shouldBeSideEffecting) {
      assertTrue(unop.isSideEffecting());
    }

    for (UnOp unop : UnOp.values()) {
      if (!shouldBeSideEffecting.contains(unop)) {
        assertFalse(unop.isSideEffecting());
      }
    }

  }

  private String getOpTesterProgram(boolean p) {

    return "void main() {"
        + "  int a = 1;"
        + "  bool b = true;"
        + "  " + choose(p, UnOp.PRE_INC.getText(), "++") + " a;"
        + "  " + choose(p, UnOp.PRE_DEC.getText(), "--") + " a;"
        + "  a " + choose(p, UnOp.POST_INC.getText(), "++") + ";"
        + "  a " + choose(p, UnOp.POST_DEC.getText(), "--") + ";"
        + "  " + choose(p, UnOp.PLUS.getText(), "+") + "a;"
        + "  " + choose(p, UnOp.MINUS.getText(), "+") + "a;"
        + "  " + choose(p, UnOp.BNEG.getText(), "+") + "a;"
        + "  " + choose(p, UnOp.LNOT.getText(), "+") + "b;"
        + "}";
  }

  private String choose(boolean pred, String first, String second) {
    return pred ? first : second;
  }

}
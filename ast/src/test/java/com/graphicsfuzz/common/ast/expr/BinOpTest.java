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

public class BinOpTest {

  @Test
  public void isSideEffecting() throws Exception {
    List<BinOp> shouldBeSideEffecting = Arrays.asList(BinOp.ASSIGN,
        BinOp.MUL_ASSIGN,
        BinOp.DIV_ASSIGN,
        BinOp.MOD_ASSIGN,
        BinOp.ADD_ASSIGN,
        BinOp.SUB_ASSIGN,
        BinOp.BAND_ASSIGN,
        BinOp.BOR_ASSIGN,
        BinOp.BXOR_ASSIGN,
        BinOp.SHL_ASSIGN,
        BinOp.SHR_ASSIGN);

    for (BinOp bop : shouldBeSideEffecting) {
      assertTrue(bop.isSideEffecting());
    }

    for (BinOp bop : BinOp.values()) {
      if (!shouldBeSideEffecting.contains(bop)) {
        assertFalse(bop.isSideEffecting());
      }
    }

  }

  private String choose(boolean pred, String first, String second) {
    return pred ? first : second;
  }

  @Test
  public void getText() throws Exception {

    String programUsingGetText = getOpTesterProgram(true);
    String programUsingStrings = getOpTesterProgram(true);

    CompareAstsDuplicate.assertEqualAsts(programUsingGetText, programUsingStrings);

  }

  private String getOpTesterProgram(boolean p) {
    return "void main() {"
        + "  int a " + choose(p, BinOp.ASSIGN.getText(), "=") + "1, b " + choose(p, BinOp.ASSIGN.getText(), "=") + "2;"
        + "  bool x " + choose(p, BinOp.ASSIGN.getText(), "=") + " true, y " + choose(p, BinOp.ASSIGN.getText(), "=") + " false;"
        + "  "
        + "  a = 3" + choose(p, BinOp.COMMA.getText(), ",") + " 4;"
        + "  a = b " + choose(p, choose(p, BinOp.MOD.getText(), "X") + " a " + BinOp.MUL.getText(), "X") + " a;"
        + "  a = b " + choose(p, BinOp.DIV.getText(), "/") + " 2 " + choose(p, BinOp.ADD.getText(), "X") + " b " + choose(p, BinOp.SUB.getText(), "-") + " 3;"
        + "  a = a " + choose(p, BinOp.BAND.getText(), "&") + " b;"
        + "  a = a " + choose(p, BinOp.BOR.getText(), "|") + " b;"
        + "  a = a " + choose(p, BinOp.BXOR.getText(), "^") + " b;"
        + "  x = x " + choose(p, BinOp.LAND.getText(), "&&") + " y;"
        + "  x = x " + choose(p, BinOp.LOR.getText(), "||") + " y;"
        + "  x = x " + choose(p, BinOp.LXOR.getText(), "^^") + " y;"
        + "  a = a " + choose(p, BinOp.SHL.getText(), "<<") + " b;"
        + "  a = a " + choose(p, BinOp.SHR.getText(), ">>") + " b;"
        + "  x = a " + choose(p, BinOp.LT.getText(), "<") + " b;"
        + "  x = a " + choose(p, BinOp.GT.getText(), ">") + " b;"
        + "  x = a " + choose(p, BinOp.LE.getText(), "<=") + " b;"
        + "  x = a " + choose(p, BinOp.GE.getText(), ">=") + " b;"
        + "  x = a " + choose(p, BinOp.EQ.getText(), "==") + " b;"
        + "  x = a " + choose(p, BinOp.NE.getText(), "!=") + " b;"
        + "  a " + choose(p, BinOp.MUL_ASSIGN.getText(), "*=") + " b;"
        + "  a " + choose(p, BinOp.DIV_ASSIGN.getText(), "/=") + " b;"
        + "  a " + choose(p, BinOp.MOD_ASSIGN.getText(), "%=") + " b;"
        + "  a " + choose(p, BinOp.ADD_ASSIGN.getText(), "+=") + " b;"
        + "  a " + choose(p, BinOp.SUB_ASSIGN.getText(), "-=") + " b;"
        + "  a " + choose(p, BinOp.BAND_ASSIGN.getText(), "&=") + " b;"
        + "  a " + choose(p, BinOp.BOR_ASSIGN.getText(), "|=") + " b;"
        + "  a " + choose(p, BinOp.BXOR_ASSIGN.getText(), "^=") + " b;"
        + "  a " + choose(p, BinOp.SHL_ASSIGN.getText(), "<<=") + " b;"
        + "  a " + choose(p, BinOp.SHR_ASSIGN.getText(), ">>=") + " b;"
        + "}";
  }

}
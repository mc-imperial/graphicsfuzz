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

import static org.junit.Assert.*;

import com.graphicsfuzz.common.ast.CompareAstsDuplicate;
import com.graphicsfuzz.common.ast.TranslationUnit;
import com.graphicsfuzz.common.ast.expr.IntConstantExpr;
import com.graphicsfuzz.common.ast.visitors.StandardVisitor;
import com.graphicsfuzz.common.util.ParseHelper;
import org.junit.Test;

public class ExprCaseLabelTest {

  @Test
  public void testReplaceChild() throws Exception {
    final String before = "void main() {"
          + "  switch(0) {"
          + "    case 1:"
          + "      return;"
          + "    default:"
          + "      break;"
          + "  }"
          + "}";
    final String after = "void main() {"
          + "  switch(0) {"
          + "    case 2:"
          + "      return;"
          + "    default:"
          + "      break;"
          + "  }"
          + "}";

    final TranslationUnit tu = ParseHelper.parse(before, false);

    new StandardVisitor() {
      @Override
      public void visitExprCaseLabel(ExprCaseLabel exprCaseLabel) {
        super.visitExprCaseLabel(exprCaseLabel);
        exprCaseLabel.replaceChild(exprCaseLabel.getExpr(), new IntConstantExpr("2"));
      }
    }.visit(tu);

    CompareAstsDuplicate.assertEqualAsts(after, tu);
  }

}
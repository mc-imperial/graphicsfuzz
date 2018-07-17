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

package com.graphicsfuzz.common.ast;

import static org.junit.Assert.assertEquals;

import com.graphicsfuzz.common.tool.PrettyPrinterVisitor;
import com.graphicsfuzz.common.util.ParseHelper;
import com.graphicsfuzz.common.util.ParseTimeoutException;
import java.io.IOException;

/**
 * This class is deliberately a duplicate of another class, CompareAsts.
 * The ability to compare ASTs is really useful and is used in various projects.
 * It was deemed simplest to duplicate this code for the case when it is used in the
 * ast project itself.
 */
public class CompareAstsDuplicate {

  public static void assertEqualAsts(String first, String second)
        throws IOException, ParseTimeoutException {
    assertEquals(
          PrettyPrinterVisitor.prettyPrintAsString(ParseHelper.parse(first, false)),
          PrettyPrinterVisitor.prettyPrintAsString(ParseHelper.parse(second, false))
    );
  }

  public static void assertEqualAsts(String string, TranslationUnit tu)
        throws IOException, ParseTimeoutException {
    assertEqualAsts(string, PrettyPrinterVisitor.prettyPrintAsString(tu));
  }

  public static void assertEqualAsts(TranslationUnit first, TranslationUnit second)
        throws IOException, ParseTimeoutException {
    assertEqualAsts(PrettyPrinterVisitor.prettyPrintAsString(first),
          PrettyPrinterVisitor.prettyPrintAsString(second));
  }

}

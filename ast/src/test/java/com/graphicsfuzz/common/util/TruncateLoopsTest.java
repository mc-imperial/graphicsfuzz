package com.graphicsfuzz.common.util;


import com.graphicsfuzz.common.ast.CompareAstsDuplicate;
import com.graphicsfuzz.common.ast.TranslationUnit;
import com.graphicsfuzz.common.tool.PrettyPrinterVisitor;
import java.io.IOException;
import org.junit.Test;

public class TruncateLoopsTest {

  private static final String[] CONDITIONS = {"x < -(-20)", "20 > x", "x <= 20", "20 >= x",
      "x > -2", "-2 < x","x >= -2", "-2 <= x"};
  private static final String[] INCREMETS = {"x++", "++x", "x += 1", "x += -(-1)", "x += 5",
      "x--", "--x", "x -= 1", "x -= 5"};
  private static final String[] INIT_CONSTS = {"x = -1", "x = 0", "int x = 0", "x = -(+(-10))",
      "int x = 10"};

  @Test
  public void intConstantsTest() throws IOException, ParseTimeoutException {
    final String programBody =
        "void main() {"
            + "int u = 10;"
            + "int x;"
            + "  for($INIT; $COND; $INCREMENT) {"
            + "    u = u * 2;"
            + "  }"
            + "}";

    for (int cond_index = 0; cond_index < CONDITIONS.length; ++cond_index) {
      for (int incr_index = 0; incr_index < INCREMETS.length; ++incr_index) {
        for (int init_index = 0; init_index < INIT_CONSTS.length; ++init_index) {
          final boolean isSane = (cond_index < 4 && incr_index < 5)
              || (4 <= cond_index && 5 <= incr_index);
          testProgram(programBody
                  .replace("$INIT", INIT_CONSTS[init_index])
                  .replace("$COND", CONDITIONS[cond_index])
                  .replace("$INCREMENT", INCREMETS[incr_index]),
              isSane);

        }
      }
    }
  }

  private void testProgram(String program, boolean isSane) throws IOException, ParseTimeoutException {
    TranslationUnit tu =  ParseHelper.parse(program, false);
    new TruncateLoops(30, "webGL_", tu);
    if(isSane) {
      CompareAstsDuplicate.assertEqualAsts(program, tu);
    } else {
      assertProgramsNotEqual(program, tu);
    }
    tu =  ParseHelper.parse(program, false);
    new TruncateLoops(0, "webGL_", tu);
    assertProgramsNotEqual(program, tu);
  }

  private void assertProgramsNotEqual(String program, TranslationUnit otherProgram)
      throws IOException, ParseTimeoutException {
    assert !PrettyPrinterVisitor.prettyPrintAsString(ParseHelper.parse(program, false))
        .equals(PrettyPrinterVisitor.prettyPrintAsString(otherProgram));
  }

}

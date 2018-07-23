package com.graphicsfuzz.common.util;


import com.graphicsfuzz.common.ast.TranslationUnit;
import com.graphicsfuzz.common.tool.PrettyPrinterVisitor;
import java.io.IOException;
import org.junit.Test;

public class TruncateLoopsTest {

  private static final String[] CONDITIONS = {"x < 20", "x <= 20", "x > -2","x >= -2"}; //Add c op V case
  private static final String[] INCREMETS = {"x++", "++x", "x += 1", "x += 5",
      "x--", "--x", "x -= 1", "x -= 5"};
  private static final String[] INIT_CONSTS = {"0", "10"};

  @Test
  public void IntConstantsTest() throws IOException, ParseTimeoutException {
    final String programBody[] = {
        "void main() {"
            + "int u = 10;"
            + "  for(int x = $INIT; $COND; $INCREMENT) {"
            + "    u = u * 2;"
            + "  }"
            + "}",
        "void main() {"
            + "int u = 10;"
            + "int x;"
            + "  for(x = $INIT; $COND; $INCREMENT) {"
            + "    u = u * 2;"
            + "  }"
            + "}"
    };

    for (int cond_index = 0; cond_index < 4; ++cond_index) {
      for (int incr_index = 0; incr_index < 8; ++incr_index) {
        for (int init_index = 0; init_index < 2; ++init_index) {
          for (int program_index = 0; program_index < 2; ++program_index) {
            final boolean isSane = (cond_index < 2 && incr_index < 4)
                || (2 <= cond_index && 4 <= incr_index);
            testProgram(programBody[program_index]
                    .replace("$INIT", INIT_CONSTS[init_index])
                    .replace("$COND", CONDITIONS[cond_index])
                    .replace("$INCREMENT", INCREMETS[incr_index]),
                isSane);
          }
        }
      }
    }
  }

  private void testProgram(String program, boolean isSane) throws IOException, ParseTimeoutException {
    TranslationUnit tu =  ParseHelper.parse(program, false);
    new TruncateLoops(20, "webGL_", tu);
    if(isSane) {
      CompareAsts.assertEqualAsts(program, tu);
    } else {
      assertProgramsNotEqual(program, tu);
    }
    tu =  ParseHelper.parse(program, false);
    new TruncateLoops(1, "webGL_", tu);
    assertProgramsNotEqual(program, tu);
  }

  private void assertProgramsNotEqual(String program, TranslationUnit otherProgram)
      throws IOException, ParseTimeoutException {
    assert !PrettyPrinterVisitor.prettyPrintAsString(ParseHelper.parse(program, false))
        .equals(PrettyPrinterVisitor.prettyPrintAsString(otherProgram));
  }

}

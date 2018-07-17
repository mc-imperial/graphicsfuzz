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

package com.graphicsfuzz.security.comparison;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import com.graphicsfuzz.security.StreamTestUtils;
import com.graphicsfuzz.security.tool.screenmonitor.AbstractImageHandler;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public abstract class AbstractImageHandlerTest {

  AbstractImageHandler diffHandler;
  AbstractImageHandler sameHandler;
  File outDir;
  static BufferedImage WHITE = StreamTestUtils.createColorImg(Color.WHITE);
  static BufferedImage BLACK = StreamTestUtils.createColorImg(Color.BLACK);
  static Mat blankMask = new Mat();

  @Rule
  public TemporaryFolder testFolder = new TemporaryFolder();

  @Test
  public void differentImagesDetected() throws IOException {
    assertTrue(sameHandler.isDifferent(WHITE, BLACK, blankMask));
  }

  @Test
  public void sameImagesDetected() throws IOException {
    assertFalse(sameHandler.isDifferent(WHITE, WHITE, blankMask));
  }

  @Test
  public void savesIfDiff() throws IOException {
    diffHandler.run();
    System.out.println(outDir.list().length);
    StreamTestUtils.checkDirContains(outDir.getPath(), 1);
  }

  @Test
  public void doesNotSaveIfSame() throws IOException {
    sameHandler.run();
    System.out.println(outDir.list().length);
    StreamTestUtils.checkDirContains(outDir.getPath(), 0);
  }

}

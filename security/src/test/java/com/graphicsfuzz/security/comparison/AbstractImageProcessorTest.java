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

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import com.graphicsfuzz.security.StreamTestUtils;
import com.graphicsfuzz.security.parallel.IImageProcessor;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public abstract class AbstractImageProcessorTest {

  static IImageProcessor imageProcessor;
  static File passImage;
  static File failImage;
  static File outDir;
  static BufferedImage BLACK = StreamTestUtils.createColorImg(Color.BLACK);

  @ClassRule
  public static TemporaryFolder testFolder = new TemporaryFolder();

  void checkImage(File image, boolean expected) throws IOException {
    imageProcessor.addImage(image);
    imageProcessor.process();
    assertEquals(expected, imageProcessor.getResultAndFinish());
  }

  //Check that IImageProcessor check passes when it should
  @Test
  public void passImagePassesCheck() throws IOException {
    checkImage(passImage, false);
  }

  //Check that IImageProcessor check fails when it should
  @Test
  public void failImageFailsCheck() throws IOException {
    checkImage(failImage, true);
  }

  //Check that getResultAndFinish() returns false if process() isn't called
  @Test
  public void noProcessDoesNotFail() throws IOException {
    imageProcessor.addImage(failImage);
    assertFalse(imageProcessor.getResultAndFinish());
  }

  //Check that resetImages() correctly clears images
  @Test
  public void resetResetsImages() throws IOException {
    imageProcessor.addImage(failImage);
    imageProcessor.resetImages();
    checkImage(passImage, false);
  }

  //Check that getResultAndFinish will clear process results
  @Test
  public void finishResetsErrorsFound() throws IOException {
    checkImage(failImage, true);
    assertFalse(imageProcessor.getResultAndFinish());
  }

  //Check that resetImages does not clear process results
  @Test
  public void resetDoesNotResetErrorsFound() throws IOException {
    imageProcessor.addImage(failImage);
    imageProcessor.process();
    imageProcessor.resetImages();
    assertTrue(imageProcessor.getResultAndFinish());
  }

  @Test
  public void invalidImageDoesNotThrowError() throws IOException {
    imageProcessor.addImage(new File(outDir, "invalid.txt"));
    imageProcessor.process();
    assertFalse(imageProcessor.getResultAndFinish());
  }

}

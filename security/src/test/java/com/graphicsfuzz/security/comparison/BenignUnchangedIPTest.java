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
import com.graphicsfuzz.security.parallel.BenignUnchangedImageProcessor;
import com.graphicsfuzz.shadersets.ExactImageFileComparator;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BenignUnchangedIPTest extends AbstractImageProcessorTest {

  private static BufferedImage WHITE = StreamTestUtils.createColorImg(Color.WHITE);

  //Initialise vars
  @BeforeClass
  public static void init() throws IOException {
    outDir = testFolder.newFolder();

    passImage = new File(outDir, "pass.png");
    ImageIO.write(WHITE, "png", passImage);
    failImage = new File(outDir, "fail.png");
    ImageIO.write(BLACK, "png", failImage);

    imageProcessor = new BenignUnchangedImageProcessor(passImage,
        new FileWriter(testFolder.newFile()), new ExactImageFileComparator(false));
  }

  //Reset imageProcessor before each test (to give new images/resetImages errorsFound var)
  @Before
  public void before() {
    imageProcessor.resetImages();
  }

  //Check that only one image is held by BenignUnchangedImageProcessor at a time (used to check
  //benign shader images only and ignore malicious shader images)
  @Test
  public void addingMultipleImagesDoesNothing() throws IOException {
    imageProcessor.addImage(passImage);
    imageProcessor.addImage(failImage);
    imageProcessor.process();
    assertFalse(imageProcessor.getResultAndFinish());
  }

  //Check that calling process multiple times does not reset previous process results
  @Test
  public void processCallsCumulative() throws IOException {
    File[] images = {passImage, failImage};
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        imageProcessor.addImage(images[j]);
        imageProcessor.process();
        imageProcessor.resetImages();
      }
      assertTrue(imageProcessor.getResultAndFinish());
      images[0] = failImage;
      images[1] = passImage;
    }

  }

}

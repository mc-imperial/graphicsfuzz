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

import com.graphicsfuzz.security.parallel.PatternImageProcessor;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Tests are non deterministic (should pass but can fail by chance), run manually to test")
public class PatternImageProcessorTest extends AbstractImageProcessorTest {

  private static BufferedImage FEATURE = createImageWithFeatures();

  //Create an image with features so that a PatternImageProcessor can detect and match features
  private static BufferedImage createImageWithFeatures() {
    BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D g = image.createGraphics();

    g.setColor(Color.WHITE);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());

    int offset = 4;
    int width = image.getWidth() / 2 - offset * 2;
    int height = image.getHeight() / 2 - offset * 2;

    g.setColor(Color.BLACK);
    g.fill3DRect(offset, offset, width, height, true);

    g.setColor(Color.BLUE);
    g.fill3DRect(image.getWidth() / 2 + offset, image.getHeight() / 2 + offset, width, height, false);

    g.setColor(Color.RED);
    g.fill3DRect(offset, image.getHeight() / 2 + offset, width, height, false);

    g.setColor(Color.GREEN);
    g.fill3DRect(image.getWidth() / 2 + offset, offset, width, height, true);

    return image;
  }

  @BeforeClass
  public static void init() throws IOException {
    outDir = testFolder.newFolder();

    failImage = new File(outDir, "fail.png");
    ImageIO.write(FEATURE, "png", failImage);
    passImage = new File(outDir, "pass.png");
    ImageIO.write(BLACK, "png", passImage);

    imageProcessor = new PatternImageProcessor(failImage,
        new FileWriter(new File(outDir, "log.txt")));
  }

  @Before
  public void before() {
    imageProcessor.resetImages();
    imageProcessor.addImage(failImage);
  }

  //Check that if multiple images are added to a PatternImageProcessor, all are checked by process()
  @Test
  public void allImagesChecked() throws IOException {
    File[] images = {failImage, passImage};
    for (int i = 0; i < 2; i++) {
      imageProcessor.addImage(images[0]);
      imageProcessor.addImage(images[1]);
      imageProcessor.process();
      assertTrue(imageProcessor.getResultAndFinish());
      imageProcessor.resetImages();
      imageProcessor.addImage(failImage);
      images[0] = passImage;
      images[1] = failImage;
    }
  }

  //Check that calling process multiple times does not reset previous process results
  @Test
  public void processCallsCumulative() throws IOException {
    File[] images = {passImage, failImage};
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        imageProcessor.addImage(failImage);
        imageProcessor.addImage(images[j]);
        imageProcessor.process();
        imageProcessor.resetImages();
      }
      assertTrue(imageProcessor.getResultAndFinish());
      images[0] = failImage;
      images[1] = passImage;
    }

  }

  @Test
  public void transparentImagesPass() throws IOException {
    BufferedImage clear = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g = clear.createGraphics();
    AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
    g.setComposite(composite);
    g.setColor(new Color(0, 0, 0, 0));
    g.fillRect(0, 0, 600, 600);
    g.dispose();
    File img = new File(outDir, "clear.png");
    ImageIO.write(clear, "png", img);
    checkImage(img, false);
  }

}

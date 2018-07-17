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

package com.graphicsfuzz.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.graphicsfuzz.security.parallel.StreamCoordinator;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.rules.TemporaryFolder;

public class StreamTestUtils {

  public static int NUMSHADERS = 4;
  public static String URL = "http://path/to/server/manageAPI";
  public static String WEBTOKEN1 = "samsung_s8";
  public static String WEBTOKEN2 = "samsung_s81";
  public static String LIBGDXTOKEN1 = "surface_benign";
  public static String LIBGDXTOKEN2 = "surface_malicious";

  public static File initShaders(TemporaryFolder tempFolder) throws IOException {
    File shaders = tempFolder.newFolder();
    return createShaders(shaders, NUMSHADERS);
  }

  public static File createShaders(File loc, int n) throws IOException {
    for (int i = 0; i < n; i++) {
      File shader = new File(loc, "variant_" + i + ".frag");
      shader.createNewFile();
      File json = new File(loc, "variant_" + i + ".json");
      json.createNewFile();
      PrintWriter pw = new PrintWriter(json);
      pw.append("{ }");
      pw.close();
    }
    return loc;
  }

  public static void checkDirContains(String dir, int numFiles) {
    File dirF = new File(dir);
    assertTrue(dirF.exists());
    assertTrue(dirF.isDirectory());
    assertEquals(numFiles, dirF.list().length);
  }

  public static void checkShaderOuts(String outDirPath, int numShaderOuts, int numFiles) {
    for (int i = 0; i < numShaderOuts; i++) {
      StreamTestUtils.checkDirContains
          (outDirPath + StreamCoordinator.SHADERCOORDOUT + i + "/", numFiles);
    }
  }

  public static BufferedImage createColorImg(Color color) {
    BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D g = bufferedImage.createGraphics();
    g.setPaint(color);
    g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
    g.dispose();
    return bufferedImage;
  }

}

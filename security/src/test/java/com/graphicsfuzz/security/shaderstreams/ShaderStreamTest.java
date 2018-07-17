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

package com.graphicsfuzz.security.shaderstreams;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.graphicsfuzz.security.StreamTestUtils;
import com.graphicsfuzz.shadersets.IShaderDispatcher;
import com.graphicsfuzz.shadersets.LocalShaderDispatcher;
import com.graphicsfuzz.shadersets.RemoteShaderDispatcher;
import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

//NB: Working directory for test should be project root (/OGLTesting)
public class ShaderStreamTest {

  private static File shaders;
  private File outDir;
  private IShaderStream shaderStream;
  private IShaderDispatcher imageGenerator = new LocalShaderDispatcher(true);

  @ClassRule
  public static TemporaryFolder testFolder = new TemporaryFolder();

  @BeforeClass
  public static void init() throws IOException {
    shaders = StreamTestUtils.initShaders(testFolder);
  }

  @Before
  public void before() throws IOException {
    shaderStream = new SimpleShaderStream(shaders);
    outDir = testFolder.newFolder();
  }

  //Tests that all .frag files in a directory are added to stream, no other (.json) files added
  @Test
  public void getsCorrectFilesFromDir() throws Exception{
    int i = 0;
    while (shaderStream.hasNext()) {
      shaderStream.next();
      i++;
    }
    assertEquals(StreamTestUtils.NUMSHADERS, i);
  }

  //Test that a ShaderStream will populate from subDirs, not just top-level files in dir
  @Test
  public void getsFilesFromSubDirs() throws Exception{
    File dir = testFolder.newFolder();
    File subDir1 = new File(dir, "subDir1");
    subDir1.mkdirs();
    File subDir2 = new File(dir, "subDir2");
    subDir2.mkdirs();
    File[] subDirs = {subDir1, subDir2};
    for (File subDir : subDirs) {
      StreamTestUtils.createShaders(subDir, 1);
    }
    shaderStream = new SimpleShaderStream(dir);
    int i = 0;
    while (shaderStream.hasNext()) {
      shaderStream.runShaderAndIterate(new LocalShaderDispatcher(true), testFolder.newFolder());
      i++;
    }
    assertEquals(2, i);
  }

  //Test that a shader can be run and output an image
  @Test
  public void runShader() throws Exception {
    int numImages = 1;
    shaderStream.runShaderAndIterate(imageGenerator, outDir);
    StreamTestUtils.checkDirContains(outDir.getPath(), numImages + 1);
  }

  //Test that runShader doesn't iterate
  @Test
  public void runShaderDoesntIterate() throws Exception {
    for (int i = 1; i < StreamTestUtils.NUMSHADERS; i++) {
      shaderStream.next();
    }
    shaderStream.runShader(imageGenerator, outDir);
    assertTrue(shaderStream.hasNext());
    shaderStream.next();
    assertFalse(shaderStream.hasNext());
  }

  //Test that when a limit is given shaders are only run until the limit is reached
  @Test
  public void shadersRunUpToLimit() throws Exception{
    int numImages = 2;
    shaderStream.runStreamUntil(imageGenerator, outDir, numImages);
    StreamTestUtils.checkDirContains(outDir.getPath(), numImages + 1);
  }

  //Test that circular shader stream generates correct number of images (acts circular, doesn't
  //run forever when a limit is provided
  @Test
  public void circularShaderStreamRunsUpToLimit() throws Exception{
    int numImages = 6;
    shaderStream = new CircularShaderStream(shaders);
    shaderStream.runStreamUntil(imageGenerator, outDir, numImages);
    StreamTestUtils.checkDirContains(outDir.getPath(), numImages + 1);
  }

  //Test that when a simple shader stream is run on a directory, all shaders are run
  @Test
  public void allShadersRun() throws Exception{
    shaderStream.runStream(imageGenerator, outDir);
    StreamTestUtils.checkDirContains(outDir.getPath(), StreamTestUtils.NUMSHADERS + 1);
  }

  //Remote Tests - remove ignore tags to run
  //Server must be running, with one worker connected (token can be found in StreamTestUtils)

  //Check if a ShaderStream can produce an image using a RemoteShaderDispatcher
  @Ignore("Requires server and tokens to run")
  @Test
  public void shaderRunsRemotely() throws Exception{
    IShaderDispatcher remoteGenerator
        = new RemoteShaderDispatcher(StreamTestUtils.URL, StreamTestUtils.WEBTOKEN1);
    shaderStream.runShader(remoteGenerator, outDir);
    StreamTestUtils.checkDirContains(outDir.getPath(), 2);
  }

}

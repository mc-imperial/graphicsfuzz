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

import com.graphicsfuzz.security.StreamTestUtils;
import com.graphicsfuzz.security.parallel.IImageProcessor;
import com.graphicsfuzz.security.parallel.StreamCoordinator;
import com.graphicsfuzz.security.shaderstreams.CircularShaderStream;
import com.graphicsfuzz.security.shaderstreams.IShaderStream;
import com.graphicsfuzz.security.shaderstreams.SimpleShaderStream;
import com.graphicsfuzz.shadersets.IShaderDispatcher;
import com.graphicsfuzz.shadersets.LocalShaderDispatcher;
import com.graphicsfuzz.shadersets.RemoteShaderDispatcher;
import com.graphicsfuzz.shadersets.ShaderDispatchException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

//NB: Working directory should be project root (/OGLTesting)
public class SecurityTesterTest {

  private IShaderDispatcher localGenerator = new LocalShaderDispatcher(true);
  private IShaderDispatcher remoteGenerator1
      = new RemoteShaderDispatcher(StreamTestUtils.URL, StreamTestUtils.WEBTOKEN1);
  private IShaderDispatcher remoteGenerator2
      = new RemoteShaderDispatcher(StreamTestUtils.URL, StreamTestUtils.WEBTOKEN2);
  private IImageProcessor processor;

  private StreamCoordinator streamCoordinator;
  private static File shaders;
  private File outDir;
  private IShaderStream benign;
  private IShaderStream malicious;

  @ClassRule
  public static TemporaryFolder testFolder = new TemporaryFolder();

  //Clear working directory
  @BeforeClass
  public static void init() throws IOException {
    shaders = StreamTestUtils.initShaders(testFolder);
  }

  @Before
  public void before() throws InterruptedException, ShaderDispatchException, IOException {
    outDir = testFolder.newFolder();
    streamCoordinator = new StreamCoordinator();
    benign = new CircularShaderStream(shaders.getPath() + "/variant_0.frag");
    benign.runShader(localGenerator, outDir);
    processor = new DummyImageProcessor();
  }

  //Check that the securityTester properly runs shaders using the ShaderStream/ImageProcessor
  @Test
  public void securityTesterRunsShaders()
      throws InterruptedException, ExecutionException, ShaderDispatchException, IOException {
    final int numImages = 5;
    malicious = new SimpleShaderStream(shaders);
    streamCoordinator.add(benign, localGenerator);
    streamCoordinator.add(malicious, localGenerator);
    streamCoordinator.runSecurityTest(outDir.getPath(), numImages, processor);
    StreamTestUtils.checkShaderOuts(outDir.getPath(), 2, StreamTestUtils.NUMSHADERS + 1);
  }

  //Check that security tests work properly with only circular streams
  @Test
  public void securityTestCircular()
      throws InterruptedException, IOException, ShaderDispatchException, ExecutionException {
    final int numImages = 5;
    malicious = new CircularShaderStream(shaders);
    streamCoordinator.add(benign, localGenerator);
    streamCoordinator.add(malicious, localGenerator);
    streamCoordinator.runSecurityTest(outDir.getPath(), numImages, processor);
    StreamTestUtils.checkShaderOuts(outDir.getPath(), 2, numImages + 1);
  }

  @Test
  public void securityTestManyStreams()
      throws InterruptedException, ShaderDispatchException, IOException, ExecutionException {
    final int numImages = 2;
    final int numStreams = 3;
    streamCoordinator.add(benign, localGenerator);
    for (int i = 0; i < numStreams - 1; i++) {
      streamCoordinator.add(new CircularShaderStream(shaders), localGenerator);
    }
    streamCoordinator.runSecurityTest(outDir.getPath(), numImages, processor);
    StreamTestUtils.checkDirContains(outDir.getPath(),  numStreams + 3);
  }

  //Remote Tests - remove ignore tags to run
  //Server must be running, with two workers connected (tokens can be found in StreamTestUtils)

  //Check that security tests work remotely
  @Ignore("Requires server and tokens to run")
  @Test
  public void securityTestWorksRemotely()
      throws InterruptedException, IOException, ShaderDispatchException, ExecutionException {
    final int numImages = 3;
    malicious = new SimpleShaderStream(shaders);
    streamCoordinator.add(benign, remoteGenerator1);
    streamCoordinator.add(malicious, remoteGenerator2);
    streamCoordinator.runSecurityTest(outDir.getPath(), numImages, processor);
    StreamTestUtils.checkShaderOuts(outDir.getPath(), 2, StreamTestUtils.NUMSHADERS + 1);
  }


}

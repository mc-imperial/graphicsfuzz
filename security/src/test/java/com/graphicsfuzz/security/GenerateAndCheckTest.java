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

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

import com.graphicsfuzz.common.util.ParseTimeoutException;
import com.graphicsfuzz.security.parallel.StreamCoordinator;
import com.graphicsfuzz.security.parallel.StreamCoordinator.Type;
import com.graphicsfuzz.shadersets.ShaderDispatchException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class GenerateAndCheckTest {

  private File benignShader;
  private File maliciousShaders;
  private File workDir;
  private static int numShaders = 30;

  @Rule
  public  TemporaryFolder testFolder = new TemporaryFolder();

  //Create shaders in temporary folder
  @Before
  public void init() throws IOException {
    workDir = StreamTestUtils.initShaders(testFolder);
    benignShader = new File(workDir, "variant_0.frag");
    maliciousShaders = workDir;
    assertTrue(maliciousShaders.exists() && benignShader.exists());
  }

  //Run a security test on two workers locally (server not needed)
  @Test
  public void testShadersLocally()
      throws InterruptedException, ExecutionException, IOException, ParseTimeoutException, URISyntaxException, ShaderDispatchException {
    assertFalse(StreamCoordinator.runSecurityTestLocal(benignShader, maliciousShaders,
        workDir, numShaders, false, Type.BenignCheck));
  }

  //Remote Tests - remove ignore tags to run
  //Server must be running, with two workers connected (tokens can be found in StreamTestUtils)

  //Run a security test on two remote web workers
  @Ignore("Requires server and tokens to run")
  @Test
  public void testShadersWebClients()
      throws InterruptedException, ParseTimeoutException, IOException, ShaderDispatchException, ExecutionException, URISyntaxException {
    assertFalse(StreamCoordinator.runSecurityTestRemote(benignShader, maliciousShaders,
        workDir, numShaders, StreamTestUtils.URL, StreamTestUtils.WEBTOKEN1,
        StreamTestUtils.WEBTOKEN2, true, Type.BenignCheck));
  }

  //Run a security test on two remote desktop workers
  @Ignore("Requires server and tokens to run")
  @Test
  public void testShadersLibgdxClients()
      throws InterruptedException, ExecutionException, ShaderDispatchException, ParseTimeoutException, IOException, URISyntaxException {
    assertFalse(StreamCoordinator.runSecurityTestRemote(benignShader, maliciousShaders,
        workDir, numShaders, StreamTestUtils.URL, StreamTestUtils.LIBGDXTOKEN1,
        StreamTestUtils.LIBGDXTOKEN2, true, Type.BenignCheck));
  }

  //Run a security test with one desktop client and one web client
  @Ignore("Requires server and tokens to run")
  @Test
  public void testShadersWebAndLibgdx()
      throws InterruptedException, ExecutionException, ShaderDispatchException, ParseTimeoutException, IOException, URISyntaxException {
    assertFalse(StreamCoordinator.runSecurityTestRemote(benignShader, maliciousShaders,
        workDir, numShaders, StreamTestUtils.URL, StreamTestUtils.LIBGDXTOKEN1,
        StreamTestUtils.WEBTOKEN1, true, Type.BenignCheck));
  }

}

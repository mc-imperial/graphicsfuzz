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

package com.graphicsfuzz.security.parallel;

import com.graphicsfuzz.security.StreamTestUtils;
import com.graphicsfuzz.security.comparison.DummyImageProcessor;
import com.graphicsfuzz.security.shaderstreams.CircularShaderStream;
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

//NB: Working directory for test should be project root (/OGLTesting)
public class StreamCoordinatorTest {

  private StreamCoordinator streamCoordinator;
  private IShaderDispatcher localGenerator = new LocalShaderDispatcher(true);
  private IShaderDispatcher remoteGenerator1
      = new RemoteShaderDispatcher(StreamTestUtils.URL, StreamTestUtils.WEBTOKEN1);
  private IShaderDispatcher remoteGenerator2
      = new RemoteShaderDispatcher(StreamTestUtils.URL, StreamTestUtils.WEBTOKEN2);
  private static File shaders;
  private File outDir;


  @ClassRule
  public static TemporaryFolder testFolder = new TemporaryFolder();

  //Empty Output Directory
  @BeforeClass
  public static void init() throws IOException {
    shaders = StreamTestUtils.initShaders(testFolder);
  }

  //Set up streamCoordinator and output directory
  @Before
  public void before() throws IOException {
    streamCoordinator = new StreamCoordinator();
    outDir = testFolder.newFolder();
    streamCoordinator.setOutDir(outDir.getPath());
  }

  //Check the coordinator methods correctly run shaders and produce images/log files
  @Test
  public void coordinatorRunsShaders()
      throws InterruptedException, IOException, ShaderDispatchException, ExecutionException {
    int numImages = 1;
    streamCoordinator.add(new SimpleShaderStream(shaders), localGenerator);
    streamCoordinator.runInParallel(numImages, new DummyImageProcessor());
    StreamTestUtils.checkShaderOuts(outDir.getPath(), 1, numImages + 1);
  }

  //Check that running two streams in parallel produces expected output
  @Test
  public void streamsRunInParallel()
      throws InterruptedException, IOException, ShaderDispatchException, ExecutionException {
    int numImages = 3;
    for (int i = 0; i < 2; i++) {
      streamCoordinator.add(new SimpleShaderStream(shaders), localGenerator);
    }
    streamCoordinator.runInParallel(numImages, new DummyImageProcessor());
    StreamTestUtils.checkShaderOuts(outDir.getPath(), 2, numImages + 1);
  }

  //Test that when a simple stream and circular stream are provided, the streamCoordinator
  //terminates after the simple stream exhausts its shader set
  @Test
  public void circularStreamAndSimpleStreamTerminates()
      throws InterruptedException, IOException, ShaderDispatchException, ExecutionException {
    int numImages = 5;
    streamCoordinator.add(new SimpleShaderStream(shaders), localGenerator);
    streamCoordinator.add(new CircularShaderStream(shaders), localGenerator);
    streamCoordinator.runInParallel(numImages, new DummyImageProcessor());
    StreamTestUtils.checkShaderOuts(outDir.getPath(), 2, StreamTestUtils.NUMSHADERS + 1);
  }

  //Test that when using two circular streams termination only occurs when the limit is hit, not
  //when all shaders are generated
  @Test
  public void multipleCircularStreamsActCircular()
      throws InterruptedException, IOException, ShaderDispatchException, ExecutionException {
    int numImages = 5;
    for (int i = 0; i < 2; i++) {
      streamCoordinator.add(new CircularShaderStream(shaders), localGenerator);
    }
    streamCoordinator.runInParallel(numImages, new DummyImageProcessor());
    StreamTestUtils.checkShaderOuts(outDir.getPath(), 2, numImages + 1);
  }

  @Test
  public void runManyStreams()
      throws InterruptedException, ExecutionException, ShaderDispatchException, IOException {
    int numImages = 1;
    int numStreams = 3;
    for (int i = 0; i < numStreams; i++) {
      streamCoordinator.add(new CircularShaderStream(shaders), localGenerator);
    }
    streamCoordinator.runInParallel(numImages, new DummyImageProcessor());
    StreamTestUtils.checkDirContains(outDir.getPath(), numStreams + 1);
  }

  //Remote tests - remove ignore tags to run
  //Server must be running, with two workers connected (tokens can be found in StreamTestUtils)

  //Tests using a remoteImageGenerator with a streamCoordinator to produce an image on another
  //worker
  @Ignore("Requires server and tokens to run")
  @Test
  public void streamRemoteImageGenerator()
      throws InterruptedException, IOException, ShaderDispatchException, ExecutionException {
    int numImages = 1;
    streamCoordinator.add(new SimpleShaderStream(shaders), remoteGenerator1);
    streamCoordinator.runInParallel(numImages, new DummyImageProcessor());
    StreamTestUtils.checkShaderOuts(outDir.getPath(), 1, numImages + 1);
  }

  //Tests using a remoteImageGenerator and multiple workers to produce multiple images each
  //NB: Worker is currently the same, needs testing on 2 different workers (ideally on 1 machine)
  @Ignore("Requires server and tokens to run")
  @Test
  public void streamRemoteImageGeneratorMultiple()
      throws InterruptedException, IOException, ShaderDispatchException, ExecutionException {
    int numImages = 3;
    streamCoordinator.add(new SimpleShaderStream(shaders), remoteGenerator1);
    streamCoordinator.add(new SimpleShaderStream(shaders), remoteGenerator2);
    streamCoordinator.runInParallel(numImages, new DummyImageProcessor());
    StreamTestUtils.checkShaderOuts(outDir.getPath(), 2, numImages + 1);
  }

}

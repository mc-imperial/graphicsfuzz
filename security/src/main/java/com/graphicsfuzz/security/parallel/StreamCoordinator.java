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

import static com.graphicsfuzz.astfuzzer.tool.MainFuzzer.generateVariants;
import static com.graphicsfuzz.security.tool.FileGetter.getUniqueFile;

import com.graphicsfuzz.common.util.ParseTimeoutException;
import com.graphicsfuzz.security.shaderstreams.CircularShaderStream;
import com.graphicsfuzz.security.shaderstreams.IShaderStream;
import com.graphicsfuzz.security.shaderstreams.SimpleShaderStream;
import com.graphicsfuzz.shadersets.ExactImageFileComparator;
import com.graphicsfuzz.shadersets.IShaderDispatcher;
import com.graphicsfuzz.shadersets.LocalShaderDispatcher;
import com.graphicsfuzz.shadersets.RemoteShaderDispatcher;
import com.graphicsfuzz.shadersets.ShaderDispatchException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

//Used to run ShaderStreams in a synchronised manner (multiple streams render one image then block
//until all streams have rendered an image, and then move on to the next image)
public class StreamCoordinator {

  private List<StreamGeneratorPair> streamGeneratorPairs = new ArrayList<>();
  private String outDir = System.getProperty("user.dir");
  private File resDir;
  public static String SHADERCOORDOUT = "/shader-out-";
  private static int TIMEOUT = 30;

  //Initialise the working directory (and make unique)
  private static File initWorkDir(File workDir) {
    workDir = getUniqueFile(workDir.getParentFile(), workDir.getName(), "");
    workDir.mkdirs();
    return workDir;
  }

  public void setOutDir(String outDir) {
    this.outDir = outDir;
    this.resDir = new File(outDir, "/synchronised-shader-sets/");
    for (int i = 0; i < streamGeneratorPairs.size(); i++) {
      File out = new File(outDir, SHADERCOORDOUT + i);
      streamGeneratorPairs.get(i).setOutDir(out);
    }
  }

  //Add ShaderStream/ImageGenerator pairs to the StreamCoordinator
  public void add(IShaderStream shaderStream, IShaderDispatcher imageGenerator) {
    streamGeneratorPairs.add(new StreamGeneratorPair(shaderStream, imageGenerator,
        new File(outDir, SHADERCOORDOUT + streamGeneratorPairs.size())));
  }

  //Generate malicious shaders
  public static File generateShaders(File maliciousShader, File outDir, int numShaders)
      throws InterruptedException, URISyntaxException, ParseTimeoutException, IOException {
    if (!maliciousShader.isFile()) {
      throw new IllegalArgumentException("Malicious shader must be a shader file or a directory!");
    }
    generateVariants(maliciousShader, outDir, numShaders, "malicious_", false);
    return outDir;
  }

  //Runs a security test on two remote workers, loads a stream of shaders from
  //maliciousShader and uses that in a security test with the benignShader.
  //The temp variable being true will cause workDir to be deleted on completion.
  public static boolean runSecurityTestRemote(
      File benignShader,
      File maliciousShader,
      File workDir,
      int numShaders,
      String url,
      String benignToken,
      String maliciousToken,
      boolean temp,
      Type type)
      throws InterruptedException, ParseTimeoutException, IOException, ShaderDispatchException,
      ExecutionException, URISyntaxException {
    workDir = initWorkDir(workDir);


    IShaderDispatcher benignGenerator = new RemoteShaderDispatcher(url, benignToken);
    IShaderStream benignShaders = new CircularShaderStream(benignShader);

    IShaderDispatcher maliciousGenerator = new RemoteShaderDispatcher(url, maliciousToken);
    IShaderStream maliciousShaders = new SimpleShaderStream(maliciousShader);

    StreamCoordinator coordinator = new StreamCoordinator();
    coordinator.add(benignShaders, benignGenerator);
    coordinator.add(maliciousShaders, maliciousGenerator);

    File refImage =  benignShaders.runShader(benignGenerator, workDir);

    return runTestAndCleanUp(coordinator, refImage, workDir, numShaders, temp, type);
  }

  //Runs the same security test as above on two local workers
  public static boolean runSecurityTestLocal(
      File benignShader,
      File maliciousShader,
      File workDir,
      int numShaders,
      boolean temp,
      Type type)
      throws InterruptedException, ShaderDispatchException, IOException, ExecutionException,
        URISyntaxException, ParseTimeoutException {
    workDir = initWorkDir(workDir);


    IShaderDispatcher benignGenerator = new LocalShaderDispatcher(true);
    IShaderStream benignShaders = new CircularShaderStream(benignShader);

    IShaderDispatcher maliciousGenerator = new LocalShaderDispatcher(true);
    IShaderStream maliciousShaders = new SimpleShaderStream(maliciousShader);

    StreamCoordinator coordinator = new StreamCoordinator();
    coordinator.add(benignShaders, benignGenerator);
    coordinator.add(maliciousShaders, maliciousGenerator);

    File refImage =  benignShaders.runShader(benignGenerator, workDir);

    return runTestAndCleanUp(coordinator, refImage, workDir, numShaders, temp, type);
  }

  //Run the security test, delete working dirs if needed, return result
  private static boolean runTestAndCleanUp(
      StreamCoordinator coordinator,
      File refImage,
      File workDir,
      int numShaders,
      boolean temp,
      Type type)
      throws InterruptedException, ExecutionException, ShaderDispatchException, IOException {

    FileWriter writer = new FileWriter(new File(workDir, "security-test-results.txt"));
    IImageProcessor processor;
    switch (type) {
      case BenignCheck:
      {
        processor = new BenignUnchangedImageProcessor(refImage, writer,
            new ExactImageFileComparator(false));
        break;
      }
      case MaliciousCheck:
      {
        processor = new PatternImageProcessor(refImage, writer);
        break;
      }
      default:
      {
        System.err.println("Valid test type must be provided!");
        writer.flush();
        writer.close();
        return false;
      }
    }

    final boolean res
        = coordinator.runSecurityTest(workDir.getPath(), numShaders, processor);

    writer.flush();
    writer.close();

    if (temp) {
      FileUtils.deleteDirectory(workDir);
      File processing = new File("processing");
      if (processing.exists()) {
        FileUtils.deleteDirectory(new File("processing"));
      }
    }
    return res;
  }

  //Runs a security test using streamGeneratorPairs[0] for the benign ShaderStream
  //Compares outputs of this stream to refImage, and logs an error if the difference is greater
  //than diffThresh
  //Returns true if test runs successfully and errors are found (i.e. test is interesting)
  //Detailed results saved in /[workDir]/security-test-results.txt
  public boolean runSecurityTest(String workDir, int numImages, IImageProcessor processor)
      throws InterruptedException, ExecutionException, ShaderDispatchException, IOException {
    setOutDir(workDir);

    runInParallel(numImages, processor);

    boolean errorsFound = processor.getResultAndFinish();

    return errorsFound;
  }


  //Run all ShaderStreams in synchronicity using their paired ImageGenerator (each stream renders
  //one image, and once every pair has rendered an image they all move onto the next image)
  //The sets of which shaders are synchronised is stored in the folder "synchronised-shader-sets"
  public void runInParallel(int numImages, IImageProcessor processor)
      throws InterruptedException, ShaderDispatchException, IOException, ExecutionException {
    int count = 0;
    resDir.mkdirs();
    ExecutorService executorService = Executors.newFixedThreadPool(streamGeneratorPairs.size());

    while (hasNextAll() && count < numImages) {
      //Run one step's worth of shaders
      CountDownLatch latch = new CountDownLatch(streamGeneratorPairs.size());
      List<StreamTask> streamTasks = new ArrayList<>();

      for (StreamGeneratorPair streamGeneratorPair : streamGeneratorPairs) {
        streamTasks.add(new StreamTask(streamGeneratorPair, latch));
      }

      List<Future<File>> fileFutures = executorService.invokeAll(streamTasks);
      latch.await(TIMEOUT, TimeUnit.SECONDS);

      //Check for timeout
      if (latch.getCount() > 0) {
        System.err.println("Timeout in step " + count);
        continue;
      }

      //Process images, save details of synced file sets
      processAndSaveImageSet(fileFutures, count, processor);
      count++;
    }
    executorService.shutdownNow();
  }

  //Check the futures to find which shaders ran together, saving synced sets in
  //"synchronised-shader-sets"
  //Process step's set of images (e.g. check benign image correct)
  private void processAndSaveImageSet(List<Future<File>> fileFutures, int count,
      IImageProcessor processor)
      throws ExecutionException, InterruptedException, IOException {
    FileWriter writer;
    File img;
    String imgName;
    processor.resetImages();

    //Get first (benign) image details
    if (fileFutures.get(0).isDone()) {
      img = fileFutures.get(0).get();
      imgName = stripOutDir(img, new File(outDir));
      new File(resDir, imgName).getParentFile().mkdirs();

      File shaderLog = new File(resDir, FilenameUtils.removeExtension(imgName) + ".txt");
      writer = new FileWriter(shaderLog);
      writer.append("Images generated concurrently with benign image ")
          .append(imgName)
          .append(":\n");
      processor.addImage(img);
    } else {
      System.err.println("Benign image not done for step " + count);
      return;
    }

    //Get other (malicious) image(s) details
    for (int i = 1; i < fileFutures.size(); i++) {
      if (fileFutures.get(i).isDone()) {
        img = fileFutures.get(i).get();
        imgName = img.getPath();
        writer.append(imgName)
            .append("\n");
        processor.addImage(img);
      } else {
        String err = "Not all files done in step " + count + " - " + imgName + " not done!";
        System.err.println(err);
        writer.append(err);
        break;
      }
    }

    writer.close();

    processor.process();
  }

  //Get file path relative to outDir/ (or outDir/shader-outs-0/ if it matches)
  private String stripOutDir(File file, File outDir) {
    Path path = file.toPath();
    Path outPath = outDir.toPath();
    while (path.getName(0).equals((outPath.getName(0)))) {
      path = path.subpath(1, path.getNameCount());
      if (outPath.getNameCount() == 1) {
        if (new File(SHADERCOORDOUT + 0).toPath().endsWith(path.getName(0))) {
          path = path.subpath(1, path.getNameCount());
        }
        break;
      }
      outPath = outPath.subpath(1, outPath.getNameCount());
    }
    return path.toString();
  }

  //Checks if all ShaderStreams have a next shader (i.e. hasNext() == true for all)
  private boolean hasNextAll() {
    for (StreamGeneratorPair streamGeneratorPair : streamGeneratorPairs) {
      if (!streamGeneratorPair.hasNext()) {
        return false;
      }
    }
    return true;
  }

  //Class to contain ShaderStream/ImageGenerator pairs and an outDir (enough to render an image)
  private class StreamGeneratorPair {

    private IShaderStream shaderStream;
    private IShaderDispatcher imageGenerator;
    private File outDir;

    StreamGeneratorPair(IShaderStream shaderStream, IShaderDispatcher imageGenerator, File outDir) {
      this.shaderStream = shaderStream;
      this.imageGenerator = imageGenerator;
      this.outDir = outDir;
    }

    void setOutDir(File outDir) {
      this.outDir = outDir;
    }

    //Uses the ImageGenerator to run a shader from the ShaderStream and store the result in outDir
    File runNextShader() throws InterruptedException, ShaderDispatchException, IOException {
      outDir.mkdirs();
      return shaderStream.runShaderAndIterate(imageGenerator, outDir);
    }

    boolean hasNext() {
      return shaderStream.hasNext();
    }

  }

  public enum Type {
    BenignCheck, MaliciousCheck
  }

  //Runnable wrapper for a call to a StreamGeneratorPair's runShaderAndIterate() method
  //Also takes a latch for synchronisation
  private class StreamTask implements Callable<File> {

    private StreamGeneratorPair streamGeneratorPair;
    private CountDownLatch latch;

    public StreamTask(StreamGeneratorPair streamGeneratorPair, CountDownLatch latch) {
      this.streamGeneratorPair = streamGeneratorPair;
      this.latch = latch;
    }

    //Call runShaderAndIterate() on the given StreamGeneratorPair, and countdown the given latch
    @Override
    public File call() throws Exception {
      try {
        File img = streamGeneratorPair.runNextShader();
        latch.countDown();
        return img;
      } catch (Exception exception) {
        exception.printStackTrace();
        return null;
      }
    }
  }

}

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

import static com.graphicsfuzz.security.tool.FileGetter.getUniqueFile;
import static com.graphicsfuzz.server.thrift.JobStatus.COMPILE_ERROR;
import static com.graphicsfuzz.server.thrift.JobStatus.CRASH;
import static com.graphicsfuzz.server.thrift.JobStatus.LINK_ERROR;
import static com.graphicsfuzz.server.thrift.JobStatus.SANITY_ERROR;
import static com.graphicsfuzz.server.thrift.JobStatus.SUCCESS;
import static com.graphicsfuzz.server.thrift.JobStatus.UNEXPECTED_ERROR;

import com.graphicsfuzz.server.thrift.ImageJobResult;
import com.graphicsfuzz.server.thrift.JobStatus;
import com.graphicsfuzz.shadersets.IShaderDispatcher;
import com.graphicsfuzz.shadersets.ShaderDispatchException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

//Abstract implementation of shader stream object, provides basic default interactions with shader
//streams (e.g. setting shader location, running shaders)
//No iterator methods given
public abstract class AbstractShaderStream implements IShaderStream {

  private File shaderLoc;                           //Dir containing shaders/subDirs
  private FileWriter log;                           //Writer for log files
  List<File> shaders = new ArrayList<>();      //List of shader files from shaderLoc
  int curI = 0;                                     //Current location in list

  public AbstractShaderStream(File shaderLoc) {
    this.shaderLoc = shaderLoc;
    addShaders(shaderLoc);
  }

  public AbstractShaderStream(String shaderDir) {
    this(Paths.get(shaderDir).toFile());
  }

  //Loads fragment shaders from path/file supplied to constructor (recursively)
  private void addShaders(File shaderLoc) {
    if (!shaderLoc.exists()) {
      System.err.println("Shader directory not valid: " + shaderLoc.getPath());
      return;
    }

    if (shaderLoc.isFile() && shaderLoc.getPath().endsWith(".frag")) {
      shaders.add(shaderLoc);
      return;
    } else if (shaderLoc.isDirectory()) {

      File[] filesInDir = shaderLoc
          .listFiles();

      if (filesInDir != null) {
        for (File file : filesInDir) {
          addShaders(file);
        }
      }
    }

    Collections.sort(shaders);
  }

  //Creates/opens the log.txt file in current workDir for logging
  private void initLog(File workDir) throws IOException {
    workDir.mkdirs();
    File logFile = new File(workDir, "log.txt");
    if (!logFile.exists()) {
      logFile.createNewFile();
    }
    log = new FileWriter(new File(workDir, "log.txt"), true);
  }

  //Runs the current shader in the stream using imageGenerator, saves results in workDir, returns
  //output image, log messages are added to the log StringBuilder
  //Does not call next()
  private File executeCurrentShader(IShaderDispatcher imageGenerator, File workDir)
      throws ShaderDispatchException, InterruptedException, IOException {
    File shader = shaders.get(curI);

    //Get path of output (include subDirs of shaderLoc)
    String outPath;
    if (shaderLoc.isDirectory()) {
      Path path = shaderLoc.toPath().relativize(shader.toPath());
      System.out.println(shader.toPath());
      System.out.println(workDir);
      System.out.println(shaderLoc.getPath());
      System.out.println(path);
      System.out.println(new File(workDir, path.toString()).getParentFile());
      new File(workDir, path.toString()).getParentFile().mkdirs();
      outPath = FilenameUtils.removeExtension(path.toString());
    } else {
      outPath = FilenameUtils.removeExtension(shader.getName());
    }
    System.out.println(outPath);
    File outputImage = getUniqueFile(workDir, outPath, ".png");

    ImageJobResult res = imageGenerator.getImage(
        FilenameUtils.removeExtension(shader.getAbsolutePath()),
        outputImage, false);

    if (res.getStatus() == SUCCESS) {
      if (res.isSetPNG()) {
        FileUtils.writeByteArrayToFile(outputImage, res.getPNG());
      }
    } else if (isError(res.getStatus())) {
      File err = getUniqueFile(workDir, outPath, ".txt");
      FileWriter errWriter = new FileWriter(err);
      errWriter.write(res.getLog());
      errWriter.close();
    }

    log.append(FilenameUtils.removeExtension(shader.getName())).append(" result: ")
        .append(res.toString()).append("\n");
    return outputImage;
  }

  private boolean isError(JobStatus status) {
    return Arrays.asList(CRASH, COMPILE_ERROR, LINK_ERROR, SANITY_ERROR, UNEXPECTED_ERROR)
          .contains(status);
  }

  //IShaderStream Methods

  //Runs the current shader and iterates, writes to log file, returns image generated
  public File runShaderAndIterate(IShaderDispatcher imageGenerator, File workDir)
      throws InterruptedException, IOException, ShaderDispatchException {
    initLog(workDir);
    File image = executeCurrentShader(imageGenerator, workDir);
    next();
    log.close();
    return image;
  }

  //Runs the current shader without iterating
  public File runShader(IShaderDispatcher imageGenerator, File workDir)
      throws InterruptedException, IOException, ShaderDispatchException {
    initLog(workDir);
    File image = executeCurrentShader(imageGenerator, workDir);
    log.close();
    return image;
  }

  //Runs all shaders in the stream using imageGenerator, saves into workDir, writes a log.txt file
  //containing results of each shader
  public void runStream(IShaderDispatcher imageGenerator, File workDir)
      throws ShaderDispatchException, InterruptedException, IOException {
    initLog(workDir);
    while (hasNext()) {
      executeCurrentShader(imageGenerator, workDir);
      next();
    }
    log.close();
  }

  //Same as runStream but given a limit, runs until limit is hit or !hasNext()
  public void runStreamUntil(IShaderDispatcher imageGenerator, File workDir, int maxShadersRun)
      throws ShaderDispatchException, InterruptedException, IOException {
    int count = 0;
    initLog(workDir);
    while (count < maxShadersRun && hasNext()) {
      executeCurrentShader(imageGenerator, workDir);
      next();
      count++;
    }
    log.close();
  }

}

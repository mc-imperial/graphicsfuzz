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

import com.graphicsfuzz.security.tool.FileGetter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.DMatchVectorVector;
import org.bytedeco.javacpp.opencv_core.KeyPointVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_features2d;
import org.bytedeco.javacpp.opencv_features2d.BFMatcher;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_xfeatures2d.SIFT;

public class PatternImageProcessor implements IImageProcessor {

  private File referenceImage;
  private FileWriter writer;
  private Mat referenceMat;
  private List<File> images = new ArrayList<>();
  private boolean errorsFound = false;
  private KeyPointVector refKeyPointVector = null;
  private Mat refDesc = null;
  private BFMatcher bfMatcher = new BFMatcher();
  private SIFT sift = SIFT.create();

  //Number of good feature matches needed to infer a pattern in referenceImage appears in image
  //(higher means less likely to find a pattern)
  private static int THRESHOLD = 8;

  //Reference image only used to verify that benign shader output isn't a transparent image
  public PatternImageProcessor(File referenceImage, FileWriter writer) throws IOException {
    this.referenceImage = referenceImage;
    assert (isImage(this.referenceImage));
    this.writer = writer;

    referenceMat = opencv_imgcodecs.imread(
        this.referenceImage.getPath(), opencv_imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
    assert (!isTransparent(referenceMat) && !referenceMat.empty());
  }

  //Add an image to images
  @Override
  public void addImage(File image) {
    images.add(image);
  }

  //Clear images
  @Override
  public void resetImages() {
    images.clear();
  }

  //Check for patterns from images[0] (NOT from referenceImage) in all other Files in images
  @Override
  public void process() throws IOException {
    //Check referenceImage is a valid image file then load it
    if (!isImage(referenceImage)) {
      return;
    }
    referenceMat = opencv_imgcodecs.imread(
        images.get(0).getPath(), opencv_imgcodecs.CV_LOAD_IMAGE_UNCHANGED);

    //Don't check transparent mat for patterns as it has no features to check
    //Empty mat can't be used
    if (referenceMat.empty() || isTransparent(referenceMat)) {
      return;
    }

    for (int i = 1; i < images.size(); i++) {
      if (isImage(images.get(i)) && containsPatterns(images.get(i))) {
        errorsFound = true;
      }
    }
  }

  //Return result of process, reset errorsFound
  @Override
  public boolean getResultAndFinish() throws IOException {
    if (!errorsFound) {
      writer.append("No security errors found!");
      System.out.println("No security errors found!");
    } else {
      System.out.println("Security errors found! Malicious images contained patterns from reference"
          + " image, see result log file for details");
    }
    boolean res = errorsFound;
    errorsFound = false;
    return res;
  }

  //Check if a file is a valid image file (png)
  private boolean isImage(File file) throws IOException {
    return Files.probeContentType(file.toPath()).endsWith("png");
  }

  //Check if a (4 channel/CV_8UC4) mat is fully transparent
  private boolean isTransparent(Mat mat) {
    //Check type, if not 4 channel (4th = alpha) then return false
    if (mat.type() != opencv_core.CV_8UC4) {
      return false;
    }

    //Get channels, check alpha channel for non-zero values
    MatVector mv = new MatVector();
    opencv_core.split(mat, mv);
    return opencv_core.countNonZero(mv.get(mv.size() - 1)) == 0;
  }

  //Compare features of image with those of images[0] (NOT referenceImage)

  //THRESHOLD is the number of good feature matches needed to infer that image contains patterns
  //from images[0]

  //NB: Images with just 1 colour will usually have no features (causing this function to return
  //false), and if image is transparent it won't be compared at all (returning false)
  private boolean containsPatterns(File image) throws IOException {
    //Read image into Mat
    Mat mat = opencv_imgcodecs.imread(image.getPath(), opencv_imgcodecs.CV_LOAD_IMAGE_UNCHANGED);

    //Check if mat is transparent - cuts down on false positives from transparent images (which can
    //never contain patterns), and is quicker than feature matching so saves time in these cases
    //Check if mat is empty, empty mat can't be used
    if (isTransparent(mat) || mat.empty()) {
      System.out.println("Transparent/empty");
      return false;
    }

    //Initialise  and find keyPoints and descriptors
    if (refKeyPointVector == null || refDesc == null) {
      refKeyPointVector = new KeyPointVector();
      refDesc = new Mat();
      try {
        sift.detectAndCompute(referenceMat, new Mat(), refKeyPointVector, refDesc);
      } catch (Exception exception) {
        return false;
      }
    }
    opencv_core.KeyPointVector keyPointVector = new KeyPointVector();
    Mat desc = new Mat();
    try {
      sift.detectAndCompute(mat, new Mat(), keyPointVector, desc);
    } catch (Exception exception) {
      return false;
    }

    //Get matches of features between images (2 matches for each feature)
    opencv_core.DMatchVectorVector matches = new DMatchVectorVector();
    bfMatcher.knnMatch(refDesc, desc, matches, 2);

    //Get number of good matches
    int good = 0;

    //Based on D Lowe's papers on feature matching, a match from one feature to another is
    //determined to be a good match if it is a closer match than a fraction of the next closest
    //match to the same feature (this is why we need 2 matches for each feature)
    for (int i = 0; i < matches.size(); i++) {
      if (matches.get(i).get(0).distance() < matches.get(i).get(1).distance() * 0.9) {
        good++;
      }
    }

    //Handle error
    if (good > THRESHOLD) {
      writer.append("Security error: ")
          .append(image.getName())
          .append(" contains patterns from ")
          .append(images.get(0).getName())
          .append("\n")
          .append(Long.toString(matches.size()))
          .append(" feature matches detected\n")
          .append(Integer.toString(good))
          .append(" good feature matches detected (THRESHOLD is ")
          .append(Integer.toString(THRESHOLD))
          .append(")\n\n");

      //Uncomment next line to save matched image diagrams when matches occur
      //saveMatches("matches.png", referenceMat, refKeyPointVector, mat, keyPointVector, matches);
    }

    return good > THRESHOLD;
  }

  //Use in containsPatterns to visualise & save the matches between referenceImage and image
  //Not currently used anywhere but may be useful in future (e.g. for debugging)
  private File saveMatches(String fileName, Mat refMat, KeyPointVector refKeyPointVector, Mat mat,
      KeyPointVector keyPointVector, DMatchVectorVector matches) {
    Mat out = new Mat();
    opencv_features2d.drawMatchesKnn(refMat, refKeyPointVector, mat, keyPointVector, matches, out);
    File file = FileGetter.getUniqueFile(fileName);
    opencv_imgcodecs.imwrite(file.getPath(), out);
    return file;
  }

  //Use for testing/debugging
  public static void main(String[] args) throws IOException {
    File image = new File("unique_images/variant_8_49.png");
    FileWriter writer = new FileWriter(new File("pattern_detection_results.txt"));
    IImageProcessor processor = new PatternImageProcessor(image, writer);
    processor.addImage(image);
    processor.addImage(image);
    processor.process();
    processor.getResultAndFinish();
    writer.flush();
    writer.close();
  }

}
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

package com.graphicsfuzz.security.tool.screenmonitor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

//Contains many methods for comparing/handling BufferedImages/Mats directly
//Mainly created to circumvent saving files to disk then reloading when comparing
public abstract class ImageDifferentiator {

  //Util function, convert a buffered image to a Mat
  //Avoids extraneous disk usage (i.e. saving BufferedImage to disk and loading into a Mat)
  public static Mat bufferedImageToMat(BufferedImage image) throws IOException {
    Mat mat = new Mat(image.getHeight(), image.getWidth(), opencv_core.CV_8UC3);
    UByteRawIndexer indexer = mat.createIndexer();

    int[] rgbs = new int[image.getHeight() * image.getWidth()];
    image.getRGB(0, 0, image.getWidth(), image.getHeight(),
        rgbs, 0, image.getWidth());

    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        for (int channel = 0; channel < 3; channel++) {
          int index = x * image.getHeight() + y;

          Color color = new Color(rgbs[index], true);
          int component;
          switch (channel) {
            case 0: component = color.getBlue();
              break;
            case 1: component = color.getGreen();
              break;
            default: component = color.getRed();
              break;
          }
          indexer.put(3 * index + channel, component);
        }
      }
    }
    return mat;
  }

  //Get the mask made of the difference between two images
  public static Mat getMask(Mat img0, Mat img1) throws IOException {
    Mat mask = new Mat();
    opencv_core.bitwise_not(getDifferenceMat(img0, img1), mask);
    return mask;
  }

  //Overload, saves mask to file
  public static File getMask(File img0, File img1, File mask) throws IOException {
    Mat mat0 = bufferedImageToMat(ImageIO.read(img0));
    Mat mat1 = bufferedImageToMat(ImageIO.read(img1));
    Mat maskMat = getMask(mat0, mat1);
    opencv_imgcodecs.imwrite(mask.getPath(), maskMat);
    return mask;
  }

  //Apply the mask to an image in Mat format (cuts out black parts of mask from img)
  public static Mat applyMask(Mat img, Mat mask) {
    Mat result = new Mat();
    img.copyTo(result, mask);
    return result;
  }

  //Gets the difference between two images on a grayscale Mat
  public static Mat getDifferenceMat(Mat img0, Mat img1) throws IOException {
    Mat diff = new Mat();
    opencv_core.absdiff(img0, img1, diff);
    return diff;
  }

  //Converts the color type of a mat from CV_8UC3 (default, BGR) to CV_8UC1 (grayscale, 1 channel)
  private static Mat convertToGrayScale(Mat mat) {
    opencv_imgproc.cvtColor(mat, mat, opencv_imgproc.COLOR_BGR2GRAY);
    mat.convertTo(mat, opencv_core.CV_8UC1);
    return mat;
  }

  //Compare two images, returns true if any differences present
  public static boolean isDifferentExact(Mat img0, Mat img1, Mat mask) throws IOException {
    if (img0.cols() != img1.cols() || img0.rows() != img1.rows()) {
      return true;
    }
    Mat diff = getDifferenceMat(img0, img1);
    diff = applyMask(diff, mask);
    diff = convertToGrayScale(diff);
    return opencv_core.countNonZero(diff) != 0;
  }

  //Overload
  public static boolean isDifferentExact(BufferedImage img0, BufferedImage img1, Mat mask)
      throws IOException {
    return isDifferentExact(
        bufferedImageToMat(img0),
        bufferedImageToMat(img1),
        mask);
  }

  //Get the difference between two images using histogram
  //Returns a double, higher means more difference
  public static double getDifferenceHist(Mat img0, Mat img1, Mat mask) throws IOException {
    assert (img0.cols() == img1.cols() && img0.rows() == img1.rows());
    Mat diff = getDifferenceMat(img0, img1);
    diff = applyMask(diff, mask);
    diff = convertToGrayScale(diff);
    Mat hist = new opencv_core.Mat();

    opencv_imgproc.calcHist(
        new opencv_core.MatVector(new Mat[]{diff}),
        new int[]{0},
        new Mat(),
        hist,
        new int[]{50},
        new float[]{0, 256}
    );


    Mat blankHist = getBlankHist(img0.cols(), img0.rows());

    return opencv_imgproc.compareHist(hist, blankHist, opencv_imgproc.HISTCMP_CHISQR);
  }

  //Overload
  public static double getDifferenceHist(BufferedImage img0, BufferedImage img1, Mat mask)
      throws IOException {
    return getDifferenceHist(
        bufferedImageToMat(img0),
        bufferedImageToMat(img1),
        mask);
  }

  //Not as accurate as indirect, very slightly faster but not worth using
  public static double getDifferenceHistDirect(Mat img0, Mat img1, Mat mask) throws IOException {
    assert (img0.cols() == img1.cols() && img0.rows() == img1.rows());
    Mat[] imgs = {applyMask(img0, mask), applyMask(img1, mask)};
    Mat[] hists = {new Mat(), new Mat()};
    for (int i = 0; i < 2; i++) {
      opencv_imgproc.cvtColor(imgs[i], imgs[i], opencv_imgproc.COLOR_BGR2HSV);
      opencv_imgproc.calcHist(
          new opencv_core.MatVector(new opencv_core.Mat[]{imgs[i]}),
          new int[]{0, 1},
          new opencv_core.Mat(),
          hists[i],
          new int[]{50, 60},
          new float[]{0, 256, 0, 256}
      );
    }
    return opencv_imgproc.compareHist(hists[0], hists[1], opencv_imgproc.HISTCMP_CHISQR);
  }

  //Overload
  public static double getDifferenceHistDirect(BufferedImage img0, BufferedImage img1, Mat mask)
      throws IOException {
    return getDifferenceHistDirect(
        bufferedImageToMat(img0),
        bufferedImageToMat(img1),
        mask);
  }

  //Calculate a histogram of a black image for comparison
  private static Mat getBlankHist(int width, int height) {
    Mat zero = Mat.zeros(width, height, opencv_core.CV_8UC1).asMat();
    opencv_core.Mat histBlack = new opencv_core.Mat();
    opencv_imgproc.calcHist(
        new opencv_core.MatVector(new opencv_core.Mat[]{zero}),
        new int[]{0},
        new opencv_core.Mat(),
        histBlack,
        new int[]{50},
        new float[]{0, 256}
    );
    return histBlack;
  }

}

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

package com.graphicsfuzz.security.tool;

import com.graphicsfuzz.security.tool.screenmonitor.AbstractScreenMonitor;
import com.graphicsfuzz.security.tool.screenmonitor.ExactScreenMonitor;
import com.graphicsfuzz.security.tool.screenmonitor.HistScreenMonitor;
import com.graphicsfuzz.security.tool.screenmonitor.ImageDifferentiator;
import java.io.File;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparsers;

public class MonitorScreen {
  private static Namespace parse(String[] args) {

    ArgumentParser parser = ArgumentParsers.newArgumentParser("MonitorScreen")
        .defaultHelp(true)
        .description("Periodically take screenshots and save if changes occur on screen");

    Subparsers subparsers = parser.addSubparsers().dest("subparser");

    //getMask subparser
    ArgumentParser getMask = subparsers.addParser("mask")
        .aliases("m")
        .help("Create a mask with the differences between two images.")
        .defaultHelp(true);

    //Required Arguments
    getMask.addArgument("img1")
        .help("The first image to be used to make the mask")
        .type(File.class);
    getMask.addArgument("img2")
        .help("The second image to be used to make the mask")
        .type(File.class);

    //Optional arguments
    getMask.addArgument("--output")
        .help("Output filepath.")
        .setDefault(FileGetter.getUniqueFile("mask.png"))
        .type(File.class);


    //record subparser
    ArgumentParser record = subparsers.addParser("record")
        .aliases("r")
        .help("Record screen and save screenshots if changes occur.")
        .defaultHelp(true);

    // Required arguments
    record.addArgument("lifetime")
        .help("Number of seconds to record screenshots.")
        .type(Long.class);

    // Optional arguments
    record.addArgument("--delay")
        .help("Number of seconds to wait before recording.")
        .setDefault(0L)
        .type(long.class);
    record.addArgument("--exact")
        .help("Use exact image comparison instead of histogram comparison.")
        .setDefault(false)
        .type(boolean.class);
    record.addArgument("--threshold")
        .help("Threshold above which histogram comparisons will be considered different (not used"
            + " with exact image comparison).")
        .setDefault(50.0f)
        .type(float.class);
    record.addArgument("--working_directory")
        .help("Path of working directory.")
        .setDefault(new File("working"))
        .type(File.class);
    record.addArgument("--mask")
        .help("Path of mask image to apply to screenshots when calculating differences. Black "
            + "sections of the mask are not compared.")
        .type(File.class);
    record.addArgument("--ss_threads")
        .help("Number of CPU threads to spend on screenshots (higher will increase rate of "
            + "screenshots.")
        .setDefault(4)
        .type(int.class);
    record.addArgument("--io_threads")
        .help("Number of CPU threads to spend on IO (higher will lessen rate of screenshots).")
        .setDefault(1)
        .type(int.class);
    record.addArgument("--time_update_interval")
        .help("Number of seconds between recording time updates.")
        .setDefault(10)
        .type(int.class);

    try {
      return parser.parseArgs(args);
    } catch (ArgumentParserException exception) {
      exception.getParser().handleError(exception);
      System.exit(1);
      return null;
    }

  }

  public static void main(String[] args) {
    Namespace ns = parse(args);

    try {
      if (ns.get("subparser") == "record") {
        final Long lifetime = ns.get("lifetime");
        final long delay = ns.get("delay");
        final boolean exact = ns.get("exact");
        final float threshold = ns.get("threshold");
        final File workDir = ns.get("working_directory");
        final File mask = ns.get("mask");
        int ssThreads = ns.get("ss_threads");
        int ioThreads = ns.get("io_threads");
        final int timeUpdateInterval = ns.get("time_update_interval");

        if (lifetime == null) {
          System.err.println("lifetime must be supplied.");
          return;
        }
        ssThreads = (ssThreads < 1) ? 1 : ssThreads;
        ioThreads = (ioThreads < 1) ? 1 : ioThreads;

        AbstractScreenMonitor screenMonitor;
        if (exact) {
          screenMonitor = new HistScreenMonitor(
              workDir, lifetime, threshold, ssThreads, ioThreads, timeUpdateInterval);
        } else {
          screenMonitor = new ExactScreenMonitor(
              workDir, lifetime, ssThreads, ioThreads, timeUpdateInterval);
        }

        if (mask != null) {
          screenMonitor.calibrate(mask);
        }
        screenMonitor.start(delay);
      } else if (ns.get("subparser") == "mask") {
        final File img0 = ns.get("img1");
        final File img1 = ns.get("img2");
        final File output = ns.get("output");

        if (img0 == null || img1 == null) {
          System.err.println("img1 and img2 must both be provided.");
          return;
        }

        ImageDifferentiator.getMask(img0, img1, output);
      } else {
        System.err.println("Specify whether recording or getting a mask using 'm' or 'r'");
        return;
      }

    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }
}

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

import com.graphicsfuzz.security.tool.cursormover.CursorMoveTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class MoveCursor {

  private static Namespace parse(String[] args) {

    ArgumentParser parser = ArgumentParsers.newArgumentParser("MoveCursor")
        .defaultHelp(true)
        .description("Move the mouse at regular intervals");

    // Required arguments
    parser.addArgument("lifetime")
        .help("How long the program will run for (seconds)")
        .type(int.class);

    // Optional arguments
    parser.addArgument("--interval")
        .help("Interval between mouse movements (seconds)")
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
      final int lifetime = ns.get("lifetime");
      final int interval = ns.get("interval");

      ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
      //Schedule mouse movements
      executor.submit(new CursorMoveTask(interval, executor));

      //Schedule shutdown
      executor.schedule(() -> {
        System.out.println("Shutting down after this move...");
        executor.shutdown();
      }, lifetime, TimeUnit.SECONDS);
    } catch (Exception exception) {
      exception.printStackTrace();
    }

  }

}

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

package com.graphicsfuzz.security.tool.cursormover;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CursorMoveTask implements Runnable {

  private Robot robot;
  private int interval;
  private ScheduledExecutorService executor;

  public CursorMoveTask(int interval, ScheduledExecutorService executor) {
    this.interval = interval;
    this.executor = executor;
    try {
      this.robot = new Robot();
    } catch (AWTException exception) {
      this.robot = null;
      System.err.println(exception.getMessage());
    }
  }

  @Override
  public void run() {
    if (robot != null) {
      int coordX = getRandom(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
      int coordY = getRandom(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
      robot.mouseMove(coordX, coordY);
      System.out.println("Moving Mouse to (" + coordX + "," + coordY + ")\n");
    }

    executor.schedule(this, interval, TimeUnit.SECONDS);
  }

  private int getRandom(double max) {
    return (int)Math.round(Math.random() * max);
  }
}

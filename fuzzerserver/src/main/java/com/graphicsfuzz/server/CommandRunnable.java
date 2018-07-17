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

package com.graphicsfuzz.server;

import com.graphicsfuzz.server.thrift.FuzzerServiceManager;
import com.graphicsfuzz.server.thrift.FuzzerServiceManager.Iface;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class CommandRunnable implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(CommandRunnable.class);

  private final String name;
  private final List<String> command;
  private final String queueName;
  private final String logFile;
  private final FuzzerServiceManager.Iface fuzzerServiceManager;
  private final ICommandDispatcher commandDispatcher;

  public CommandRunnable(
        String name,
        List<String> command,
        String queueName,
        String logFile,
        Iface fuzzerServiceManager,
        ICommandDispatcher commandDispatcher) {
    this.name = name;
    this.command = command;
    this.queueName = queueName;
    this.logFile = logFile;
    this.fuzzerServiceManager = fuzzerServiceManager;
    this.commandDispatcher = commandDispatcher;
  }

  @Override
  public void run() {
    try {
      if (logFile != null) {
        // Due to the way the logging framework is configured (see log4j2.xml),
        // this will redirect the log to a file.
        MDC.put("logfile", logFile);
      }

      LOGGER.info(String.join(" ", command));

      commandDispatcher.dispatchCommand(command, fuzzerServiceManager);

    } catch (Throwable ex) {
      throw new RuntimeException("Command threw Throwable", ex);
    } finally {
      if (logFile != null) {
        MDC.remove("logfile");
      }
    }
  }

  public String getName() {
    return name;
  }

  public List<String> getCommand() {
    return command;
  }

  public String getLogFile() {
    return logFile;
  }
}

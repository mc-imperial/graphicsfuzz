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

import com.graphicsfuzz.common.util.ExecHelper;
import com.graphicsfuzz.common.util.ExecHelper.RedirectType;
import com.graphicsfuzz.common.util.ExecResult;
import com.graphicsfuzz.common.util.ToolPaths;
import com.graphicsfuzz.server.thrift.CommandInfo;
import com.graphicsfuzz.server.thrift.CommandResult;
import com.graphicsfuzz.server.thrift.FuzzerServiceManager;
import com.graphicsfuzz.server.thrift.ImageJob;
import com.graphicsfuzz.server.thrift.Job;
import com.graphicsfuzz.server.thrift.ServerInfo;
import com.graphicsfuzz.server.thrift.TokenNotFoundException;
import com.graphicsfuzz.server.thrift.WorkerInfo;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuzzerServiceManagerImpl implements FuzzerServiceManager.Iface {

  private static final Logger LOGGER = LoggerFactory.getLogger(FuzzerServiceManagerImpl.class);

  private FuzzerServiceImpl service;

  private final AtomicLong jobIdCounter;

  private final ICommandDispatcher commandDispatcher;

  public FuzzerServiceManagerImpl(FuzzerServiceImpl service,
        ICommandDispatcher commandDispatcher) {
    this.service = service;
    this.jobIdCounter = new AtomicLong();
    this.commandDispatcher = commandDispatcher;
  }

  @Override
  public void clearClientJobQueue(String forClient) throws TException {
    try {
      service.getClientWorkQueue(forClient).clearQueue();
    } catch (InterruptedException exception) {
      throw new TException(exception);
    }
  }

  @Override
  public Job submitJob(Job job, String forClient, int retryLimit) throws TException {
    LOGGER.info("submitJob {}", forClient);
    Job[] result = new Job[1];

    if (!service.getSessionMap().containsToken(forClient)) {
      throw new TokenNotFoundException().setToken(forClient);
    }

    service.getSessionMap().lockSessionAndExecute(forClient, session -> {
      session.jobQueue.add(new SingleJob(job, job1 -> {
        synchronized (result) {
          result[0] = job1;
          result.notifyAll();
        }
      }, jobIdCounter, retryLimit));
      return null;
    });

    synchronized (result) {
      while (result[0] == null) {
        try {
          result.wait();
        } catch (InterruptedException exception) {
          throw new TException(exception);
        }
      }
    }

    return result[0];
  }

  @Override
  public void queueCommand(
        String name,
        List<String> command,
        String queueName,
        String logFile)
        throws TException {

    if (name == null) {
      throw new TException("name must be set.");
    }

    if (command == null || command.isEmpty()) {
      throw new TException("Command must be a non-empty list.");
    }

    if (queueName == null) {
      throw new TException("queueName must be set.");
    }

    final String token = new String(queueName);

    if (!service.getSessionMap().containsToken(token)) {
      throw new TokenNotFoundException().setToken(token);
    }

    try {
      Path workDir = Paths.get(".").toAbsolutePath().normalize();

      if (logFile != null) {
        Path child = Paths.get(logFile).toAbsolutePath().normalize();
        if (!child.startsWith(workDir)) {
          throw new TException("Invalid log file location.");
        }
      }

      service.getSessionMap().lockSessionAndExecute(
          token, session -> {
            session.workQueue.add(new CommandRunnable(
                name,
                command,
                queueName,
                logFile,
                this,
                commandDispatcher));
            return null;
          });
    } catch (Exception ex) {
      LOGGER.error("", ex);
      throw new TException(ex);
    }
  }

  @Override
  public CommandResult executeCommand(String name, List<String> command) throws TException {
    try {
      ExecResult res =
            new ExecHelper(ToolPaths.getPythonDriversDir()).exec(
                  RedirectType.TO_BUFFER,
                  null,
                  true,
                  command.toArray(new String[0])
            );

      return new CommandResult().setOutput(res.stdout.toString())
            .setError(res.stderr.toString())
            .setExitCode(res.res);
    } catch (Exception ex) {
      throw new TException(ex);
    }
  }

  @Override
  public ServerInfo getServerState() throws TException {

    // Get reduction queue.
    List<CommandInfo> reductionQueue = new ArrayList<>();

    {
      List<String> reductionQueueStr = service.getReductionWorkQueue().queueToStringList();
      for (String reductionCommand : reductionQueueStr) {
        reductionQueue.add(new CommandInfo().setName(reductionCommand));
      }
    }

    // Get workers
    List<WorkerInfo> workers = new ArrayList<>();

    {
      Set<String> tokens = service.getSessionMap().getTokenSet();
      for (String token : tokens) {
        service.getSessionMap().lockSessionAndExecute(token, session -> {

          workers.add(
                new WorkerInfo()
                      .setToken(token)
                      .setCommandQueue(session.workQueue.getQueueAsCommandInfoList())
                      .setJobQueue(getJobQueueAsJobInfoList(session.jobQueue))
                      .setLive(session.isLive())
          );

          return null;
        });
      }
    }

    return
          new ServerInfo()
                .setReductionQueue(reductionQueue)
                .setWorkers(workers);
  }

  private List<String> getJobQueueAsJobInfoList(Queue<IServerJob> jobQueue) {
    List<String> res = new ArrayList<>();
    for (IServerJob job : jobQueue) {
      if (job instanceof SingleJob) {
        SingleJob sj = (SingleJob) job;
        if (sj.job.isSetImageJob()) {
          ImageJob ij = sj.job.getImageJob();

          StringBuilder infoString = new StringBuilder();
          if (ij.isSetName()) {
            infoString.append(ij.getName());
          }
          if (ij.isSetName()) {
            infoString.append(ij.getName());
            infoString.append("; ");
          }
          res.add(infoString.toString());
          continue;
        }
      }
      // otherwise:
      res.add(job.toString());
    }
    return res;
  }

}

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

import com.graphicsfuzz.server.thrift.ImageJobResult;
import com.graphicsfuzz.server.thrift.Job;
import com.graphicsfuzz.server.thrift.JobStatus;
import com.graphicsfuzz.server.thrift.ResultConstant;
import com.graphicsfuzz.server.thrift.SkipJob;
import java.util.concurrent.atomic.AtomicLong;

public class SingleJob implements IServerJob {

  @FunctionalInterface
  public interface ISingleJobCompleter {

    void completeJob(Job job);
  }

  public final Job job;
  private Job skipJob;
  private ISingleJobCompleter completer;

  private final AtomicLong skipJobIdCounter;

  private int counter;
  private final int limit;

  public SingleJob(Job job, ISingleJobCompleter completer, AtomicLong skipJobIdCounter,
      int retryLimit) {
    this.job = job;
    this.completer = completer;
    this.skipJobIdCounter = skipJobIdCounter;
    this.limit = retryLimit + 1;
  }

  @Override
  public Job getJob() throws ServerJobException {
    if (counter + 1 >= limit) {
      skipJob = new Job()
          .setJobId(skipJobIdCounter.incrementAndGet())
          .setSkipJob(new SkipJob());
      job.getImageJob()
          .setResult(
              new ImageJobResult()
                  .setStatus(JobStatus.SKIPPED)
                  .setLog(ResultConstant.SKIPPED.toString() + "\n"));
      return skipJob;
    } else {
      ++counter;
    }
    return job;
  }

  @Override
  public boolean finishJob(Job returnedJob) throws ServerJobException {

    if (skipJob != null) {
      if (returnedJob.getJobId() != skipJob.getJobId()) {
        throw new ServerJobException("Client tried to finish a job that did not match"
            + "the current skip job.");
      }
      if (job == null) {
        // this call to jobDone() may be due to an old crash, in which case skipjob is null,
        // and so is job. Hence, complete the returnedJob.
        completer.completeJob(returnedJob);
      } else {
        completer.completeJob(job);
      }
      return true;
    }

    if (returnedJob.getJobId() != job.getJobId()) {
      throw new ServerJobException("Client tried to finish a job that did not match"
          + "the currently queued job.");
    }
    completer.completeJob(returnedJob);
    return true;
  }
}

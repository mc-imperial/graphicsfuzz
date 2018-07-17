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

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

public final class SessionMap {

  public static class Session {

    public final Queue<IServerJob> jobQueue = new ArrayDeque<>();
    public String platformInfo;
    private final Object mutex = new Object();
    private volatile long touched = System.currentTimeMillis();

    private static final long FIVE_MINUTES = 5 * 60 * 1000;

    public void touch() {
      touched = System.currentTimeMillis();
    }

    public boolean isLive() {
      return System.currentTimeMillis() - touched < FIVE_MINUTES;
    }

    // Uses its own internal mutex:
    public final WorkQueue workQueue;

    /**
     * Used as dummy object.
     */
    public Session() {
      workQueue = null;
    }

    public Session(
        String token,
        String platformInfo,
        ExecutorService executorService) {
      this.platformInfo = platformInfo;
      workQueue = new WorkQueue(executorService, "WorkQueue(" + token + ")");
    }
  }

  @FunctionalInterface
  public interface SessionWorkerEx<T, E extends Throwable> {

    T go(Session session) throws E;
  }

  private final ConcurrentMap<String, Session> sessions = new ConcurrentHashMap<>();

  public Set<String> getTokenSet() {
    return Collections.unmodifiableSet(sessions.keySet());
  }

  public boolean containsToken(String token) {
    return sessions.containsKey(token);
  }

  public boolean isLive(String token) {
    Session session = this.sessions.get(token);
    if (session == null) {
      return false;
    }
    return session.isLive();
  }

  /**
   * @return true if token was absent and so has been put into the map.
   */
  public boolean putIfAbsent(String token, Session session) {
    return sessions.putIfAbsent(token, session) == null;
  }

  public void replace(String token, Session oldSession, Session session) {
    sessions.replace(token, oldSession, session);
  }

  public void remove(String token) {
    sessions.remove(token);
  }

  public WorkQueue getWorkQueue(String token) {
    return sessions.get(token).workQueue;
  }

  public <T, E extends Throwable> T lockSessionAndExecute(String token,
      SessionWorkerEx<T, E> sessionWorker) throws E {
    Session session = sessions.get(token);
    synchronized (session.mutex) {
      return sessionWorker.go(session);
    }
  }


}

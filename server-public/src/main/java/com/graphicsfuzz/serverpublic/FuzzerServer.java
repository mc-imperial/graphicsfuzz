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

package com.graphicsfuzz.serverpublic;

import com.graphicsfuzz.common.util.ToolPaths;
import com.graphicsfuzz.server.FileDownloadServlet;
import com.graphicsfuzz.server.FuzzerServiceImpl;
import com.graphicsfuzz.server.FuzzerServiceManagerImpl;
import com.graphicsfuzz.server.LocalArtifactManager;
import com.graphicsfuzz.server.thrift.FuzzerService;
import com.graphicsfuzz.server.thrift.FuzzerServiceManager;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FuzzerServer {

  private static final Logger LOGGER = LoggerFactory.getLogger(FuzzerServer.class);
  private final String workingDir = "";
  private final String shaderSetsDir = "shadersets";
  private final String processingDir = "processing";

  private final ExecutorService executorService = Executors.newCachedThreadPool();

  private final int port;

  public FuzzerServer(int port) {
    this.port = port;
  }

  public void start() throws Exception {

    FuzzerServiceImpl fuzzerService = new FuzzerServiceImpl(
        new LocalArtifactManager(
            Paths.get(workingDir, shaderSetsDir).toString(),
            Paths.get(workingDir, processingDir).toString()
        ),
        Paths.get(workingDir, processingDir).toString(),
        executorService);

    FuzzerService.Processor processor =
        new FuzzerService.Processor<FuzzerService.Iface>(fuzzerService);

    FuzzerServiceManagerImpl fuzzerServiceManager = new FuzzerServiceManagerImpl(fuzzerService,
          new PublicServerCommandDispatcher());
    FuzzerServiceManager.Processor managerProcessor =
        new FuzzerServiceManager.Processor<FuzzerServiceManager.Iface>(fuzzerServiceManager);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");

    {
      ServletHolder sh = new ServletHolder();
      sh.setServlet(new TServlet(processor, new TBinaryProtocol.Factory()));
      context.addServlet(sh, "/request");
    }
    {
      ServletHolder serveltHolderJson = new ServletHolder();
      serveltHolderJson.setServlet(new TServlet(processor, new TJSONProtocol.Factory()));
      context.addServlet(serveltHolderJson, "/requestJSON");
    }

    {
      ServletHolder shManager = new ServletHolder();
      shManager.setServlet(new TServlet(managerProcessor, new TBinaryProtocol.Factory()));
      context.addServlet(shManager, "/manageAPI");
    }

    final String staticDir = ToolPaths.getStaticDir();
    context.addServlet(
          new ServletHolder(
                new FileDownloadServlet(
                    (pathInfo, token) -> Paths.get(staticDir, pathInfo).toFile(), staticDir)),
          "/static/*");

    HandlerList handlerList = new HandlerList();
    handlerList.addHandler(context);

    GzipHandler gzipHandler = new GzipHandler();
    gzipHandler.setHandler(handlerList);

    Server server = new Server(port);

    server.setHandler(gzipHandler);
    server.start();
    server.join();
  }
}

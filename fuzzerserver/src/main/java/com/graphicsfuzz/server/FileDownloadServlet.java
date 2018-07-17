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

import static com.graphicsfuzz.server.thrift.FuzzerServiceConstants.DOWNLOAD_FIELD_NAME_TOKEN;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

public class FileDownloadServlet extends HttpServlet {

  @FunctionalInterface
  public interface DownloadProcessor {

    /**
     * @return The file to write to.
     */
    File processDownload(String pathInfo, String token) throws DownloadException;
  }

  private final DownloadProcessor downloadProcessor;

  private final Path directoryRoot;

  /**
   * @param directoryRoot A safety precaution: only files inside this directory can be accessed.
   *                      Downloading files above this directory is disallowed.
   */
  public FileDownloadServlet(DownloadProcessor downloadProcessor, String directoryRoot) {
    this.downloadProcessor = downloadProcessor;
    this.directoryRoot = Paths.get(directoryRoot).toAbsolutePath().normalize();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String pathInfo = request.getPathInfo();
    if (pathInfo == null) {
      throw new ServletException("Missing path!");
    }
    pathInfo = pathInfo.substring(1);
    File file = null;
    String token = request.getParameter(DOWNLOAD_FIELD_NAME_TOKEN);
    try {
      file = downloadProcessor.processDownload(pathInfo, token);
    } catch (DownloadException exception) {
      throw new ServletException(exception);
    }
    Path pathOfFile = file.toPath().toAbsolutePath();
    if (!pathOfFile.startsWith(directoryRoot)) {
      throw new IOException("Invalid path!");
    }

    try (FileInputStream fis = new FileInputStream(file)) {
      response.setStatus(HttpServletResponse.SC_OK);
      try (OutputStream out = response.getOutputStream()) {
        IOUtils.copy(fis, out);
      }
    }

  }
}

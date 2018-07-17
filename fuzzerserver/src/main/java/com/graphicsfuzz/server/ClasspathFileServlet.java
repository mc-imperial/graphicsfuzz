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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;


public class ClasspathFileServlet extends HttpServlet {

  private String directory = "ogltestingstatic";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String pathInfo = request.getPathInfo();
    if (pathInfo == null) {
      throw new ServletException("Missing path!");
    }

    // Here, we try hard to make sure users cannot do ".." to access the class files
    // of our server. I think Jetty might guard against this anyway.

    // pathInfo example: "/index.html"

    if (pathInfo.contains("\\") || pathInfo.contains("%") || pathInfo.contains("/../")) {
      throw new RuntimeException("Invalid URL");
    }
    pathInfo = "/" + pathInfo.substring(1);
    pathInfo = Paths.get(pathInfo).normalize().toString();
    pathInfo = pathInfo.replaceAll("\\\\", "/");

    if (!pathInfo.startsWith("/") || pathInfo.contains("/../")) {
      throw new RuntimeException("Invalid URL");
    }

    pathInfo = directory + pathInfo;

    ClassLoader cl = Thread.currentThread().getContextClassLoader();

    try (InputStream is = cl.getResourceAsStream(pathInfo)) {
      if (is == null) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      response.setStatus(HttpServletResponse.SC_OK);
      try (OutputStream out = response.getOutputStream()) {
        IOUtils.copy(is, out);
      }
    }
  }

}

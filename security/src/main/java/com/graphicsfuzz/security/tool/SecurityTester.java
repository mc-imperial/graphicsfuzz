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

import com.graphicsfuzz.security.parallel.StreamCoordinator;
import com.graphicsfuzz.security.parallel.StreamCoordinator.Type;
import java.io.File;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparsers;

public abstract class SecurityTester {

  private static Namespace parse(String[] args) {

    ArgumentParser parser = ArgumentParsers.newArgumentParser("SecurityTester")
        .defaultHelp(true)
        .description("Run a security test on two workers using benign and malicious shaders");

    Subparsers subparsers = parser.addSubparsers().dest("subparser");

    //Remote security tester subparser
    ArgumentParser remote = subparsers.addParser("remote")
        .aliases("r")
        .help("Run the security test remotely.");

    // Required arguments
    remote.addArgument("benign_shader")
        .help("Path of benign fragment shader to be rendered.")
        .type(File.class);
    remote.addArgument("malicious_shader")
        .help("Path of malicious fragment shader to be fuzzed.")
        .type(File.class);
    remote.addArgument("benign_token")
        .help("Token for benign shader worker.")
        .type(String.class);
    remote.addArgument("malicious_token")
        .help("Token for malicious shader worker.")
        .type(String.class);

    // Optional arguments
    remote.addArgument("--working_directory")
        .help("Path of working directory.")
        .setDefault(new File("working"))
        .type(File.class);
    remote.addArgument("--number_of_shaders")
        .help("How many malicious shaders you want to test.")
        .setDefault(25)
        .type(int.class);
    remote.addArgument("--server")
        .help("URL of the server.")
        .setDefault("http://localhost:8080")
        .type(String.class);
    remote.addArgument("--temporary")
        .help("Delete the working directory after testing.")
        .setDefault(false)
        .type(boolean.class);
    remote.addArgument("--type")
        .help("Type of security test to run\n\t0 = check benign images\n\t 1 = check malicious "
            + "images.")
        .setDefault(0)
        .type(int.class);

    //Local security test subparser
    ArgumentParser local = subparsers.addParser("local")
        .aliases("l")
        .help("Run a security test locally.");

    //Required Arguments
    local.addArgument("benign_shader")
        .help("Path of benign fragment shader to be rendered.")
        .type(File.class);
    local.addArgument("malicious_shader")
        .help("Path of malicious shader(s) to be rendered.")
        .type(File.class);

    //Optional Arguments
    local.addArgument("--number_of_shaders")
        .help("Number of shaders to be tested.")
        .setDefault(25)
        .type(int.class);
    local.addArgument("--working_directory")
        .help("Path to working directory.")
        .setDefault(new File("working"))
        .type(File.class);
    local.addArgument("--temporary")
        .help("Delete the working directory after testing.")
        .setDefault(false)
        .type(boolean.class);
    local.addArgument("--type")
        .help("Type of security test to run\n\t0 = check benign images\n\t 1 = check malicious "
            + "images.")
        .setDefault(0)
        .type(int.class);

    //Malicious shader generator subparser
    ArgumentParser generate = subparsers.addParser("generate")
        .aliases("g")
        .help("Generate malicious shaders.");

    //Required Arguments
    generate.addArgument("malicious_shader")
        .help("Path of malicious shader to be fuzzed.")
        .type(File.class);

    //Optional Arguments
    generate.addArgument("--output_directory")
        .help("Path to output directory.")
        .setDefault("malicious_shaders")
        .type(File.class);
    generate.addArgument("--number_of_shaders")
        .help("Number of shaders to be produced.")
        .setDefault(25)
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
      final File maliciousShader = ns.get("malicious_shader");
      if (ns.get("subparser") == "generate") {
        final File outDir = ns.get("output_directory");
        final int numShaders = ns.get("number_of_shaders");

        if (maliciousShader == null) {
          System.err.println("Malicious shader must be provided!");
          return;
        }

        StreamCoordinator.generateShaders(maliciousShader, outDir, numShaders);

      } else {
        final File benignShader = ns.get("benign_shader");
        final File workDir = ns.get("working_directory");
        final int numShaders = ns.get("number_of_shaders");
        final boolean temp = ns.get("temporary");
        final int type = ns.get("type");

        StreamCoordinator.Type testType;
        switch (type) {
          case 0:
          {
            testType = Type.BenignCheck;
            break;
          }
          case 1:
          {
            testType = Type.MaliciousCheck;
            break;
          }
          default:
          {
            System.err.println("Type must be 0 or 1!");
            return;
          }
        }

        if (ns.get("subparser") == "remote") {
          final String url = ns.get("server");
          final String benignToken = ns.get("benign_token");
          final String maliciousToken = ns.get("malicious_token");

          if (benignShader == null || maliciousShader == null
              || benignToken == null || maliciousToken == null) {
            System.err.println("benign_shader, malicious_shader, benign_token and "
                + "malicious_token must be supplied.");
            return;
          }

          StreamCoordinator
              .runSecurityTestRemote(benignShader, maliciousShader, workDir,
                  numShaders, url, benignToken, maliciousToken, temp, testType);
        } else {
          if (benignShader == null || maliciousShader == null) {
            System.err.println("benign_shader and malicious_shader must be supplied.");
          }
          StreamCoordinator.runSecurityTestLocal(benignShader, maliciousShader,
              workDir, numShaders, temp, testType);
        }
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

}

package com.qc.cli;

import com.qc.cli.commands.CookieReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
public class CookieProcessorApplication {

  public static void main(String[] args) {
    SpringApplication.run(CookieProcessorApplication.class, args);
    int execute = new CommandLine(new CookieReader()).execute(args);
    System.exit(execute);
  }

}

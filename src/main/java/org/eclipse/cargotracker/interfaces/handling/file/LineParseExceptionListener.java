package org.eclipse.cargotracker.interfaces.handling.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.batch.api.chunk.listener.SkipReadListener;
import jakarta.batch.runtime.context.JobContext;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Dependent
@Named("LineParseExceptionListener")
public class LineParseExceptionListener implements SkipReadListener {

  private static final String FAILED_DIRECTORY = "failed_directory";

  @Inject private Logger logger;

  @Inject private JobContext jobContext;

  @Override
  public void onSkipReadItem(Exception e) throws Exception {
    File failedDirectory = new File(jobContext.getProperties().getProperty(FAILED_DIRECTORY));

    if (!failedDirectory.exists()) {
      failedDirectory.mkdirs();
    }

    EventLineParseException parseException = (EventLineParseException) e;

    logger.log(Level.WARNING, "Problem parsing event file line", parseException);

    try (PrintWriter failed =
        new PrintWriter(
            new BufferedWriter(
                new FileWriter(
                    new File(
                        failedDirectory,
                        "failed_"
                            + jobContext.getJobName()
                            + "_"
                            + jobContext.getInstanceId()
                            + ".csv"),
                    true)))) {
      failed.println(parseException.getLine());
    }
  }
}

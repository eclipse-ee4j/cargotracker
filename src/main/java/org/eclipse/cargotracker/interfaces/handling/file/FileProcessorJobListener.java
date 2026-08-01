package org.eclipse.cargotracker.interfaces.handling.file;

import jakarta.batch.api.listener.JobListener;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent
@Named("FileProcessorJobListener")
public class FileProcessorJobListener implements JobListener {

  @Inject private Logger logger;

  @Override
  public void beforeJob() throws Exception {
    logger.log(Level.INFO, "Handling event file processor batch job starting at {0}", new Date());
  }

  @Override
  public void afterJob() throws Exception {
    logger.log(Level.INFO, "Handling event file processor batch job completed at {0}", new Date());
  }
}

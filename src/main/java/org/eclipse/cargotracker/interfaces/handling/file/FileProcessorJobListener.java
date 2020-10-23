package org.eclipse.cargotracker.interfaces.handling.file;

import javax.batch.api.listener.JobListener;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent
@Named("FileProcessorJobListener")
public class FileProcessorJobListener implements JobListener {

    private static final Logger logger = Logger.getLogger(
            FileProcessorJobListener.class.getName());

    @Override
    public void beforeJob() throws Exception {
        logger.log(Level.INFO,
                "Handling event file processor batch job starting at {0}",
                LocalDateTime.now());
    }

    @Override
    public void afterJob() throws Exception {
        logger.log(Level.INFO,
                "Handling event file processor batch job completed at {0}",
                LocalDateTime.now());
    }
}

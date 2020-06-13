package jakarta.cargotracker.interfaces.handling.file;

import jakarta.cargotracker.application.ApplicationEvents;
import jakarta.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.*;
import java.util.List;

@Dependent
@Named("EventItemWriter")
public class EventItemWriter extends AbstractItemWriter {

    private static final String ARCHIVE_DIRECTORY = "archive_directory";
    @Inject
    private JobContext jobContext;
    @Inject
    private ApplicationEvents applicationEvents;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        File archiveDirectory = new File(jobContext.getProperties().getProperty(ARCHIVE_DIRECTORY));

        if (!archiveDirectory.exists()) {
            archiveDirectory.mkdirs();
        }
    }

    @Override
    @Transactional
    public void writeItems(List<Object> items) throws Exception {
        try (PrintWriter archive = new PrintWriter(new BufferedWriter(new FileWriter(
                new File(jobContext.getProperties().getProperty(ARCHIVE_DIRECTORY)
                        + "/archive_" + jobContext.getJobName()
                        + "_" + jobContext.getInstanceId()
                        + ".csv"), true)))) {
            for (Object item : items) {
                HandlingEventRegistrationAttempt attempt = (HandlingEventRegistrationAttempt) item;
                applicationEvents.receivedHandlingEventRegistrationAttempt(attempt);
                archive.println(attempt.getRegistrationTime()
                        + "," + attempt.getCompletionTime()
                        + "," + attempt.getTrackingId()
                        + "," + attempt.getVoyageNumber()
                        + "," + attempt.getUnLocode()
                        + "," + attempt.getType());
            }
        }
    }
}

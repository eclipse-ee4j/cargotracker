/*
    The MIT License
    
    Copyright (c) 2019 Oracle and/or its affiliates
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/
package jakarta.cargotracker.interfaces.handling.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import jakarta.cargotracker.application.ApplicationEvents;
import jakarta.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;

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

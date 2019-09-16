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
package net.java.cargotracker.interfaces.handling.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.chunk.listener.SkipReadListener;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

@Dependent
@Named("LineParseExceptionListener")
public class LineParseExceptionListener implements SkipReadListener {

    private static final String FAILED_DIRECTORY = "failed_directory";
    private static final Logger logger = Logger.getLogger(
            LineParseExceptionListener.class.getName());
    @Inject
    private JobContext jobContext;

    @Override
    public void onSkipReadItem(Exception e) throws Exception {
        File failedDirectory = new File(jobContext.getProperties().getProperty(
                FAILED_DIRECTORY));

        if (!failedDirectory.exists()) {
            failedDirectory.mkdirs();
        }

        EventLineParseException parseException = (EventLineParseException) e;

        logger.log(Level.WARNING, "Problem parsing event file line",
                parseException);

        try (PrintWriter failed = new PrintWriter(new BufferedWriter(new FileWriter(
                new File(failedDirectory,
                        "failed_" + jobContext.getJobName()
                        + "_" + jobContext.getInstanceId()
                        + ".csv"), true)))) {
            failed.println(parseException.getLine());
        }
    }
}

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

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.listener.JobListener;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Dependent
@Named("FileProcessorJobListener")
public class FileProcessorJobListener implements JobListener {

    private static final Logger logger = Logger.getLogger(
            FileProcessorJobListener.class.getName());

    @Override
    public void beforeJob() throws Exception {
        logger.log(Level.INFO,
                "Handling event file processor batch job starting at {0}",
                new Date());
    }

    @Override
    public void afterJob() throws Exception {
        logger.log(Level.INFO,
                "Handling event file processor batch job completed at {0}",
                new Date());
    }
}

package org.eclipse.cargotracker.interfaces.handling.file;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * Periodically scans a certain directory for files and attempts to parse handling event
 * registrations from the contents by calling a batch job.
 *
 * <p>Files that fail to parse are moved into a separate directory, successful files are deleted.
 */
@Stateless
// TODO [Jakarta EE 8] Review if transaction handling is correct.
@TransactionManagement(TransactionManagementType.BEAN) // Batch steps manage their own transactions.
public class UploadDirectoryScanner {

  @Schedule(minute = "*/2", hour = "*") // In production, run every fifteen minutes
  public void processFiles() {
    JobOperator jobOperator = BatchRuntime.getJobOperator();
    jobOperator.start("EventFilesProcessorJob", null);
  }
}

package org.eclipse.cargotracker.interfaces.handling.file;

import jakarta.annotation.security.PermitAll;
import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

/**
 * Periodically scans a certain directory for files and attempts to parse handling event
 * registrations from the contents by calling a batch job.
 *
 * <p>Files that fail to parse are moved into a separate directory, successful files are deleted.
 */
@Stateless
@PermitAll
@TransactionManagement(TransactionManagementType.BEAN) // Batch steps manage their own transactions.
public class UploadDirectoryScanner {

  @Schedule(minute = "*/2", hour = "*") // In production, run every fifteen minutes
  public void processFiles() {
    JobOperator jobOperator = BatchRuntime.getJobOperator();
    jobOperator.start("EventFilesProcessorJob", null);
  }
}

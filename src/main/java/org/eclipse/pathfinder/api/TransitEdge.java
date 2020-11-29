package org.eclipse.pathfinder.api;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents an edge in a path through a graph, describing the route of a
 * cargo.
 */
public class TransitEdge implements Serializable {

	private static final long serialVersionUID = 1L;

	private String voyageNumber;
	private String fromUnLocode;
	private String toUnLocode;
	private Date fromDate;
	private Date toDate;

	public TransitEdge() {
		// Nothing to do.
	}

	public TransitEdge(String voyageNumber, String fromUnLocode, String toUnLocode, Date fromDate, Date toDate) {
		this.voyageNumber = voyageNumber;
		this.fromUnLocode = fromUnLocode;
		this.toUnLocode = toUnLocode;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public String getVoyageNumber() {
		return voyageNumber;
	}

	public void setVoyageNumber(String voyageNumber) {
		this.voyageNumber = voyageNumber;
	}

	public String getFromUnLocode() {
		return fromUnLocode;
	}

	public void setFromUnLocode(String fromUnLocode) {
		this.fromUnLocode = fromUnLocode;
	}

	public String getToUnLocode() {
		return toUnLocode;
	}

	public void setToUnLocode(String toUnLocode) {
		this.toUnLocode = toUnLocode;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	@Override
	public String toString() {
		return "TransitEdge{" + "voyageNumber=" + voyageNumber + ", fromUnLocode=" + fromUnLocode + ", toUnLocode="
				+ toUnLocode + ", fromDate=" + fromDate + ", toDate=" + toDate + '}';
	}
}

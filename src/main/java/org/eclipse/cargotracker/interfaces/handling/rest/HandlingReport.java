package org.eclipse.cargotracker.interfaces.handling.rest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Transfer object for handling reports.
 */
@XmlRootElement
public class HandlingReport {

	@NotNull
	@Size(min = 16, max = 16)
	private String completionTime;
	@NotNull
	@Size(min = 4)
	private String trackingId;
	@NotNull
	@Size(min = 4, max = 7)
	private String eventType;
	@NotNull
	@Size(min = 5, max = 5)
	private String unLocode;
	@Size(min = 4, max = 5)
	private String voyageNumber;

	public String getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(String value) {
		this.completionTime = value;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String value) {
		this.eventType = value;
	}

	public String getUnLocode() {
		return unLocode;
	}

	public void setUnLocode(String value) {
		this.unLocode = value;
	}

	public String getVoyageNumber() {
		return voyageNumber;
	}

	public void setVoyageNumber(String value) {
		this.voyageNumber = value;
	}
}

package org.eclipse.cargotracker.domain.model.voyage;

import org.eclipse.cargotracker.domain.model.location.Location;
import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({
		@NamedQuery(name = "Voyage.findByVoyageNumber", query = "Select v from Voyage v where v.voyageNumber = :voyageNumber"),
		@NamedQuery(name = "Voyage.findAll", query = "Select v from Voyage v order by v.voyageNumber") })

public class Voyage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Null object pattern
	public static final Voyage NONE = new Voyage(new VoyageNumber(""), Schedule.EMPTY);	
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Embedded
	@NotNull
	private VoyageNumber voyageNumber;
	
	@Embedded
	@NotNull
	private Schedule schedule;

	public Voyage() {
		// Nothing to initialize
	}

	public Voyage(VoyageNumber voyageNumber, Schedule schedule) {
		Validate.notNull(voyageNumber, "Voyage number is required");
		Validate.notNull(schedule, "Schedule is required");

		this.voyageNumber = voyageNumber;
		this.schedule = schedule;
	}

	public VoyageNumber getVoyageNumber() {
		return voyageNumber;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	@Override
	public int hashCode() {
		return voyageNumber.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof Voyage)) {
			return false;
		}

		Voyage that = (Voyage) o;

		return sameIdentityAs(that);
	}

	public boolean sameIdentityAs(Voyage other) {
		return other != null && this.getVoyageNumber().sameValueAs(other.getVoyageNumber());
	}

	@Override
	public String toString() {
		return "Voyage " + voyageNumber;
	}

	/**
	 * Builder pattern is used for incremental construction of a Voyage aggregate.
	 * This serves as an aggregate factory.
	 */
	public static class Builder {

		private List<CarrierMovement> carrierMovements = new ArrayList<>();
		private VoyageNumber voyageNumber;
		private Location departureLocation;

		public Builder(VoyageNumber voyageNumber, Location departureLocation) {
			Validate.notNull(voyageNumber, "Voyage number is required");
			Validate.notNull(departureLocation, "Departure location is required");

			this.voyageNumber = voyageNumber;
			this.departureLocation = departureLocation;
		}

		public Builder addMovement(Location arrivalLocation, Date departureTime, Date arrivalTime) {
			carrierMovements.add(new CarrierMovement(departureLocation, arrivalLocation, departureTime, arrivalTime));

			// Next departure location is the same as this arrival location
			this.departureLocation = arrivalLocation;

			return this;
		}

		public Voyage build() {
			return new Voyage(voyageNumber, new Schedule(carrierMovements));
		}
	}
}

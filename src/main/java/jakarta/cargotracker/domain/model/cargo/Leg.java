package jakarta.cargotracker.domain.model.cargo;

import jakarta.cargotracker.domain.model.location.Location;
import jakarta.cargotracker.domain.model.voyage.Voyage;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Leg implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "voyage_id")
    @NotNull
    private Voyage voyage;
    @ManyToOne
    @JoinColumn(name = "load_location_id")
    @NotNull
    private Location loadLocation;
    @ManyToOne
    @JoinColumn(name = "unload_location_id")
    @NotNull
    private Location unloadLocation;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "load_time")
    @NotNull
    private Date loadTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "unload_time")
    @NotNull
    private Date unloadTime;

    public Leg() {
        // Nothing to initialize.
    }

    public Leg(Voyage voyage, Location loadLocation, Location unloadLocation,
               Date loadTime, Date unloadTime) {
        Validate.noNullElements(new Object[]{voyage, loadLocation,
                unloadLocation, loadTime, unloadTime});

        this.voyage = voyage;
        this.loadLocation = loadLocation;
        this.unloadLocation = unloadLocation;
        this.loadTime = loadTime;
        this.unloadTime = unloadTime;
    }

    public Voyage getVoyage() {
        return voyage;
    }

    public Location getLoadLocation() {
        return loadLocation;
    }

    public Location getUnloadLocation() {
        return unloadLocation;
    }

    public Date getLoadTime() {
        return new Date(loadTime.getTime());
    }

    public Date getUnloadTime() {
        return new Date(unloadTime.getTime());
    }

    private boolean sameValueAs(Leg other) {
        return other != null
                && new EqualsBuilder().append(this.voyage, other.voyage)
                .append(this.loadLocation, other.loadLocation)
                .append(this.unloadLocation, other.unloadLocation)
                .append(this.loadTime, other.loadTime)
                .append(this.unloadTime, other.unloadTime).isEquals();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Leg leg = (Leg) o;

        return sameValueAs(leg);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(voyage).append(loadLocation)
                .append(unloadLocation).append(loadTime).append(unloadTime)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Leg{" + "id=" + id + ", voyage=" + voyage + ", loadLocation="
                + loadLocation + ", unloadLocation=" + unloadLocation
                + ", loadTime=" + loadTime + ", unloadTime=" + unloadTime + '}';
    }
}

package jakarta.cargotracker.application;

import jakarta.cargotracker.domain.model.cargo.TrackingId;

import javax.validation.constraints.NotNull;

public interface CargoInspectionService {

    /**
     * Inspect cargo and send relevant notifications to interested parties, for
     * example if a cargo has been misdirected, or unloaded at the final
     * destination.
     */
    public void inspectCargo(
            @NotNull(message = "Tracking ID is required") TrackingId trackingId);
}

package org.eclipse.cargotracker.interfaces.booking.web;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author davidd
 */
@ManagedBean(name = "changeDestinationDialog")
@SessionScoped
public class ChangeDestinationDialog implements Serializable {


    public void showDialog(String trackingId) {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentWidth", 410);
        options.put("contentHeight", 280);

        Map<String, List<String>> params = new HashMap<>();
        List<String> values = new ArrayList<>();
        values.add(trackingId);
        params.put("trackingId", values);

        PrimeFaces.current().dialog().openDynamic("/admin/dialogs/changeDestination.xhtml", options, params);
    }

    public void handleReturn(SelectEvent event) {  }

    public void cancel() {
        // just kill the dialog
        PrimeFaces.current().dialog().closeDynamic("");
    }
}


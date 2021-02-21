package org.eclipse.cargotracker.interfaces.booking.web;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

@Named
@ViewScoped
public class DashboardView implements Serializable {

  private static final long serialVersionUID = 1L;

  private DashboardModel model;

  private String name = "TestDD";

  public DashboardView() {
    // Initialize the dashboard model
    this.model = new DefaultDashboardModel();
    DashboardColumn mainColumn = new DefaultDashboardColumn();

    mainColumn.addWidget("Routed");
    mainColumn.addWidget("NotRouted");
    mainColumn.addWidget("Claimed");

    this.model.addColumn(mainColumn);
  }

  public String getName() {
    return name;
  }

  public DashboardModel getModel() {
    return model;
  }

  public void setModel(DashboardModel model) {
    this.model = model;
  }
}

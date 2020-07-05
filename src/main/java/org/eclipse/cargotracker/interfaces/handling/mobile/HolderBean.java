package org.eclipse.cargotracker.interfaces.handling.mobile;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowHandler;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author davidd
 */
@Named
@SessionScoped
@Deprecated
public class HolderBean implements Serializable {

    // TODO: this is really a workaround for now as viewaction can't invoke a faceflow directly!

    private String holder = "workaround";

    void setHolder(String holder) {
        this.holder = holder;
    }

    public String getHolder() {
        return holder;
    }

    public String initFlow() {
        FacesContext context = FacesContext.getCurrentInstance();
        FlowHandler handler = context.getApplication().getFlowHandler();
        handler.transition(context, null, handler.getFlow(context, "", "eventLogger"), null, "");
        return "eventLogger";
    }

}

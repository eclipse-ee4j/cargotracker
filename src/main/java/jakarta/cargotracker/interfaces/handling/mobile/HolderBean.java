/*
    The MIT License
    
    Copyright (c) 2019 Oracle and/or its affiliates
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/
package jakarta.cargotracker.interfaces.handling.mobile;

//import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowHandler;
import javax.inject.Named;

/**
 *
 * @author davidd
 */
@Named
@SessionScoped
public class HolderBean implements Serializable{

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

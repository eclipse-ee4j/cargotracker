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
package jakarta.cargotracker.interfaces.booking.web;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

/**
 *
 * @author davidd
 */
@Named
@ViewScoped
public class DashboardView implements Serializable{

    private DashboardModel model;

    private String name = "TestDD";

    public String getName() {
        return name;
    }
       
    public DashboardView(){
        // Initialize the dashboard model
        this.model = new DefaultDashboardModel();
        DashboardColumn mainColumn = new DefaultDashboardColumn();
 
        mainColumn.addWidget("Routed");
        mainColumn.addWidget("NotRouted");
        mainColumn.addWidget("Claimed");      
 
        this.model.addColumn(mainColumn);
 
    }
 
    public DashboardModel getModel() {
        return model;
    }
 
    public void setModel(DashboardModel model) {
        this.model = model;
    }
    
}

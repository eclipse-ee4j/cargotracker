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
package jakarta.pathfinder.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GraphDao implements Serializable {

    private final Random random = new Random();

    public List<String> listLocations() {
        return new ArrayList<>(Arrays.asList("CNHKG", "AUMEL", "SESTO",
                "FIHEL", "USCHI", "JNTKO", "DEHAM", "CNSHA", "NLRTM", "SEGOT",
                "CNHGH", "USNYC", "USDAL"));
    }

    public String getVoyageNumber(String from, String to) {
        int i = random.nextInt(5);

        switch (i) {
            case 0:
                return "0100S";
            case 1:
                return "0200T";
            case 2:
                return "0300A";
            case 3:
                return "0301S";
            default:
                return "0400S";
        }
    }
}

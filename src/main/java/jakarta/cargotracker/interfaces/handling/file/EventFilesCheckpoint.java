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
package jakarta.cargotracker.interfaces.handling.file;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class EventFilesCheckpoint implements Serializable {

    private List<File> files = new LinkedList<>();
    private int fileIndex = 0;
    private long filePointer = 0;

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public long getFilePointer() {
        return filePointer;
    }

    public void setFilePointer(long filePointer) {
        this.filePointer = filePointer;
    }

    public File currentFile() {
        if (files.size() > fileIndex) {
            return files.get(fileIndex);
        } else {
            return null;
        }
    }

    public File nextFile() {
        filePointer = 0;

        if (files.size() > ++fileIndex) {
            return files.get(fileIndex);
        } else {
            return null;
        }
    }
}

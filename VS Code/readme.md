# VS Code IDE

This section outlines how to set up the application in the VS Code IDE.

## Prerequisites

* Java SE 8, Java SE 11 or Java SE 17 is required.
* Payara Server 5 is required. You can download Payara Server 5 from https://www.payara.fish/downloads/ 
* The VS Code IDE is required. You can download the VS Code IDE from https://code.visualstudio.com/download

## Download

Download the source code zip file and expand it somewhere in your file system. Note that this is a Maven project. https://github.com/eclipse-ee4j/cargotracker/archive/master.zip

## VS Code IDE Set-up

### Installing Necessary Tools

Start the VS Code IDE

Go to ->Extensions (Ctrl+Shift+ X)

Search for "Payara Tools" [https://marketplace.visualstudio.com/items?itemName=Payara.payara-vscode] and  "Extension Pack for Java" [https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack] and install Payara Tools and Extension Pack for Java.

 ![Payara Tools](<images/payara tools.PNG>)

 ![Extension Pack for Java](<images/extension pack for java.PNG>)

### Adding Payara Server

Once the Payara Tools plugin is installed, 
1. Click on the Payara icon on the left side bar below the Extensions icon,then click on the '+' icon on the Servers.

![Adding Server](<images/adding payara server 1.PNG>)

* Before doing the next step,please make sure that you have properly set up JAVA_HOME on your PC.
2. choose local domain -> browse the payara server,then select the directory where you installed the Payara 5. 

![Local Domain](<images/Adding payara server 2.png>)

![browse](<images/Adding payara server 3.png>)

3. Name it Payara5 and hit next. Select default domain1.

![Payara5](<images/Adding payara server 4.png>)

![Local domain1](<images/Adding payara server 5.png>)

4. Now ,a payara5 name icon with the Payara logo comes under the servers.Right-click on that icon,and select start,it will automatically start the Payara 5.

![running](<images/Running payara server 1.png>)

5. A small green color triangle comes under the Payara5 logo in the servers,this means your Payara server has been successfully started.You can verify if you wanted by visiting localhost:8080

![running sucessful](<images/Running payara server 2.png>)

### Importing Code

1. Go to File -> Open Folder ->Select the root directory of the Cargo Tracker project in your file system, and Hit finish.VS Code will automatically identify it as a Maven project.

![import](<images/importing cargotracker 1.png>)

2. This will take a few minutes to import the project for the first time.

![import2](<images/importing cargotracker 2.png>)

![import3](<images/importing cargotracker 3.png>)

### Building and Running on Payara 5
1. After the project loads,Go under the maven tab on the left bottom side,the eclipse cargo tracker name will be shown.Right-click on it and proceed with run maven commands clean,package,install and compile(which will take a while the very first time as Maven downloads dependencies)

![build1](<images/building cargotracker 1.png>)

![build2](<images/building cargotracker 2.png>)

2. After this a war file of the name cargo-tracker.war will be built under the target section.

![target](<images/deployment 1.png>)

3. Right-click on that war file,select run on Payara server option

![cargo-tracker.war](<images/deployment 2.png>)
4. The first-time startup might take a bit of time. After this, VS Code will automatically open up a default browser window with the application. 

![launch](<images/deployment 3.png>)

![sucess](<images/sucess 1.png>)

There is a tracking interface to track the current status of cargo and a booking interface to book and route cargo. You should explore both interfaces before diving into the code. You should also check out the REST(https://github.com/eclipse-ee4j/cargotracker/blob/master/src/main/java/org/eclipse/cargotracker/interfaces/handling/rest/HandlingReportService.java) and file processing(https://github.com/eclipse-ee4j/cargotracker/blob/master/src/main/java/org/eclipse/cargotracker/interfaces/handling/file/UploadDirectoryScanner.java) interfaces to register handling events as well as the mobile web interface. You can test against the REST interfaces using our soapUI tests(https://github.com/eclipse-ee4j/cargotracker/tree/master/src/test/soapui).

![tracking](images/tracking.png)

![admin](images/admin.png)

### Stopping Cargo-tracker

* Click on the 'x' icon to stop the cargo-tracker application on Payara5.

![stop1](images/stop1.png)

![stop2](images/stop2.png)

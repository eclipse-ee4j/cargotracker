# Visual Studio Code

This section outlines how to set up the application in Visual Studio Code.

## Prerequisites

* Java SE 8, Java SE 11, or Java SE 17 is required. Please make sure that you have properly set up the JAVA_HOME environment variable.
* Payara Server 5 is required. You can download Payara Server 5 from [here](https://mvnrepository.com/artifact/fish.payara.distributions/payara).
* Visual Studio Code is required. You can download Visual Studio Code from [here](https://code.visualstudio.com/download).
* Ensure that the Visual Studio Code [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) is installed.

## Download

[Download](https://github.com/eclipse-ee4j/cargotracker/archive/master.zip) the source code zip file and expand it somewhere in your file system. Note that this is a Maven project.

## Visual Studio Code Setup

Start Visual Studio Code. Go to -> Extensions (Ctrl+Shift+X). Search for [Payara Tools](https://marketplace.visualstudio.com/items?itemName=Payara.payara-vscode). Install Payara Tools.

 ![Payara_Tools](<images/vs_add_payara_tools.png>)

Once the Payara Tools plugin is installed, click on the Payara icon on the left side bar below the Extensions icon, then click on the '+' icon on the Servers. Choose 'Local Domain' -> 'Browse the Payara Server', then select the directory where you installed Payara 5.

![Local_Domain](<images/vs_add_payara_1.png>)

![Browse](<images/vs_add_payara_2.png>)

Name the instance Payara 5 and hit next. Select the default domain1.

![Payara 5](<images/vs_add_payara_3.png>)

![domain1](<images/vs_add_payara_4.png>)

Now you will see the instance under Servers. Right-click on the instance and select 'Start'. Once Payara starts, you can verify it is up by visiting http://localhost:8080.

![running](<images/vs_run_payara.png>)

## Running the Application

You will now need to get the application into Visual Studio Code. Go to File -> Open Folder -> Select the root directory of the Cargo Tracker project in your file system, and hit finish. Visual Studio Code will automatically identify it as a Maven project. It will take a few minutes to import the project for the first time.

![import](<images/vs_import.png>)

After the project loads, go to the Maven tab on the left bottom side. You will see that Eclipse Cargo Tracker is a recognized Maven project. Right click on it and run the 'clean' Maven command. Finally, run the 'package' Maven command. It will take a while the very first time as Maven downloads dependencies.

![build](<images/vs_build.png>)

After this, a war file named 'cargo-tracker.war' will be built under the 'target' directory. Right-click on the war file and select the 'Run on Payara Server' option.

![deploy](<images/vs_deploy.png>)

The first time start up might take a bit of time. Once the deployement is done, Visual Studio Code will automatically open up a default browser window with the application. 

![sucess](<images/vs_sucess.png>)

There is a tracking interface to track the current status of cargo and a booking interface to book and route cargo. You should explore both interfaces before diving into the code. You should also check out the [REST](https://github.com/eclipse-ee4j/cargotracker/blob/master/src/main/java/org/eclipse/cargotracker/interfaces/handling/rest/HandlingReportService.java) and [file processing](https://github.com/eclipse-ee4j/cargotracker/blob/master/src/main/java/org/eclipse/cargotracker/interfaces/handling/file/UploadDirectoryScanner.java) interfaces to register handling events as well as the mobile web interface. You can test against the REST interfaces using our [soapUI tests](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/test/soapui).

Once you are done, click on the 'x' icon on the Payara server instance to stop the Cargo Tracker application.

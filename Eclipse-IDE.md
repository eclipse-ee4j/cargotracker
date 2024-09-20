This section outlines how to set up the application in the Eclipse IDE.

## Prerequisites

* Java SE 8, Java SE 11 or Java SE 17 is required.
* Payara Server 5 is required. You can download Payara Server 5 from 
  [here](https://www.payara.fish/downloads/).
* The Eclipse IDE for Enterprise Java Developers is required. You can download 
  the Eclipse IDE from [here](https://www.eclipse.org/downloads/packages/).

## Download

[Download](https://github.com/eclipse-ee4j/cargotracker/archive/master.zip) the 
source code zip file and expand it somewhere in your file system. Note that 
this is a Maven project.

## Eclipse IDE Setup

Start the Eclipse IDE, you will first need to add the Payara plugin. Go to 
Help -> Eclipse Marketplace. Search for "Payara" and install Payara Tools.

[[images/eclipse_step1.png|ATL TEXT]]

Once the Payara plugin is installed, go to Servers -> New -> Server. Select 
Payara and hit next. Select where Payara 5 is installed. Hit next.

[[images/eclipse_step2.png|ATL TEXT]]

Accept the defaults for Payara properties, hit next and then finish.

Go to File -> Import -> Existing Maven Projects. Hit next. Select the root 
directory of the Cargo Tracker project in your file system. Hit finish.

[[images/eclipse_step3.png|ATL TEXT]]

After the project loads, build it by right clicking on it and going to 
Run As -> Maven install. It may take a little bit for the project to build.

After the project builds, you are now ready to run it. Right click the project 
and go to Run As -> Run on Server. Select Payara as the server and choose to 
always run the project on Payara. Hit finish.

[[images/eclipse_step4.png|ATL TEXT]]

The first time startup might take a bit of time. After Payara starts, the 
Eclipse IDE should open up a browser window with the application.

[[images/eclipse_step5.png|ATL TEXT]]

There is a tracking interface to track the current status of cargo and a 
booking interface to book and route cargo. You should explore both interfaces 
before diving into the code. You should also check out the 
[REST](https://github.com/eclipse-ee4j/cargotracker/blob/master/src/main/java/org/eclipse/cargotracker/interfaces/handling/rest/HandlingReportService.java) 
and [file processing](https://github.com/eclipse-ee4j/cargotracker/blob/master/src/main/java/org/eclipse/cargotracker/interfaces/handling/file/UploadDirectoryScanner.java) 
interfaces to register handling events as well as the mobile web interface. You 
can test against the REST interfaces using our 
[soapUI tests](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/test/soapui).

# Eclipse

This section outlines how to set up the application in the Eclipse IDE.

## Prerequisites

* Java SE 8 is required. You can download Java SE 8 from [here](https://www.azul.com/downloads/zulu-community/?version=java-8-lts).
* Payara Server 4.1 is required (this is the Payara version that supports only Java SE 8). You can download Payara Server 4.1 from [here](https://repo1.maven.org/maven2/fish/payara/distributions/payara/4.1.2.181/payara-4.1.2.181.zip).
* The 2020-06 release of Eclipse for Enterprise Java Developers is required (this is the latest Eclipse IDE version that supports Java SE 8). You can download the Eclipse IDE from [here](https://www.eclipse.org/downloads/packages/release/2020-06/r/eclipse-ide-enterprise-java-developers).

## Download

[Download](https://github.com/eclipse-ee4j/cargotracker/archive/master.zip) the source code zip file and expand it somewhere in your file system. Note that this is a Maven project.

## Eclipse Setup

Start Eclipse, you will first need to add the Payara plugin. Go to Help -> Eclipse Marketplace. Search for "Payara" and install Payara Tools.

![ ](.gitbook/assets/eclipse_step1.png)

Once the Payara plugin is installed, go to Servers -> New -> Server. Select Payara and hit next. Select where Payara 4.1 is installed. Hit next.

![ ](.gitbook/assets/eclipse_step2.png)

Accept the defaults for Payara properties, hit next and then finish.

Go to File -> Import -> Existing Maven Projects. Hit next. Select the root directory of the Cargo Tracker project in your file system. Hit finish.

![ ](.gitbook/assets/eclipse_step3.png)

Go to File-&gt;Open Project. Select the Cargo Tracker project from the file system and hit _'Open Project'_. NetBeans should recognize it as a Maven project on the file system. It may take a little bit to open and scan the project.

![ ](.gitbook/assets/nb_step4.png)

After the project opens, immediately build it \(you may be prompted to do a priming build, you can ignore the prompt\). This will automatically download all the Maven dependencies. To build, right click on the project and hit _'Clean and Build'_. It may take a little bit for the project to build.

![ ](.gitbook/assets/nb_step5.png)

After the project builds, you are now ready to run it.

![ ](.gitbook/assets/nb_step6.png)

You will now need to associate the Cargo Tracker project with Payara. To do this, right-click the project and select _'Properties'_. Then select _'Run'_. Select the Payara instance you just created as the _'Server'_, make sure the _'Context Path'_ is set to _'/cargo-tracker'_, accept the rest of the defaults and hit _'OK'_. It might take a bit to save the project data.

![ ](.gitbook/assets/nb_step7.png)

You are now ready to run the application. Right click on the project and hit _'Run'_. The first time startup might take a bit of time. After Payara starts, NetBeans should open up a browser window with the application.

There is a tracking interface to track the current status of cargo and a booking interface to book and route cargo. You should explore both interfaces before diving into the code. You should also check out the [REST](https://github.com/m-reza-rahman/cargo-tracker/blob/master/src/main/java/net/java/cargotracker/interfaces/handling/rest/HandlingReportService.java) and [file processing](https://github.com/m-reza-rahman/cargo-tracker/blob/master/src/main/java/net/java/cargotracker/interfaces/handling/file/UploadDirectoryScanner.java) interfaces to register handling events as well as the mobile web interface. You can test against the REST interfaces using our [soapUI tests](https://github.com/m-reza-rahman/cargo-tracker/tree/master/src/test/soapui).


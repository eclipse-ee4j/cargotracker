This section outlines how to set up the application in the Visual Studio Code.

## Prerequisites

* Java SE 8, Java SE 11 or Java SE 17 is required.
* Payara Server 5 is required. You can download Payara Server 5 from [here](https://www.payara.fish/downloads/).
* The Visual Studio Code is required. You can download the Visual Studio Code from [here](https://code.visualstudio.com/download).
* [Java Extension Pack, by Microsoft](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)

## Download

[Download](https://github.com/eclipse-ee4j/cargotracker/archive/master.zip) the source code zip file and expand it somewhere in your file system. Note that this is a Maven project.

## Visual Studio Code Setup

You need to install the payara extensions from the Visual Studio Code marketplace :

* [Payara Tools, by Payara](https://marketplace.visualstudio.com/items?itemName=Payara.payara-vscode)

You can install extensions from Extensions view (Ctrl + Shift + X) in the activity bar of Visual Studio Code:

![Payara](image/payara_server.png)
 
Let’s start:

 * Open the command pallet using Ctrl + Shift + P, type Maven and import the existing Maven Project :

  ![Maven](image/maven.png)

 * Or in the explorer sidebar expand Maven Projects, Select the + sign to create a Maven Project.

 * Select the destination folder, where the project folder will be generated and answer the prompts (groupId, artifactId, version etc) in the focused TERMINAL window to setup the maven project.

 * Open the Maven project folder in VS Code via File menu -> Open Folder… and select the CARGOTRACKER-MASTER project folder:

 ![Cargo](image/cargo.png)

 ## Adding Payara Server
 
 * Open the command pallet using Ctrl + Shift + P, type Payara and select the Add Payara Server option:

 ![payara1](image/payara1.png)

 * Or In the explorer sidebar (or Payara activity bar), expand Payara Servers, Select the + sign to add Payara Server:

 ![payara2](image/payara2.png)

 * Select the location of your 'payara5' directory within the Payara Server installation:

 ![payara3](image/payara3.png)

 * Now name the server whatever you want. Press Enter:

 ![payara5](image/payara5.png)

 * Finally, select a domain from the list or press the + sign to create the new domain:
 
 ![payara6](image/payara6.png)

 * Then, select the maven side-bar & right-click to clean packageg or else you can run 'mvn clean package' in the terminal:

 ![Maveni](image/maveni.png)

 * After Build success. To start the Payara Server right click on the server in Payara Server explorer and select Start:

 ![payara7](image/payara7.png)

 ## Running the Payara Server Application

 * Now go to your project, and find the cargo-tracker.war file. Ther right click on it and select the Run on Payara Server option. The project will start and application will be deployed to Payara Server (Payara Server instance will be started if not running already):

 ![Run](image/run.png)

 * Now you should see a page (http://localhost:8080/cargo-tracker), eventually opened in your default browser.

 ![Final](image/final.png)










 
 





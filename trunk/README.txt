Compiling
Each subdirectory is an eclipse project, import into eclipse and it will 
build everything.

Running
Most projects are just class libraries and lack a main class. 
FacemapTest can be run as a JUnit test suite and FacemapGui can be run 
as an Android appication.

The client-server demo requires JBoss 7.1.0 (not released yet) which is 
available at http://facemap-jhu.googlecode.com/files/jboss-as-7.x.zip 
Download and unzip and add as a JBoss 7 server to eclipse. The 
eclipse integration needs to be edited to work with JBoss 7.1.0. Under 
the Servers view, souble click the new server instance and find the 
dropdown list labeled "Startup poller". Change it to "Web Port". 
Additionally, uncheck the "Automatically detect" option for the 
management port and enter the value "9999". Type Ctrl-S to save your 
changes. 

To test the client server communication, the HelloWorldCdiEar 
application must be deployed to the JBoss 7.1 server instance. Right 
click the server instance under the Servers view and select "Add/Remove" 
and select the HelloWorldCdiEar project to add. Start the server. Open 
the HelloWorldCdiRemote project and find the TestHelloWorld class. Run 
it as a JUnit test to test remote EJB invocation on JBoss 7.1 

Directory contents
+ FacemapClient			facemap client implementation
+ FacemapDomainModel		domain model interfaces
+ FacemapEntities		persistent domain objects 
+ FacemapGui			android gui
+ FacemapServer			facemap server implementation
+ FacemapTest			JUnit tests
+ j2ee-tutorial-jboss7		j2ee tutorial ported to run on jboss7
+ HelloWorldCdiEar      	j2ee application project
+ HelloWorldCdiWar      	j2ee web application - also contains EJB implementations 
+ HelloWorldCdiJpa		j2ee JPA entities used in HelloWorldCdi*
+ HelloWorldCdiInterfaces	EJB remote interfaces used in HelloWorldCdi*
+ HelloWorldCdiRemote		EJB remote invocation tests set up for JBoss 7.1

Android Test Data Setup
There are a total of 10 accounts in the test repository that will
be created when the android application loads up.
for i from 0 to 9
name = iii
email = i@mail.com
password = i
phone = 123456789i

Since add friend is currently not wired into the client, accounts 5 
and above will have contacts added into their friend list and can be 
seen under All Contacts. 

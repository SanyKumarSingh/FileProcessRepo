LargeFileProcessing - Production ready Spring Boot Rest application following RESTful Architecture. PFB the details.

Source Code Repository - https://github.com/SanyKumarSingh/LargeFileProcessing

The application reads custom-build server logs from file logfile.txt which logs various events. 
Every event has 2 entries in the file - one entry when the event was started and another when the event was finished. 
The entries in the file have no specific order (a finish event could occur before a start event)

Every line in the file is a JSON object containing the below mandatory event data:
 id - the unique event identifier
 state - whether the event was started or finished (can have values "STARTED" or "FINISHED")
 timestamp - the timestamp of the event in milliseconds
Application Server logs also have below optional attributes:
 type - type of log
 host - hostname
 
The Program takes the path to logfile.txt as an input argument
1. Parse the contents of logfile.txt
2. Flag any long events that take longer than 4ms
3. Write the found event details to file-based HSQLDB in the working folder
4. The application creates a EVENT table and store the following values:
 Event id
 Event duration
 Type and Host if applicable
 Alert (true if the event took longer than 4ms, otherwise false)

To execute this SpringBootApplication run Application.java main class, it will automatically start the application on embedded Apache Tomcat and InMemory H2 database. Deployable jar after mvn clean install could be found at - C:\artifact.repository\com\cs\demo\LargeFileProcessing\0.0.1-SNAPSHOT\LargeFileProcessing-0.0.1-SNAPSHOT.jar


The application exposes Restful Webservice - http://localhost:8080/api/v1/fileProcessing having Basic Auth where user - admin password - admin

The Http GET request will invoke the file processing of the default logfile.txt available at ../LargeFileProcessing/src/main/resources/logfile.txt
The Http POST request will take the path to logfile.txt as an input argument

Postman(Chrome Plugin) to test below REST API's -
https://sanykumarsingh.postman.co/workspace/My-Workspace~eca08a27-fc29-484b-8b79-774e9414c4a8/request/create?requestId=7fa5a509-dc91-4c65-831d-1ed73d673d92


######## HyperSQL Database (HSQLDB) #######

 Each HSQLDB is called a catalog. There are three types of catalog depending on how the data is stored.
 mem: stored entirely in RAM. file: stored in file system. res: stored in a Java resource, such as a Jar or Zip and are always read-only.

 A file catalog consists 2 – 6 files, all named same as the <database-name> but with different extensions. All these files are essential and should never be deleted. 
 <database-name>.properties – 	Contains settings about the database.
 <database-name>.script – 		Contains definitions for tables and other database objects plus data for non-cached tables.
 <database-name>.log – 		Contains recent changes made to the data. Log is removed when database shutdown normally. For abnormal shutdown, it is used to redo the changes in the next startup.
 <database-name>.data – 		Contains the data for cached tables.
 <database-name>.backup – 		It is used to revert to the last known consistent state of the data file.
 <database-name>.lck - 		Used to lock the database.

 When the database engine shutdown, it creates temporary files with the extension .new. These files should not be deleted by the user. 
 During next startup, all such files will be renamed as above or deleted by the database engine. 
 In some circumstances, <database-name>.data.xxx.old is created and deleted afterwards by the database engine. The user can delete these testdb.data.xxx.old files.

 HSQLDB is supported by spring-boot-starter-parent by 2.0.0.RELEASE
 In this Project used hsql_cfg.xml to load HSQLDB configuration on Application Server startup by importing the xml file in Application.java



#Navigate to HSQLDB working folder
cd C:\eclipseWorkspaceCS3\LargeFileProcessing\src\main\resources\hsqldb

##### HSQLDB below two GUI database managers 
java -cp C:\artifact.repository\org\hsqldb\hsqldb\2.4.1\hsqldb-2.4.1.jar org.hsqldb.util.DatabaseManagerSwing

java -cp C:\artifact.repository\org\hsqldb\hsqldb\2.4.1\hsqldb-2.4.1.jar org.hsqldb.util.DatabaseManager


##### HSQLDB In-Memory catalog connection details to GUI #####
Driver: 	org.hsqldb.jdbcDriver
URL: 		jdbc:hsqldb:hsql://192.168.1.2:9001/testdb
			#jdbc:hsqldb:hsql://<ip_address>:<port>/<dbname>
user: 		admin
password: 	admin

##### HSQLDB File catalog connection details to GUI #####
Setting Name:  	testdb
Driver: 		org.hsqldb.jdbcDriver
URL: 			jdbc:hsqldb:file:/eclipseWorkspaceCS3/LargeFileProcessing/src/main/resources/hsqldb/testdb
user: 			admin
password: 		admin

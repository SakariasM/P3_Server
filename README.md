# P3 Server
Prerequisites

- Java Development Kit (JDK)
  - Version JDK 22 or later.

- MySQL
  - Install and configure a MySQL server. Use the provided SQL script to create and populate the database.

- IntelliJ IDEA:
    - Community or Ultimate edition.

- Maven:
    - Version: 3.9.9 or later

The project uses Maven for dependency management. Dependencies will be automatically resolved during the build process.
- Optionally you can manually download the dependencies with maven in IntelliJ IDEA

To run the Program

1. Clone the Repository

2. Open the Project in IntelliJ IDEA

3. Verify Project Structure

Ensure the project uses the correct JDK version:

Go to File > Project Structure > Project Settings > Project.

Set the Project SDK to Java 22 or later.

Confirm Maven is configured:

Go to File > Settings > Build, Execution, Deployment > Build Tools > Maven.

Ensure the Maven home directory is set correctly.

4. Set up the database

Download the latest MySQL version and install it. 
The file "database_setup.sql" found in "/resources" can be run in MySQL to fill your database with testing data
In the "application.properties" folder, ensure your username, password and database name match with your database

5. Run the program

The server can be run directly through IntelliJ idea or can be packaged into a runnable jar file. The main file of the project it the "ServerApplication" file

For the complete program to work correctly, the server must be running at the same time as the client-side is running 
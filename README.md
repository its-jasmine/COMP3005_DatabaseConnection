# COMP 3005 Assignment 3 Question 1

## Database Connection in Java using JDBC

### Files:
* StudentDatabaseConnection.java
  * application connecting to the database and executing queries on the 'students' relation
* pom.xml
  * maven configur
  * includes dependencies required to establish database connection and execute SQL queries
* DDL+DML.sql
  * database creation script

### Usage using IntelliJ
1. PgAdmin4: Create database or use an exising one 
2. PgAdmin4: Use Query tool to run DDL+DML.sql
3. IntelliJ: Open project 
4. IntelliJ > pom.xml: Click the cycle in the top right corner to reload the Maven project and download the dependencies
5. IntelliJ > StudentDatabasConnection.java: Modify the database name (if needed) in main.
7. Run main.


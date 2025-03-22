
### README: Setting Up Database Connection for CW_2601 Project

#### Prerequisites
To set up the database connection for the project, ensure the following prerequisites are met:

1. **MySQL Database**: Ensure MySQL is installed and running on your system.
2. **Java Development Kit (JDK)**: Ensure you have JDK 8 or later installed.
3. **MySQL Connector for Java**: The project includes the required MySQL JDBC driver (`com.mysql.cj.jdbc.Driver`). No additional installation is required for this.

---

#### Steps to Set Up the Database

1. **Import the Database**:
   - Locate the exported SQL file you received with the project (e.g., `signin.sql`).
   - Open your MySQL client or use a database management tool such as **MySQL Workbench**.
   - Create a new database named `signin` (or a name of your choice):
     ```sql
     CREATE DATABASE signin;
     ```
   - Import the SQL file into the `signin` database:
     ```bash
     mysql -u root -p signin < path_to_signin.sql
     ```

2. **Configure Database Credentials**:
   - Open the `DatabaseConnection` class located in `src/main/java/com/example/cw_2601/DatabaseConnection.java`.
   - Update the following constants to match your MySQL setup:
     ```java
     private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/signin"; // Replace 'signin' if you use a different database name.
     private static final String USER = "root"; // Replace with your MySQL username.
     private static final String PASSWORD = "3285"; // Replace with your MySQL password.
     ```

3. **Ensure MySQL JDBC Driver is Available**:
   - The project includes the necessary dependency for MySQL (`mysql-connector-java`). Ensure your IDE or build tool (e.g., IntelliJ IDEA) recognizes it.

4. **Verify Connection**:
   - After importing the database and updating the credentials, you can test the connection by running the application. If there are no errors, the connection is successful.

---

#### Troubleshooting
- **"MySQL JDBC Driver not found"**:
  - Ensure that the MySQL Connector JAR file is included in the project's classpath.
  - If using Maven, check that the dependency is added in the `pom.xml`:
    ```xml
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.x</version>
    </dependency>
    ```

- **"Access denied for user"**:
  - Double-check the `USER` and `PASSWORD` values in the `DatabaseConnection` class.
  - Ensure the user has the necessary privileges to access the database.

- **Database not found**:
  - Verify that the database name in the `DB_URL` matches the name of the imported database.

---

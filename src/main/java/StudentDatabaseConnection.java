import java.sql.*;
import java.util.ArrayList;

/**
 * This class creates a connection to a PostgresSQL database which is expected to contain a 'students' relation,
 * and allows the user to perform specific CRUD operations.
 *
 * The 'students' relation has the following schema:
 *      student_id: Integer, Primary Key, Auto-increment
 *      first_name: Text, Not Null
 *      last_name: Text, Not Null
 *      email: Text, Not Null, Unique
 *      enrollment_date: Date
 *
 * @author Jasmine Gad El Hak
 * @version 1.0
 */
public class StudentDatabaseConnection {
    private final Connection connection;
    public static final String[] ATTRIBUTES = {"student_id", "first_name", "last_name", "email", "enrollment_date"};

    StudentDatabaseConnection(String databaseName){
        String ip = "localhost"; // Postgres is running on the same computer, so we can use "localhost"
        int port = 5432; // 5432 is the port number used by the pgAdmin application when running in this OS,

        String url = String.format("jdbc:postgresql://%s:%d/%s", ip, port, databaseName); // url to access the database

        // credentials to establish connection
        String user = "postgres";
        String password = "Password1!";

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Connected to database");
            } else {
                System.out.println("Failed to connect to database");
                System.exit(1);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // QUERY METHODS
    /**
     * Prints all the students in the 'students' relation.
     */
    public void getAllStudents(){
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM students";
            System.out.println(query);
            statement.executeQuery(query); // executes SELECT query
            ResultSet resultSet = statement.getResultSet(); //
            StudentDatabaseConnection.printResults(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new student to the 'students' relation
     * @param first_name of student
     * @param last_name of student
     * @param email of student
     * @param enrollment_date of student
     */
    public void addStudent(String first_name, String last_name, String email, String enrollment_date){
        try {
            Statement statement = connection.createStatement();
            String query = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES " + String.format("('%s', '%s', '%s', '%s')", first_name, last_name, email, enrollment_date);
            System.out.println(query + "\n");
            statement.executeUpdate(query); // select query for all students
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the email of a student in the 'students' relation
     * @param studentId of student being updated
     * @param new_email of student
     */
    public void updateStudentEmail(int studentId, String new_email){
        try {
            Statement statement = connection.createStatement();
            String query = String.format("UPDATE students SET email='%s' WHERE student_id=%s", new_email, studentId);
            System.out.println(query + "\n");
            statement.executeUpdate(query); // select query for all students
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a student from the 'students' relation.
     * @param studentId of student being deleted
     */
    public void deleteStudent(int studentId){
        try {
            Statement statement = connection.createStatement();
            String query = String.format("DELETE FROM students WHERE student_id=%s", studentId);
            System.out.println(query + "\n");
            statement.executeUpdate(query); // select query for all students
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // HELPER METHODS FOR TABLE PRINTING
    /**
     * Helper method to print results of SELECT query.
     * @param resultSet returned from query
     * @throws SQLException
     */
    private static void printResults(ResultSet resultSet) throws SQLException {
        ArrayList<String[]> values = getValues(resultSet);
        values.add(0,ATTRIBUTES);
        printHelper(getMaxLengths(values), values);
        System.out.println();
    }

    /**
     * Retreives all the values from result set and stores them in arrays.
     * @param resultSet returned from query
     * @return ArrayList of rows of data
     * @throws SQLException
     */
    private static ArrayList<String[]> getValues(ResultSet resultSet) throws SQLException {
        ArrayList<String[]> values = new ArrayList<>();
        int i = 0;
        while(resultSet.next()) { // prints each attribute
            values.add(new String[ATTRIBUTES.length]);
            for (int j = 0; j < ATTRIBUTES.length; j++) {
                values.get(i)[j] = resultSet.getString(ATTRIBUTES[j]);
            }
            i++;
        }
        return values;
    }

    /**
     * Determines the maximum string length in each column of data.
     * @param values contains the array of each header name, as well as all arrays of data.
     * @return array of maximum lengths for each column
     */
    private static int[] getMaxLengths(ArrayList<String[]> values){
        int [] maxLengths = new int[ATTRIBUTES.length];
        int valueLength;
        for (int i = 0; i < ATTRIBUTES.length; i++) {
            for (String[] tuple : values) {
                valueLength = tuple[i].length();
                if (valueLength > maxLengths[i]) maxLengths[i] = valueLength;
            }
        }

        return maxLengths;
    }

    /**
     * Prints the provided headers and data, formatted according to the required maximum length
     * @param maxLengths array of maximum string length of each column
     * @param values the arrays of header and data values
     */
    private static void printHelper(int[] maxLengths, ArrayList<String[]> values) {
        for (String[] tuple : values) {
            for (int i = 0; i < ATTRIBUTES.length; i++){
                System.out.print(String.format("%-"+(maxLengths[i] + 3)+"s",tuple[i]));
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        String dataBaseName = "Assignment (3) - Question (1)"; // Change as needed
        StudentDatabaseConnection studentDatabaseConnection = new StudentDatabaseConnection(dataBaseName);
        studentDatabaseConnection.getAllStudents();

        studentDatabaseConnection.addStudent("test", "test", "test", "2023-09-01");
        studentDatabaseConnection.getAllStudents();

        studentDatabaseConnection.updateStudentEmail(1, "test2");
        studentDatabaseConnection.getAllStudents();

        studentDatabaseConnection.deleteStudent(1);
        studentDatabaseConnection.getAllStudents();
    }
}

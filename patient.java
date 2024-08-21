import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class patient {
    private Connection connection;
    private Scanner scanner;

    public patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatients() {
        System.out.println("emter Patient Name : ");
        String name = scanner.next();
        System.out.println("Enter Patient Age : ");
        int age = scanner.nextInt();
        System.out.println("Enter patient Gender : ");
        String gender = scanner.next();

        try {
            String query = "insert into patients(name, age, gender) values(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("patient added succesfully");
            } else {
                System.out.println("Failed to add Patient");
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void viewPatients() {
        String query = "select * from patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+----------+------------+ ");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+ ");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s|%-20s|%-10s|%-12s\n", id, name, age, gender);
                System.out.println("+------------+--------------------+----------+------------+ ");
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query="select * from patients where id=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;

            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false; 
    }

}
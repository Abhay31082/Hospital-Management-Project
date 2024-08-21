import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "2004";
    private static String appointmentDate;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            patient patient = new patient(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. view Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter Your Choice");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        patient.addPatients();
                        System.out.println();
                        break;

                    // add patients
                    case 2:
                        patient.viewPatients();
                        System.out.println();
                        break;
                    // view patients
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    // view doctors
                    case 4:
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    // Book Appointment
                    case 5:
                        System.out.println("THANKYOU!! For Using Hospital Management Systems");
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        System.out.println("Enter Patient Id: ");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.println("enter appointment date(YYY-MM-DD):");
        String appointment = scanner.next();
        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
                String appointmentQuery = " insert into appointments(patient_id, doctor_id, appointment_date) values (?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment Booked!");
                    } else {
                        System.out.println("Failed to booked Appointment!");
                    }

                } catch (SQLException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor not availabale on this date!!");
            }
        } else {
            System.out.println("Either doctor or patient doesn't exist!!!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
        String query = "select count(*) from appointments where doctor_id=? AND appointment_date=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }
}

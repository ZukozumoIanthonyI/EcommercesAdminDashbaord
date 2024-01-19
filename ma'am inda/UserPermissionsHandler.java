import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UserPermissionDashboard {

    // List to store user information
    private static List<User> userList = new ArrayList<>();

    public static void main(String[] args) {
        showUserPermissionWindow();
    }

    private static void showUserPermissionWindow() {
        JFrame frame = new JFrame("User Permission Window");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField jobPositionField = new JTextField();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Job Position:"));
        panel.add(jobPositionField);

        JButton addButton = new JButton("Add User");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String jobPosition = jobPositionField.getText();

                // Validate the input (add your validation logic here)

                // Add user to the list
                userList.add(new User(name, email, jobPosition));

                // Display the list of users
                showUserList();

                // Clear the text fields after adding the user
                nameField.setText("");
                emailField.setText("");
                jobPositionField.setText("");
            }
        });

        panel.add(addButton);

        frame.getContentPane().add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showUserList() {
        // Display the list of users using JOptionPane
        StringBuilder userListString = new StringBuilder("User List:\n");
        for (User user : userList) {
            userListString.append("Name: ").append(user.getName()).append(", ")
                    .append("Email: ").append(user.getEmail()).append(", ")
                    .append("Job Position: ").append(user.getJobPosition()).append("\n");
        }

        JOptionPane.showMessageDialog(null, userListString.toString());
    }

    // User class to represent user information
    private static class User {
        private String name;
        private String email;
        private String jobPosition;

        public User(String name, String email, String jobPosition) {
            this.name = name;
            this.email = email;
            this.jobPosition = jobPosition;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getJobPosition() {
            return jobPosition;
        }
    }
}

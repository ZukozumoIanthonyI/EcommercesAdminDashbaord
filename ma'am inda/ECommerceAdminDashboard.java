import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ECommerceAdminDashboard {

    private static String[] options = {
            "Product Management",
            "Order Management",
            "Sales Analytics",
            "Inventory Management",
            "Marketing and Promotions",
            "Shipping and Logistics",
            "Content Management",
            "Customer Management",
            "User Permissions and Security",
            "Profile",
            "Exit"
    };

    public static void main(String[] args) {
        runDashboardLoop();
    }

    private static void runDashboardLoop() {
        while (true) {
            int choice = displayDashboard();

            // Check if the user chose to exit
            if (choice == options.length - 1) {
                // User chose to exit, break out of the loop
                break;
            } else if (choice == options.length - 2) {
                // User chose "Settings," display the profile window
                displayProfileWindow();
            } else if (choice == options.length - 3) {
                // User chose "User Permissions and Security," display the permissions window
                new UserPermissionsHandler().handlePermissions();

            } else if (choice == options.length - 4) {
                // User chose "Customer Management," display the customer management window
                new CustomerManagementHandler().handleCustomerManagement();
            } else if (choice == options.length - 5) {
                // User chose "Content Management," display the content management window
                ContentManagementSystem cms = new ContentManagementSystem();
                DefaultListModel<Category> categoryListModel = new DefaultListModel<>();
                DefaultListModel<Product> productListModel = new DefaultListModel<>();
                ContentManagementHandler contentManagementHandler = new ContentManagementHandler(cms, categoryListModel,
                        productListModel);
                contentManagementHandler.handleContentManagement();
            }

            else {
                // Display the chosen option in the console
                System.out.println("You chose option: " + (choice + 1));
                // No need to ask the user if they want to continue, simply continue the loop
            }
        }
    }

    private static int displayDashboard() {
        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose an option:",
                "E-commerce Admin Dashboard",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        // Return the index of the chosen option
        return choice;
    }

    private static void displayProfileWindow() {
        JFrame profileFrame = new JFrame("User Profile");
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setSize(300, 200);

        JPanel profilePanel = new JPanel(new GridLayout(5, 2, 10, 10)); // 5 rows, 2 columns with gaps

        // Sample user profile data
        String name = "John Doe";
        String email = "john.doe@example.com";
        String position = "Administrator";

        // Add components to the panel
        profilePanel.add(new JLabel("Name:"));
        profilePanel.add(new JLabel(name));
        profilePanel.add(new JLabel("Email:"));
        profilePanel.add(new JLabel(email));
        profilePanel.add(new JLabel("Position:"));
        profilePanel.add(new JLabel(position));

        // Add an image (you need to replace the placeholder with the actual image path)
        ImageIcon icon = new ImageIcon("path_to_your_image.jpg");
        profilePanel.add(new JLabel(icon));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the profile window
                profileFrame.dispose();
                // Restart the dashboard loop
                runDashboardLoop();
            }
        });

        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close both the profile window and the original window
                profileFrame.dispose();
                System.exit(0); // Terminate the program
            }
        });

        profilePanel.add(backButton);
        profilePanel.add(logoutButton);

        profileFrame.getContentPane().add(profilePanel);

        profileFrame.setLocationRelativeTo(null);
        profileFrame.setVisible(true);
    }
}

class UserPermissionsHandler {

    private JDialog permissionsDialog;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField positionField;
    private DefaultListModel<String> accountsListModel;
    private JList<String> accountsList;

    private static final String USER_DATA_FILE = "user_data.txt";

    public void handlePermissions() {
        // Create and configure the permissions window
        createPermissionsDialog();

        // Load existing user data
        loadUserData();

        // Display the permissions window as a modal dialog
        permissionsDialog.setVisible(true);

        // Save user data when the dialog is closed
        saveUserData();
    }

    private void createPermissionsDialog() {
        // Initialize the JDialog
        permissionsDialog = new JDialog();
        permissionsDialog.setTitle("User Permissions Management");
        permissionsDialog.setSize(400, 300);
        permissionsDialog.setLayout(new BorderLayout());

        // Create a panel for the top section (add, update, delete buttons and user
        // information fields)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        nameField = new JTextField(10);
        emailField = new JTextField(15);
        positionField = new JTextField(10);

        topPanel.add(addButton);
        topPanel.add(updateButton);
        topPanel.add(deleteButton);
        topPanel.add(new JLabel("Name:"));
        topPanel.add(nameField);
        topPanel.add(new JLabel("Email:"));
        topPanel.add(emailField);
        topPanel.add(new JLabel("Position:"));
        topPanel.add(positionField);

        // Create a panel for the bottom section (list of accounts)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        accountsListModel = new DefaultListModel<>();
        accountsList = new JList<>(accountsListModel);
        JScrollPane accountsScrollPane = new JScrollPane(accountsList);

        bottomPanel.add(accountsScrollPane, BorderLayout.CENTER);

        // Add action listeners to buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAccount();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAccount();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });

        // Add a "Back" button to close the dialog
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                permissionsDialog.dispose(); // Close the dialog
            }
        });

        // Add components to the main frame
        permissionsDialog.add(topPanel, BorderLayout.NORTH);
        permissionsDialog.add(bottomPanel, BorderLayout.CENTER);
        permissionsDialog.add(backButton, BorderLayout.SOUTH);

        // Set the dialog to be modal
        permissionsDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        // Set the default close operation to dispose the dialog
        permissionsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void addAccount() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String position = positionField.getText().trim();

        if (!name.isEmpty() && !email.isEmpty() && !position.isEmpty()) {
            String accountInfo = name + ";" + email + ";" + position;
            accountsListModel.addElement(accountInfo);

            // Save the updated user data (including the newly added account)
            saveUserData();

            // Clear the input fields after adding
            nameField.setText("");
            emailField.setText("");
            positionField.setText("");
        }
    }

    private void updateAccount() {
        int selectedIndex = accountsList.getSelectedIndex();
        if (selectedIndex != -1) {
            String updatedName = nameField.getText().trim();
            String updatedEmail = emailField.getText().trim();
            String updatedPosition = positionField.getText().trim();

            if (!updatedName.isEmpty() && !updatedEmail.isEmpty() && !updatedPosition.isEmpty()) {
                String updatedAccountInfo = updatedName + ";" + updatedEmail + ";" + updatedPosition;
                accountsListModel.setElementAt(updatedAccountInfo, selectedIndex);

                // Clear the input fields after updating
                nameField.setText("");
                emailField.setText("");
                positionField.setText("");
            }
        } else {
            JOptionPane.showMessageDialog(permissionsDialog, "Please select an account to update.");
        }
    }

    private void deleteAccount() {
        int selectedIndex = accountsList.getSelectedIndex();
        if (selectedIndex != -1) {
            accountsListModel.removeElementAt(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(permissionsDialog, "Please select an account to delete.");
        }
    }

    private void loadUserData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                accountsListModel.addElement(line);
            }
        } catch (IOException e) {
            // Handle file read error (e.g., file not found)
            e.printStackTrace();
        }
    }

    private void saveUserData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE))) {
            for (int i = 0; i < accountsListModel.size(); i++) {
                writer.write(accountsListModel.getElementAt(i) + "\n");
            }
        } catch (IOException e) {
            // Handle file write error
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // For testing purposes
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserPermissionsHandler().handlePermissions();
            }
        });
    }
}

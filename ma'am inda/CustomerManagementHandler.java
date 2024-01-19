import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

class CustomerManagementHandler {

    private JDialog customerManagementDialog;
    private JTextField customerNameField;
    private JTextField customerEmailField;
    private JTextField customerAddressField;
    private DefaultListModel<String> customersListModel;
    private JList<String> customersList;

    private static final String CUSTOMER_DATA_FILE = "customer_data.txt";

    public void handleCustomerManagement() {
        // Create and configure the customer management window
        createCustomerManagementDialog();

        // Load existing customer data
        loadCustomerData();

        // Display the customer management window as a modal dialog
        customerManagementDialog.setVisible(true);

        // Save customer data when the dialog is closed
        saveCustomerData();
    }

    private void createCustomerManagementDialog() {
        // Initialize the JDialog
        customerManagementDialog = new JDialog();
        customerManagementDialog.setTitle("Customer Management");
        customerManagementDialog.setSize(400, 300);
        customerManagementDialog.setLayout(new BorderLayout());

        // Create a panel for the top section (add, update, delete buttons and customer
        // information fields)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        customerNameField = new JTextField(10);
        customerEmailField = new JTextField(15);
        customerAddressField = new JTextField(10);

        topPanel.add(addButton);
        topPanel.add(updateButton);
        topPanel.add(deleteButton);
        topPanel.add(new JLabel("Name:"));
        topPanel.add(customerNameField);
        topPanel.add(new JLabel("Email:"));
        topPanel.add(customerEmailField);
        topPanel.add(new JLabel("Address:"));
        topPanel.add(customerAddressField);

        // Create a panel for the bottom section (list of customers)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        customersListModel = new DefaultListModel<>();
        customersList = new JList<>(customersListModel);
        JScrollPane customersScrollPane = new JScrollPane(customersList);

        bottomPanel.add(customersScrollPane, BorderLayout.CENTER);

        // Add action listeners to buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCustomer();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });

        // Add a "Back" button to close the dialog
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerManagementDialog.dispose(); // Close the dialog
            }
        });

        // Add components to the main frame
        customerManagementDialog.add(topPanel, BorderLayout.NORTH);
        customerManagementDialog.add(bottomPanel, BorderLayout.CENTER);
        customerManagementDialog.add(backButton, BorderLayout.SOUTH);

        // Set the dialog to be modal
        customerManagementDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        // Set the default close operation to dispose the dialog
        customerManagementDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void addCustomer() {
        String name = customerNameField.getText().trim();
        String email = customerEmailField.getText().trim();
        String address = customerAddressField.getText().trim();

        if (!name.isEmpty() && !email.isEmpty() && !address.isEmpty()) {
            String customerInfo = name + ";" + email + ";" + address;
            customersListModel.addElement(customerInfo);

            // Save the updated customer data (including the newly added customer)
            saveCustomerData();

            // Clear the input fields after adding
            customerNameField.setText("");
            customerEmailField.setText("");
            customerAddressField.setText("");
        }
    }

    private void updateCustomer() {
        int selectedIndex = customersList.getSelectedIndex();
        if (selectedIndex != -1) {
            String updatedName = customerNameField.getText().trim();
            String updatedEmail = customerEmailField.getText().trim();
            String updatedAddress = customerAddressField.getText().trim();

            if (!updatedName.isEmpty() && !updatedEmail.isEmpty() && !updatedAddress.isEmpty()) {
                String updatedCustomerInfo = updatedName + ";" + updatedEmail + ";" + updatedAddress;
                customersListModel.setElementAt(updatedCustomerInfo, selectedIndex);

                // Clear the input fields after updating
                customerNameField.setText("");
                customerEmailField.setText("");
                customerAddressField.setText("");
            }
        } else {
            JOptionPane.showMessageDialog(customerManagementDialog, "Please select a customer to update.");
        }
    }

    private void deleteCustomer() {
        int selectedIndex = customersList.getSelectedIndex();
        if (selectedIndex != -1) {
            customersListModel.removeElementAt(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(customerManagementDialog, "Please select a customer to delete.");
        }
    }

    private void loadCustomerData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                customersListModel.addElement(line);
            }
        } catch (IOException e) {
            // Handle file read error (e.g., file not found)
            e.printStackTrace();
        }
    }

    private void saveCustomerData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_DATA_FILE))) {
            for (int i = 0; i < customersListModel.size(); i++) {
                writer.write(customersListModel.getElementAt(i) + "\n");
            }
        } catch (IOException e) {
            // Handle file write error
            e.printStackTrace();
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ContentManagementSystem {

    private List<Category> categories;

    public ContentManagementSystem() {
        categories = new ArrayList<>();
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public static void main(String[] args) {
        ContentManagementSystem cms = new ContentManagementSystem();
        setupTestData(cms); // Initialize with some test data

        DefaultListModel<Category> categoryListModel = new DefaultListModel<>();
        DefaultListModel<Product> productListModel = new DefaultListModel<>();

        for (Category category : cms.getCategories()) {
            categoryListModel.addElement(category);
        }

        ContentManagementHandler handler = new ContentManagementHandler(cms, categoryListModel, productListModel);
        handler.handleContentManagement();
    }

    private static void setupTestData(ContentManagementSystem cms) {
        Category electronics = new Category();
        electronics.setName("Electronics");

        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setDescription("Powerful laptop for work and gaming");

        Product smartphone = new Product();
        smartphone.setName("Smartphone");
        smartphone.setDescription("Latest smartphone with high-quality camera");

        electronics.setProducts(new ArrayList<>());
        electronics.getProducts().add(laptop);
        electronics.getProducts().add(smartphone);

        cms.addCategory(electronics);
    }
}

class Category {
    private String name;
    private List<Product> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return name;
    }
}

class Product {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}

class ContentManagementHandler {

    private ContentManagementSystem cms;
    private DefaultListModel<Category> categoryListModel;
    private DefaultListModel<Product> productListModel;

    public ContentManagementHandler(ContentManagementSystem cms,
            DefaultListModel<Category> categoryListModel,
            DefaultListModel<Product> productListModel) {
        this.cms = cms;
        this.categoryListModel = categoryListModel;
        this.productListModel = productListModel;
    }

    public void handleContentManagement() {
        JFrame frame = new JFrame("Simple Content Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a category list
        JList<Category> categoryList = new JList<>(categoryListModel);
        JScrollPane categoryScrollPane = new JScrollPane(categoryList);
        mainPanel.add(categoryScrollPane, BorderLayout.WEST);

        // Create a product list
        JList<Product> productList = new JList<>(productListModel);
        JScrollPane productScrollPane = new JScrollPane(productList);
        mainPanel.add(productScrollPane, BorderLayout.CENTER);

        // Create buttons for adding new category and product
        JButton addCategoryButton = new JButton("Add Category");
        JButton addProductButton = new JButton("Add Product");

        addCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String categoryName = JOptionPane.showInputDialog(frame, "Enter category name:");
                if (categoryName != null && !categoryName.isEmpty()) {
                    Category newCategory = new Category();
                    newCategory.setName(categoryName);
                    cms.addCategory(newCategory);
                    categoryListModel.addElement(newCategory);
                }
            }
        });

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Category selectedCategory = categoryList.getSelectedValue();
                if (selectedCategory != null) {
                    String productName = JOptionPane.showInputDialog(frame, "Enter product name:");
                    if (productName != null && !productName.isEmpty()) {
                        Product newProduct = new Product();
                        newProduct.setName(productName);

                        if (selectedCategory.getProducts() == null) {
                            selectedCategory.setProducts(new ArrayList<>());
                        }
                        selectedCategory.getProducts().add(newProduct);

                        productListModel.addElement(newProduct);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a category first.");
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addCategoryButton);
        buttonPanel.add(addProductButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add a selection listener for the category list
        categoryList.addListSelectionListener(e -> {
            Category selectedCategory = categoryList.getSelectedValue();
            if (selectedCategory != null) {
                productListModel.clear();
                List<Product> products = selectedCategory.getProducts();
                if (products != null) {
                    for (Product product : products) {
                        productListModel.addElement(product);
                    }
                }
            }
        });

        frame.getContentPane().add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

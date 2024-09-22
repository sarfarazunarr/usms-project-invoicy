package com.invoicy;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class MainFrame extends JPanel {

    double totalAmount = 0.0;
    ArrayList<String> products = new ArrayList<>();
    String customerName, customerEmail;
    DefaultListModel<String> productListModel = new DefaultListModel<>();
    JList<String> productList = new JList<>(productListModel);
    JLabel totalAmountLabel;
    Database db;

    public MainFrame(Main main) { 
        db = new Database();

        setLayout(null); 

        // Create main heading
        JLabel mainHeading = new JLabel("Welcome to Invoice App");
        mainHeading.setBounds(220, 20, 400, 40);
        mainHeading.setFont(new Font("Arial", Font.BOLD, 28));
        mainHeading.setForeground(new Color(31, 55, 163));
        add(mainHeading);

        // Create customer details panel
        JPanel customerDetailsPanel = new JPanel();
        customerDetailsPanel.setBounds(50, 70, 320, 130);
        customerDetailsPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        customerDetailsPanel.setLayout(null);
        add(customerDetailsPanel);

        // Create customer details fields
        JLabel customerNameLabel = new JLabel("Customer Name:");
        customerNameLabel.setBounds(20, 30, 120, 20);
        customerDetailsPanel.add(customerNameLabel);

        JTextField customerNameField = new JTextField();
        customerNameField.setBounds(140, 30, 150, 20);
        customerDetailsPanel.add(customerNameField);

        JLabel customerEmailLabel = new JLabel("Customer Email:");
        customerEmailLabel.setBounds(20, 70, 120, 20);
        customerDetailsPanel.add(customerEmailLabel);

        JTextField customerEmailField = new JTextField();
        customerEmailField.setBounds(140, 70, 150, 20);
        customerDetailsPanel.add(customerEmailField);

        // Create products panel
        JPanel productsPanel = new JPanel();
        productsPanel.setBounds(50, 220, 320, 200);
        productsPanel.setBorder(BorderFactory.createTitledBorder("Products"));
        productsPanel.setLayout(null);
        add(productsPanel);

        // Create products fields
        JLabel productNameLabel = new JLabel("Product Name:");
        productNameLabel.setBounds(20, 30, 120, 20);
        productsPanel.add(productNameLabel);

        JTextField productNameField = new JTextField();
        productNameField.setBounds(140, 30, 150, 20);
        productsPanel.add(productNameField);

        JLabel productQuantityLabel = new JLabel("Product Quantity:");
        productQuantityLabel.setBounds(20, 70, 120, 20);
        productsPanel.add(productQuantityLabel);

        JTextField productQuantityField = new JTextField();
        productQuantityField.setBounds(140, 70, 150, 20);
        productsPanel.add(productQuantityField);

        JLabel productPriceLabel = new JLabel("Product Price:");
        productPriceLabel.setBounds(20, 110, 120, 20);
        productsPanel.add(productPriceLabel);

        JTextField productPriceField = new JTextField();
        productPriceField.setBounds(140, 110, 150, 20);
        productsPanel.add(productPriceField);

        // Create add product button
        JButton addProduct = new JButton("Add Product");
        addProduct.setBounds(170, 150, 120, 30);
        addProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String productName = productNameField.getText();
                String productQuantity = productQuantityField.getText();
                String productPrice = productPriceField.getText();

                if (productName.isEmpty() || productQuantity.isEmpty() || productPrice.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                } else {
                    totalAmount += Double.parseDouble(productPrice) * Double.parseDouble(productQuantity);
                    String productDetails = productName + "- Qty: " + productQuantity + " Rs." + productPrice;
                    products.add(productDetails);

                    // Update the product list and total amount
                    productListModel.addElement(productDetails);
                    updateTotalAmount();

                    // Clear the fields
                    productNameField.setText("");
                    productQuantityField.setText("");
                    productPriceField.setText("");
                }
            }
        });
        productsPanel.add(addProduct);

        // Details Section
        JPanel detailsPanel = new JPanel();
        detailsPanel.setBounds(400, 70, 350, 350);
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Invoice Details"));
        detailsPanel.setLayout(null);
        add(detailsPanel);

        // Total amount label
        totalAmountLabel = new JLabel("Total Amount: Rs.0.00");
        totalAmountLabel.setBounds(20, 30, 500, 20);
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalAmountLabel.setForeground(new Color(0, 128, 0));
        detailsPanel.add(totalAmountLabel);

        // Products label
        JLabel productsLabel = new JLabel("Products: ");
        productsLabel.setBounds(20, 70, 200, 20);
        detailsPanel.add(productsLabel);

        // Products list display
        JScrollPane productScrollPane = new JScrollPane(productList);
        productScrollPane.setBounds(20, 100, 300, 200);
        detailsPanel.add(productScrollPane);

        JButton generateInvoice = new JButton("Generate Invoice");
        generateInvoice.setBounds(180, 310, 130, 30);
        generateInvoice.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                customerName = customerNameField.getText();
                customerEmail = customerEmailField.getText();
                // Check if customer details are filled
                if (customerName.isEmpty() || customerEmail.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in the customer details.");
                    return;
                }

                // Retrieve product details
                StringBuilder productsData = new StringBuilder();
                for (int i = 0; i < productListModel.size(); i++) {
                    productsData.append(productListModel.get(i)).append("|");
                }

                // Check if there are any products added
                if (productListModel.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please add products before generating the invoice.");
                    return;
                }

                // Save invoice to the database
                int id = db.saveInvoice(customerName, customerEmail, productsData.toString(), totalAmount);

                // Display confirmation message
                JOptionPane.showMessageDialog(null, "Invoice generated and saved successfully!");
                PDFGenerator pg = new PDFGenerator();
                try {
                    pg.generatePDF(customerName, customerEmail, productsData.toString(), totalAmount, id);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                // Clear fields after saving
                customerNameField.setText("");
                customerEmailField.setText("");
                productListModel.clear();
                totalAmount = 0.0;
                totalAmountLabel.setText("Total Amount: Rs.0.0");

            }

        });
        detailsPanel.add(generateInvoice);



        // Timer to refresh the product list (if needed)
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productListModel.clear();
                for (String product : products) {
                    productListModel.addElement(product);
                }
            }
        });
        timer.start();
    }

    // Method to update the total amount label
    private void updateTotalAmount() {
        totalAmountLabel.setText("Total Amount: Rs." + String.format("%.2f", totalAmount));
    }
}

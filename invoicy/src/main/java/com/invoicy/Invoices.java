package com.invoicy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Invoices extends JPanel {

    Database db;
    JTable invoicesTable;
    DefaultTableModel tableModel;

    public Invoices(Main main) {
        setLayout(new BorderLayout());
        
        JLabel label = new JLabel("Invoices:");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.NORTH);

        // Table for displaying invoices
        tableModel = new DefaultTableModel();
        invoicesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(invoicesTable);
        add(scrollPane, BorderLayout.CENTER);

        // Fetch data from the database and populate the table
        db = new Database();
        loadInvoices();
    }

    // Method to fetch invoices from database and load them into the JTable
    private void loadInvoices() {
        ResultSet resultSet = db.getAllInvoices();
        try {
            if (resultSet != null) {
                // Clear existing table data
                tableModel.setRowCount(0);
                tableModel.setColumnCount(0);

                // Get ResultSet metadata to create table columns dynamically
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Add column names to the table model
                for (int i = 1; i <= columnCount; i++) {
                    tableModel.addColumn(metaData.getColumnName(i));
                }

                // Add rows to the table model
                while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = resultSet.getObject(i);
                    }
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

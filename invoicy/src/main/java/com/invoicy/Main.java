package com.invoicy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    CardLayout cardLayout = new CardLayout();
    JPanel cardPanel;

    public Main() {
        // Set up the main frame
        setTitle("Invoicy - Manage your invoices and generate bills!");
        setSize(800, 520);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Center frame on screen
        Toolkit nt = getToolkit();
        Dimension size = nt.getScreenSize();
        setLocation(size.width / 2 - getWidth() / 2, size.height / 2 - getHeight() / 2);

        // Create main panel with CardLayout to switch between screens
        cardPanel = new JPanel(cardLayout);

        // Create the different screens (panels)
        MainFrame addDataPanel = new MainFrame(this);  // Pass Main reference
        Invoices invoicesPanel = new Invoices(this);  // Pass Main reference
        Settings settings = new Settings(this);  // Pass Main reference

        // Add the panels to the card layout
        cardPanel.add(addDataPanel, "AddData");
        cardPanel.add(invoicesPanel, "ViewInvoices");
        cardPanel.add(settings, "settings");

        // Set up the navigation panel with buttons
        JPanel navigationPanel = new JPanel();
        JButton addDataButton = new JButton("Create Invoice");
        JButton viewInvoicesButton = new JButton("View Invoices");
        JButton viewSetting = new JButton("View Settings");

        // Button action to switch to the Add Data screen
        addDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "AddData");
            }
        });

        // Button action to switch to the View Invoices screen
        viewInvoicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ViewInvoices");
            }
        });

        viewSetting.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
               cardLayout.show(cardPanel, "settings");
            }
            
        });
        navigationPanel.add(addDataButton);
        navigationPanel.add(viewInvoicesButton);
        navigationPanel.add(viewSetting);

        // Add navigation and card panels to the frame
        add(navigationPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        if (ManageSettings.getSettings().getCompanyName().isEmpty() || ManageSettings.getSettings().getName().isEmpty()) {
            cardLayout.show(cardPanel, "settings");
        } else {
            cardLayout.show(cardPanel, "AddData");
        }
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}

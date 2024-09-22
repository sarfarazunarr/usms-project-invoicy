package com.invoicy;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Settings extends JPanel {


    public Settings(Main main) {

        setLayout(null);
        JLabel mainHeading = new JLabel("Company Data");
        mainHeading.setBounds(250, 20, 400, 40);
        mainHeading.setFont(new Font("Arial", Font.BOLD, 28));
        mainHeading.setForeground(new Color(31, 55, 163));
        add(mainHeading);

        // Create customer details panel
        JPanel companydetailsJPanel = new JPanel();
        companydetailsJPanel.setBounds(220, 70, 320, 300);
        companydetailsJPanel.setBorder(BorderFactory.createTitledBorder("Company Details"));
        companydetailsJPanel.setLayout(null);
        add(companydetailsJPanel);

        JLabel companyName = new JLabel("Company Name:");
        companyName.setBounds(20, 30, 120, 20);
        companydetailsJPanel.add(companyName);
        JTextField companyNameField = new JTextField();
        companyNameField.setText(ManageSettings.getSettings().getCompanyName().isEmpty() ? "" : ManageSettings.getSettings().getCompanyName());
        companyNameField.setBounds(140, 30, 150, 20);
        companydetailsJPanel.add(companyNameField);

        JLabel companyPhone = new JLabel("Company Phone:");
        companyPhone.setBounds(20, 60, 120, 20);
        companydetailsJPanel.add(companyPhone);
        JTextField companyPhoneField = new JTextField();
        companyPhoneField.setBounds(140, 60, 150, 20);
        companyPhoneField.setText(ManageSettings.getSettings().getPhone().isEmpty() ? "" : ManageSettings.getSettings().getPhone());
        companydetailsJPanel.add(companyPhoneField);

        JLabel companyEmail = new JLabel("Company Email:");
        companyEmail.setBounds(20, 90, 120, 20);
        companydetailsJPanel.add(companyEmail);
        JTextField companyEmailField = new JTextField();
        companyEmailField.setText(ManageSettings.getSettings().getEmail().isEmpty() ? "" : ManageSettings.getSettings().getEmail());
        companyEmailField.setBounds(140, 90, 150, 20);
        companydetailsJPanel.add(companyEmailField);

        JLabel username = new JLabel("User Name");
        username.setBounds(20, 120, 120, 20);
        companydetailsJPanel.add(username);
        JTextField usernameField = new JTextField();
        usernameField.setText(ManageSettings.getSettings().getName().isEmpty() ? "" : ManageSettings.getSettings().getName());
        usernameField.setBounds(140, 120, 150, 20);
        companydetailsJPanel.add(usernameField);

        JLabel companyzipcode = new JLabel("Zipcode:");
        companyzipcode.setBounds(20, 150, 120, 20);
        companydetailsJPanel.add(companyzipcode);
        JTextField companyzipcodeField = new JTextField();
        companyzipcodeField.setText(ManageSettings.getSettings().getZipcode().isEmpty() ? "" : ManageSettings.getSettings().getZipcode());
        companyzipcodeField.setBounds(140, 150, 150, 20);
        companydetailsJPanel.add(companyzipcodeField);

        JLabel companyAddress = new JLabel("Address:");
        companyAddress.setBounds(20, 180, 120, 20);
        companydetailsJPanel.add(companyAddress);
        JTextField companyAddressField = new JTextField();
        companyAddressField.setText(ManageSettings.getSettings().getAddress().isEmpty() ? "" : ManageSettings.getSettings().getAddress());
        companyAddressField.setBounds(140, 180, 150, 20);
        companydetailsJPanel.add(companyAddressField);

        // Create add product button
        JButton saveSetting = new JButton("Save Settings");
        saveSetting.setBounds(170, 210, 120, 30);
        saveSetting.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String companyname = companyNameField.getText();
                String companyEmail = companyEmailField.getText();
                String companyPhone = companyPhoneField.getText();
                String username = usernameField.getText();
                String zipcode = companyzipcodeField.getText();
                String address = companyAddressField.getText();
                if (companyname.isEmpty() || companyEmail.isEmpty() || companyPhone.isEmpty() || username.isEmpty()
                        || zipcode.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all details.");
                    return;
                }
                Database db = new Database();
                db.setProfile(companyname, zipcode, address, username, companyEmail, companyPhone);
                JOptionPane.showMessageDialog(null, "Settings Saved, Restart App to apply!");
            }

        });
        companydetailsJPanel.add(saveSetting);

        add(companydetailsJPanel);
    }
}

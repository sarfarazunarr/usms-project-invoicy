package com.invoicy;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageSettings {
    private String companyName = "";
    private String zipcode = "";
    private String address = "";
    private String name = "";
    private String email = "";
    private String phone = "";

    static ManageSettings getSettings() {
    try {
        Database db = new Database();
        ResultSet tempdata = db.getSettings();
        ManageSettings settings = new ManageSettings();
        if(tempdata.next()){
            settings.setCompanyName(tempdata.getString("company_name"));
            settings.setZipcode(tempdata.getString("zipcode"));
            settings.setAddress(tempdata.getString("address"));
            settings.setName(tempdata.getString("name")); 
            settings.setEmail(tempdata.getString("email"));
            settings.setPhone(tempdata.getString("phone"));
        }
        return settings;
    } catch (SQLException e) {
        // handle exception
        System.out.println(e.getMessage());
        System.out.println(e.getErrorCode());
        return null;
    } 
}


    // getters and setters
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getZipcode() {
        return zipcode;
    }
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}

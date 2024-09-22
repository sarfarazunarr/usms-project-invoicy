package com.invoicy;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFGenerator {

    public void generatePDF(String customerName, String customerEmail, String productsData, double totalAmount, int id)
            throws IOException {
        ManageSettings data = ManageSettings.getSettings();

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Set font for content
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);

        // Add Company Name and Header
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
        contentStream.newLineAtOffset(50, 780);
        contentStream.showText(data.getCompanyName());
        contentStream.endText();

        // Company Address and Contact Information
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        contentStream.newLineAtOffset(50, 760);
        contentStream.showText(data.getAddress());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Zip Code: " + data.getZipcode());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Phone: " + data.getPhone());
        contentStream.endText();

        // Invoice and Date Header Section
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.newLineAtOffset(400, 780);
        contentStream.showText("INVOICE");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        contentStream.newLineAtOffset(400, 760);
        contentStream.showText("Invoice #: " + id);
        contentStream.newLineAtOffset(0, -15);
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        contentStream.showText("Date: " + date);
        contentStream.endText();

        // Bill To section
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.newLineAtOffset(50, 710);
        contentStream.showText("BILL TO:");
        contentStream.endText();

        // Customer Details
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        contentStream.newLineAtOffset(50, 690);
        contentStream.showText(customerName);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Email: " + customerEmail);
        contentStream.endText();

        // Table Header: Description and Amount
        contentStream.setLineWidth(1f);
        contentStream.moveTo(50, 630);
        contentStream.lineTo(550, 630);
        contentStream.stroke();

        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.newLineAtOffset(50, 640);
        contentStream.showText("DESCRIPTION");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(450, 640);
        contentStream.showText("AMOUNT");
        contentStream.endText();

        // Add product details (simulating table rows)
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        String[] productLines = productsData.split("\\|");
        int yPosition = 620; // Start drawing the products a bit lower in the page

        for (String productLine : productLines) {
            if (!productLine.isEmpty()) {
                String description = productLine.split("-")[0]; // Extract product name
                String qtyAndPrice = productLine.split("-")[1].trim(); // Extract quantity and price
                double productAmount = extractAmount(qtyAndPrice); // Calculate total for the product

                // Add product description
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText(description);
                contentStream.endText();

                // Add product amount
                contentStream.beginText();
                contentStream.newLineAtOffset(410, yPosition);
                contentStream.showText("Rs. " + String.format("%.2f", productAmount));
                contentStream.endText();

                // Move to next line
                yPosition -= 20;
            }
        }

        // Add horizontal line after product details
        contentStream.setLineWidth(1f);
        contentStream.moveTo(50, yPosition - 10);
        contentStream.lineTo(550, yPosition - 10);
        contentStream.stroke();

        // Total Amount
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.newLineAtOffset(420, yPosition - 30);
        contentStream.showText("TOTAL: PKR " + String.format("%.2f", totalAmount));
        contentStream.endText();

        // Footer Note
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        contentStream.newLineAtOffset(50, 100);
        contentStream.showText("Thank you for your business!");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(50, 80);
        contentStream.showText("If you have any questions about this invoice, please contact");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText(data.getName() + " " + data.getPhone() + " " + data.getEmail());
        contentStream.endText();

        // Close content stream and save the document
        contentStream.close();
        String directoryPath = "invoices"; // Specify your desired directory name
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path); // Create the directory
        }

        // Save the PDF in the invoices directory
        String filePath = directoryPath + File.separator + "invoice " + id + ".pdf";
        document.save(filePath);
        document.close();

        try {
            File pdfFile = new File(filePath);
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                System.out.println("File does not exist");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private double extractAmount(String qtyAndPrice) {
        try {
            // Assuming the string format is "Qty: X Rs.YYY"
            String[] parts = qtyAndPrice.split(" ");
            int quantity = Integer.parseInt(parts[1].replace("Qty:", ""));
            double price = Double.parseDouble(parts[2].replace("Rs.", ""));
            return quantity * price; // Total for this product
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

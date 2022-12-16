package com.example.authmoduls.auth.decorator;

import com.example.authmoduls.ar.auth.decorator.ShoppingListLog;
import com.example.authmoduls.ar.auth.model.RecipeIngredient;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

public class PdfGenerator {

    public void generate(ShoppingListLog shoppingListLogs, HttpServletResponse response) throws DocumentException, IOException {

        // Creating the Object of Document
        Document document = new Document(PageSize.A4);

        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());

        // Opening the created document to change it
        document.open();

        // Creating font
        // Setting font style and size
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTitle.setSize(20);

        // Creating paragraph (Heading for the PDF data)
        Paragraph paragraph1 = new Paragraph("List of the Shopping", fontTitle);
        paragraph1.setAlignment(Element.ALIGN_CENTER);

        // Adding the created paragraph in the document
        document.add(paragraph1);

        // Creating a table of the 4 columns
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{5, 5, 2, 2});
        table.setSpacingBefore(10);

        // Create Table Cells for the table header
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.DARK_GRAY);
        cell.setPadding(5);
        cell.setHorizontalAlignment(1);

        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(Color.WHITE);

        // Adding headings in the created table cell or  header
        // Adding Cell to table
        cell.setPhrase(new Phrase("Login Id", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Recipe Id", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Quantity", font));
        table.addCell(cell);
        // Iterating the list of shopping
            for (RecipeIngredient ingredient : shoppingListLogs.getIngredients()) {
                table.addCell(String.valueOf(shoppingListLogs.getLoginId()));
                table.addCell(String.valueOf(shoppingListLogs.getRecipeId()));
                table.addCell(ingredient.getIngredientName());
                table.addCell(String.valueOf(ingredient.getQuantity()));
            }

        // Adding the created table to the document
        document.add(table);

        // Closing the document
        document.close();

    }

}
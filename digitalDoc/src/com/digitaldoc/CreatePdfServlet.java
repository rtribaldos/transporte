package com.digitaldoc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@SuppressWarnings("serial")
public class CreatePdfServlet extends HttpServlet {

 @Override
 protected void doGet(HttpServletRequest request,
   HttpServletResponse response) throws ServletException, IOException {
  response.setContentType("application/pdf");

  try {
   Document document = new Document();
   ByteArrayOutputStream output = new ByteArrayOutputStream();
   PdfWriter.getInstance(document, output) ;
   document.open();
   document.add(new Paragraph("This is a paragraph"));
   document.close();
   
  // System.out.println(new String(output.toByteArray())); 
   
   response.getOutputStream().write(output.toByteArray()); 
   
  } catch (DocumentException e) {
   e.printStackTrace();
  }
 }

}

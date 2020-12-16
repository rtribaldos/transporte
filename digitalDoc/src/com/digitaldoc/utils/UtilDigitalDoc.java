package com.digitaldoc.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.net.ssl.KeyManagerFactory;

import com.digitaldoc.model.AnticipoFile;
import com.digitaldoc.model.CancelFile;
import com.digitaldoc.model.MediaObject;
import com.digitaldoc.model.Usuario;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PrivateKeySignature;


public class UtilDigitalDoc {
	
	public static final String KEYSTORE = "/resources/garceray.pfx";
	public static final char[] PASSWORD = "4321".toCharArray();
	public static final String EMAIL_NOTIFICA = "admin@garceray.com";
	public static final String PWD_MAIL= "TGarcerai1972";
	
		
	public static void cargaMailEmpleado(StringBuffer asunto, StringBuffer cuerpo, Usuario user){
		asunto.append("Su nómina correspondiente al mes de x está disponible");
		cuerpo.append("<html><body>Hola " + user.getNombre() +",<br>");
		cuerpo.append("Su n&oacute;mina correspondiente al mes de x está disponible en nuestro sistema Online, puede consultar y descargarla "
				+ " desde nuestro sistema Online.<br>");
		cuerpo.append("https://garceray.appspot.com/");
		cuerpo.append("<br>Les recordamos que toda la documentaci&oacute;n descargada desde nuestro sistema, est&aacute; firmada digitalmente con la firma de Garceray "
				+ "Log&iacute;stica 2000 S.L.<br>");
		cuerpo.append("Muchas gracias.");
		cuerpo.append("</body></html>");
	}
	
	public static void cargaMailCliente(StringBuffer asunto, StringBuffer cuerpo, Usuario user, String factura, Usuario usuario){
		
		if(usuario.getIdioma() != null && usuario.getIdioma().equals("fr")) {
			
			asunto.append("Facture " + factura +" disponible");
			
			cuerpo.append("<html charset=\"ISO-8859-1\"><body>Bonjour.<br><br>");
			cuerpo.append("Ci-joint notre facture  " + factura +"<br>Vous pouvez aussi la trouver avec toute la documentation correspondante dans l’espace"
					+ " clients de notre site web.<br><br>www.garceray.com<br><br>");
			cuerpo.append("Usuaire :  " + usuario.getEmail() + "<br>Mot de passe : " + usuario.getPassword() + "<br><br>");
			cuerpo.append("N´hésitez pas a nous contacter pour n’importe quelle question. <br> Merci bien!!!");
			cuerpo.append("<br><br>Cordialement,<br>");
			cuerpo.append("Lola Martinez<br>");
			cuerpo.append("<img src=\"data:image/jpeg;base64," + getLogoB64() + "\">");
			cuerpo.append("</body></html>");
			//
		}else if(usuario.getIdioma() != null && usuario.getIdioma().equals("en")) {
			
			asunto.append("Our invoice " + factura +" ");
			
			cuerpo.append("<html charset=\"ISO-8859-1\"><body>Good day.<br><br>");
			cuerpo.append("Please see enclosed our invoice  " + factura +"<br>You can also find it along with all documentation in the customer area of our website.<br><br>www.garceray.com<br><br>");
			cuerpo.append("Username :  " + usuario.getEmail() + "<br>Password : " + usuario.getPassword() + "<br><br>");
			cuerpo.append("Do not hesitate to contact us for any clarification. <br> Thank you so much!!!!!!");
			cuerpo.append("<br><br>Best Regards,<br>");
			cuerpo.append("Lola Martinez<br>");
			cuerpo.append("<img src=\"data:image/jpeg;base64," + getLogoB64() + "\">");
			cuerpo.append("</body></html>");
			
		}else {
			
			asunto.append("Factura " + factura +" disponible");
			
			cuerpo.append("<html charset=\"ISO-8859-1\"><body>Buenos días.<br><br>");
			cuerpo.append("Adjuntamos  nuestra factura   " + factura +"<br>Puedo consultar y descargar toda la documentación correspondiente en el área de clientes de nuestra página web.<br><br>www.garceray.com<br><br>");
			cuerpo.append("Usuario :  " + usuario.getEmail() + "<br>Contraseña : " + usuario.getPassword() + "<br><br>");
			cuerpo.append("No duden en contactarnos para cualquier aclaración. <br> Muchas Gracias!!!!!!!");
			cuerpo.append("<br><br>Atentamente,<br>");
			cuerpo.append("Lola Martinez<br>");
			cuerpo.append("<img src=\"data:image/jpeg;base64," + getLogoB64() + "\">");
			cuerpo.append("</body></html>");
			
		}
		
		
	}
	
	public static void cargaMailAcreedor(StringBuffer asunto, StringBuffer cuerpo, Usuario user, String factura){
		asunto.append("Solicitud anticipo " + factura +" está disponible");
		cuerpo.append("<html><body>Muy Sres. Nuestros,<br><br>");
		cuerpo.append("Solicitud anticipo " + factura +" est&aacute; disponible en nuestro sistema Online, puede consultar y descargar "
				+ " toda la documentaci&oacute;n correspondiente a esta solicitud desde nuestro sistema Online.<br><br>");
		cuerpo.append("https://garceray.appspot.com/");
		cuerpo.append("<br><br>Les recordamos que toda la documentaci&oacute;n descargada desde nuestro sistema, est&aacute; firmada digitalmente con la firma de Garceray "
				+ "Log&iacute;stica 2000 S.L.<br><br>");
		cuerpo.append("Recuerde que para entrar en nuestro sistema debe usar su correo como usuario.<br>");
		cuerpo.append("Muchas gracias.");
		cuerpo.append("</body></html>");
	}
	
	
	public static void cargaMailCancel(StringBuffer asunto, StringBuffer cuerpo, Usuario user, String factura){
		asunto.append("SOLICITUD CANCELACION ANTICIPO  " + factura +" est&aacute; disponible");
		cuerpo.append("<html><body>Muy Sres. Nuestros,<br>");
		cuerpo.append("Les solicitamos la cancelaci&oacute;n del anticipo, según el detalle del archivo adjunto. <br> Muchas gracias.<br>");
		cuerpo.append("https://garceray.appspot.com/");
		cuerpo.append("</body></html>");
	}
	
	public static void enviaCorreo(String email, String sHtml, String subject, byte[] facturaFirmada, String nomFactura) {

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.garceray.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_NOTIFICA, PWD_MAIL);
                    }
                });

        try {
        	        	        	
        	MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_NOTIFICA));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject(subject);
           
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(sHtml, "text/html");
                                  
        	MimeBodyPart attachPart = new MimeBodyPart();
        	ByteArrayDataSource bds = new ByteArrayDataSource(facturaFirmada, "application/pdf"); 
        	attachPart.setDataHandler(new DataHandler(bds));
        	attachPart.setFileName(nomFactura);         	
        	
        	Multipart multipart = new MimeMultipart();		   
        	multipart.addBodyPart(messageBodyPart);
        	multipart.addBodyPart(attachPart);
		    
		    message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
	}
	
	
	
	public static void enviaCorreo(String email, String sHtml, String subject, byte[] attachmentBytes, String attachmentName, boolean copiaConta){
	 try {	
		 MailService mailService = MailServiceFactory.getMailService();
		 MailService.Message message = new MailService.Message();
		 message.setSender("garceray.online@gmail.com");
		
		 message.setSubject(subject.toString());
		 
		 List<String> correos = new ArrayList<String>();
		 email = email.replace(";", ",");
		 StringTokenizer st = new StringTokenizer(email, ",");

		 while(st.hasMoreTokens()) {
			 correos.add(st.nextToken());
		 }			 
		 //correos.add(email);
		 message.setTo(correos);
		/* if(copiaConta){
			 message.setCc("contabilidad@garceray.com");
		 }else{
			 message.setCc("info@garceray.com");
		 }*/
		 
		 if(attachmentBytes != null){
			 MailService.Attachment attachment=new MailService.Attachment(attachmentName,attachmentBytes);
			 message.setAttachments(attachment);
		 }
		 
		// message.setTextBody("Le adjuntamos factura, atentamente Garceray.");
		 message.setHtmlBody(sHtml);
		
		 // MailService.Attachment attachment =
		 //        new MailService.Attachment("pic.jpg", picture);
		 //message.setAttachments(attachment);
		 mailService.send(message);
		 } catch (Exception e) {
				e.printStackTrace();
		}
		
	}
	
	public static void enviaCorreo(String email, String sHtml, String subject, Map<String, byte[]> ficheros, String attachmentName, boolean copiaConta){
		 try {	
			 MailService mailService = MailServiceFactory.getMailService();
			 MailService.Message message = new MailService.Message();
			 message.setSender("garceray.online@gmail.com");
			 message.setSubject(subject.toString());
			 
			 List<String> correos = new ArrayList<String>();
			 email = email.replace(";", ",");
			 StringTokenizer st = new StringTokenizer(email, ",");

			 while(st.hasMoreTokens()) {
				 correos.add(st.nextToken());
			 }			 
			 //correos.add(email);
			 message.setTo(correos);
			/* if(copiaConta){
				 message.setCc("contabilidad@Garceray.com");
			 }else{
				 message.setCc("info@Garceray.com");
			 }*/
			 
			 if(ficheros != null){
				 List<MailService.Attachment> attachments = new ArrayList<>();
				 for (Map.Entry<String, byte[]> entry : ficheros.entrySet()) {
					 MailService.Attachment attachment=new MailService.Attachment(entry.getKey(),entry.getValue());
					 attachments.add(attachment);
				 }
				 message.setAttachments(attachments);
			 }
			 
			// message.setTextBody("Le adjuntamos factura, atentamente Garceray.");
			 message.setHtmlBody(sHtml);
			
			 // MailService.Attachment attachment =
			 //        new MailService.Attachment("pic.jpg", picture);
			 //message.setAttachments(attachment);
			 mailService.send(message);
			 } catch (Exception e) {
					e.printStackTrace();
			}
			
		}

	
	public static byte[] signFactura(MediaObject doc){
		try {
			// InputStreamReader is = new InputStreamReader(new BlobstoreInputStream(doc.getBlob()));
			 BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(doc.getBlob());
			 BlobstoreInputStream stream = new BlobstoreInputStream(doc.getBlob());
			 byte[]  bytesArray = new byte[(int) blobInfo.getSize()];
			 stream.read(bytesArray);
			    
			 KeyManagerFactory kmf = javax.net.ssl.KeyManagerFactory.getInstance("SunX509");
	         KeyStore keystore = KeyStore.getInstance("PKCS12");
	         
	         URL resource = UtilDigitalDoc.class.getResource(KEYSTORE);
	 		 File fPfx = new File(resource.toURI());
	         
	         keystore.load(new FileInputStream(fPfx),PASSWORD);
	         kmf.init(keystore, PASSWORD);
			 
			 String alias = (String)keystore.aliases().nextElement();
		     PrivateKey pk = (PrivateKey) keystore.getKey(alias, PASSWORD);
		     Certificate[] chain = keystore.getCertificateChain(alias);
			
		     return sign(bytesArray, chain, pk, DigestAlgorithms.SHA1, "SunJSSE" , CryptoStandard.CMS, "Firma", "Garceray");
		     
		    //blobstoreService.serve(blobKey, resp);
		    // doc.setBlob(blobKey);
	 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] signAnticipo(AnticipoFile doc){
		try {
			// InputStreamReader is = new InputStreamReader(new BlobstoreInputStream(doc.getBlob()));
			 BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(doc.getBlob());
			 BlobstoreInputStream stream = new BlobstoreInputStream(doc.getBlob());
			 byte[]  bytesArray = new byte[(int) blobInfo.getSize()];
			 stream.read(bytesArray);
			    
			 KeyManagerFactory kmf = javax.net.ssl.KeyManagerFactory.getInstance("SunX509");
	         KeyStore keystore = KeyStore.getInstance("PKCS12");	        

	         URL resource = UtilDigitalDoc.class.getResource(KEYSTORE);
	 		 File fPfx = new File(resource.toURI());
	         
	         keystore.load(new FileInputStream(fPfx),PASSWORD);
	         kmf.init(keystore, PASSWORD);
			 
			 String alias = (String)keystore.aliases().nextElement();
		     PrivateKey pk = (PrivateKey) keystore.getKey(alias, PASSWORD);
		     Certificate[] chain = keystore.getCertificateChain(alias);
			
		     return sign(bytesArray, chain, pk, DigestAlgorithms.SHA1, "SunJSSE" , CryptoStandard.CMS, "Firma", "Garceray");
		    
		     //blobstoreService.serve(blobKey, resp);
		     // doc.setBlob(blobKey);
	 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] signCancelacion(CancelFile doc){
		try {
			// InputStreamReader is = new InputStreamReader(new BlobstoreInputStream(doc.getBlob()));
			 BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(doc.getBlob());
			 BlobstoreInputStream stream = new BlobstoreInputStream(doc.getBlob());
			 byte[]  bytesArray = new byte[(int) blobInfo.getSize()];
			 stream.read(bytesArray);
			    
			 KeyManagerFactory kmf = javax.net.ssl.KeyManagerFactory.getInstance("SunX509");
	         KeyStore keystore = KeyStore.getInstance("PKCS12");
	         
	         URL resource = UtilDigitalDoc.class.getResource(KEYSTORE);
	 		 File fPfx = new File(resource.toURI());
	         
	         keystore.load(new FileInputStream(fPfx), PASSWORD);
	         kmf.init(keystore, PASSWORD);
			 
			 String alias = (String)keystore.aliases().nextElement();
		     PrivateKey pk = (PrivateKey) keystore.getKey(alias, PASSWORD);
		     Certificate[] chain = keystore.getCertificateChain(alias);
			
		     return sign(bytesArray, chain, pk, DigestAlgorithms.SHA1, "SunJSSE" , CryptoStandard.CMS, "Firma", "Garceray");
		     		   
		     //blobstoreService.serve(blobKey, resp);		     
			// doc.setBlob(blobKey);
	 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
		
	
	public static byte[] sign(byte[] src, Certificate[] chain, PrivateKey pk, String digestAlgorithm, String provider, CryptoStandard subfilter, 
			String reason, String location)	throws GeneralSecurityException, IOException, DocumentException {
		
        // Creating the reader and the stamper
        PdfReader reader = new PdfReader(src);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
        // Creating the appearance
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setReason(reason);
        appearance.setLocation(location);
        appearance.setVisibleSignature(new Rectangle(516, 860, 144, 800), 1, "sig");
        // Creating the signature
        ExternalDigest digest = new BouncyCastleDigest();
        ExternalSignature signature = new PrivateKeySignature(pk, digestAlgorithm, provider);
        MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, subfilter);        
        return os.toByteArray();
	}
 
	public static void main2(String[] args) throws GeneralSecurityException, IOException, DocumentException, URISyntaxException {
	
		 KeyManagerFactory kmf = javax.net.ssl.KeyManagerFactory.getInstance("SunX509");
         KeyStore keystore = KeyStore.getInstance("PKCS12");
        
         URL resource = UtilDigitalDoc.class.getResource(KEYSTORE);
 		 File file = new File(resource.toURI());
         
         keystore.load(new FileInputStream(file), PASSWORD);
         kmf.init(keystore, PASSWORD);
		 
		 String alias = (String)keystore.aliases().nextElement();
	     PrivateKey pk = (PrivateKey) keystore.getKey(alias, PASSWORD);
	     Certificate[] chain = keystore.getCertificateChain(alias);
		
	     String pdf="/home/lydia/Descargas/garceray.pdf";
	     String pdf2="/home/lydia/Descargas/firmado2.pdf";
	     
	     File fPdf = new File(pdf);
	   //init array with file length
	     byte[] bytesArray = new byte[(int) fPdf.length()];

	     FileInputStream fis = new FileInputStream(fPdf);
	     fis.read(bytesArray); //read file into bytes[]
	     fis.close();
	   
	   
		 byte[] firmado= sign(bytesArray, chain, pk, DigestAlgorithms.SHA1, "SunJSSE" , CryptoStandard.CMS, "Firma", "Garceray");
		  
		 try (FileOutputStream fos = new FileOutputStream(pdf2)) {
		    fos.write(firmado);
		 }

	     System.out.println("Hecho!");
	  
	}
	
	
	public static void main(String[] args) {

        final String username = "admin@garceray.com";
        final String password = "TGarcerai1972";

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.garceray.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("raultribaldos@gmail.com")
            );
            message.setSubject("Testeando correo....");
            message.setText("Esto es una prueba,"
                    + "\n\n Por favor no m envies spam al mail!");

            Transport.send(message);

            System.out.println("Hecho");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

	
	public static String findKeyAlias(KeyStore store, byte[] storeFile, char[] password) throws Exception {
	   	 
		store.load(new ByteArrayInputStream(storeFile), password);
		
    	Enumeration e = store.aliases();
    	String keyAlias = null;

    	while (e.hasMoreElements()) {
        	String alias = (String) e.nextElement();
        	if (store.isKeyEntry(alias)) {
            	keyAlias = alias;
        	}
    	}

    	if (keyAlias == null) {
        	throw new IllegalArgumentException("can't find a private key in keyStore");
    	}

    	return keyAlias;
	   		 
	}

	
	public static String getLogoB64() {
		return "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";
	}
	
}

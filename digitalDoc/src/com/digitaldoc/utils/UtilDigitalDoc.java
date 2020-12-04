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
		
		if(usuario.getIdioma() != null || usuario.getIdioma().equals("fr")) {
			
			asunto.append("Facture " + factura +" disponible");
			
			cuerpo.append("<html><body>Bonjour.<br><br>");
			cuerpo.append("Ci-joint notre facture  " + factura +"<br>Vous pouvez aussi la trouver avec toute la documentation correspondante dans l’espace"
					+ " clients de notre site web.<br><br>www.garceray.com<br><br>");
			cuerpo.append("Usuaire :  " + usuario.getEmail() + "<br>Mot de passe : " + usuario.getPassword() + "<br><br>");
			cuerpo.append("N´hésitez pas a nous contacter pour n’importe quelle question. <br> Merci bien!!!");
			cuerpo.append("<br><br>Cordialement,<br>");
			cuerpo.append("<img src=\"/assets/img/garceray/firma.png\">");
			//
		}else if(usuario.getIdioma() != null || usuario.getIdioma().equals("en")) {
			
			asunto.append("Our invoice " + factura +" ");
			
			cuerpo.append("<html><body>Good day.<br><br>");
			cuerpo.append("Please see enclosed our invoice  " + factura +"<br>You can also find it along with all documentation in the customer area of our website.<br><br>www.garceray.com<br><br>");
			cuerpo.append("Username :  " + usuario.getEmail() + "<br>Password : " + usuario.getPassword() + "<br><br>");
			cuerpo.append("Do not hesitate to contact us for any clarification. <br> Thank you so much!!!!!!");
			cuerpo.append("<br><br>Best Regards,<br>");
			cuerpo.append("<img src=\"/assets/img/garceray/firma.png\">");
			
		}else {
			
			asunto.append("Our invoice " + factura +" ");
			
			cuerpo.append("<html><body>Good day.<br><br>");
			cuerpo.append("Please see enclosed our invoice  " + factura +"<br>You can also find it along with all documentation in the customer area of our website.<br><br>www.garceray.com<br><br>");
			cuerpo.append("Username :  " + usuario.getEmail() + "<br>Password : " + usuario.getPassword() + "<br><br>");
			cuerpo.append("Do not hesitate to contact us for any clarification. <br> Thank you so much!!!!!!");
			cuerpo.append("<br><br>Best Regards,<br>");
			cuerpo.append("<img src=\"/assets/img/garceray/firma.png\">");
			
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

	
}

/* Copyright (c) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.digitaldoc.model;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitaldoc.dao.AnticiposDAO;
import com.digitaldoc.dao.CancelacionesDAO;
import com.digitaldoc.dao.DocumentosDAO;
import com.digitaldoc.dao.UsuarioDAO;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.User;

@SuppressWarnings("serial")
public class UploadPost extends HttpServlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	public void trataAnticipo(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		
		String op = req.getParameter("op");
		String idAnticipo = req.getParameter("idAnticipo");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		Anticipo anticipo= null;
		String idUsuario  = req.getParameter("id");
		Usuario usuario = UsuarioDAO.getUsuario(PMF.get().getPersistenceManager(), Long.parseLong(idUsuario));
		
		if("nuevoAnticipo".equals(op)){
			//cancelacion= CancelacionesDAO.nuevaCancelacion(req, pm);
		}else{
			//editamos
			if("ediAnticipo".equals(op)){
				anticipo = AnticiposDAO.getAnticipo(pm, idAnticipo);
			}
			
		}
		
		if (!blobs.keySet().isEmpty()) {
			
			Iterator<String> names = blobs.keySet().iterator();
			
			while(names.hasNext()){

				String blobName = names.next();
				BlobKey blobKey = blobs.get(blobName);

				BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
				BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKey);  

				String contentType = blobInfo.getContentType();
				long size = blobInfo.getSize();
				Date creation = blobInfo.getCreation();
				String fileName = blobInfo.getFilename();
				
				if(blobName.equals("ficheroAnticipo")){
					User user = null;
					
					if (size > 0) {
						
						user =new User(usuario.getEmail(), "");
						
						AnticipoFile mediaObj = new AnticipoFile(user, blobKey, creation,
								contentType, fileName, size, "anticipo", usuario.getCif(), anticipo);
						
						anticipo.setFichero(mediaObj);
						pm.makePersistent(anticipo);
						
						
					}
				}
			}
		}
		
		
		pm.flush();
		
		
		pm.refreshAll();
		
		
		
		String destino="/digitalDoc?op=ediAnticipo&id=" + usuario.getId() + "&idAnticipo=" + anticipo.getId();
		resp.sendRedirect(destino);
		
	}
	

	public void trataCancelacion(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		
		String op = req.getParameter("op");
		String id = req.getParameter("id");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		Cancelacion cancelacion= null;
		String empresa  = req.getParameter("empresa");
		Usuario usuario = UsuarioDAO.getUsuario(PMF.get().getPersistenceManager(), Long.parseLong(empresa));
		
		if("nuevaCancelacion".equals(op)){
			cancelacion= CancelacionesDAO.nuevaCancelacion(req, pm);
		}else{
			//editamos
			if("editaCancelacion".equals(op)){
				cancelacion= CancelacionesDAO.editaCancelacion(req, pm);
			}
			
		}
		
		if (!blobs.keySet().isEmpty()) {
			
			Iterator<String> names = blobs.keySet().iterator();
			
			while(names.hasNext()){

				String blobName = names.next();
				BlobKey blobKey = blobs.get(blobName);

				BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
				BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKey);  

				String contentType = blobInfo.getContentType();
				long size = blobInfo.getSize();
				Date creation = blobInfo.getCreation();
				String fileName = blobInfo.getFilename();
				
				if(blobName.equals("fichero")){
					User user = null;
					
					if (size > 0) {
						
						user =new User(usuario.getEmail(), "");
						
						CancelFile mediaObj = new CancelFile(user, blobKey, creation,
								contentType, fileName, size, "factura", usuario.getCif(), cancelacion, 0);
											
						cancelacion.setFichero(mediaObj);
						
					}
				}
			}
		}
		
		pm.makePersistent(cancelacion);
		pm.flush();
		pm.refreshAll();
		
		List<Usuario> listUsuarios = UsuarioDAO.getUsuarios(pm);
		req.setAttribute("listUsuarios", listUsuarios);
		List<Cancelacion> listCancelaciones = CancelacionesDAO.getCancelaciones(pm, usuario);
		req.setAttribute("listCancelaciones", listCancelaciones);
		String destino="/digitalDoc?op=cancelaciones&id="+ id; 
		
		
		pm.flush();
		resp.sendRedirect(destino);
		
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		// UserService userService = UserServiceFactory.getUserService();
		// User user = userService.getCurrentUser();

		String idCancelacion= req.getParameter("idCancelacion");
		String idAnticipo= req.getParameter("idAnticipo");
		if(idCancelacion != null && !"".equals(idCancelacion)){
		
			try {
				
				trataCancelacion(req, resp);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if(idAnticipo != null && !"".equals(idAnticipo)){
			
			try {
				
				trataAnticipo(req, resp);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}else{
			
			String serie = req.getParameter("serie");
			String numero= req.getParameter("numero");
			String importe = req.getParameter("importe");
			String fecha = req.getParameter("fecha");
			String empresa  = req.getParameter("empresa");
			String id = req.getParameter("id");
			String idFactura = req.getParameter("idFactura");
			String destino = "";
			
			try{
				Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
				
				Usuario usuario = UsuarioDAO.getUsuario(PMF.get().getPersistenceManager(), Long.parseLong(empresa));
				
				String fileName = "";
				long size = 0;
				Date creation = null;
				String blobName = "";
				String contentType ="";
				BlobKey blobKey = null;
				MediaObject mediaObj = null;
				Documento documento = null;
				
				if("".equals(idFactura) || "0".equals(idFactura)){
					
					destino = "/digitalDoc?op=facturas&id="+ id;
					
					documento = new Documento();
					documento.setSerie(serie);
					documento.setNumero(numero);
					documento.setImporte(Double.parseDouble(importe));
					//documento.setFichPrincipal(fileName);
					//documento.setFichAnticipo(fichAnticipo);
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					documento.setFecha(sdf.parse(fecha.replace("/", "-")));
					
					Random rnd = new Random();
					String sNuevoId= serie+ "-" +numero+ "_" + rnd.nextInt(99999);
					documento.setId(sNuevoId);
					
					usuario.addFactura(documento);
				
				}else{
					if(!"".equals(serie) && !"".equals(numero) &&  serie != null &&  numero != null){
						documento=actualizaFactura(usuario, serie, numero,  fecha, id,  importe, idFactura);
					}else{
						documento=DocumentosDAO.getDocumento(PMF.get().getPersistenceManager(), idFactura);
					}
					destino = "/digitalDoc?op=ediFactura&id="+ id+"&idFactura="+idFactura;
				}
				
				if (!blobs.keySet().isEmpty()) {
					
					Iterator<String> names = blobs.keySet().iterator();
					while(names.hasNext()){

						blobName = names.next();
						blobKey = blobs.get(blobName);

						BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
						BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKey);  

						contentType = blobInfo.getContentType();
						size = blobInfo.getSize();
						creation = blobInfo.getCreation();
						fileName = blobInfo.getFilename();
						
						if(blobName.equals("fichero")){
							
							if(!"".equals(fileName) &&  (documento.getFichPrincipal() != null) && !documento.getFichPrincipal().equals(fileName)){
								for(int i=0; i<documento.getFicheros().size(); i++){
									MediaObject fichMed = documento.getFicheros().get(i);
									if(documento.getFichPrincipal().equals(fichMed.getFilename())){
										documento.getFicheros().remove(i);
									}
								}
							}
							documento.setFichPrincipal(fileName);
						}else if(blobName.equals("ficheroAnticipo")){
							
							if(!"".equals(fileName) && (documento.getFichAnticipo() != null) && !documento.getFichAnticipo().equals(fileName)){
								for(int i=0; i<documento.getFicheros().size(); i++){
									MediaObject fichMed = documento.getFicheros().get(i);
									if(documento.getFichAnticipo().equals(fichMed.getFilename())){
										documento.getFicheros().remove(i);
									}
								}
							}
							documento.setFichAnticipo(fileName);
							
						}
						
						User user = null;
						
						if (size > 0) {
							
							user =new User(usuario.getEmail(), "");
							
							mediaObj = new MediaObject(user, blobKey, creation,
									contentType, fileName, size, "factura", usuario.getCif(), documento, 0);
												
							documento.addFichero(mediaObj);
							
						}
						
					}
				}
				
				
				PMF.get().getPersistenceManager().flush();
				resp.sendRedirect(destino);

			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	private Documento actualizaFactura(Usuario empresa, String serie, String numero, String fecha, String id, String importe, 
			String idFactura) throws Exception{
	
		for(Documento documento: empresa.getFacturas()){
			if(documento.getId().equals(idFactura)){
				documento.setNumero(numero);
				documento.setImporte(Double.parseDouble(importe));
				documento.setSerie(serie);
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				documento.setFecha(sdf.parse(fecha.replace("/", "-")));
				
				return documento;
			}
		}
		 return null;
	}
	

}

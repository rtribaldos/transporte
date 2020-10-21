package com.digitaldoc.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import com.digitaldoc.model.Anticipo;
import com.digitaldoc.model.Cancelacion;
import com.digitaldoc.model.Documento;
import com.digitaldoc.model.PMF;
import com.digitaldoc.model.Usuario;

public class CancelacionesDAO {

public static Cancelacion getCancelacion(PersistenceManager pm, String id) throws Exception{
		
		String query = "select from " +	Cancelacion.class.getName() + 
				" where id == '"+ id + "'";
			List<Cancelacion> listCancelaciones= (List<Cancelacion>) pm.newQuery(query).execute();
			if(listCancelaciones != null){
				return listCancelaciones.get(0);
			}
			else return null;
	}


	
	public static void updateCancelacion(PersistenceManager pm, Cancelacion cancelacion) throws Exception{
		pm.makePersistent(cancelacion);
	}
	
	public static void deleteCancelacion(PersistenceManager pm, Cancelacion cancelacion) {
		pm.deletePersistent(cancelacion);
		pm.flush();
		pm.refreshAll();
		
	}
	
	public static void guardaCancelacion(PersistenceManager pm, Cancelacion cancelacion){
		try{
		  	  pm.makePersistent(cancelacion);
	    	  pm.flush();
	    	  pm.refresh(cancelacion);
	    	} finally {
		    //  pm.close();
		   }
	}
	
	@SuppressWarnings("unchecked")
	public static List<Cancelacion> getCancelaciones(PersistenceManager pm, Usuario usuario) throws Exception{
		Query query = pm.newQuery(Cancelacion.class);
		try{
			if(usuario.getPerfil().equals("SuperAdmin")){
				return (List<Cancelacion>) query.execute();
			}else{
				if(usuario.getCancelaciones() != null && !usuario.getCancelaciones().isEmpty()){
					return usuario.getCancelaciones();
				}else{
					List<Cancelacion> listCancelaciones = new ArrayList();
					List<Cancelacion> listTodas = (List<Cancelacion>) query.execute();
					for(Cancelacion cancel : listTodas){
						if(cancel.getCliente().getEmail().equals(usuario.getEmail())){
							listCancelaciones.add(cancel);
						}
					}
				
					return listCancelaciones;
					
				}
				
			}
			
		}finally{
			query.closeAll();
	//		pm.close();
		}
		
	}
	
	public static Cancelacion nuevaCancelacion(HttpServletRequest req, PersistenceManager pm) throws Exception{
		
		String fecha = req.getParameter("fecha");
		String vencimiento = req.getParameter("vencimiento");
		String empresa  = req.getParameter("empresa");
		String idCancelacion = req.getParameter("idCancelacion");
		String factura = req.getParameter("factura");
		String importe = req.getParameter("importe");
		String divisas = req.getParameter("divisas");
		String referencia= req.getParameter("referencia");
		
		Usuario usuario = UsuarioDAO.getUsuario(pm, Long.parseLong(empresa));
		
		Cancelacion cancelacion = new Cancelacion();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		cancelacion.setFechaAnticipo(sdf.parse(fecha.replace("/", "-")));
		cancelacion.setVencimiento(sdf.parse(vencimiento.replace("/", "-")));
		if(factura != null && !"".equals(factura)){
			try{
				Documento docFact = DocumentosDAO.getDocumento(pm, factura);
				docFact.setIdCancelacion(cancelacion.getId());
				cancelacion.setFactura(factura);
			}catch(Exception e){
				
			}
			
		}
		
		cancelacion.setId(idCancelacion);
		cancelacion.setReferencia(referencia);
		cancelacion.setImporte(Double.parseDouble(importe));
		cancelacion.setImporteDivisas(Double.parseDouble(divisas));
		cancelacion.setCliente(usuario);
		
		pm.makePersistent(cancelacion);
		
		usuario.addCancelacion(cancelacion);
		
		return cancelacion;
	}
	
	
public static Cancelacion editaCancelacion(HttpServletRequest req, PersistenceManager pm) throws Exception{
		
		String fecha = req.getParameter("fecha");
		String vencimiento = req.getParameter("vencimiento");
		String empresa  = req.getParameter("empresa");
		String idCancelacion = req.getParameter("idCancelacion");
		String factura = req.getParameter("factura");
		
		
		
		String importe = req.getParameter("importe");
		String divisas = req.getParameter("divisas");
		String referencia= req.getParameter("referencia");
		
		Usuario usuario = UsuarioDAO.getUsuario(pm, Long.parseLong(empresa));
		
		Cancelacion cancelacion = CancelacionesDAO.getCancelacion(pm, idCancelacion);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		cancelacion.setFechaAnticipo(sdf.parse(fecha.replace("/", "-")));
		cancelacion.setVencimiento(sdf.parse(vencimiento.replace("/", "-")));
		
		if(factura != null && !"".equals(factura)){
			try{
				
				Documento docFact = DocumentosDAO.getDocumento(pm, factura);
				docFact.setIdCancelacion(cancelacion.getId());
				cancelacion.setFactura(factura);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		cancelacion.setId(idCancelacion);
		cancelacion.setReferencia(referencia);
		cancelacion.setImporte(Double.parseDouble(importe));
		cancelacion.setImporteDivisas(Double.parseDouble(divisas));
		cancelacion.setCliente(usuario);
				
		pm.makePersistent(cancelacion);
		pm.flush();
		return cancelacion;
	}
}

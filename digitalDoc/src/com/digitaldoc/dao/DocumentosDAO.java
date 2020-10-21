package com.digitaldoc.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.digitaldoc.model.Anticipo;
import com.digitaldoc.model.Documento;
import com.digitaldoc.model.MediaObject;
import com.digitaldoc.model.PMF;

public class DocumentosDAO {

public static Documento getDocumento(PersistenceManager pm, String id) throws Exception{
		
		String query = "select from " +	Documento.class.getName() + 
				" where id == '"+ id + "'";
			List<Documento> listDocumentos= (List<Documento>) pm.newQuery(query).execute();
			if(!listDocumentos.isEmpty()){
				return listDocumentos.get(0);
			}
			else return null;
	}


	public static String getUrl(String idFactura){
		try{
			Documento factura = getDocumento(PMF.get().getPersistenceManager(), idFactura); 
			return factura.getUrl();
		}catch(Exception e){
			return "";
		}
	}
	
	public static String getUrlAnticipo(String idFactura){
		try{
			Documento factura = getDocumento(PMF.get().getPersistenceManager(), idFactura); 
			return factura.getUrlAnticipo();
		}catch(Exception e){
			return "";
		}
	}
	
	
	public static void updateDocumento(PersistenceManager pm, Documento Documento) throws Exception{
		pm.makePersistent(Documento);
	}
	
	public static void deleteDocumento(PersistenceManager pm, Documento Documento) {
		pm.deletePersistent(Documento);
		pm.flush();
		pm.refreshAll();
		
	}
	
	public static void guardaDocumento(PersistenceManager pm, Documento Documento){
		try{
		  	  pm.makePersistent(Documento);
	    	  pm.flush();
	    	  pm.refresh(Documento);
	    	} finally {
		    //  pm.close();
		   }
	}
	
	@SuppressWarnings("unchecked")
	public static List<Documento> getNoAnticipadas(PersistenceManager pm) throws Exception{
		Query query = pm.newQuery(Documento.class);
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -2);
			
			//
			query.setFilter("idAnticipo == null && idCancelacion==null && fecha > sparam");
			query.declareParameters("java.util.Date sparam");
			query.setOrdering("fecha");
			List<Documento> results = (List<Documento>) query.execute(calendar.getTime());
			
			return results;
		}finally{
			query.closeAll();
	//		pm.close();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static List<Documento> getNoCanceladas(PersistenceManager pm) throws Exception{
		Query query = pm.newQuery(Documento.class);
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -2);
			
			//
			query.setFilter("idCancelacion==null &&  fecha > sparam");
			query.declareParameters("java.util.Date sparam");
			query.setOrdering("fecha");
			List<Documento> results = (List<Documento>) query.execute(calendar.getTime());
			
			return results;
		}finally{
			query.closeAll();
	//		pm.close();
		}
		
	}
			
	
	@SuppressWarnings("unchecked")
	public static List<Documento> getDocumentos(PersistenceManager pm) throws Exception{
	
		Query query = pm.newQuery(Documento.class);
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -2);
			
			//
			query.setFilter("fecha > sparam");
			query.declareParameters("java.util.Date sparam");
			query.setOrdering("fecha");
			List<Documento> results = (List<Documento>) query.execute(calendar.getTime());
			
			return results;
		}finally{
			query.closeAll();
	//		pm.close();
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<Documento> getTodas(PersistenceManager pm) throws Exception{
		Query query = pm.newQuery(Documento.class);
		try{
			query.setOrdering("fecha");
			return (List<Documento>) query.execute();
		}finally{
			query.closeAll();
		}
		
	}
	
	
}

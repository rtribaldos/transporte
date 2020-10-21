package com.digitaldoc.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import com.digitaldoc.model.Anticipo;
import com.digitaldoc.model.Documento;
import com.digitaldoc.model.PMF;
import com.digitaldoc.model.Usuario;


public class AnticiposDAO {

public static Anticipo getAnticipo(PersistenceManager pm, String id) throws Exception{
		
		String query = "select from " +	Anticipo.class.getName() + 
				" where id == '"+ id + "'";
			List<Anticipo> listAnticipos= (List<Anticipo>) pm.newQuery(query).execute();
			if(listAnticipos != null && listAnticipos.size() > 0){
				return listAnticipos.get(0);
			}
			else return null;
	}

	public static void deleteAnticipo(PersistenceManager pm, Anticipo anticipo) {
		anticipo.setAcreedor(null);
		pm.deletePersistent(anticipo);
		pm.flush();
		pm.refreshAll();
	}
	
	public static Anticipo nuevoAnticipo(HttpServletRequest req, PersistenceManager pm) throws Exception{
		String fecha = req.getParameter("fecha");
		String empresa  = req.getParameter("empresa");
		String idAnticipo = req.getParameter("idAnticipo");
		
		Usuario usuario = UsuarioDAO.getUsuario(pm, Long.parseLong(empresa));
		
		Anticipo anticipo = new Anticipo();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		anticipo.setFecha(sdf.parse(fecha.replace("/", "-")));
		anticipo.setId(idAnticipo);
		
		anticipo.setAcreedor(usuario);
		pm.makePersistent(anticipo);
		
		usuario.addAnticipo(anticipo);
		
		pm.flush();
		return anticipo;
	}
	
	public static void editaAnticipo(HttpServletRequest req, PersistenceManager pm) throws Exception{
		String fecha = req.getParameter("fecha");
		String empresa  = req.getParameter("empresa");
		String idAnticipo = req.getParameter("idAnticipo");
				
		Usuario usuario = UsuarioDAO.getUsuario(pm, Long.parseLong(empresa));
		
		Anticipo anticipo = AnticiposDAO.getAnticipo(pm, idAnticipo);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		anticipo.setFecha(sdf.parse(fecha.replace("/", "-")));
		anticipo.setId(idAnticipo);
		anticipo.setAcreedor(usuario);
		
		pm.makePersistent(anticipo);
		
		req.setAttribute("anticipo", anticipo);
		
	}
	
	public static void subeFactAnticipo(HttpServletRequest req, PersistenceManager pm) throws Exception{
		
		String idAnticipo = req.getParameter("idAnticipo");
		String idFactura = req.getParameter("idFactura");
		
		Anticipo anticipo = AnticiposDAO.getAnticipo(pm, idAnticipo);
		
		if(anticipo.getFacturas()==null){
			anticipo.setFacturas(new ArrayList<String>());
		}
		
		anticipo.getFacturas().add(idFactura);
		
		Documento factura = DocumentosDAO.getDocumento(pm, idFactura);
		factura.setIdAnticipo(idAnticipo);
		
		pm.makePersistent(anticipo);
		pm.flush();
		pm.refreshAll();
		
		req.setAttribute("anticipo", anticipo);
		
	}
	
	
	
	
	
	public static void guardaAnticipo(PersistenceManager pm, Anticipo anticipo){
		try{
		  	  pm.makePersistent(anticipo);
	    	  pm.flush();
	    	  pm.refresh(anticipo);
	    	} finally {
		    //  pm.close();
		   }
	}
	
	@SuppressWarnings("unchecked")
	public static List<Anticipo> getAnticipos(PersistenceManager pm) throws Exception{
		Query query = pm.newQuery(Anticipo.class);
		try{
			return (List<Anticipo>) query.execute();
		}finally{
			query.closeAll();
		}
		
	}
}

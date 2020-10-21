package com.digitaldoc.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.digitaldoc.model.Archivo;

public class ArchivosDAO {


	@SuppressWarnings("unchecked")
	public static List<Archivo> getArchivos(PersistenceManager pm) throws Exception{
		Query query = pm.newQuery(Archivo.class);
		try{
			return (List<Archivo>) query.execute();
		}finally{
			query.closeAll();
		}
	}
	
	public static Archivo getArchivo(PersistenceManager pm, String id) throws Exception{
		String query = "select from " +	Archivo.class.getName() + 
				" where id == '"+ id + "'";
			List<Archivo> listArchivos= (List<Archivo>) pm.newQuery(query).execute();
			if(listArchivos != null && listArchivos.size() > 0){
				return listArchivos.get(0);
			}
			else return null;
	}

	public static void deleteArchivo(PersistenceManager pm, Archivo archivo) {
		
		pm.deletePersistent(archivo);
		pm.flush();
		pm.refreshAll();
	}
	
	
}

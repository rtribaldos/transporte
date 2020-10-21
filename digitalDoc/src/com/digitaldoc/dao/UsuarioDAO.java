package com.digitaldoc.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import com.digitaldoc.model.Usuario;
	
public class UsuarioDAO {
	
	public static Usuario getUsuario(PersistenceManager pm, long id) throws Exception{
		return (Usuario) pm.getObjectById(Usuario.class, id);
	}
	
	public static void updateExpediente(PersistenceManager pm, Usuario usuario) throws Exception{
		pm.makePersistent(usuario);
	}
	
	public static void creaAdmin(PersistenceManager pm) throws Exception{
		Usuario usuario = new Usuario();
		usuario.setEmail("admin@garceray.com");
		usuario.setPassword("TGarcerai1972");
		usuario.setActivo(true);		
		usuario.setPerfil("SuperAdmin");
		pm.makePersistent(usuario);
	}
	
	public static void deleteUsuario(PersistenceManager pm, Usuario usuario) {
		pm.deletePersistent(usuario);
		pm.flush();
		pm.refreshAll();
	}
	
	public static Usuario getUsuarioByMail(PersistenceManager pm, String email) throws Exception{
		String query = "select from " +	Usuario.class.getName() + 
			" where email == '"+ email +"'";
		List<Usuario> usuarios = (List<Usuario>) pm.newQuery(query).execute();
		if (usuarios.size() > 0) return (Usuario) usuarios.get(0);
		else return null;
	}
	
	public static String getEmpresaByCif(PersistenceManager pm, String cif) throws Exception{
		String query = "select from " +	Usuario.class.getName() + 
			" where cif == '"+ cif +"'";
		List<Usuario> usuarios = (List<Usuario>) pm.newQuery(query).execute();
		if (usuarios.size() > 0) return usuarios.get(0).getEmpresa();
		else return null;
	}
	
	public static List<Usuario> getEmpleados(PersistenceManager pm) throws Exception{
		String query = "select from " +	Usuario.class.getName() + 
			" where perfil == 'empleado'";
		return (List<Usuario>) pm.newQuery(query).execute();
	}
	
	public static List<Usuario> getAcreedores(PersistenceManager pm) throws Exception{
		String query = "select from " +	Usuario.class.getName() + 
			" where perfil == 'acreedor'";
		return (List<Usuario>) pm.newQuery(query).execute();
	}
	
	public static List<String> getCifs(PersistenceManager pm) throws Exception{
		
		Query q = pm.newQuery (Usuario.class);
		q.setResult ("distinct cif");
		return (List<String>)q.execute (); 
	}
	
	public static String getAdministradores(PersistenceManager pm) throws Exception{
		String result="";
		String query = "select from " +	Usuario.class.getName() + 
			" where perfil == 'Administrador'";
		List<Usuario> usuarios = (List<Usuario>) pm.newQuery(query).execute();
		int conta=0;
		for(Usuario user : usuarios){
			result+= user.getEmail();
			conta++;
			if(conta < usuarios.size()) result+=", ";
		}
		return result;		
	}
		
	@SuppressWarnings("unchecked")
	public static List<Usuario> getUsuarios(PersistenceManager pm) throws Exception{
		Query query = pm.newQuery(Usuario.class);
		query.setFilter("perfil != 'SuperAdmin'");
		query.setOrdering("perfil");
		List<Usuario> listUsuarios = (List<Usuario>) query.execute();
		for(Usuario usuario : listUsuarios){
			if(usuario.isAcreedor()){
				usuario.setPerfil("acreedor");
			}else if (usuario.isEmpleado()){
				usuario.setPerfil("empleado");
			}else{
				if("".equals(usuario.getPerfil())){
					usuario.setPerfil("cliente");
				}
			}
		}
		
		return listUsuarios;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Usuario> getClientes(PersistenceManager pm) throws Exception{
		Query query = pm.newQuery(Usuario.class);
		query.setFilter("perfil == 'cliente'");
		query.setOrdering("empresa");
		return (List<Usuario>) query.execute();
		
	}
	
	public static void nuevoCliente(HttpServletRequest req, PersistenceManager pm) throws Exception{
		String email = req.getParameter("email");
		String otrosEmails=req.getParameter("otrosEmails");
		String empresa= req.getParameter("empresa");
		String cif= req.getParameter("cif");
		String telefono= req.getParameter("telefono");
		String nombre= req.getParameter("nombre");
		String password= req.getParameter("password");
		String direccion= req.getParameter("direccion");
		String poblacion= req.getParameter("poblacion");
		String provincia= req.getParameter("provincia");
		String cp= req.getParameter("cp");
		String pais= req.getParameter("pais");
		String acreedor= req.getParameter("acreedor");
		String empleado= req.getParameter("empleado");
		
		Usuario usuario = new Usuario();
		usuario.setActivo(true);
		usuario.setCif(cif);
		usuario.setCp(cp);
		usuario.setDireccion(direccion);
		usuario.setPoblacion(poblacion);
		usuario.setProvincia(provincia);
		usuario.setEmail(email);
		usuario.setOtrosEmails(otrosEmails);
		usuario.setEmpresa(empresa);
		usuario.setNombre(nombre);
		usuario.setDireccion(direccion);
		usuario.setPassword(password);
		usuario.setPerfil("cliente");
		usuario.setTelefono(telefono);
		usuario.setPais(pais);
		if("1".equals(acreedor)){
			usuario.setAcreedor(true);
		}else{
			usuario.setAcreedor(false);
		}
		if("1".equals(empleado)){
			usuario.setEmpleado(true);
		}else{
			usuario.setEmpleado(false);
		}
		
		pm.makePersistent(usuario);
		
		
	}
	
	public static void actualizaCliente(HttpServletRequest req, PersistenceManager pm) throws Exception{
		String sIdCli= req.getParameter("idCliente");
		String email = req.getParameter("email");
		String otrosEmails=req.getParameter("otrosEmails");
		String empresa= req.getParameter("empresa");
		String cif= req.getParameter("cif");
		String telefono= req.getParameter("telefono");
		String nombre= req.getParameter("nombre");
		String password= req.getParameter("password");
		String direccion= req.getParameter("direccion");
		String poblacion= req.getParameter("poblacion");
		String provincia= req.getParameter("provincia");
		String cp= req.getParameter("cp");
		String pais= req.getParameter("pais");
		String acreedor= req.getParameter("acreedor");
		String empleado= req.getParameter("empleado");
		
		Usuario usuario = UsuarioDAO.getUsuario(pm, Long.parseLong(sIdCli));
		usuario.setActivo(true);
		usuario.setCif(cif);
		usuario.setCp(cp);
		usuario.setDireccion(direccion);
		usuario.setPoblacion(poblacion);
		usuario.setProvincia(provincia);
		usuario.setEmail(email);
		usuario.setOtrosEmails(otrosEmails);
		usuario.setEmpresa(empresa);
		usuario.setNombre(nombre);
		usuario.setDireccion(direccion);
		usuario.setPassword(password);
		usuario.setPerfil("cliente");
		usuario.setTelefono(telefono);
		usuario.setPais(pais);
		if("1".equals(acreedor)){
			usuario.setAcreedor(true);
		}else{
			usuario.setAcreedor(false);
		}
		if("1".equals(empleado)){
			usuario.setEmpleado(true);
		}else{
			usuario.setEmpleado(false);
		}
		
		pm.makePersistent(usuario);
		
		
	}
	
	
}



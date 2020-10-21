package com.digitaldoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitaldoc.dao.AnticiposDAO;
import com.digitaldoc.dao.ArchivosDAO;
import com.digitaldoc.dao.CancelacionesDAO;
import com.digitaldoc.dao.DocumentosDAO;
import com.digitaldoc.dao.UsuarioDAO;
import com.digitaldoc.model.Anticipo;
import com.digitaldoc.model.Archivo;
import com.digitaldoc.model.Cancelacion;
import com.digitaldoc.model.Documento;
import com.digitaldoc.model.PMF;
import com.digitaldoc.model.Usuario;
import com.digitaldoc.utils.UtilDigitalDoc;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class DigitalDocServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			String destino = "login.jsp";
			String operacion = req.getParameter("op");
			String sId = req.getParameter("id");

			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();

			String authURL = userService.createLogoutURL("/");
			String uploadURL = blobstoreService.createUploadUrl("/post");

			req.setAttribute("uploadURL", uploadURL);
			req.setAttribute("authURL", authURL);
			req.setAttribute("user", user);

			PersistenceManager pm = PMF.get().getPersistenceManager();
			Usuario usuario = null;
			if (sId != null && !"".equals(sId) && !"null".equals(sId) && !"0".equals(sId)) {
				pm = PMF.get().getPersistenceManager();
				usuario = UsuarioDAO.getUsuario(pm, Long.parseLong(sId));
				req.setAttribute("usuario", usuario);
				if (usuario.getPerfil().equals("cliente")) {
					destino = "indexCli.jsp";
				} else if (usuario.getPerfil().equals("acreedor")) {
					destino = "indexAcre.jsp";
					List<Anticipo> listAnticipos = AnticiposDAO.getAnticipos(pm);
					req.setAttribute("listAnticipos", listAnticipos);
				}
			}

			if ("registro".equals(operacion) || "ediCliente".equals(operacion)) {

				if ("ediCliente".equals(operacion)) {
					cargaCliente(req, pm);
				}
				destino = "/registro.jsp";
			} else if ("superadmin".equals(req.getParameter("op"))) {

				UsuarioDAO.creaAdmin(PMF.get().getPersistenceManager());
			} else if ("recordar".equals(operacion)) {

				destino = "/recordatorio.jsp";
			} else if ("alta".equals(operacion) || "ediFactura".equals(operacion) || "borraDoc".equals(operacion)) {

				destino = gestionaFactura(req, resp, operacion, pm, usuario);

			} else if ("altaAnticipo".equals(operacion) || "ediAnticipo".equals(operacion)
					|| "borraFacAnt".equals(operacion)) {

				destino = trataAnticipo(req, destino, operacion, pm, usuario);

			} else if ("altaCancelacion".equals(operacion) || "ediCancelacion".equals(operacion)
					|| "enviaCancel".equals(operacion)) {

				destino = trataCancelacion(req, destino, operacion, pm, usuario);

			} else if ("clientes".equals(operacion) || "borraCliente".equals(operacion)) {

				destino = trataClientes(req, operacion);

			} else if ("facturas".equals(operacion) || "enviaFactura".equals(operacion)
					|| "borraFactura".equals(operacion) || "todasFacturas".equals(operacion)) {

				destino = trataFacturas(req, operacion, pm, usuario);

			} else if ("anticipos".equals(operacion) || "borraAnticipo".equals(operacion)
					|| "enviaAnticipo".equals(operacion)) {

				destino = gestionaAnticipos(req, operacion, pm, usuario);

			} else if ("cancelaciones".equals(operacion) || "borraCancelacion".equals(operacion)) {

				destino = gestionaCancelaciones(req, operacion, pm, usuario);

			} else if ("archivos".equals(operacion)) {

				List<Archivo> listArchivos = ArchivosDAO.getArchivos(pm);
				req.setAttribute("listArchivos", listArchivos);
				if (usuario.getPerfil().equalsIgnoreCase("SuperAdmin")) {
					destino = "/archivos.jsp";
				}
			}

			RequestDispatcher dispatcher = req.getRequestDispatcher(destino);
			dispatcher.forward(req, resp);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String gestionaFactura(HttpServletRequest req, HttpServletResponse resp, String operacion,
			PersistenceManager pm, Usuario usuario) throws Exception {
		String destino;
		if ("ediFactura".equals(operacion)) {
			recuperaFactura(req, pm);
		} else if ("borraDoc".equals(operacion)) {
			borraDoc(req, pm, resp);
		}

		if (!usuario.getPerfil().equals("SuperAdmin")) {
			if (usuario.getPerfil().equals("acreedor")) {
				destino = "/facturaAcree.jsp";
			} else {
				destino = "/facturaCli.jsp";
			}
		} else {
			List<Usuario> listUsuarios = UsuarioDAO.getClientes(pm);
			req.setAttribute("listUsuarios", listUsuarios);
			destino = "/alta.jsp";
		}
		return destino;
	}

	private String gestionaCancelaciones(HttpServletRequest req, String operacion, PersistenceManager pm,
			Usuario usuario) throws Exception {
		String destino;
		if ("borraCancelacion".equals(operacion) || "enviaCancel".equals(operacion)) {
			borraCancelacion(req, PMF.get().getPersistenceManager());
		} else if ("enviaCancel".equals(operacion)) {
			enviaCancel(req, pm);
			req.setAttribute("mensaje", "Cancelaci&oacute;n enviada");
		}
		List<Cancelacion> listCancelaciones = CancelacionesDAO.getCancelaciones(pm, usuario);
		req.setAttribute("listCancelaciones", listCancelaciones);
		destino = "/cancelaciones.jsp";
		return destino;
	}

	private String gestionaAnticipos(HttpServletRequest req, String operacion, PersistenceManager pm, Usuario usuario)
			throws Exception {
		String destino;
		if ("borraAnticipo".equals(operacion)) {
			borraAnticipo(req, PMF.get().getPersistenceManager());
		} else if ("enviaAnticipo".equals(operacion)) {
			enviaAnticipo(req, pm);
			req.setAttribute("mensaje", "Anticipo enviado");
		}
		List<Anticipo> listAnticipos = AnticiposDAO.getAnticipos(pm);
		req.setAttribute("listAnticipos", listAnticipos);
		if (!usuario.getPerfil().equals("SuperAdmin")) {
			destino = "/indexAcre.jsp";
		} else {
			destino = "/anticipos.jsp";
		}
		return destino;
	}

	private String trataFacturas(HttpServletRequest req, String operacion, PersistenceManager pm, Usuario usuario)
			throws Exception {
		String destino;
		if ("borraFactura".equals(operacion)) {
			borraFactura(req, PMF.get().getPersistenceManager());
		} else if ("enviaFactura".equals(operacion)) {
			enviaFactura(req, pm);
			req.setAttribute("mensaje", "Factura enviada");
		}

		if (!usuario.getPerfil().equals("SuperAdmin")) {
			destino = "indexCli.jsp";
			req.setAttribute("listFacturas", usuario.getFacturas());
		} else {
			if ("todasFacturas".equals(operacion)) {
				cargaTodasFacturas(req, pm);
			} else {
				cargaFacturas(req, pm);
			}

			destino = "/index.jsp";
		}
		return destino;
	}

	private String trataClientes(HttpServletRequest req, String operacion) throws Exception {
		String destino;
		if ("borraCliente".equals(operacion)) {
			borraCliente(req, PMF.get().getPersistenceManager());
		}
		cargaUsuarios(req, PMF.get().getPersistenceManager());
		destino = "/clientes.jsp";
		return destino;
	}

	private String trataCancelacion(HttpServletRequest req, String destino, String operacion, PersistenceManager pm,
			Usuario usuario) throws Exception {
		if ("ediCancelacion".equals(operacion)) {
			recuperaCancelacion(req, pm);
		} else if ("enviaCancel".equals(operacion)) {
			enviaCancel(req, pm);
		}

		if (usuario.getPerfil().equals("SuperAdmin") || usuario.getPerfil().equals("acreedor")) {
			List<Usuario> listUsuarios = UsuarioDAO.getAcreedores(pm);
			req.setAttribute("listUsuarios", listUsuarios);
			if (usuario.getPerfil().equals("acreedor")) {
				destino = "/cancelacionView.jsp";
			} else {
				destino = "/cancelacion.jsp";
			}
		}
		return destino;
	}

	private String trataAnticipo(HttpServletRequest req, String destino, String operacion, PersistenceManager pm,
			Usuario usuario) throws Exception {
		if ("ediAnticipo".equals(operacion)) {
			recuperaAnticipo(req, pm);
		} else if ("borraFacAnt".equals(operacion)) {
			borraFacAnt(req, pm);
		}

		if (usuario.getPerfil().equals("SuperAdmin") || usuario.getPerfil().equals("acreedor")) {
			List<Usuario> listUsuarios = UsuarioDAO.getAcreedores(pm);
			req.setAttribute("listUsuarios", listUsuarios);
			cargaNoAnticipadas(req, pm);
			if (usuario.getPerfil().equals("acreedor")) {
				destino = "/anticipoView.jsp";
			} else {
				destino = "/anticipo.jsp";
			}
		}
		return destino;
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		try {

			PersistenceManager pm = PMF.get().getPersistenceManager();

			String destino = "/index.jsp";
			String op = req.getParameter("op");

			String sUsuario = req.getParameter("usuario");
			String password = req.getParameter("password");
			String sId = req.getParameter("id");

			Usuario usuario = null;

			if (sId != null && !"".equals(sId) && !"null".equals(sId) && !"0".equals(sId)) {

				pm = PMF.get().getPersistenceManager();
				usuario = UsuarioDAO.getUsuario(pm, Long.parseLong(sId));
				req.setAttribute("usuario", usuario);

			} else {

				usuario = UsuarioDAO.getUsuarioByMail(pm, sUsuario);

			}

			if (usuario != null) {

				if (usuario.getPassword().equals(password)) {

					req.setAttribute("usuario", usuario);

					if (usuario.getPerfil().equals("cliente")) {
						destino = "indexCli.jsp";
						req.setAttribute("listFacturas", usuario.getFacturas());
					} else if (usuario.getPerfil().equals("acreedor")) {
						destino = "indexAcre.jsp";
						List<Anticipo> listAnticipos = AnticiposDAO.getAnticipos(pm);
						req.setAttribute("listAnticipos", listAnticipos);
					} else {
						// SuperAdmin
						if (op == null || "".equals(op)) {
							cargaFacturas(req, pm);
						}

					}

				} else if ("nuevoCliente".equals(op) || "actualizaCliente".equals(op)) {

					if ("nuevoCliente".equals(op)) {
						UsuarioDAO.nuevoCliente(req, pm);
					} else {
						UsuarioDAO.actualizaCliente(req, pm);
					}
					List<Usuario> listUsuarios = UsuarioDAO.getClientes(pm);
					req.setAttribute("listUsuarios", listUsuarios);
					destino = "/clientes.jsp";

				} else if ("nuevoAnticipo".equals(op) || "editaAnticipo".equals(op) || "subeFactAnticipo".equals(op)) {
					if ("nuevoAnticipo".equals(op)) {
						Anticipo anticipo = AnticiposDAO.nuevoAnticipo(req, pm);
						req.setAttribute("anticipo", anticipo);
						List<Usuario> listUsuarios = UsuarioDAO.getAcreedores(pm);
						req.setAttribute("listUsuarios", listUsuarios);
						cargaNoAnticipadas(req, pm);
						destino = "/anticipo.jsp";
					} else {
						// editamos
						if ("editaAnticipo".equals(op)) {
							AnticiposDAO.editaAnticipo(req, pm);
						} else {
							AnticiposDAO.subeFactAnticipo(req, pm);
						}
						List<Usuario> listUsuarios = UsuarioDAO.getAcreedores(pm);
						req.setAttribute("listUsuarios", listUsuarios);
						cargaNoAnticipadas(req, pm);
						destino = "/anticipo.jsp";
					}

				} else if ("nuevaCancelacion".equals(op) || "editaCancelacion".equals(op)) {
					if ("nuevaCancelacion".equals(op)) {
						Cancelacion cancelacion = CancelacionesDAO.nuevaCancelacion(req, pm);
					} else {
						// editamos
						if ("editaCancelacion".equals(op)) {
							CancelacionesDAO.editaCancelacion(req, pm);
						}

					}
					List<Usuario> listUsuarios = UsuarioDAO.getUsuarios(pm);
					req.setAttribute("listUsuarios", listUsuarios);
					List<Cancelacion> listCancelaciones = CancelacionesDAO.getCancelaciones(pm, usuario);
					req.setAttribute("listCancelaciones", listCancelaciones);
					destino = "/cancelaciones.jsp";
				}

			} else {

				destino = "/login.jsp";

			}

			RequestDispatcher dispatcher = req.getRequestDispatcher(destino);
			dispatcher.forward(req, resp);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void cargaFacturas(HttpServletRequest req, PersistenceManager pm) throws Exception {
		List<Documento> listDocs = DocumentosDAO.getDocumentos(pm);
		req.setAttribute("listFacturas", listDocs);
	}

	public void cargaTodasFacturas(HttpServletRequest req, PersistenceManager pm) throws Exception {
		List<Documento> listDocs = DocumentosDAO.getTodas(pm);
		req.setAttribute("listFacturas", listDocs);
	}

	public void cargaNoAnticipadas(HttpServletRequest req, PersistenceManager pm) throws Exception {
		List<Documento> listDocs = DocumentosDAO.getNoAnticipadas(pm);
		req.setAttribute("listFacturas", listDocs);
	}

	public void borraCliente(HttpServletRequest req, PersistenceManager pm) throws Exception {
		Usuario usuario = UsuarioDAO.getUsuario(pm, Long.parseLong(req.getParameter("idCliente")));
		pm.deletePersistent(usuario);
		pm.flush();
		pm.refreshAll();
	}

	public void borraAnticipo(HttpServletRequest req, PersistenceManager pm) throws Exception {
		Anticipo anticipo = AnticiposDAO.getAnticipo(pm, req.getParameter("idAnticipo"));
		anticipo.setAcreedor(null);
		pm.deletePersistent(anticipo);
		pm.flush();
		pm.refreshAll();

	}

	public void borraCancelacion(HttpServletRequest req, PersistenceManager pm) throws Exception {
		Cancelacion cancelacion = CancelacionesDAO.getCancelacion(pm, req.getParameter("idCancelacion"));
		cancelacion.setCliente(null);
		pm.deletePersistent(cancelacion);
		pm.flush();
		pm.refreshAll();

	}

	public void borraFactura(HttpServletRequest req, PersistenceManager pm) throws Exception {
		Documento factura = DocumentosDAO.getDocumento(pm, req.getParameter("idFactura"));
		pm.deletePersistent(factura);
		pm.flush();
		pm.refreshAll();
	}

	public void borraDoc(HttpServletRequest req, PersistenceManager pm, HttpServletResponse resp) throws Exception {
		Documento factura = DocumentosDAO.getDocumento(pm, req.getParameter("idFactura"));
		String fileName = req.getParameter("fileName");
		for (int i = 0; i < factura.getFicheros().size(); i++) {
			if (fileName.equals(factura.getFicheros().get(i).getFilename())) {
				if (factura.getFichPrincipal().equals(fileName)) {
					factura.setFichPrincipal("");
				} else if (factura.getFichAnticipo().equals(fileName)) {
					factura.setFichAnticipo("");
				}
				factura.getFicheros().remove(i);
			}
		}
		recuperaFactura(req, pm);
	}

	public void enviaFactura(HttpServletRequest req, PersistenceManager pm) throws Exception {
		Documento factura = DocumentosDAO.getDocumento(pm, req.getParameter("idFactura"));
		byte[] firmado = null;
		for (int i = 0; i < factura.getFicheros().size(); i++) {
			if (factura.getFichPrincipal().equals(factura.getFicheros().get(i).getFilename())) {
				firmado = UtilDigitalDoc.signFactura(factura.getFicheros().get(i));
			}
		}

		StringBuffer asunto = new StringBuffer();
		StringBuffer cuerpo = new StringBuffer();

		UtilDigitalDoc.cargaMailCliente(asunto, cuerpo, factura.getUsuario(),
				factura.getSerie() + "_" + factura.getNumero());
		String destino = factura.getUsuario().getEmail();
		if (!"".equals(factura.getUsuario().getOtrosEmails())
				&& !"null".equals(factura.getUsuario().getOtrosEmails())) {
			destino = factura.getUsuario().getOtrosEmails();
		}

		UtilDigitalDoc.enviaCorreo(destino, cuerpo.toString(), asunto.toString(), firmado,
				factura.getSerie() + "_" + factura.getNumero() + "_firmado.pdf", false);

		factura.setEnviado(true);
		pm.makePersistent(factura);
		// HttpPost.send(BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/upload"),
		// firmado);
	}

	public void enviaAnticipo(HttpServletRequest req, PersistenceManager pm) throws Exception {
		Anticipo anticipo = AnticiposDAO.getAnticipo(pm, req.getParameter("idAnticipo"));
		byte[] firmado = null;
		if (anticipo.getFichero() != null) {
			firmado = UtilDigitalDoc.signAnticipo(anticipo.getFichero());
		}

		Map<String, byte[]> ficheros = new HashMap();
		ficheros.put(anticipo.getFichero().getFilename(), firmado);

		List<String> listFacturas = anticipo.getFacturas();
		for (String idFactura : anticipo.getFacturas()) {
			Documento factura = DocumentosDAO.getDocumento(PMF.get().getPersistenceManager(), idFactura);
			byte[] facturaFirmada = null;
			for (int i = 0; i < factura.getFicheros().size(); i++) {
				if (factura.getFichAnticipo().equals(factura.getFicheros().get(i).getFilename())) {
					facturaFirmada = UtilDigitalDoc.signFactura(factura.getFicheros().get(i));
				}
			}
			if (facturaFirmada.length > 0) {
				ficheros.put(factura.getFichPrincipal(), facturaFirmada);
			}
		}

		StringBuffer asunto = new StringBuffer();
		StringBuffer cuerpo = new StringBuffer();

		UtilDigitalDoc.cargaMailAcreedor(asunto, cuerpo, anticipo.getAcreedor(), anticipo.getId());
		String destino = anticipo.getAcreedor().getEmail();
		if (!"".equals(anticipo.getAcreedor().getOtrosEmails())
				&& !"null".equals(anticipo.getAcreedor().getOtrosEmails())) {
			destino = anticipo.getAcreedor().getOtrosEmails();
		}

		UtilDigitalDoc.enviaCorreo(destino, cuerpo.toString(), asunto.toString(), ficheros, anticipo.getId() + ".pdf", true);
		anticipo.setEnviado(true);
		pm.makePersistent(anticipo);
	}

	public void enviaCancel(HttpServletRequest req, PersistenceManager pm) throws Exception {
		Cancelacion cancelacion = CancelacionesDAO.getCancelacion(pm, req.getParameter("idCancelacion"));
		byte[] firmado = null;
		if (cancelacion.getFichero() != null) {
			firmado = UtilDigitalDoc.signCancelacion(cancelacion.getFichero());
		}

		StringBuffer asunto = new StringBuffer();
		StringBuffer cuerpo = new StringBuffer();

		UtilDigitalDoc.cargaMailCancel(asunto, cuerpo, cancelacion.getCliente(), cancelacion.getId());

		String destino = cancelacion.getCliente().getEmail();
		if (!"".equals(cancelacion.getCliente().getOtrosEmails())
				&& !"null".equals(cancelacion.getCliente().getOtrosEmails())) {
			destino = cancelacion.getCliente().getOtrosEmails();
		}

		UtilDigitalDoc.enviaCorreo(destino, cuerpo.toString(), asunto.toString(), firmado, cancelacion.getId() + ".pdf",
				true);
		cancelacion.setEnviado(true);
		pm.makePersistent(cancelacion);

	}

	public void borraFacAnt(HttpServletRequest req, PersistenceManager pm) throws Exception {
		Anticipo anticipo = AnticiposDAO.getAnticipo(pm, req.getParameter("idAnticipo"));
		String idFactura = req.getParameter("idFactura");
		if (anticipo.getFacturas() != null && !anticipo.getFacturas().isEmpty()) {
			anticipo.getFacturas().remove(idFactura);
		}
		req.setAttribute("anticipo", anticipo);
	}

	public void recuperaFactura(HttpServletRequest req, PersistenceManager pm) throws Exception {
		Documento factura = DocumentosDAO.getDocumento(pm, req.getParameter("idFactura"));
		req.setAttribute("factura", factura);
	}

	public void recuperaAnticipo(HttpServletRequest req, PersistenceManager pm) throws Exception {
		Anticipo anticipo = AnticiposDAO.getAnticipo(pm, req.getParameter("idAnticipo"));
		req.setAttribute("anticipo", anticipo);
	}

	public void recuperaCancelacion(HttpServletRequest req, PersistenceManager pm) throws Exception {
		Cancelacion cancelacion = CancelacionesDAO.getCancelacion(pm, req.getParameter("idCancelacion"));
		req.setAttribute("cancelacion", cancelacion);
		List<Documento> listFacturas = new ArrayList<>();
		for (Anticipo anticipo : cancelacion.getCliente().getAnticipos()) {
			for (String sFac : anticipo.getFacturas()) {
				Documento doc = DocumentosDAO.getDocumento(pm, sFac);
				if (doc != null && !"".equals(doc.getIdAnticipo()) && "".equals(doc.getIdCancelacion())) {
					listFacturas.add(doc);
				}
			}
		}
		req.setAttribute("listFacturas", listFacturas);
	}

	public void cargaCliente(HttpServletRequest req, PersistenceManager pm) throws NumberFormatException, Exception {
		String sId = req.getParameter("idCliente");
		Usuario usuario = UsuarioDAO.getUsuario(pm, Long.parseLong(sId));
		req.setAttribute("cliente", usuario);
	}

	public void cargaUsuarios(HttpServletRequest req, PersistenceManager pm) throws Exception {
		List<Usuario> listUsuarios = null;

		String tipoCliente = req.getParameter("tipoCliente");
		if (tipoCliente != null && "e".contentEquals(tipoCliente)) {
			listUsuarios = UsuarioDAO.getEmpleados(pm);
			req.setAttribute("tipoCliente", "Empleados");
		} else if (tipoCliente != null && "a".contentEquals(tipoCliente)) {
			listUsuarios = UsuarioDAO.getAcreedores(pm);
			req.setAttribute("tipoCliente", "Acreedores");
		} else if (tipoCliente != null && "c".contentEquals(tipoCliente)) {
			listUsuarios = UsuarioDAO.getClientes(pm);
			req.setAttribute("tipoCliente", "Clientes");
		} else {
			listUsuarios = UsuarioDAO.getUsuarios(pm);
			req.setAttribute("tipoCliente", "Usuarios");
		}
		req.setAttribute("listUsuarios", listUsuarios);
	}

}

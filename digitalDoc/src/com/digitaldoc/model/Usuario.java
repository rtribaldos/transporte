package com.digitaldoc.model;

import java.io.Serializable;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION) 
public class Usuario implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String nombre;
	
	@Persistent
	private String empresa;
		
	@Persistent
	private String cif;
	
	@Persistent
	private String email;
	
	@Persistent
	private String otrosEmails;
	
	@Persistent
	private String notificaciones;
	
	@Persistent
	private String password;
	
	@Persistent
	private String telefono;
	
	@Persistent
	private String cp;
	
	@Persistent
	private String provincia;
	
	@Persistent
	private String poblacion;
	
	@Persistent
	private String direccion;
	
	@Persistent
	private boolean activo;
	
	@Persistent
	private String perfil;
	
	@Persistent
	private String pais;
	
	@Persistent
	private boolean acreedor;
	
	@Persistent
	private boolean empleado;
	
	
	@Persistent(mappedBy = "usuario")
    private List<Documento> facturas;
	
	@Persistent(mappedBy = "acreedor")
    private List<Anticipo> anticipos;
	
	@Persistent(mappedBy = "cliente")
    private List<Cancelacion> cancelaciones;
		
		
	

	public Usuario(Long id, String nombre, String empresa, String cif, String email, String otrosEmails,
			String notificaciones, String password, String telefono, String cp, String provincia, String poblacion,
			String direccion, boolean activo, String perfil, String pais, boolean acreedor, boolean empleado,
			List<Documento> facturas, List<Anticipo> anticipos, List<Cancelacion> cancelaciones) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.empresa = empresa;
		this.cif = cif;
		this.email = email;
		this.otrosEmails = otrosEmails;
		this.notificaciones = notificaciones;
		this.password = password;
		this.telefono = telefono;
		this.cp = cp;
		this.provincia = provincia;
		this.poblacion = poblacion;
		this.direccion = direccion;
		this.activo = activo;
		this.perfil = perfil;
		this.pais = pais;
		this.acreedor = acreedor;
		this.empleado = empleado;
		this.facturas = facturas;
		this.anticipos = anticipos;
		this.cancelaciones = cancelaciones;
	}

	public Usuario() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public List<Documento> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Documento> facturas) {
		this.facturas = facturas;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getNotificaciones() {
		return notificaciones;
	}

	public void setNotificaciones(String notificaciones) {
		this.notificaciones = notificaciones;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public void addFactura(Documento doc){
		this.facturas.add(doc);
	}

	public List<Anticipo> getAnticipos() {
		return anticipos;
	}

	public void setAnticipos(List<Anticipo> anticipos) {
		this.anticipos = anticipos;
	}
	
	public void addAnticipo(Anticipo anticipo){
		this.anticipos.add(anticipo);
	}
	
	public void addCancelacion(Cancelacion cancelacion){
		this.cancelaciones.add(cancelacion);
	}

	public boolean isAcreedor() {
		return acreedor;
	}

	public void setAcreedor(boolean acreedor) {
		this.acreedor = acreedor;
	}

	public boolean isEmpleado() {
		return empleado;
	}

	public void setEmpleado(boolean empleado) {
		this.empleado = empleado;
	}

	public List<Cancelacion> getCancelaciones() {
		return cancelaciones;
	}

	public void setCancelaciones(List<Cancelacion> cancelaciones) {
		this.cancelaciones = cancelaciones;
	}

	public String getOtrosEmails() {
		return otrosEmails;
	}

	public void setOtrosEmails(String otrosEmails) {
		this.otrosEmails = otrosEmails;
	}
	
	
}

package com.digitaldoc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;


@PersistenceCapable(identityType = IdentityType.APPLICATION) 
public class Documento implements Serializable{
		
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String id;
	
	@Persistent
	private String serie;
	
	@Persistent
	private double importe;
	
	@Persistent
	private String numero;
	
	@Persistent
	private String name;
	
	@Persistent
	private String estado;
	
	@Persistent
	private String fichPrincipal;
	
	@Persistent
	private String fichAnticipo;
	
	@Persistent
	private String idAnticipo;
	
	@Persistent
	private String idCancelacion;
	
	@Persistent
	private boolean enviado;
	
	//tipo f - factura, 
	@Persistent
	private String tipo;
	
	@Persistent(mappedBy = "documento")
    @Element(dependent = "true")
    private List<MediaObject> ficheros;
	
	@Persistent
	private Usuario usuario;
	
	@Persistent
	private boolean activo;
	
	@Persistent
	private boolean abierto;
	
	@Persistent
	private Date fecha;
	
	private String url;
	

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumExp() {
		return numero;
	}

	public void setNumExp(String numExp) {
		this.numero = numExp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<MediaObject> getFicheros() {
		return ficheros;
	}

	public void setFicheros(List<MediaObject> ficheros) {
		this.ficheros = ficheros;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isAbierto() {
		return abierto;
	}

	public void setAbierto(boolean abierto) {
		this.abierto = abierto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNumDoc() {
		return numero;
	}

	public void setNumDoc(String numDoc) {
		this.numero = numDoc;
	}

	public String getFactura() {
		return fichPrincipal;
	}

	public void setFactura(String factura) {
		if(!"".equals(factura)){
			this.fichPrincipal = factura;
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getFichPrincipal() {
		return fichPrincipal;
	}

	public void setFichPrincipal(String fichPrincipal) {
		if(!"".equals(fichPrincipal)){
			this.fichPrincipal = fichPrincipal;
		}
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	

	public Documento() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	 public void addFichero(MediaObject foto){
    	this.ficheros.add(foto);
    }
	 
	public String getUrl(){
		for(MediaObject docMedia: this.getFicheros()){
			if(docMedia.getFilename().equals(this.getFichPrincipal())){
				return docMedia.getDisplayURL();
			}
		}
		return "";
	}
	
	public String getUrlAnticipo(){
		for(MediaObject docMedia: this.getFicheros()){
			if(docMedia.getFilename().equals(this.getFichAnticipo())){
				return docMedia.getDisplayURL();
			}
		}
		return "";
	}

	public String getFichAnticipo() {
		return fichAnticipo;
	}

	public void setFichAnticipo(String fichAnticipo) {
		if(!"".equals(fichAnticipo)){
			this.fichAnticipo = fichAnticipo;
		}
	}

	public String getIdAnticipo() {
		if(idAnticipo==null || "null".equals(idAnticipo)){
			return "";
		}else{
			return idAnticipo;
		}
	}

	public void setIdAnticipo(String idAnticipo) {
		this.idAnticipo = idAnticipo;
	}

	public String getIdCancelacion() {
		if(idCancelacion==null || "null".equals(idCancelacion)){
			return "";
		}else{
			return idCancelacion;
		}
	}

	public void setIdCancelacion(String idCancelacion) {
		this.idCancelacion = idCancelacion;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isEnviado() {
		return enviado;
	}

	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
	}

	public Documento(Key key, String id, String serie, double importe, String numero, String name, String estado,
			String fichPrincipal, String fichAnticipo, String idAnticipo, String idCancelacion, boolean enviado,
			String tipo, List<MediaObject> ficheros, Usuario usuario, boolean activo, boolean abierto, Date fecha,
			String url) {
		super();
		this.key = key;
		this.id = id;
		this.serie = serie;
		this.importe = importe;
		this.numero = numero;
		this.name = name;
		this.estado = estado;
		this.fichPrincipal = fichPrincipal;
		this.fichAnticipo = fichAnticipo;
		this.idAnticipo = idAnticipo;
		this.idCancelacion = idCancelacion;
		this.enviado = enviado;
		this.tipo = tipo;
		this.ficheros = ficheros;
		this.usuario = usuario;
		this.activo = activo;
		this.abierto = abierto;
		this.fecha = fecha;
		this.url = url;
	}
			
}

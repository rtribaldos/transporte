package com.digitaldoc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;


@PersistenceCapable(identityType = IdentityType.APPLICATION) 
public class Anticipo implements Serializable{

	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String id;
	
	@Persistent
	private Date fecha;
	
	@Persistent
	private Usuario acreedor;
	
	@Persistent
	private List<String> facturas;
	
	@Persistent
	private AnticipoFile fichero;
	
	@Persistent
	private boolean enviado;
		
	
	public List<String> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<String> facturas) {
		this.facturas = facturas;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Usuario getAcreedor() {
		return acreedor;
	}

	public void setAcreedor(Usuario acreedor) {
		this.acreedor = acreedor;
	}

	
		
	public Anticipo(Key key, String id, Date fecha, Usuario acreedor, List<String> facturas, AnticipoFile fichero,
			boolean enviado) {
		super();
		this.key = key;
		this.id = id;
		this.fecha = fecha;
		this.acreedor = acreedor;
		this.facturas = facturas;
		this.fichero = fichero;
		this.enviado = enviado;
	}

	public Anticipo() {
		// TODO Auto-generated constructor stub
	}

	public String getAllFacturas(){
		String result="";
		if(this.facturas != null && this.facturas.size() > 0){
			for(String factura : this.facturas){
				result+=factura+",";
			}
		}
		
		return result;
	}

	public AnticipoFile getFichero() {
		return fichero;
	}

	public void setFichero(AnticipoFile fichero) {
		this.fichero = fichero;
	}

	public boolean isEnviado() {
		return enviado;
	}

	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
	}
	
	
	
	
}

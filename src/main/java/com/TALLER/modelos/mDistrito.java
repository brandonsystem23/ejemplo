package com.TALLER.modelos;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="distrito")
public class mDistrito {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer iddistrito;
	@Column private String nombre;
	
	@JsonIgnore
	@OneToMany(mappedBy = "distrito", cascade = CascadeType.ALL)
	private Collection<mCliente> cliente;
	
	@JsonIgnore
	@OneToMany(mappedBy = "distrito", cascade = CascadeType.ALL)
	private Collection<mTrabajador> trabajador;

	public Integer getIddistrito() {
		return iddistrito;
	}

	public void setIddistrito(Integer iddistrito) {
		this.iddistrito = iddistrito;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Collection<mCliente> getCliente() {
		return cliente;
	}

	public void setCliente(Collection<mCliente> cliente) {
		this.cliente = cliente;
	}

	public Collection<mTrabajador> getTrabajador() {
		return trabajador;
	}

	public void setTrabajador(Collection<mTrabajador> trabajador) {
		this.trabajador = trabajador;
	}
	
	
}

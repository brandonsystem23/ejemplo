package com.TALLER.modelos;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author brand
 *
 */
@Entity
@Table(name="trabajador")
public class mTrabajador {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idtrabajador;
	@Column private String apellidos;
	@Column private String nombres;
	@Column private String dni;
	@Column private String direccion;
	@Column private String telefono;
	@Column private String email;
	@Column private String estado="ACTIVO";
	@Column private String foto;
	
	@ManyToOne
	@JoinColumn(name = "iddistrito")
	mDistrito distrito;
	
	@JsonIgnore
	@OneToOne(mappedBy = "trabajador", cascade = CascadeType.ALL)
    private User user;
	
	@JsonIgnore
	@OneToMany(mappedBy = "trabajador", cascade = CascadeType.ALL)
	private Collection<mPedido> pedido;

	public Integer getIdtrabajador() {
		return idtrabajador;
	}

	public void setIdtrabajador(Integer idtrabajador) {
		this.idtrabajador = idtrabajador;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<mPedido> getPedido() {
		return pedido;
	}

	public void setPedido(Collection<mPedido> pedido) {
		this.pedido = pedido;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public mDistrito getDistrito() {
		return distrito;
	}

	public void setDistrito(mDistrito distrito) {
		this.distrito = distrito;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}	
	
	
}

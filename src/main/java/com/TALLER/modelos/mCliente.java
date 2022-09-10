package com.TALLER.modelos;

import java.io.Serializable;
import java.sql.Date;
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
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="cliente")
public class mCliente implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idcliente;
	@Column private String dni;
	@Column private String apellidos;
	@Column private String nombres;
	@Column private String telefono;
	@Column private String email;
	@Column private String direccion;
	@Column private String estado="ACTIVO";
	@Column private Date fecharegistro = new Date(System.currentTimeMillis());
	
	@ManyToOne
	@JoinColumn(name = "iddistrito")
	mDistrito distrito;
	
	@JsonIgnore
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
	private Collection<mPedido> pedido;

	public Integer getIdcliente() {
		return idcliente;
	}



	public void setIdcliente(Integer idcliente) {
		this.idcliente = idcliente;
	}



	public String getDni() {
		return dni;
	}



	public void setDni(String dni) {
		this.dni = dni;
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



	public Collection<mPedido> getPedido() {
		return pedido;
	}



	public void setPedido(Collection<mPedido> pedido) {
		this.pedido = pedido;
	}
	

	public String getEstado() {
		return estado;
	}



	public void setEstado(String estado) {
		this.estado = estado;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public Date getFecharegistro() {
		return fecharegistro;
	}



	public void setFecharegistro(Date fecharegistro) {
		this.fecharegistro = fecharegistro;
	}



	@Override
	public String toString() {
		return "mCliente [dni=" + dni + ", apellidos=" + apellidos + ", nombres=" + nombres + ", telefono=" + telefono
				+ ", email=" + email + ", direccion=" + direccion + ", distrito=" + distrito + ", pedido=" + pedido
				+ "]";
	}
	

}

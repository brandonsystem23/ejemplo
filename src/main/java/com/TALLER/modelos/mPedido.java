package com.TALLER.modelos;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

import java.util.List;

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
@Table(name = "pedido")
public class mPedido implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idpedido;
	@Column
	private Date fecha;
	@Column
	private Double monto;
	@Column
	private String estado="REGISTRADO";

	@ManyToOne
	@JoinColumn(name = "idcliente")
	mCliente cliente;

	@ManyToOne
	@JoinColumn(name = "idtrabajador")
	mTrabajador trabajador;

	@JsonIgnore
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	private List<mDetallePedido> detallepedido = new ArrayList<>();
	
	
	public mPedido(Date fecha, mCliente cliente, mTrabajador trabajador, Double monto) {
		this.fecha = fecha;
		this.cliente = cliente;
		this.trabajador = trabajador;
		this.monto = monto;
	}
	
	public mPedido() {

	}

	public Integer getIdpedido() {
		return idpedido;
	}

	public void setIdpedido(Integer idpedido) {
		this.idpedido = idpedido;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public mCliente getCliente() {
		return cliente;
	}

	public void setCliente(mCliente cliente) {
		this.cliente = cliente;
	}

	public mTrabajador getTrabajador() {
		return trabajador;
	}

	public void setTrabajador(mTrabajador trabajador) {
		this.trabajador = trabajador;
	}

	public List<mDetallePedido> getDetallepedido() {
		return detallepedido;
	}

	public void setDetallepedido(List<mDetallePedido> detallepedido) {
		this.detallepedido = detallepedido;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}

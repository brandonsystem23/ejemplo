package com.TALLER.modelos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity(name="detallepedido")
@Table(name="detallepedido")
@IdClass(DetallePedidoId.class)
public class mDetallePedido {

	@Id private Integer codproducto;
	@Id private Integer codpedido;
	@Column private Double precio;
	@Column private Integer cantidad;

	@ManyToOne
	@JoinColumn(name = "codproducto", referencedColumnName = "idproducto",insertable = false, updatable = false)
	private mProducto producto;
	
	@ManyToOne
	@JoinColumn(name = "codpedido", referencedColumnName = "idpedido",insertable = false, updatable = false)
	private mPedido pedido;

	
	public mDetallePedido(Integer codproducto, Integer codpedido, Double precio, Integer cantidad, mProducto producto,
			mPedido pedido) {
		super();
		this.codproducto = codproducto;
		this.codpedido = codpedido;
		this.precio = precio;
		this.cantidad = cantidad;
		this.producto = producto;
		this.pedido = pedido;
	}

	public mDetallePedido(Double precio, Integer cantidad, mProducto producto,
			mPedido pedido) {
		
		this.precio = precio;
		this.cantidad = cantidad;
		this.producto = producto;
		this.pedido = pedido;
	}
	
	public mDetallePedido() {
		
	}
	
	public mDetallePedido(Integer cantidad, mProducto producto) {
		this.cantidad = cantidad;
		this.producto = producto;

	}

	public Integer getCodproducto() {
		return codproducto;
	}

	public void setCodproducto(Integer codproducto) {
		this.codproducto = codproducto;
	}

	public Integer getCodpedido() {
		return codpedido;
	}

	public void setCodpedido(Integer codpedido) {
		this.codpedido = codpedido;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public mProducto getProducto() {
		return producto;
	}

	public void setProducto(mProducto producto) {
		this.producto = producto;
	}

	public mPedido getPedido() {
		return pedido;
	}

	public void setPedido(mPedido pedido) {
		this.pedido = pedido;
	}
	
    public Double getTotal() {
        return this.getPrecio() * this.cantidad;
    }
    
    public void aumentarCantidad() {
        this.cantidad++;
    }

}

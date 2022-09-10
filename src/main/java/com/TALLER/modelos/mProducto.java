package com.TALLER.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "producto")
public class mProducto implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idproducto;
	@Column
	private String nombre;
	@Column
	private Double precio;
	@Column
	@Min(value = 0, message = "El stock mínimo es 0")
	private Integer stock;
	@Column
	private String estado = "ACTIVO";
	@Column 
	private String estadostock="EN STOCK";

	@ManyToOne
	@JoinColumn(name = "idcategoria")
	mCategoria categoria;

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "producto",cascade = CascadeType.ALL)
	private List<mDetallePedido> detallepedido = new ArrayList<>();
	
	
	public mProducto(Integer idproducto, String nombre, Double precio, Integer stock, String estado,
			mCategoria categoria, List<mDetallePedido> detallepedido) {
		super();
		this.idproducto = idproducto;
		this.nombre = nombre;
		this.precio = precio;
		this.stock = stock;
		this.estado = estado;
		this.categoria = categoria;
		this.detallepedido = detallepedido;
	}
	
	public mProducto() {
			
	}

	public Integer getIdproducto() {
		return idproducto;
	}

	public void setIdproducto(Integer idproducto) {
		this.idproducto = idproducto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public mCategoria getCategoria() {
		return categoria;
	}

	public void setCategoria(mCategoria categoria) {
		this.categoria = categoria;
	}

	public List<mDetallePedido> getDetallepedido() {
		return detallepedido;
	}

	public void setDetallepedido(List<mDetallePedido> detallepedido) {
		this.detallepedido = detallepedido;
	}

	public String getEstadostock() {
		return estadostock;
	}

	public void setEstadostock(String estadostock) {
		this.estadostock = estadostock;
	}

	public boolean sinExistencia() {
        return this.stock <= 0;
    }
	
	public void fueraStock() {
		if(this.stock==0) {
			this.estadostock="SIN STOCK";
		}
		
		if(this.stock>0){
			this.estadostock="EN STOCK";
		}
	}
	
	public void restarStock(Integer cantidad) {
        this.stock -= cantidad;
    }

	@Override
	public String toString() {
		return "mProducto [idproducto=" + idproducto + ", nombre=" + nombre + ", precio=" + precio + ", stock=" + stock
				+ ", estado=" + estado + ", idcategoria=" + categoria.getIdcategoria() + ", descripcion categoria=" + categoria.getDescripcion() + "]";
	}
	
	

}

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
@Table(name="categoria")
public class mCategoria {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idcategoria;
	

	@Column private String descripcion;
	

	@JsonIgnore
	@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
	private Collection<mProducto> producto;

	public Integer getIdcategoria() {
		return idcategoria;
	}

	public void setIdcategoria(Integer idcategoria) {
		this.idcategoria = idcategoria;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Collection<mProducto> getProducto() {
		return producto;
	}

	public void setProducto(Collection<mProducto> producto) {
		this.producto = producto;
	}
	
	
}

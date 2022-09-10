package com.TALLER.modelos;

import java.io.Serializable;

import javax.persistence.Column;

public class DetallePedidoId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Column
    private Integer codproducto;

    @Column
    private Integer codpedido;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	} 
    
}

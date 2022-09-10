package com.TALLER.interfaces;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.TALLER.modelos.mCategoria;
import com.TALLER.modelos.mProducto;
@Transactional
public interface iProducto extends JpaRepository<mProducto,Integer> {
	@Modifying 
	@Query(value="update producto set stock=200 where id_producto=:idProducto",nativeQuery = true)
	void actualizarStock(@Param("idProducto") Integer idProducto);
	
	@Modifying 
	@Query(value="update producto set estado=1 where id_producto=:idProducto",nativeQuery = true)
	void estadoEnviado(@Param("idProducto") Integer idProducto);
	
	@Modifying 
	@Query(value="update producto set estado=2 where id_producto=:idProducto",nativeQuery = true)
	void estadoAceptado(@Param("idProducto") Integer idProducto);
	
	@Modifying 
	@Query(value="update producto set estado=0 where id_producto=:idProducto",nativeQuery = true)
	void estadoRecibido(@Param("idProducto") Integer idProducto);
	
	Page<mProducto> findByCategoria(mCategoria categoria, Pageable pageable);
//	
//	@Modifying
//	@Query(value="update solicitudes set estado='A' where numero=:numero",nativeQuery = true)
//	void aprobarSolicitud(@Param("numero") Integer numero);
}

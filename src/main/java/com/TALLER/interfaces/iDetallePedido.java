package com.TALLER.interfaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.TALLER.modelos.mDetallePedido;


@Transactional
public interface iDetallePedido extends JpaRepository<mDetallePedido, Integer>{
//	@Modifying 
//	@Query(value="update solicitudes set estado='D' where numero=:numero",nativeQuery = true)
//	void rechazarSolicitud(@Param("numero") Integer numero);
	
	@Transactional
    @Modifying
    @Query("DELETE FROM detallepedido dp WHERE dp.codpedido=:id")
    void eliminarDetalle(@Param("id") Integer id);
	
//	@Modifying
//	@Query(value="update solicitudes set estado='A' where numero=:numero",nativeQuery = true)
//	void aprobarSolicitud(@Param("numero") Integer numero);

}

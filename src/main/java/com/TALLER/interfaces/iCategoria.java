package com.TALLER.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TALLER.modelos.mCategoria;

public interface iCategoria extends JpaRepository<mCategoria,Integer> {
//	@Modifying 
//	@Query(value="update solicitudes set estado='D' where numero=:numero",nativeQuery = true)
//	void rechazarSolicitud(@Param("numero") Integer numero);
//	
//	@Modifying
//	@Query(value="update solicitudes set estado='A' where numero=:numero",nativeQuery = true)
//	void aprobarSolicitud(@Param("numero") Integer numero);
}

package com.TALLER.interfaces;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import com.TALLER.modelos.mPedido;

@Transactional
public interface iPedido extends JpaRepository<mPedido,Integer> {
//	@Modifying 
//	@Query(value="update solicitudes set estado='D' where numero=:numero",nativeQuery = true)
//	void rechazarSolicitud(@Param("numero") Integer numero);
//	
//	@Modifying
//	@Query(value="update solicitudes set estado='A' where numero=:numero",nativeQuery = true)
//	void aprobarSolicitud(@Param("numero") Integer numero);
	Page<mPedido> findByEstado(String estado, Pageable pageable);
	
}

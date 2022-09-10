package com.TALLER.interfaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.TALLER.modelos.mCliente;

@Transactional
public interface iCliente extends JpaRepository<mCliente,Integer>{
//	@Modifying 
//	@Query(value="update solicitudes set estado='D' where numero=:numero",nativeQuery = true)
//	void rechazarSolicitud(@Param("numero") Integer numero);
//	
	
//	@Modifying
//	@Query(value="update solicitudes set estado='A' where numero=:numero",nativeQuery = true)
//	void aprobarSolicitud(@Param("numero") Integer numero);
}

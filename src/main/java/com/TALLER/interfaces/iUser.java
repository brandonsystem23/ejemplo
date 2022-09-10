package com.TALLER.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.TALLER.modelos.User;
@Transactional
public interface iUser extends JpaRepository<User, Long>{
	@Modifying 
	@Query(value="update user set authority=:idauthority where id=:id",nativeQuery = true)
	void actualizarAuthority(@Param("idauthority") Long idauthority,@Param("id") Long id);
	
    @Modifying
    @Query(value="DELETE FROM user WHERE trabajador=:id",nativeQuery = true)
    void eliminarUsuario(@Param("id") Integer id);

}

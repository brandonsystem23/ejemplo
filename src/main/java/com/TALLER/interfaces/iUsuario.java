package com.TALLER.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;


import com.TALLER.modelos.User;

public interface iUsuario extends JpaRepository<User,Integer>{
	
	public User findByUsername(String username);

}

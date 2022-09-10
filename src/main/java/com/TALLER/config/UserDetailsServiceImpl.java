package com.TALLER.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.TALLER.interfaces.UserRepository;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
	
    @Override
     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
     //Buscar el usuario con el repositorio y si no existe lanzar una exepcion
    	 com.TALLER.modelos.User appUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No existe usuario"));
		
    //Mapear nuestra lista de Authority con la de spring security 
//    List grantList = new ArrayList();
//    for (Authority authority: appUser.getAuthority()) {
//        // ROLE_USER, ROLE_ADMIN,..
//        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
//            grantList.add(grantedAuthority);
//    }
    	 List grantList = new ArrayList();

         GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(appUser.getAuthority().getAuthority());
             grantList.add(grantedAuthority);
		
    //Crear El objeto UserDetails que va a ir en sesion y retornarlo.
    @SuppressWarnings("unchecked")
	UserDetails user = (UserDetails) new User(appUser.getUsername(), appUser.getPassword(), grantList);
         return user;
    }
}
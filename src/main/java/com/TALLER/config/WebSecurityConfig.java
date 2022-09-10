package com.TALLER.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	//Necesario para evitar que la seguridad se aplique a los resources
    //Como los css, imagenes y javascripts
    String[] resources = new String[]{
            "/animate.css/**","/autosize/**","/bootstrap/**","/css/**","/icons/**","/images/**","/js/**","/layer/**",
            "/bootstrap-daterangepicker/**","/bootstrap-datetimepicker/**","/bootstrap-progressbar/**","/bootstrap-wysiwyg/**"
            ,"/build/**","/Chart.js/**","/cropper/**","/dattables.net/**","/dattables.net-bs/**","/dattables.net-buttons/**"
            ,"/dattables.net-buttons-bs/**","/dattables.net-fixedheader/**","/dattables.net-fixerheader-bas/**"
            ,"/dattables.net-keytable/**","/dattables.net-responsive/**","/dattables.net-responsive-bs/**","/dattables.net-scroller/**"
            ,"/dattables.net-scroller-bs/**","/DateJS/**","/devbridge-autocomplete/**","/dropzone/**","/echarts/**","/eve/**"
            ,"/fastclick/**","/Flot/**","/flot.curvedlines/**","/flot.orderbars/**","/flot-spline/**","/fontawesome/**","/font-awesome/**"
            ,"/fonts/**","/fullcalendar/**","/gauge.js/**","/google-code-prettify/**","/iCheck/**","/ion.rangeSlider/**","/jquery/**"
            ,"/jquery.easy-pie-chart/**","/jquery.hotkeys/**","/jquery.inputmask/**","/jquery-knob/**","/jquery-mousewheel/**"
            ,"/jQuery-Smart-Wizard/**","/jquery-sparkline/**","/jqvmap/**","/jszip/**","/malihu-custom-scrollbar-plugin/**"
            ,"/mjolnic-bootstrap-colorpicker/**","/mocha/**","/moment/**","/morris.js/**","/normalize-css/**"
            ,"/nprogress/**","/parsleyjs/**","/pdfmake/**","/pnotify/**","/raphael/**","/requirejs/**","/select2/**"
            ,"/skycons/**","/starrr/**","/switchery/**","/transitionize/**","/validator/**"
    };
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
	        .antMatchers(resources).permitAll()  
	        .antMatchers("/","/index","/login").permitAll()
	        .antMatchers("/reportes/*").hasRole("ADMINISTRADOR")
	        .antMatchers("/producto/*","/cliente/*","/venta/listar","/venta/registrar","/trabajador/*").hasAnyRole("ADMINISTRADOR","SECRETARIA")
	        .antMatchers("/venta/pagar").hasAnyRole("ADMINISTRADOR","SECRETARIA","REPARTIDOR")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/menu",true)
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
            .logout()
                .permitAll()
                .logoutSuccessUrl("/login?logout");
    }
    
    //Crea el encriptador de contraseñas	
    public BCryptPasswordEncoder passwordEncoder() {
    	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(4);
//El numero 4 representa que tan fuerte quieres la encriptacion.
//Se puede en un rango entre 4 y 31. 
//Si no pones un numero el programa utilizara uno aleatoriamente cada vez
//que inicies la aplicacion, por lo cual tus contrasenas encriptadas no funcionaran bien
        return bCryptPasswordEncoder;
    }
	
    @Autowired
    UserDetailsServiceImpl userDetailsService;
	
    //Registra el service para usuarios y el encriptador de contrasena
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { 
 
        // Setting Service to find User in the database.
        // And Setting PassswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());     
    }
}
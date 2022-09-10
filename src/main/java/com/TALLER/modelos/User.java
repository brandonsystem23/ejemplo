package com.TALLER.modelos;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name="user")
public class User {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column
	private String username;
	
	@Column
	private String password;

	@Column
	private boolean enabled=true;
	
	@OneToOne
    @JoinColumn(name = "trabajador", updatable = false, nullable = false)
    private mTrabajador trabajador;
	
	@ManyToOne
	@JoinColumn(name = "authority", updatable = false, nullable = false)
	private Authority authority;

//Getters y Setters
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	
	public User(String username, String password, mTrabajador trabajador, Authority authority) {
	this.username = username;
	this.password = password;
	this.trabajador = trabajador;
	this.authority = authority;
}
	
	public User() {
	
	}

	public String getFecha() {

		return new Date(System.currentTimeMillis()).toString();
	}

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}
	
	
	public void setPassword(String password) {
		this.password = password;
	}



	public boolean isEnabled() {
		return enabled;
	}



	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	

	

	public Authority getAuthority() {
		return authority;
	}



	public void setAuthority(Authority authority) {
		this.authority = authority;
	}



	public mTrabajador getTrabajador() {
		return trabajador;
	}



	public void setTrabajador(mTrabajador trabajador) {
		this.trabajador = trabajador;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
	}

}
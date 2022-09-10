package com.TALLER.controladores;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.TALLER.config.Passgenerator;
import com.TALLER.interfaces.IUploadFileService;
import com.TALLER.interfaces.iDistrito;
import com.TALLER.interfaces.iProducto;
import com.TALLER.interfaces.iTrabajador;
import com.TALLER.interfaces.iUser;
import com.TALLER.modelos.User;
import com.TALLER.modelos.mDistrito;
import com.TALLER.modelos.mProducto;
import com.TALLER.modelos.mTrabajador;


@Controller
@SessionAttributes("user")
public class IndexController {

	@Autowired
	private iUser miusuario;
	
	@Autowired
	private iTrabajador mitrabajador;
	
	@Autowired
	private iProducto miproducto;
	
	@Autowired
	private iDistrito midistrito;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;

		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
	
	@GetMapping({"/index","/"}) // RUTA DONDE RETORNARA
	public String index(Model model) { //IMPORTAR MODEL
		model.addAttribute("titulo","GAS KOKO'S");
		List<mProducto> productos = miproducto.findAll();
		model.addAttribute("productos",productos);
		return "index";		
	}
	
	
	@GetMapping({"/login"}) // RUTA DONDE RETORNARA
	public String login(Model model, SessionStatus status) { //IMPORTAR MODEL
		model.addAttribute("titulo","Inicio de Sesión");
		status.setComplete();
		return "seguridad/login";		
	}
	
	@GetMapping({"/menu"}) // RUTA DONDE RETORNARA
	public String home(Model model) { //IMPORTAR MODEL
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios",usuarios);
		model.addAttribute("fragmento",0);
		return "seguridad/home";		
	}
	
	@GetMapping({"/perfil/{id}"}) // RUTA DONDE RETORNARA
	public String perfil(@PathVariable int id, Map<String, Object> model) { //IMPORTAR MODEL
		model.put("fragmento",13);
		model.put("trabajador", mitrabajador.findById(id).orElse(null));
		List<mDistrito> distritos = midistrito.findAll();
		model.put("distritos", distritos);
		List<User> usuarios = miusuario.findAll();
		model.put("usuarios",usuarios);
		return "seguridad/home";		
	}
	
	@PostMapping("/perfil/{id}")
	public String perfil(@ModelAttribute @Valid mTrabajador trabajador, BindingResult bindingResult,
			RedirectAttributes redirectAttrs) {
		
		if (bindingResult.hasErrors()) {
			if (trabajador.getIdtrabajador() != null) {
				return "trabajador/editar_trabajador";
			}
			return "redirect:/perfil/"+trabajador.getIdtrabajador();
		}

		mTrabajador t = mitrabajador.findById(trabajador.getIdtrabajador()).get();
		
		List<mDistrito> distritos = midistrito.findAll();
		for (mDistrito distrito : distritos) {
			if(distrito.getNombre().equals(trabajador.getDistrito().getNombre())) {
				t.setDistrito(distrito);
			}
		}
		t.setEmail(trabajador.getEmail());
		t.setTelefono(trabajador.getTelefono());
		t.setApellidos(trabajador.getApellidos().toUpperCase());
		t.setNombres(trabajador.getNombres().toUpperCase());
		t.setDireccion(trabajador.getDireccion().toUpperCase());
		mitrabajador.save(t);

		
		redirectAttrs.addFlashAttribute("mensaje", "Pefil editado correctamente").addFlashAttribute("clase", "success");
		return "redirect:/perfil/"+trabajador.getIdtrabajador();
	}
	
	
	@PostMapping("/nuevacontra")
	public String contra(@RequestParam Long idusuario,@RequestParam String password,Model model) {

		User u = miusuario.findById(idusuario).get();
		
		Passgenerator pass = new Passgenerator();
		String newPassword = pass.passwordEncry(password);
		u.setPassword(newPassword);
		miusuario.save(u);
		
		model.addAttribute("fragmento",13);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios",usuarios);
		return "redirect:/perfil/"+u.getTrabajador().getIdtrabajador();
	}
	
	public mTrabajador buscarTrabajador(@PathVariable("id") Integer id) {
		
		return mitrabajador.findById(id).get();
	}
	
	@GetMapping({"/borrar/{id}"}) // RUTA DONDE RETORNARA
	public String borrarPerfil(@PathVariable int id,RedirectAttributes flash) { //IMPORTAR MODEL

			mTrabajador trabajador = mitrabajador.findById(id).get();
			
			

			if (uploadFileService.delete(trabajador.getFoto())) {
				flash.addFlashAttribute("info", "Foto " + trabajador.getFoto() + " eliminada con exito!");
			}
			
			trabajador.setFoto(null);
			mitrabajador.save(trabajador);

			return "redirect:/perfil/"+trabajador.getIdtrabajador();		
	}
	
	@PostMapping("/nuevafoto")
	public String contra(@RequestParam Integer idtrabajador,@RequestParam("file") MultipartFile foto, RedirectAttributes flash,Model model) {

		mTrabajador trabajador = mitrabajador.findById(idtrabajador).get();
		
		
		if (trabajador.getFoto()!=null) {
		if (uploadFileService.delete(trabajador.getFoto())) {
			flash.addFlashAttribute("info", "Foto " + trabajador.getFoto() + " eliminada con exito!");
		}
		}
		
		
		if (!foto.isEmpty()) {
	
			
			if (trabajador.getIdtrabajador() != null && trabajador.getIdtrabajador()> 0 && trabajador.getFoto() != null
					&& trabajador.getFoto().length() > 0) {

				uploadFileService.delete(trabajador.getFoto());
			}

			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
			trabajador.setFoto(uniqueFilename);
		}
		
		mitrabajador.save(trabajador);

		return "redirect:/perfil/"+trabajador.getIdtrabajador();
	}
	
}

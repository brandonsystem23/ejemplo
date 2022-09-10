package com.TALLER.controladores;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.TALLER.interfaces.IUploadFileService;
import com.TALLER.interfaces.iAuthority;
import com.TALLER.interfaces.iDistrito;
import com.TALLER.interfaces.iTrabajador;
import com.TALLER.interfaces.iUser;
import com.TALLER.modelos.Authority;
import com.TALLER.modelos.User;
import com.TALLER.modelos.mDistrito;
import com.TALLER.modelos.mTrabajador;
import com.TALLER.pagination.PageRender;
import com.TALLER.reports.TrabajadorReport;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("trabajador")
public class cTrabajador {
	@Autowired
	private iTrabajador mitrabajador;

	@Autowired
	private iUser miuser;

	@Autowired
	private iAuthority miauthority;

	@Autowired
	private iUser miusuario;

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

	@GetMapping(value = "/registrar")
	public String agregarProducto(Model model) {
		List<Authority> authorities = miauthority.findAll();
		model.addAttribute("authorities", authorities);
		model.addAttribute("trabajador", new mTrabajador());
		model.addAttribute("fragmento", 11);
		List<mDistrito> distritos = midistrito.findAll();
		model.addAttribute("distritos", distritos);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios", usuarios);
		return "seguridad/home";
	}

	@PostMapping(value = "/registrar")
	public String guardarProducto(@ModelAttribute @Valid mTrabajador trabajador, BindingResult bindingResult,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, RedirectAttributes redirectAttrs) {
		boolean encontro=false;
		if (bindingResult.hasErrors()) {
			return "trabajador/registrar_trabajador";
		}

		List<mTrabajador> trabajadores = mitrabajador.findAll();

		for (mTrabajador t : trabajadores) {
			if (trabajador.getDni().equalsIgnoreCase(t.getDni())) {
				redirectAttrs.addFlashAttribute("mensaje", "Ya existe un trabajador con el mismo DNI")
						.addFlashAttribute("clase", "warning");
				return "redirect:/trabajador/registrar";

			}
		}

		if (!foto.isEmpty()) {
			System.out.println(foto.getName());

			if (trabajador.getIdtrabajador() != null && trabajador.getIdtrabajador() > 0 && trabajador.getFoto() != null
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

		mTrabajador t = new mTrabajador();
		List<mDistrito> distritos = midistrito.findAll();
		for (mDistrito distrito : distritos) {
			if (distrito.getNombre().equals(trabajador.getDistrito().getNombre())) {
				t.setDistrito(distrito);
				encontro = true;
			}
		}

		if (encontro == false) {
			redirectAttrs.addFlashAttribute("mensaje", "El distrito no existe o no pertenece a La Libertad")
					.addFlashAttribute("clase", "danger");
			return "redirect:/trabajador/registrar";
		}

		t.setEmail(trabajador.getEmail());
		t.setTelefono(trabajador.getTelefono());
		t.setApellidos(trabajador.getApellidos().toUpperCase());
		t.setNombres(trabajador.getNombres().toUpperCase());
		t.setDireccion(trabajador.getDireccion().toUpperCase());
		t.setDni(trabajador.getDni());

		t.setFoto(trabajador.getFoto());
		mitrabajador.save(t);

//		//registrar usuario
		String[] apellido_separado = trabajador.getApellidos().split(" ");
		String username = trabajador.getNombres().toLowerCase().charAt(0) + apellido_separado[0].toLowerCase()
				+ "@gaskokos.com";
		Authority a = buscaAuthority(trabajador.getUser().getAuthority().getId());
		User user = new User(username, "$2a$04$h4f7YdnvQi.jqAxmGP9kJ.YvzmXSXmhftnMGUKZgZP2U3thqk/psi", t, a);
		miuser.save(user);

		redirectAttrs
				.addFlashAttribute("mensaje",
						"Trabajador registrado correctamente con username: " + username + " y password: 1234")
				.addFlashAttribute("clase", "success");

		return "redirect:/trabajador/registrar";

	}

	@GetMapping("listar")
	public String listarTrabajador(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 12);
		Page<mTrabajador> trabajadores = mitrabajador.findAll(pageRequest);
		PageRender<mTrabajador> pageRender = new PageRender<>("/trabajador/listar", trabajadores);
		model.addAttribute("trabajadores", trabajadores);
		model.addAttribute("fragmento", 9);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("page", pageRender);
		return "seguridad/home";
	}

	@PostMapping("/eliminar")
	public String eliminar(@ModelAttribute mTrabajador trabajador, RedirectAttributes redirectAttrs,
			RedirectAttributes flash) {
		redirectAttrs.addFlashAttribute("mensaje", "Eliminado correctamente").addFlashAttribute("clase", "warning");
		miuser.eliminarUsuario(trabajador.getIdtrabajador());
		mTrabajador t = buscaTrabajador(trabajador.getIdtrabajador());
		t.setEstado("INACTIVO");
		// Eliminar foto
		if (trabajador.getFoto() != null) {
			if (uploadFileService.delete(t.getFoto())) {
				flash.addFlashAttribute("info", "Foto " + t.getFoto() + " eliminada con exito!");
			}

		}
		mitrabajador.save(t);

		return "redirect:/trabajador/listar";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		List<Authority> authorities = miauthority.findAll();
		model.addAttribute("authorities", authorities);
		model.addAttribute("trabajador", mitrabajador.findById(id).orElse(null));
		model.addAttribute("fragmento", 10);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios", usuarios);
		List<mDistrito> distritos = midistrito.findAll();
		model.addAttribute("distritos", distritos);
		return "seguridad/home";
	}

	@PostMapping("/editar/{id}")
	public String actualizarCliente(@ModelAttribute @Valid mTrabajador trabajador, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, @RequestParam("file") MultipartFile foto, RedirectAttributes flash) {
		boolean encontro=false;
		if (bindingResult.hasErrors()) {
			if (trabajador.getIdtrabajador() != null) {
				return "trabajador/editar_trabajador";
			}
			return "redirect:/trabajador/listar";
		}

		if (!foto.isEmpty()) {

			if (trabajador.getIdtrabajador() != null && trabajador.getIdtrabajador() > 0 && trabajador.getFoto() != null
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

		mTrabajador t = buscaTrabajador(trabajador.getIdtrabajador());
		List<mDistrito> distritos = midistrito.findAll();
		for (mDistrito distrito : distritos) {
			if (distrito.getNombre().equals(trabajador.getDistrito().getNombre())) {
				t.setDistrito(distrito);
				encontro=true;
			}
		}
		
		if (encontro == false) {
			redirectAttrs.addFlashAttribute("mensaje", "El distrito no existe o no pertenece a La Libertad")
					.addFlashAttribute("clase", "danger");
			return "redirect:/trabajador/editar/"+trabajador.getIdtrabajador();
		}

		t.setEmail(trabajador.getEmail());
		t.setTelefono(trabajador.getTelefono());
		t.setApellidos(trabajador.getApellidos().toUpperCase());
		t.setNombres(trabajador.getNombres().toUpperCase());
		t.setDireccion(trabajador.getDireccion().toUpperCase());
		t.setDni(trabajador.getDni());
		if (!foto.isEmpty()) {
			t.setFoto(trabajador.getFoto());
		}
		mitrabajador.save(t);

		Authority a = buscaAuthority(trabajador.getUser().getAuthority().getId());
		User u = buscaUser(t.getUser().getId());
		miuser.actualizarAuthority(a.getId(), u.getId());

		redirectAttrs.addFlashAttribute("mensaje", "Trabajador editado correctamente").addFlashAttribute("clase",
				"success");
		return "redirect:/trabajador/listar";
	}

	@GetMapping("/exportarPDF")
	public void ExportarClientesPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");

		DateFormat daterFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = daterFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Trabajadores_" + fechaActual + ".pdf";
		response.setHeader(cabecera, valor);
		List<mTrabajador> trabajadores = mitrabajador.findAll();
		TrabajadorReport exporter = new TrabajadorReport(trabajadores);
		exporter.exportar(response);
	}

	@GetMapping("/exportarEXCEL")
	public void ExportarClientesEXCEL(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octec-stream");

		DateFormat daterFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = daterFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Trabajadores_" + fechaActual + ".xlsx";
		response.setHeader(cabecera, valor);
		List<mTrabajador> trabajadores = mitrabajador.findAll();
		TrabajadorReport exporter = new TrabajadorReport(trabajadores);
		exporter.exportarExcel(response);
	}

	public mTrabajador buscaTrabajador(@PathVariable("idtrabajador") Integer idtrabajador) {

		return mitrabajador.findById(idtrabajador).get();
	}

	public User buscaUser(@PathVariable("id") Long id) {

		return miuser.findById(id).get();
	}

	public Authority buscaAuthority(@PathVariable("id") Long id) {

		return miauthority.findById(id).get();
	}

}

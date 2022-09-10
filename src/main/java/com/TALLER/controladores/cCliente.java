package com.TALLER.controladores;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.TALLER.interfaces.iCliente;
import com.TALLER.interfaces.iDistrito;
import com.TALLER.interfaces.iUser;
import com.TALLER.modelos.User;
import com.TALLER.modelos.mCliente;
import com.TALLER.modelos.mDistrito;
import com.TALLER.pagination.PageRender;
import com.TALLER.reports.ClienteReport;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("cliente")
public class cCliente {
	@Autowired
	private iCliente micliente;

	@Autowired
	private iDistrito midistrito;

	@Autowired
	private iUser miusuario;

	@GetMapping(value = "/registrar")
	public String agregarProducto(Model model) {
		model.addAttribute("cliente", new mCliente());
		model.addAttribute("fragmento", 6);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios", usuarios);
		List<mDistrito> distritos = midistrito.findAll();
		model.addAttribute("distritos", distritos);
		return "seguridad/home";
	}

	@PostMapping(value = "/registrar")
	public String guardarProducto(@ModelAttribute @Valid mCliente cliente, BindingResult bindingResult,
			RedirectAttributes redirectAttrs) {
		boolean encontro = false;
		if (bindingResult.hasErrors()) {
			return "cliente/registrar_cliente";
		}

		List<mCliente> clientes = micliente.findAll();

		for (int i = 0; i < clientes.size(); i++) {
			if (cliente.getDni().equalsIgnoreCase(clientes.get(i).getDni())) {
				redirectAttrs.addFlashAttribute("mensaje", "Ya existe un cliente con ese DNI")
						.addFlashAttribute("clase", "warning");
				return "redirect:/cliente/registrar";

			}
		}
		List<mDistrito> distritos = midistrito.findAll();
		for (mDistrito distrito : distritos) {
			if(distrito.getNombre().equals(cliente.getDistrito().getNombre())) {
				cliente.setDistrito(distrito);
				encontro=true;
			}
		}
		
		if (encontro==false) {
	        redirectAttrs
	                .addFlashAttribute("mensaje", "El distrito no existe o no pertenece a La Libertad")
	                .addFlashAttribute("clase", "danger");
	        return "redirect:/cliente/registrar";
	    }

		cliente.setApellidos(cliente.getApellidos().toUpperCase());
		cliente.setNombres(cliente.getNombres().toUpperCase());
		cliente.setDireccion(cliente.getDireccion().toUpperCase());
		
		micliente.save(cliente);
		redirectAttrs.addFlashAttribute("mensaje", "Cliente registrado correctamente").addFlashAttribute("clase",
				"success");

		return "redirect:/cliente/registrar";

	}

	@GetMapping("listar")
	public String listarCliente(@RequestParam(name="page",defaultValue = "0") int page,Model model) {
		Pageable pageRequest = PageRequest.of(page, 12);
		Page<mCliente> clientes = micliente.findAll(pageRequest);
		PageRender<mCliente> pageRender = new PageRender<>("/cliente/listar", clientes);
		model.addAttribute("clientes", clientes);
		model.addAttribute("fragmento", 4);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("page", pageRender);
		return "seguridad/home";
	}

	@PostMapping("/eliminar")
	public String eliminar(@ModelAttribute mCliente cliente, RedirectAttributes redirectAttrs) {
		redirectAttrs.addFlashAttribute("mensaje", "Eliminado correctamente").addFlashAttribute("clase", "warning");
		mCliente c = micliente.findById(cliente.getIdcliente()).get();
		c.setEstado("INACTIVO");
		micliente.save(c);
		return "redirect:/cliente/listar";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		model.addAttribute("cliente", micliente.findById(id).orElse(null));
		model.addAttribute("fragmento", 5);
		List<mDistrito> distritos = midistrito.findAll();
		model.addAttribute("distritos", distritos);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios", usuarios);
		return "seguridad/home";
	}

	@PostMapping("/editar/{id}")
	public String actualizarCliente(@ModelAttribute @Valid mCliente cliente, BindingResult bindingResult,
			RedirectAttributes redirectAttrs) {
		boolean encontro=false;
		if (bindingResult.hasErrors()) {
			if (cliente.getIdcliente() != null) {
				return "cliente/editar_cliente";
			}
			return "redirect:/cliente/editar/"+cliente.getIdcliente();
		}

		List<mDistrito> distritos = midistrito.findAll();
		for (mDistrito distrito : distritos) {
			if(distrito.getNombre().equals(cliente.getDistrito().getNombre())) {
				cliente.setDistrito(distrito);
				encontro=true;
			}
		}
		
		if (encontro==false) {
	        redirectAttrs
	                .addFlashAttribute("mensaje", "El distrito no existe o no pertenece a La Libertad")
	                .addFlashAttribute("clase", "danger");
	        return "redirect:/cliente/editar/"+cliente.getIdcliente();
	    }
		cliente.setApellidos(cliente.getApellidos().toUpperCase());
		cliente.setNombres(cliente.getNombres().toUpperCase());
		cliente.setDireccion(cliente.getDireccion().toUpperCase());
		micliente.save(cliente);
		redirectAttrs.addFlashAttribute("mensaje", "Cliente editado correctamente").addFlashAttribute("clase",
				"success");
		return "redirect:/cliente/listar";
	}
	
	@GetMapping("/exportarPDF")
	public void ExportarClientesPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		
		DateFormat daterFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = daterFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Clientes_"+fechaActual+".pdf";
		response.setHeader(cabecera, valor);
		List<mCliente> clientes = micliente.findAll();
		ClienteReport exporter = new ClienteReport(clientes);
		exporter.exportar(response);
	}
	
	@GetMapping("/exportarEXCEL")
	public void ExportarClientesEXCEL(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octec-stream");
		
		DateFormat daterFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = daterFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Clientes_"+fechaActual+".xlsx";
		response.setHeader(cabecera, valor);
		List<mCliente> clientes = micliente.findAll();
		ClienteReport exporter = new ClienteReport(clientes);
		exporter.exportarExcel(response);
	}

}

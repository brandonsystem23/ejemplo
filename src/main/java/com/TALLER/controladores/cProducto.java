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

import com.TALLER.interfaces.iCategoria;
import com.TALLER.interfaces.iProducto;
import com.TALLER.interfaces.iUser;
import com.TALLER.modelos.User;
import com.TALLER.modelos.mCategoria;
import com.TALLER.modelos.mProducto;
import com.TALLER.pagination.PageRender;
import com.TALLER.reports.ProductoReport;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("producto")
public class cProducto {
	@Autowired
	private iProducto miproducto;

	@Autowired
	private iCategoria micategoria;
	
	@Autowired
	private iUser miusuario;

	@GetMapping(value = "/registrar")
	public String agregarProducto(Model model) {
		model.addAttribute("producto", new mProducto());
		model.addAttribute("fragmento", 3);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios",usuarios);
		return "seguridad/home";
	}

	@PostMapping(value = "/registrar")
	public String guardarProducto(@ModelAttribute @Valid mProducto producto, BindingResult bindingResult,
			RedirectAttributes redirectAttrs) {
		if (bindingResult.hasErrors()) {
			return "producto/registrar_producto";
		}
		
		List<mProducto> productos = miproducto.findAll();
		
		for(int i=0; i< productos.size();i++) {
			if(producto.getNombre().equalsIgnoreCase(productos.get(i).getNombre())) {
				redirectAttrs.addFlashAttribute("mensaje", "Ya existe un producto con ese nombre")
				.addFlashAttribute("clase", "warning");
				return "redirect:/producto/registrar";
			}
		}

		
		mCategoria cat =  buscar(producto.getCategoria().getIdcategoria());
		producto.setCategoria(cat);
		producto.setNombre(producto.getNombre().toUpperCase());
		producto.fueraStock();
		miproducto.save(producto);
		redirectAttrs.addFlashAttribute("mensaje", "Producto registrado correctamente").addFlashAttribute("clase", "success");
		
		return "redirect:/producto/registrar";
		
	}

	@GetMapping("listar/{modo}")
	public String listarProducto(@RequestParam(name="page",defaultValue = "0") int page,Model model,@PathVariable Integer modo) {
		Pageable pageRequest = PageRequest.of(page, 12);
		Page<mProducto> productos;
		if(modo==1) {
			productos = miproducto.findAll(pageRequest);
			model.addAttribute("modo", 1);
		}
		else if(modo==2) {
			productos = miproducto.findByCategoria(micategoria.findById(1).get(),pageRequest);
			model.addAttribute("modo", 2);
		}
		else {
			productos = miproducto.findByCategoria(micategoria.findById(2).get(),pageRequest);
			model.addAttribute("modo", 3);
		}
		PageRender<mProducto> pageRender = new PageRender<>("/producto/listar", productos);
		for(mProducto p:productos) {
			p.fueraStock();
			miproducto.save(p);
		}
		model.addAttribute("productos", productos);
		model.addAttribute("fragmento", 1);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios",usuarios);
		model.addAttribute("page", pageRender);
		return "seguridad/home";
	}

	@PostMapping("/eliminar")
	public String eliminar(@ModelAttribute mProducto producto, RedirectAttributes redirectAttrs) {
		redirectAttrs.addFlashAttribute("mensaje", "Eliminado correctamente").addFlashAttribute("clase", "warning");
		mProducto p = buscarProducto(producto.getIdproducto());
		p.setEstado("INACTIVO");
		miproducto.save(p);
		return "redirect:/producto/listar/1";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		model.addAttribute("producto", miproducto.findById(id).orElse(null));
		model.addAttribute("fragmento", 2);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios",usuarios);
		return "seguridad/home";
	}

	@PostMapping("/editar/{id}")
	public String actualizarProducto(@ModelAttribute @Valid mProducto producto, BindingResult bindingResult,
			RedirectAttributes redirectAttrs) {
		if (bindingResult.hasErrors()) {
			if (producto.getIdproducto() != null) {
				return "producto/editar_producto";
			}
			return "redirect:/producto/listar/1";
		}
		producto.setNombre(producto.getNombre().toUpperCase());
		producto.fueraStock();
		miproducto.save(producto);
		
		redirectAttrs.addFlashAttribute("mensaje", "Producto editado correctamente").addFlashAttribute("clase", "success");
		return "redirect:/producto/listar/1";
	}
	
	@GetMapping("/exportarPDF")
	public void ExportarClientesPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		
		DateFormat daterFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = daterFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Productos_"+fechaActual+".pdf";
		response.setHeader(cabecera, valor);
		List<mProducto> productos = miproducto.findAll();
		ProductoReport exporter = new ProductoReport(productos);
		exporter.exportar(response);
	}
	
	@GetMapping("/exportarEXCEL")
	public void ExportarClientesEXCEL(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octec-stream");
		
		DateFormat daterFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = daterFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Productos_"+fechaActual+".xlsx";
		response.setHeader(cabecera, valor);
		List<mProducto> productos = miproducto.findAll();
		ProductoReport exporter = new ProductoReport(productos);
		exporter.exportarExcel(response);
	}


	public mCategoria buscar(@PathVariable("idCategoria") Integer idCategoria) {

		return micategoria.findById(idCategoria).get();
	}
	
	public mProducto buscarProducto(@PathVariable("id") Integer id) {

		return miproducto.findById(id).get();
	}

}

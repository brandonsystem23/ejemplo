package com.TALLER.controladores;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.TALLER.interfaces.iCliente;
import com.TALLER.interfaces.iDetallePedido;
import com.TALLER.interfaces.iPedido;
import com.TALLER.interfaces.iProducto;
import com.TALLER.interfaces.iTrabajador;
import com.TALLER.interfaces.iUser;
import com.TALLER.modelos.User;
import com.TALLER.modelos.mCliente;
import com.TALLER.modelos.mDetallePedido;
import com.TALLER.modelos.mPedido;
import com.TALLER.modelos.mProducto;
import com.TALLER.modelos.mTrabajador;
import com.TALLER.pagination.PageRender;
import com.TALLER.reports.BoletaReport;
import com.TALLER.reports.PedidoReport;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("venta")
@SessionAttributes("venta")
public class cVentas {
	
	@Autowired
	private iProducto miproducto;
	
	@Autowired
	private iCliente micliente;
	
	@Autowired
	private iTrabajador mitrabajador;
	
	@Autowired
	private iPedido mipedido;
	
	@Autowired
	private iDetallePedido midetallepedido;
	
	@Autowired
	private iUser miusuario;
	
	private ArrayList<mDetallePedido> obtenerCarrito(HttpServletRequest request) {
	    @SuppressWarnings("unchecked")
		ArrayList<mDetallePedido> carrito = (ArrayList<mDetallePedido>) request.getSession().getAttribute("carrito");
	    if (carrito == null) {
	        carrito = new ArrayList<>();
	    }
	    return carrito;
	}

	private void guardarCarrito(ArrayList<mDetallePedido> carrito, HttpServletRequest request) {
	    request.getSession().setAttribute("carrito", carrito);
	}
	
	//Metodo para invertir una lista
	public List<mPedido> revlist(List<mPedido> list)
    {
		List<mPedido> lista = new ArrayList<>();
		
		for(int i=list.size()-1;i>=0;i--) {
			lista.add(list.get(i));
		}
		System.out.println(lista.size());
		return lista;
    }
	
	@GetMapping("listar/{modo}")
	public String listarPedidos(@RequestParam(name="page",defaultValue = "0") int page,Model model,@PathVariable Integer modo) {
		Pageable pageRequest = PageRequest.of(page, 12);
		Page<mPedido> pedidos;
		if(modo==1) {
			pedidos = mipedido.findAll(pageRequest);
			model.addAttribute("modo", 1);
		}
		else if(modo==2) {
			pedidos = mipedido.findByEstado("REGISTRADO",pageRequest);
			model.addAttribute("modo", 2);
		}
		else {
			pedidos = mipedido.findByEstado("FACTURADO",pageRequest);
			model.addAttribute("modo", 3);
		}
		
		PageRender<mPedido> pageRender = new PageRender<>("/venta/listar", pedidos);
		model.addAttribute("pedidos", pedidos);
		model.addAttribute("fragmento", 8);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios",usuarios);
		model.addAttribute("page", pageRender);
		return "seguridad/home";
	}
	
	@GetMapping("/pagar")
	public String facturarPedidos(@RequestParam(name="page",defaultValue = "0") int page,Model model) {
		Pageable pageRequest = PageRequest.of(page, 12);
		Page<mPedido> pedidos = mipedido.findByEstado("REGISTRADO",pageRequest);
		PageRender<mPedido> pageRender = new PageRender<>("/venta/pagar", pedidos);
		model.addAttribute("pedidos", pedidos);
		model.addAttribute("fragmento", 12);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios",usuarios);
		model.addAttribute("page", pageRender);
		return "seguridad/home";
	}
	
	@GetMapping(value = "/registrar")
	public String interfazVender(Model model, HttpServletRequest request) {
		List<mProducto> productos	= miproducto.findAll();
		List<mCliente> clientes	= micliente.findAll();
		List<mTrabajador> trabajadores	= mitrabajador.findAll();
	    model.addAttribute("productos", productos);
	    model.addAttribute("clientes", clientes);
	    model.addAttribute("trabajadores", trabajadores);
	    Double total = 0.0;
	    ArrayList<mDetallePedido> carrito = this.obtenerCarrito(request);
	    for (mDetallePedido p: carrito) total += p.getTotal();
	    model.addAttribute("total", total);
	    model.addAttribute("fragmento", 7);
	    List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios",usuarios);
	    return "seguridad/home";
	}
	
	@PostMapping(value = "/registrar")
	public String agregarAlCarrito(@RequestParam String nombreproducto, @RequestParam Integer cantidad,HttpServletRequest request, RedirectAttributes redirectAttrs,SessionStatus status) {
		mProducto p = new mProducto();
		List<mProducto> productos = miproducto.findAll();
		for (mProducto producto : productos) {
			if(producto.getNombre().equals(nombreproducto)) {
				p=producto;
			}
		}
		
		if (p.getNombre()==null) {
	        redirectAttrs
	                .addFlashAttribute("mensaje", "El producto no existe")
	                .addFlashAttribute("clase", "danger");
	        return "redirect:/venta/registrar";
	    }
		
		ArrayList<mDetallePedido> carrito = this.obtenerCarrito(request);


	    if (p.sinExistencia()) {
	        redirectAttrs
	                .addFlashAttribute("mensaje", "El producto está agotado")
	                .addFlashAttribute("clase", "warning");
	        return "redirect:/venta/registrar";
	    }
	    
	    if (p.getStock()<cantidad) {
	        redirectAttrs
	                .addFlashAttribute("mensaje", "Stock insuficiente, el producto actualmente cuenta con "+p.getStock()+" unidades en stock")
	                .addFlashAttribute("clase", "warning");
	        return "redirect:/venta/registrar";
	    }
	    
	    for(mDetallePedido x:carrito) {
	    	if (nombreproducto.equals(x.getProducto().getNombre())) {
		        redirectAttrs
		                .addFlashAttribute("mensaje", "El producto ya se encuentra agregado al carrito")
		                .addFlashAttribute("clase", "warning");
		        return "redirect:/venta/registrar";
		    }
	    }
	    
	    carrito.add(new mDetallePedido(p.getPrecio(), cantidad, p, new mPedido()));
	    
	    this.guardarCarrito(carrito, request);
	    return "redirect:/venta/registrar";
	}
	
	@PostMapping(value = "/quitar/{indice}")
	public String quitarDelCarrito(@PathVariable int indice, HttpServletRequest request) {
	    ArrayList<mDetallePedido> carrito = this.obtenerCarrito(request);
	    if (carrito != null && carrito.size() > 0 && carrito.get(indice) != null) {
	        carrito.remove(indice);
	        this.guardarCarrito(carrito, request);
	    }
	    return "redirect:/venta/registrar";
	}
	
	
	private void limpiarCarrito(HttpServletRequest request) {
	    this.guardarCarrito(new ArrayList<>(), request);
	}

	@GetMapping(value = "/limpiar")
	public String cancelarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs) {
	    this.limpiarCarrito(request);
	    redirectAttrs
	            .addFlashAttribute("mensaje", "Pedido cancelado")
	            .addFlashAttribute("clase", "info");
	    return "redirect:/venta/registrar";
	}
	
	
	@PostMapping(value = "/terminar")
	public String terminarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs,@RequestParam String nombretrabajador,@RequestParam String nombrecliente,@RequestParam Date fecha,@RequestParam Double total) {
	    ArrayList<mDetallePedido> carrito = this.obtenerCarrito(request);
	    // Si no hay carrito o está vacío, regresamos inmediatamente
	    if (carrito == null || carrito.size() <= 0) {
	    	redirectAttrs
            .addFlashAttribute("mensaje", "Debe agregar al carrito algun producto para guardar el pedido")
            .addFlashAttribute("clase", "warning");
	    	return "redirect:/venta/registrar";
	    }
	    
	    //Creando pedido
	    mCliente c = new mCliente();
		List<mCliente> clientes = micliente.findAll();
		for (mCliente cliente : clientes) {
			String nombre = cliente.getNombres()+" "+cliente.getApellidos();
			if( nombre.equals(nombrecliente)) {
				c=cliente;
			}
		}
		

		if (c.getNombres()==null) {
	        redirectAttrs
	                .addFlashAttribute("mensaje", "El cliente no existe")
	                .addFlashAttribute("clase", "danger");
	        return "redirect:/venta/registrar";
	    }
		
	    mTrabajador t = new mTrabajador();
	    List<mTrabajador> trabajadores = mitrabajador.findAll();
		for (mTrabajador trabajador : trabajadores) {
			String nombre = trabajador.getNombres()+" "+trabajador.getApellidos();
			if( nombre.equals(nombretrabajador)) {
				t=trabajador;
			}
		}
		
		if (t.getNombres()==null) {
	        redirectAttrs
	                .addFlashAttribute("mensaje", "El repartidor no existe")
	                .addFlashAttribute("clase", "danger");
	        return "redirect:/venta/registrar";
	    }
	    
	    mPedido v = mipedido.save(new mPedido(fecha,c,t,total));
	    // Recorrer el carrito
	    for (mDetallePedido detallepedido : carrito) {
	        // Obtener el producto fresco desde la base de datos
	        mProducto p = miproducto.findById(detallepedido.getProducto().getIdproducto()).orElse(null);
	        if (p == null) continue; // Si es nulo o no existe, ignoramos el siguiente código con continue
	        // Le restamos existencia
	        p.restarStock(detallepedido.getCantidad());
	        // Lo guardamos con la existencia ya restada
	        miproducto.save(p);
	        // Creamos un nuevo producto que será el que se guarda junto con la venta
	        mDetallePedido productoVendido = new mDetallePedido(p.getIdproducto(),v.getIdpedido(),detallepedido.getPrecio(),detallepedido.getCantidad(),p,v);
	        // Y lo guardamos
	        midetallepedido.save(productoVendido);
	    }

	    // Al final limpiamos el carrito
	    this.limpiarCarrito(request);
	    // e indicamos una venta exitosa
	    redirectAttrs
	            .addFlashAttribute("mensaje", "Pedido registrado correctamente")
	            .addFlashAttribute("clase", "success");
	    return "redirect:/venta/registrar";
	}
	
	@PostMapping("/eliminar")
	public String eliminar(@ModelAttribute mPedido pedido, RedirectAttributes redirectAttrs) {
		redirectAttrs.addFlashAttribute("mensaje", "Pedido eliminado correctamente").addFlashAttribute("clase", "warning");
		//Eliminado todos los detalles
		midetallepedido.eliminarDetalle(pedido.getIdpedido());
		
		
		//Eliminado el pedido
		mipedido.deleteById(pedido.getIdpedido());
		return "redirect:/venta/listar/1";
	}
	
	@PostMapping("/facturar")
	public String facturar(@ModelAttribute mPedido pedido, RedirectAttributes redirectAttrs) {
		redirectAttrs.addFlashAttribute("mensaje", "Pedido facturado correctamente").addFlashAttribute("clase", "success");
		//Facturar pedido
		mPedido p = buscaPedido(pedido.getIdpedido());
		p.setEstado("FACTURADO");
		mipedido.save(p);
		return "redirect:/venta/pagar";
	}
	
	@GetMapping("/exportarPDF")
	public void ExportarClientesPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		
		DateFormat daterFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		long miliseconds = System.currentTimeMillis();
		String fechaActual = daterFormatter.format(new Date(miliseconds));
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Pedidos_"+fechaActual+".pdf";
		response.setHeader(cabecera, valor);
		List<mPedido> pedidos = mipedido.findAll();
		PedidoReport exporter = new PedidoReport(pedidos);
		exporter.exportar(response);
	}
	
	@GetMapping("/exportarEXCEL")
	public void ExportarClientesEXCEL(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octec-stream");
		
		DateFormat daterFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		long miliseconds = System.currentTimeMillis();
		String fechaActual = daterFormatter.format(new Date(miliseconds));
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Pedidos_"+fechaActual+".xlsx";
		response.setHeader(cabecera, valor);
		List<mPedido> pedidos = mipedido.findAll();
		PedidoReport exporter = new PedidoReport(pedidos);
		exporter.exportarExcel(response);
	}
	
	@GetMapping("/boleta/{id}")
	public void boletaPDF(HttpServletResponse response,@PathVariable int id) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		
		DateFormat daterFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		long miliseconds = System.currentTimeMillis();
		String fechaActual = daterFormatter.format(new Date(miliseconds));
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Boleta_"+fechaActual+".pdf";
		response.setHeader(cabecera, valor);
		mPedido pedido = mipedido.findById(id).get();
		BoletaReport exporter = new BoletaReport(pedido);
		exporter.exportar(response);
	}
	
	public mProducto buscar(@PathVariable("idproducto") Integer idproducto) {

		return miproducto.findById(idproducto).get();
	}
	
	public mCliente buscarCliente(@PathVariable("idcliente") Integer idcliente) {

		return micliente.findById(idcliente).get();
	}
	
	public mTrabajador buscarTrabajador(@PathVariable("idtrabajador") Integer idtrabajador) {

		return mitrabajador.findById(idtrabajador).get();
	}
	
	public mPedido buscaPedido(@PathVariable("idpedido") Integer idpedido) {

		return mipedido.findById(idpedido).get();
	}
	
}

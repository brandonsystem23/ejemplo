package com.TALLER.controladores;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.TALLER.interfaces.iCliente;
import com.TALLER.interfaces.iPedido;
import com.TALLER.interfaces.iProducto;
import com.TALLER.interfaces.iUser;
import com.TALLER.modelos.User;
import com.TALLER.modelos.mCliente;
import com.TALLER.modelos.mPedido;
import com.TALLER.modelos.mProducto;

@Controller
@RequestMapping("reporte")
public class GraphController {
	@Autowired
	private iProducto miproducto;
	
	@Autowired
	private iPedido mipedido;
	
	@Autowired
	private iUser miusuario;
	
	@Autowired
	private iCliente micliente;
	
	
	//REPORTE DE STOCK DE PRODUCTOS
	@GetMapping("/stock")
	public String barGraph(Model model) {
		Map<String, Integer> surveyMap = new LinkedHashMap<>();
		List <mProducto> productos = miproducto.findAll();
		for(mProducto producto: productos) {
			if(producto.getEstado().equals("ACTIVO")) {
			surveyMap.put(producto.getNombre(), producto.getStock());
			}
		}

		model.addAttribute("surveyMap", surveyMap);
		model.addAttribute("fragmento", 14);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios",usuarios);
		return "seguridad/home";
	}
	
	
	@GetMapping("/ventas/{fecha}")
	public String ventasDiarias(@PathVariable Date fecha,Model model) {
		Double ventatotal=0.0;
		List <mPedido> pedidos = mipedido.findAll();
		List <mPedido> pedidosfiltrados = new ArrayList<>();
		for(mPedido pedido: pedidos) {
			if(pedido.getEstado().equals("FACTURADO") && pedido.getFecha().toString().equals(fecha.toString())) {
					pedidosfiltrados.add(pedido);
					ventatotal += pedido.getMonto();
			}
		}
		
		model.addAttribute("venta", ventatotal);
		model.addAttribute("fecha", fecha);
		model.addAttribute("fragmento", 15);
		model.addAttribute("pedidosfiltrados",pedidosfiltrados);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios",usuarios);
		return "seguridad/home";
	}
	
	
	@GetMapping("/clientes/{mes}")
	public String clientesMensual(@PathVariable String mes,Model model) {

		List <mCliente> clientes = micliente.findAll();
		List <mCliente> clientesfiltrados = new ArrayList<>();
		for(mCliente cliente: clientes) {
			if(cliente.getEstado().equals("ACTIVO") && cliente.getFecharegistro().toString().substring(0,7).equals(mes)) {
					clientesfiltrados.add(cliente);
			}
		}
		
		model.addAttribute("mes", mes);
		model.addAttribute("fragmento", 16);
		model.addAttribute("clientesfiltrados",clientesfiltrados);
		List<User> usuarios = miusuario.findAll();
		model.addAttribute("usuarios",usuarios);
		return "seguridad/home";
	}
	
}

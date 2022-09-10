package com.TALLER.reports;

import java.awt.Color;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.TALLER.modelos.mDetallePedido;
import com.TALLER.modelos.mPedido;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class BoletaReport {
private mPedido pedido;
	



	public BoletaReport(mPedido pedido) {
		this.pedido = pedido;
	}
	
	private void escribirCabeceraDeLaTablaCliente(PdfPTable tabla){
		   PdfPCell celda = new PdfPCell();
		
		   celda.setBackgroundColor(Color.BLACK);
		   celda.setPadding(3);
		   celda.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
		   Font fuente=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		   fuente.setColor(Color.WHITE);
		   fuente.setSize(12);
		      
		   celda.setPhrase(new Phrase("CLIENTE",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("DNI",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("DISTRITO",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("DIRECCIÓN",fuente));
		   tabla.addCell(celda);
		   

	}
	
	
	private void escribirDatosDeLaTablaCliente(PdfPTable tabla){
		Font fuente=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		   fuente.setColor(Color.BLACK);
		   fuente.setSize(10);
		   PdfPCell celda = new PdfPCell();
		   celda.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
  	

				   	celda.setPhrase(new Phrase(pedido.getCliente().getNombres()+" "+pedido.getCliente().getApellidos(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(pedido.getCliente().getDni(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(pedido.getCliente().getDistrito().getNombre(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(pedido.getCliente().getDireccion(),fuente));
				   	tabla.addCell(celda);
   
	 }
	
	private void escribirCabeceraDeLaTablaPedido(PdfPTable tabla){
		   PdfPCell celda = new PdfPCell();
		
		   celda.setBackgroundColor(Color.BLACK);
		   celda.setPadding(3);
		   celda.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
		   Font fuente=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		   fuente.setColor(Color.WHITE);
		   fuente.setSize(12);
		      
		   celda.setPhrase(new Phrase("PRODUCTO",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("CANTIDAD",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("PRECIO",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("SUB TOTAL",fuente));
		   tabla.addCell(celda);
		   

	}
	
	
	private void escribirDatosDeLaTablaPedido(PdfPTable tabla){
		Font fuente=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		   fuente.setColor(Color.BLACK);
		   fuente.setSize(10);
		   PdfPCell celda = new PdfPCell();
		   celda.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
	

		   for(mDetallePedido detalle:pedido.getDetallepedido()){
   	
				    celda.setPhrase(new Phrase(detalle.getProducto().getNombre(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(detalle.getCantidad().toString(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase("S/. "+detalle.getPrecio().toString(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase("S/. "+detalle.getTotal().toString(),fuente));
				   	tabla.addCell(celda);   
		        
		   }

	 }
	
	public void exportar(HttpServletResponse response) throws DocumentException, IOException{
        Document documento = new Document(PageSize.A4);
        
		PdfWriter.getInstance(documento,response.getOutputStream());
		documento.open();
		Font fuenteTitulo=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		fuenteTitulo.setColor(Color.BLACK);
		fuenteTitulo.setSize(16);
		
		Font fuenteItem=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		fuenteItem.setColor(Color.BLACK);
		fuenteItem.setSize(12);
		
		Font fuenteTotal=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fuenteTotal.setColor(Color.RED);
		fuenteTotal.setSize(16);
		
		Paragraph titulo=new Paragraph("BOLETA - CÓD. PEDIDO: "+"P00"+pedido.getIdpedido(),fuenteTitulo);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		Image imagen= Image.getInstance("src/main/resources/static/images/app/encabezado.png");
		imagen.scaleAbsolute(150, 50);
		imagen.setAlignment(Paragraph.ALIGN_CENTER);
		imagen.setSpacingBefore(0);
		imagen.setSpacingAfter(0);
        documento.add(imagen);
		documento.add(titulo);
		
		Paragraph tituloFecha =new Paragraph("FECHA: "+pedido.getFecha().toString(),fuenteTitulo);
		tituloFecha.setSpacingBefore(5);
		tituloFecha.setSpacingAfter(5);
		documento.add(tituloFecha);
		
		Paragraph tituloRepartidor =new Paragraph("REPARTIDOR: "+pedido.getTrabajador().getNombres()+" "+pedido.getTrabajador().getApellidos(),fuenteTitulo);
		tituloRepartidor.setSpacingBefore(5);
		tituloRepartidor.setSpacingAfter(5);
		documento.add(tituloRepartidor);
		
		Paragraph titulocliente =new Paragraph("DATOS DEL CLIENTE",fuenteTitulo);
		titulocliente.setSpacingBefore(5);
		titulocliente.setSpacingAfter(5);
		documento.add(titulocliente);
		
		PdfPTable tabla=new PdfPTable(4);
		tabla.setWidthPercentage(90);
		tabla.setSpacingBefore(10);
		tabla.setWidths(new float[]{9f,4f,8f,9f});
		tabla.setWidthPercentage(90);
		tabla.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
		escribirCabeceraDeLaTablaCliente(tabla);
		escribirDatosDeLaTablaCliente(tabla);
		documento.add(tabla);
		
		Paragraph titulopedido =new Paragraph("DETALLE DEL PEDIDO",fuenteTitulo);
		titulopedido.setSpacingBefore(5);
		titulopedido.setSpacingAfter(5);
		documento.add(titulopedido);
		
		PdfPTable tabla2=new PdfPTable(4);
		tabla2.setWidthPercentage(90);
		tabla2.setSpacingBefore(10);
		tabla2.setWidths(new float[]{9f,4f,4f,4f});
		tabla2.setWidthPercentage(90);
		tabla2.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
		escribirCabeceraDeLaTablaPedido(tabla2);
		escribirDatosDeLaTablaPedido(tabla2);
		documento.add(tabla2);
		
		Paragraph tituloTotal =new Paragraph("TOTAL: S/. "+pedido.getMonto(),fuenteTotal);
		tituloTotal.setSpacingBefore(8);
		tituloTotal.setSpacingAfter(5);
		tituloTotal.setAlignment(Paragraph.ALIGN_RIGHT);
		documento.add(tituloTotal);
		
		documento.close();
		         
	}
	
}

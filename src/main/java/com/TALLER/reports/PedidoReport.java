package com.TALLER.reports;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

public class PedidoReport {
	
private List<mPedido> listapedido;
	
	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	public PedidoReport(List<mPedido> listapedidos) {
		this.listapedido = listapedidos;
		this.libro = new XSSFWorkbook();
		this.hoja = libro.createSheet("Pedidos");
	}
	
	private void escribirCabeceraDeLaTabla(PdfPTable tabla){
		   PdfPCell celda = new PdfPCell();
		
		   celda.setBackgroundColor(Color.BLACK);
		   celda.setPadding(3);
		   celda.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
		   Font fuente=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		   fuente.setColor(Color.WHITE);
		   fuente.setSize(7);
		   
		   celda.setPhrase(new Phrase("CÓDIGO",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("CLIENTE",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("DISTRITO",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("DIRECCIÓN",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("FECHA",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("TOTAL",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("REPARTIDOR",fuente));
		   tabla.addCell(celda);
	}
	
	
	private void escribirDatosDeLaTabla(PdfPTable tabla){
		Font fuente=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		   fuente.setColor(Color.BLACK);
		   fuente.setSize(7);
		   PdfPCell celda = new PdfPCell();
		   celda.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
		   for(mPedido pedido:listapedido){
			   if(pedido.getEstado().equals("FACTURADO")) {
				   	
				    celda.setPhrase(new Phrase("P00"+pedido.getIdpedido().toString(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(pedido.getCliente().getNombres()+" "+pedido.getCliente().getApellidos(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(pedido.getCliente().getDistrito().getNombre(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(pedido.getCliente().getDireccion(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(pedido.getFecha().toString(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(pedido.getMonto().toString(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(pedido.getTrabajador().getNombres()+" "+pedido.getTrabajador().getApellidos(),fuente));
				   	tabla.addCell(celda); 
			   }
		        
		   }
	 }
	
	public void exportar(HttpServletResponse response) throws DocumentException, IOException{
        Document documento = new Document(PageSize.A4);
        
		PdfWriter.getInstance(documento,response.getOutputStream());
		documento.open();
		Font fuente=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		fuente.setColor(Color.BLACK);
		fuente.setSize(16);
		Paragraph titulo=new Paragraph("LISTA DE PEDIDOS",fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		Image imagen= Image.getInstance("src/main/resources/static/images/app/encabezado.png");
		imagen.scaleAbsolute(150, 50);
		imagen.setAlignment(Paragraph.ALIGN_CENTER);
		imagen.setSpacingBefore(0);
		imagen.setSpacingAfter(0);
        documento.add(imagen);
		documento.add(titulo);
		
		
		
		PdfPTable tabla=new PdfPTable(7);
		tabla.setWidthPercentage(100);
		tabla.setSpacingBefore(10);
		tabla.setWidths(new float[]{2.5f,5.5f,4f,4f,2.8f,2.5f,5.5f});
		tabla.setWidthPercentage(110);
		tabla.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);
		
		documento.add(tabla);
		documento.close();
		         
	}
	
	
	
	private void escribirCabeceraDeTablaExcel(){
	    Row fila = hoja.createRow(0);
	    CellStyle estilo = libro.createCellStyle();
	    XSSFFont fuente = libro.createFont();
	    fuente.setBold(true);
	    fuente.setFontHeight(16);
	    estilo.setFont(fuente);
	    
	    Cell celda = fila.createCell(0);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("CÓDIGO");
	    
	    celda = fila.createCell(1);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("CLIENTE");
	    
	    celda = fila.createCell(2);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("DISTRITO");
	    
	    celda = fila.createCell(3);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("DIRECCIÓN");
	    
	    celda = fila.createCell(4);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("FECHA");
	    
	    celda = fila.createCell(5);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("TOTAL");
	    
	    celda = fila.createCell(6);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("REPARTIDOR");

	}
	
	private void escribirDatosDeLaTablaExcel(){
	    int numeroFilas=1;
	    CellStyle estilo = libro.createCellStyle();
	    XSSFFont fuente = libro.createFont();
	    fuente.setFontHeight(14);
	    estilo.setFont(fuente);
	    
	    for(mPedido pedido:listapedido) {
	    	if(pedido.getEstado().equals("FACTURADO")) {
	    		Row fila = hoja.createRow(numeroFilas++);
	    		 Cell celda = fila.createCell(0);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue("P00"+pedido.getIdpedido());
	    		 hoja.autoSizeColumn(0);
	    		 
	    		 celda = fila.createCell(1);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(pedido.getCliente().getNombres()+" "+pedido.getCliente().getApellidos());
	    		 hoja.autoSizeColumn(1);
	    		 
	    		 celda = fila.createCell(2);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(pedido.getCliente().getDistrito().getNombre());
	    		 hoja.autoSizeColumn(2);
	    		 
	    		 celda = fila.createCell(3);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(pedido.getCliente().getDireccion());
	    		 hoja.autoSizeColumn(3);
	    		 
	    		 celda = fila.createCell(4);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(pedido.getFecha().toString());
	    		 hoja.autoSizeColumn(4);
	    		 
	    		 celda = fila.createCell(5);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(pedido.getMonto());
	    		 hoja.autoSizeColumn(5);
	    		 
	    		 celda = fila.createCell(6);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(pedido.getTrabajador().getNombres()+" "+pedido.getTrabajador().getApellidos());
	    		 hoja.autoSizeColumn(6);
	    		 
	    		
	    	}
	    }
	
	}
	
	public void exportarExcel(HttpServletResponse response) throws IOException {
		escribirCabeceraDeTablaExcel();
		escribirDatosDeLaTablaExcel();
		
		ServletOutputStream outputStream= response.getOutputStream();
		libro.write(outputStream);
		libro.close();
		outputStream.close();
	}
	
}

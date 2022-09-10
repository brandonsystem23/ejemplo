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


import com.TALLER.modelos.mProducto;
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

public class ProductoReport {
private List<mProducto> listaproducto;
	
	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	public ProductoReport(List<mProducto> listaproductos) {
		this.listaproducto = listaproductos;
		this.libro = new XSSFWorkbook();
		this.hoja = libro.createSheet("Productos");
	}
	
	private void escribirCabeceraDeLaTabla(PdfPTable tabla){
		   PdfPCell celda = new PdfPCell();
		
		   celda.setBackgroundColor(Color.BLACK);
		   celda.setPadding(3);
		   celda.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
		   Font fuente=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		   fuente.setColor(Color.WHITE);
		   fuente.setSize(10);
		   
		   celda.setPhrase(new Phrase("CÓDIGO",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("PRODUCTO",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("CATEGORÍA",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("PRECIO",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("STOCK",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("ESTADO",fuente));
		   tabla.addCell(celda);
		   
		   
	}
	
	
	private void escribirDatosDeLaTabla(PdfPTable tabla){
		Font fuente=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		   fuente.setColor(Color.BLACK);
		   fuente.setSize(10);
		   PdfPCell celda = new PdfPCell();
		   celda.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
		   for(mProducto producto:listaproducto){
			   if(producto.getEstado().equals("ACTIVO")) {
				    celda.setPhrase(new Phrase(producto.getIdproducto().toString(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(producto.getNombre(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(producto.getCategoria().getDescripcion(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(producto.getPrecio().toString(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(producto.getStock().toString(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(producto.getEstadostock(),fuente));
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
		Paragraph titulo=new Paragraph("LISTA DE PRODUCTOS",fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		Image imagen= Image.getInstance("src/main/resources/static/images/app/encabezado.png");
		imagen.scaleAbsolute(150, 50);
		imagen.setAlignment(Paragraph.ALIGN_CENTER);
		imagen.setSpacingBefore(0);
		imagen.setSpacingAfter(0);
        documento.add(imagen);
		documento.add(titulo);
		
		
		
		PdfPTable tabla=new PdfPTable(6);
		tabla.setWidthPercentage(100);
		tabla.setSpacingBefore(10);
		tabla.setWidths(new float[]{3f,5f,5f,3f,3f,5f});
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
	    celda.setCellValue("PRODUCTO");
	    
	    celda = fila.createCell(2);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("CATEGORÍA");
	    
	    celda = fila.createCell(3);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("PRECIO");
	    
	    celda = fila.createCell(4);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("STOCK");
	    
	    celda = fila.createCell(5);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("ESTADO");

	}
	
	private void escribirDatosDeLaTablaExcel(){
	    int numeroFilas=1;
	    CellStyle estilo = libro.createCellStyle();
	    XSSFFont fuente = libro.createFont();
	    fuente.setFontHeight(14);
	    estilo.setFont(fuente);
	    
	    for(mProducto producto:listaproducto) {
	    	if(producto.getEstado().equals("ACTIVO")) {
	    		Row fila = hoja.createRow(numeroFilas++);
	    		 Cell celda = fila.createCell(0);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(producto.getIdproducto());
	    		 hoja.autoSizeColumn(0);
	    		 
	    		 celda = fila.createCell(1);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(producto.getNombre());
	    		 hoja.autoSizeColumn(1);
	    		 
	    		 celda = fila.createCell(2);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(producto.getCategoria().getDescripcion());
	    		 hoja.autoSizeColumn(2);
	    		 
	    		 celda = fila.createCell(3);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(producto.getPrecio());
	    		 hoja.autoSizeColumn(3);
	    		 
	    		 celda = fila.createCell(4);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(producto.getStock());
	    		 hoja.autoSizeColumn(4);
	    		 
	    		 celda = fila.createCell(5);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(producto.getEstadostock());
	    		 hoja.autoSizeColumn(5);
	    		 
	    		
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

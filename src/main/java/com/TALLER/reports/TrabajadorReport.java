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
import com.TALLER.modelos.mTrabajador;
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

public class TrabajadorReport {

private List<mTrabajador> listatrabajador;
	
	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	public TrabajadorReport(List<mTrabajador> listatrabajadores) {
		this.listatrabajador = listatrabajadores;
		this.libro = new XSSFWorkbook();
		this.hoja = libro.createSheet("Trabajadores");
	}
	
	private void escribirCabeceraDeLaTabla(PdfPTable tabla){
		   PdfPCell celda = new PdfPCell();
		
		   celda.setBackgroundColor(Color.BLACK);
		   celda.setPadding(3);
		   celda.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
		   Font fuente=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		   fuente.setColor(Color.WHITE);
		   fuente.setSize(7);
		   
		   celda.setPhrase(new Phrase("TRABAJADOR",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("DNI",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("DISTRITO",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("DIRECCIÓN",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("CARGO",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("TELEFONO",fuente));
		   tabla.addCell(celda);
		   
		   celda.setPhrase(new Phrase("EMAIL",fuente));
		   tabla.addCell(celda);
	}
	
	
	private void escribirDatosDeLaTabla(PdfPTable tabla){
		Font fuente=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		   fuente.setColor(Color.BLACK);
		   fuente.setSize(6);
		   PdfPCell celda = new PdfPCell();
		   celda.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
		   for(mTrabajador trabajador:listatrabajador){
			   if(trabajador.getEstado().equals("ACTIVO")) {
				   	
				    celda.setPhrase(new Phrase(trabajador.getNombres()+" "+trabajador.getApellidos(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(trabajador.getDni(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(trabajador.getDistrito().getNombre(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(trabajador.getDireccion(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(trabajador.getUser().getAuthority().getAuthority().substring(5 ,trabajador.getUser().getAuthority().getAuthority().length()),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(trabajador.getTelefono(),fuente));
				   	tabla.addCell(celda);
				   	celda.setPhrase(new Phrase(trabajador.getEmail(),fuente));
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
		Paragraph titulo=new Paragraph("LISTA DE TRABAJADORES",fuente);
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
		tabla.setWidths(new float[]{6f,2f,3f,6f,2.8f,2.8f,5.5f});
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
	    celda.setCellValue("TRABAJADOR");
	    
	    celda = fila.createCell(1);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("DNI");
	    
	    celda = fila.createCell(2);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("DISTRITO");
	    
	    celda = fila.createCell(3);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("DIRECCIÓN");
	    
	    celda = fila.createCell(4);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("CARGO");
	    
	    celda = fila.createCell(5);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("TELEFONO");
	    
	    celda = fila.createCell(6);
	    celda.setCellStyle(estilo);
	    celda.setCellValue("EMAIL");

	}
	
	private void escribirDatosDeLaTablaExcel(){
	    int numeroFilas=1;
	    CellStyle estilo = libro.createCellStyle();
	    XSSFFont fuente = libro.createFont();
	    fuente.setFontHeight(14);
	    estilo.setFont(fuente);
	    
	    for(mTrabajador trabajador:listatrabajador) {
	    	if(trabajador.getEstado().equals("ACTIVO")) {
	    		Row fila = hoja.createRow(numeroFilas++);
	    		 Cell celda = fila.createCell(0);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(trabajador.getNombres()+" "+trabajador.getApellidos());
	    		 hoja.autoSizeColumn(0);
	    		 
	    		 celda = fila.createCell(1);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(trabajador.getDni());
	    		 hoja.autoSizeColumn(1);
	    		 
	    		 celda = fila.createCell(2);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(trabajador.getDistrito().getNombre());
	    		 hoja.autoSizeColumn(2);
	    		 
	    		 celda = fila.createCell(3);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(trabajador.getDireccion());
	    		 hoja.autoSizeColumn(3);
	    		 
	    		 celda = fila.createCell(4);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(trabajador.getUser().getAuthority().getAuthority().substring(5 ,trabajador.getUser().getAuthority().getAuthority().length()));
	    		 hoja.autoSizeColumn(4);
	    		 
	    		 celda = fila.createCell(5);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(trabajador.getTelefono());
	    		 hoja.autoSizeColumn(5);
	    		 
	    		 celda = fila.createCell(6);
	    		 celda.setCellStyle(estilo);
	    		 celda.setCellValue(trabajador.getEmail());
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

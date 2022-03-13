package aPack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.awt.*;

public class Forceout2 {
	private String requestor;
	private File forceout;	//The file to copy to POPDEP
	private String filename;
	private File log;
	private String logSheet;
	
	private File popLocation;
	private File archLocation;
	
	private final int numCol=5;
	private int numRow;
	
	
	public Forceout2() {
		requestor="N/A";
		String filepath = "C:\\";
		filename = pullFileNamefromFilepath(filepath);
		forceout = new File(filepath);
		this.log = new File("Path\\Log location");
		logSheet = determineSheet();
		archLocation = new File("Path\\Archive Location");
		popLocation = new File("Path\\PopDepLocation");
	}
	
	public Forceout2(String name, String filepath) {
		requestor=name;
		forceout = new File(filepath);
		filename= pullFileNamefromFilepath(filepath);
		this.log= new File("C:\\POPDEP_LOG_2021.xls");
		logSheet = determineSheet();
		popLocation = new File("C:\\");
		archLocation = new File("C:\\BackUp");
	}


	public Forceout2(String name, String filepath, String logpath, String pop, String arch) {
		requestor = name;
		forceout = new File(filepath);
		filename= pullFileNamefromFilepath(filepath);
		log = new File(logpath);
		logSheet = determineSheet();
		popLocation = new File(pop);
		archLocation = new File(arch);
	}
	/**
	 * 
	 * @param name: Requester name
	 * @param filepath: Just the filepath, the filename gets pulled later
	 * @param log: Log path and location
	 * @param sheet: Log's have sheets, which sheet do we save on.
	 * @param location: POPDEP.xls needs a location to put.
	 * @param archive: The file in copied to and archive location.
	 */
	public Forceout2(String name, String filepath, String log, String sheet, String location, String archive) {
		requestor=name;
		filename = pullFileNamefromFilepath(filepath);
		forceout = new File(filepath);
		this.log= new File(log);
		logSheet = sheet;
		popLocation = new File(location);
		archLocation = new File(archive);
	}
	
	public static void main(String [] args) throws IOException {
		Forceout2 fo= new Forceout2();
		fo.createCopy();
		fo.writeToLog();
	}
	
	private String determineSheet() {
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter getYear = DateTimeFormatter.ofPattern("yyyy");
		String sheet = date.format(getYear);
		
		return sheet;
	}
	
	public String getRequestor() {
		return requestor;
	}
	
	private String pullFileNamefromFilepath(String in) {
		String out;
		int i=in.lastIndexOf("\\");
		
		out = in.substring(i+1);
		return out;
	}
	/**
	 * NOTE: createCopy MUST run first so that numRow is given a value.
	 * @throws IOException
	 * @throws IOException
	 */
	public void writeToLog() throws IOException, IOException{
		HSSFWorkbook log = new HSSFWorkbook(new FileInputStream(this.log));
		HSSFSheet logSheet = log.getSheet(this.logSheet);
		int i=0;
		Row row = logSheet.getRow(i);
		//Move to the bottom of the sheet (Could be a better way to do this maybe)
		while(row!=null) {
			row=logSheet.getRow(i++);
		}
		//create a new row
		row = logSheet.createRow(--i);
		//Set Requester
		Cell cell = row.createCell(0);
		cell.setCellValue(requestor);
		//Set Filename
		cell = row.createCell(1);
		cell.setCellValue(this.getFilename()+" ("+numRow+")");
		
		LocalDateTime dateNtime = LocalDateTime.now();
		//Set Today's Date
		DateTimeFormatter dateForm = DateTimeFormatter.ofPattern("MM/dd/yyyy");  
		cell = row.createCell(2);
		cell.setCellValue(dateNtime.format(dateForm));
		//Set Uploaded time
		DateTimeFormatter timeForm = DateTimeFormatter.ofPattern("HH:mm a");  
		cell = row.createCell(3);
		cell.setCellValue(dateNtime.format(timeForm));
		
		System.out.println("Write to log is succeful");
		log.write(new FileOutputStream(this.log));
		log.close();
		
	}
	
	/**
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void createCopy()throws IOException, FileNotFoundException {
		int i=0;
		boolean flag=false;
		Cell cell;
		HSSFWorkbook xbook = new HSSFWorkbook(new FileInputStream(forceout));
		HSSFSheet xSheet = xbook.getSheet("Sheet1");
		
		Workbook newBook = new HSSFWorkbook();
		Sheet newSheet = newBook.createSheet("Sheet1");
		
		
		HSSFRow row = xSheet.getRow(i);
		Row newRow = newSheet.createRow(i);
		
		String header = row.getCell(0).getStringCellValue();
		//This creates a blank header, might change later TODO
		while(checkForHeader(header)) {
			i++;
			row = xSheet.getRow(i);
			newRow = newSheet.createRow(i);
			header = row.getCell(0).getStringCellValue();
		}
		
		do {
			for(int j=0;j<numCol;j++) {
				cell = newRow.createCell(j);
				//HARMON
				if(j==0) {
					cell.setCellValue(row.getCell(j).getStringCellValue());
				}
				//STORE
				if(j==1) {
					if(row.getCell(j).getNumericCellValue()>9999 ||row.getCell(j).getNumericCellValue() <=0) {
						System.err.print("Column B at row "+ i +" has a bad store value.");
						ruleViolation();
					}
					//Checks to see if store is equal to warehouse
                    if(row.getCell(j).getNumericCellValue() == row.getCell(j+1).getNumericCellValue()) {
                           System.err.print("Column B at row "+ (i+1) +" has a store is equal to warehouse value.");
                           ruleViolation();
                     }
				}
				//Warehouse
				if(j==2) {
					if(row.getCell(j).getNumericCellValue()!= 8660 && row.getCell(j).getNumericCellValue() != 8671) {
						System.err.print("Column C at row "+ i + " has a bad warehouse ID.");
						ruleViolation();
					}
				}
				if(j==3) {
					if(row.getCell(j).getNumericCellValue() == 0) {
						System.err.println("SKU at row "+ i + " is a 0 ID.");
					}
					if(row.getCell(j).getNumericCellValue() < 0 || row.getCell(j).getNumericCellValue() > 999999999) {
						System.err.print("SKU: "+ row.getCell(j).getNumericCellValue() +" @ row " + i + " is unacceptable.");
						ruleViolation();
					}
				}
				if(j==4) {
					if(row.getCell(j).getNumericCellValue() <= 0) {
						System.err.print("Quantity of item "+row.getCell(3)+"is less that 1");
						ruleViolation();
					}
				}
				try {
					cell.setCellValue(row.getCell(j).getNumericCellValue());
				}catch(Exception e){
					cell.setCellValue(row.getCell(j).getStringCellValue());
				}
			}
			i++;
				row = xSheet.getRow(i);
				newRow = newSheet.createRow(i);
				if(row==null) {
					flag=true;
				}
			numRow=i;
		}while(!flag);
		
		newBook.write(new FileOutputStream(popLocation+"\\POPDEP.xls"));
		moveFile(forceout,archLocation);
		newBook.close();
        System.out.println("POPDEP has been succesfully created");
	}
	
	private static boolean checkForHeader(String header) {
		if(header.equalsIgnoreCase("Company"))
		{
			return false;
		}
		return true;
	}
	
	private static void ruleViolation() {
		JFrame window = new JFrame();
		
		JOptionPane.showMessageDialog(window,
			    "A rule violation has occured. Aborting program.",
			    "Rule Violation",
			    JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	
	public void moveFile(File start, File end) {
		String temp = end.toString()+"\\"+filename;
		try {
			Files.move(start.toPath(),Paths.get(temp));
			
		}catch (IOException e) {
			System.err.println(e);
		}
		
	}
	
	/**
	 * 
	 * @param val
	 * @param pos
	 * @return out
	 * cellFormater is written with numCol in mind
	 * it is hard coded to handle the formatting.
	 * TODO: Returning a string means the excel does not read them as Numbers.
	 */
	private static String cellFormater(double val, int pos) {
		DecimalFormat form = null;
		String out="";
		
		switch(pos) {
		case 1:
		case 2:
			form = new DecimalFormat("00000");
			break;
		case 3:
			form = new DecimalFormat("###########");
			break;
		case 4:
			form = new DecimalFormat("00000");
			break;
		default:
			out = "ERROR SHOULD NOT BE HERE: "+val;
		}
		out = form.format(val);
		return out;
	}
	
	/**
	 * Tester for reading Excels
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void readFromExcel() throws IOException, FileNotFoundException{
		HSSFWorkbook xbook = new HSSFWorkbook(new FileInputStream(forceout));
		HSSFSheet xSheet = xbook.getSheet("Sheet1");
		HSSFRow row = xSheet.getRow(0);
		
		System.out.println(row.getCell(0).getStringCellValue());
		
		System.out.println(cellFormater(row.getCell(3).getNumericCellValue(),3));
		
		row = xSheet.getRow(1);
		System.out.println(row.getCell(0).getStringCellValue());
		
		System.out.println(cellFormater(row.getCell(3).getNumericCellValue(),3));
		
		xbook.close();
	}
	/**
	 * Tester for writing to excel
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void createPOPDEP() throws IOException, FileNotFoundException {
		
		HSSFWorkbook xbook = new HSSFWorkbook(new FileInputStream(forceout));
		HSSFSheet xSheet = xbook.getSheet("Sheet1");
		HSSFRow row = xSheet.getRow(0);
		
		Workbook book = new HSSFWorkbook();
		Sheet sheet = book.createSheet("Sheet1");
		Row newRow = sheet.createRow(0);
		
		Cell cell = newRow.createCell(0);
		cell.setCellValue(row.getCell(0).getStringCellValue());
		
		row = xSheet.getRow(1);
		newRow = sheet.createRow(1);
		
		cell = newRow.createCell(0);
		cell.setCellValue(row.getCell(1).getNumericCellValue());
		
		
		book.write(new FileOutputStream("POPDEP.xls"));
        book.close();
		
	}

	public String getFilename() {
		return filename;
	}
	
}

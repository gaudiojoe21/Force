package aPack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SettingsAccessor {
	private String filepath;
	private String logpath;
	private String pop;
	private String arch;
	private File settings = new File("C:\\Settings.txt");
	
	public SettingsAccessor() throws FileNotFoundException {
		Scanner in = new Scanner(settings);
		int i = 0;
		while (in.hasNextLine()) {
			String data = in.nextLine();
			
			switch(i) {
			case 0:
				setFilepath(formater(data));
				break;
			case 1:
				setLogpath(formater(data));
				break;
			case 2:
				setPop(formater(data));
				break;
			case 3:
				setArch(formater(data));
				break;
			}
			i++;
		}
		
		in.close();
	}
	
	//TODO: Pulling from Settings txt means the \ is not doubled
	// It still reads the labels as part of the path.
	public SettingsAccessor(File loc) throws FileNotFoundException {
		settings = loc;
		Scanner in = new Scanner(settings);
		int i = 0;
		while (in.hasNextLine()) {
			String data = in.nextLine();
			
			try {
				
			}catch(Exception e) {
				
			}
			
			
			switch(i) {
			case 0:
				setFilepath(formater(data));
				break;
			case 1:
				setLogpath(formater(data));
				break;
			case 2:
				setPop(formater(data));
				break;
			case 3:
				setArch(formater(data));
				break;
			}
			i++;
		}
		
		
		in.close();
	}
	
	public static void main(String [] args) throws FileNotFoundException {
		SettingsAccessor set = new SettingsAccessor();
		System.out.println(set.getFilepath());
		System.out.println(set.getLogpath());
		System.out.println(set.getPop());
		System.out.println(set.getArch());
	}
	
	public String formater(String s) {
		String out = s;
		out = out.substring(out.indexOf(':')+1);
		out = out.trim();
	    out = out.replace("\\", "\\\\");
		return out;
	}
	
	public File getSettingsFile() {
		return settings;
	}
	
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getLogpath() {
		return logpath;
	}
	public void setLogpath(String logpath) {
		this.logpath = logpath;
	}
	public String getPop() {
		return pop;
	}
	public void setPop(String pop) {
		this.pop = pop;
	}
	public String getArch() {
		return arch;
	}
	public void setArch(String arch) {
		this.arch = arch;
	}
	
}

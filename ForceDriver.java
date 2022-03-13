package aPack;

import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
public class ForceDriver {

	
	public static void main(String[]args) throws InterruptedException, FileNotFoundException {
		
		SettingsAccessor set = new SettingsAccessor();
	
		MyFileChooser chooser= new MyFileChooser(set);
		
	}
	
	
}

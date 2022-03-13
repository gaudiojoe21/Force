package aPack;

import javax.swing.JFileChooser;
import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MyFileChooser2 extends JFileChooser {
	File forcePath;
	String requestor="";
	JFrame frame = new JFrame("File/Requestor Chooser");
	JFileChooser chooser= new JFileChooser();
	FlowLayout flow = new FlowLayout(10);
	
public MyFileChooser2() throws InterruptedException {

		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(400,150);
		frame.setLayout(flow);
		
		JLabel label = new JLabel("Enter Requestor's Name: ");
		
		JTextField request = new JTextField("", 20);
		
		JLabel submition = new JLabel("Set Requestor name");
		
		JButton b = new JButton("Set");
		JButton save = new JButton("Submit");
		
		frame.add(label);
		frame.add(request);
		frame.add(submition);
		frame.add(b);
		frame.add(save);
		frame.setVisible(true);
		
		
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				submition.setText("\""+request.getText()+"\" has been set as the requestor. ");
				requestor = request.getText();
			}
		});
		
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg) {
				if(requestor.equals("")) {
					requestor = request.getText();
				}
				
				//TODO: Constructor should pass starting file path
				chooser.setCurrentDirectory(new File("C:\\"));
				
				int result = chooser.showOpenDialog(frame);
				if(result == JFileChooser.APPROVE_OPTION) {
					forcePath = chooser.getSelectedFile();
				}
					
				Forceout force = new Forceout(requestor,forcePath.toString(),0);
				
				
				try {
					force.createCopy();
					force.writeToLog();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("Something went wrong");
					e.printStackTrace();
				}
				
				System.exit(0);
			}
		});
		
		
		
	}	
	
public MyFileChooser2(SettingsAccessor setting) throws InterruptedException {

		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(400,150);
		frame.setLayout(flow);
		
		JLabel label = new JLabel("Enter Requestor's Name: ");
		
		JTextField request = new JTextField("", 20);
		
		JLabel submition = new JLabel("Set Requestor name");
		
		JButton b = new JButton("Set");
		JButton save = new JButton("Submit");
		
		frame.add(label);
		frame.add(request);
		frame.add(submition);
		frame.add(b);
		frame.add(save);
		frame.setVisible(true);
		
		
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				submition.setText("\""+request.getText()+"\" has been set as the requestor. ");
				requestor = request.getText();
			}
		});
		
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg) {
				if(requestor.equals("")) {
					requestor = request.getText();
				}
				
				//TODO: Constructor should pass starting file path
				chooser.setCurrentDirectory(new File(setting.getFilepath()));
				
				int result = chooser.showOpenDialog(frame);
				if(result == JFileChooser.APPROVE_OPTION) {
					forcePath = chooser.getSelectedFile();
				}
				
				Forceout force = new Forceout(requestor, forcePath.toString(), setting.getLogpath(), setting.getPop(), setting.getArch());
				
				
				try {
					force.createCopy();
					force.writeToLog();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("Something went wrong");
					e.printStackTrace();
				}
				
				System.exit(0);
			}
		});
		
		
		
	}	
	
	public static void main(String [] args) throws InterruptedException {
		MyFileChooser2 m = new MyFileChooser2();
		
		System.out.println(m.requestor);
		System.out.println(m.forcePath);
		System.out.println(m.getPathAsString());
	}
	
	public String getRequestor() {
		return requestor;
	}
	
	public File getForcepath() {
		return forcePath;
	}
	
	public String getPathAsString() {
		return forcePath.toString();
	}
	
}

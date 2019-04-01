package uk.ac.newcastle.stannard.liam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import javax.swing.SwingWorker;
import javax.swing.text.DateFormatter;

import com.sun.prism.Presentable;

import javax.swing.*;

/**
 * Write a description of class ShellWorker here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */

public class ShellWorker extends SwingWorker<Set<String>, Set<String>> {
	private JTextField tfStatus;
	JTextField tfPredCount;
	JTextField tfKnownCount;
	private ProcessBuilder processBuilder;
	private Process process;
	private String shellPath = "/home/pi/LiamStan/Diss";
	private StringBuilder output;
	private Set<String> macAddresses;
	private DefaultListModel<String> copyAddrModel;
	private DefaultListModel<String> copyOccuModel;
	private int predOccupancyCount = 0;
	private int knownOccupnacyCount = 0;
	private PersonStore personStoreCopy;
	private int round = 0;
	
	private final String ROUTER_MAC_ADDRESS = "48:d3:43:3f:7b:08";

	/**
	 * Constructor for objects of class ShellWorker
	 */
	public ShellWorker(DefaultListModel<String> addrModel, DefaultListModel<String> occuModel, JTextField tStatus,
			JTextField tPredCount, JTextField tKnownCount, PersonStore p) {
		processBuilder = new ProcessBuilder();

		setCommand();

		copyOccuModel = occuModel;
		copyAddrModel = addrModel;
		tfStatus = tStatus;
		tfPredCount = tPredCount;
		tfKnownCount = tKnownCount;
		personStoreCopy = p;

	}

	@Override
	public Set<String> doInBackground() {
		while (!this.isCancelled()) {
			round ++;
			System.out.println("Start - " + round);
			try {
				setCommand();
				process = processBuilder.start();
				//processBuilder.command("res/ping_windows10.bat");
				//Process p = processBuilder.start();
				
				macAddresses = new HashSet<String>();
				File log = new File("log");
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				output = new StringBuilder();
				tfStatus.setText("Running");
				
				
				

				String line;
				output.append("MAC ADDRESSES: \n");
				while ((line = reader.readLine()) != null ) {
				System.out.println(line);
					if(!macAddresses.contains(line)) {
					System.out.println(line + "\n");
					macAddresses.add(line);}
				}
				macAddresses.remove(ROUTER_MAC_ADDRESS);
				macAddresses.remove("eth.src");
				int exitVal = process.waitFor();
				if (exitVal == 0) {
					System.out.println("Success!");
					System.out.println(output);
					
					writeToFile();
					updateGUI();

				} else {

					// abnormal...
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Finish!\n");

		}
		return macAddresses;
	}

	@Override
	protected void done() {
		tfStatus.setText("Finished");
	}

	public StringBuilder getOutput() {
		return output;
	}

	public Set<String> getMacAddresses() {
		return macAddresses;
	}

	public void updateGUI() {
		try {
			predOccupancyCount = 0;
			knownOccupnacyCount = 0;
			copyAddrModel.clear();
			copyOccuModel.clear();
			for (String str : macAddresses) {
				copyAddrModel.addElement(str);
				System.out.println("Addresses:\n" + str + "\n");
				for (Person p : personStoreCopy.getPersonList()) {
					System.out.print(p);
					boolean present = false;
					for (String addr : p.getAddresses()) {
						if (addr.equals(str)) 
						{
							if(str.equals(p.getPrimaryAddress()))
							{
								System.out.print(" - Present\n");
								present = true;
							}
							
							macAddresses.remove(str);
						}
					}
					if(present)
					{
						knownOccupnacyCount++;
						copyOccuModel.addElement(p.getName());
					}

				}

			}
			predOccupancyCount = macAddresses.size();

			tfPredCount.setText(Integer.toString(predOccupancyCount));
			tfKnownCount.setText(Integer.toString(knownOccupnacyCount));
		} catch (Exception ignore) {
		}
	}

	private String getOperatingSystem() {
		System.out.println(System.getProperty("os.name"));
		return System.getProperty("os.name");
	}

	private void setCommand() {

		switch (getOperatingSystem()) {
		case "Windows 10":
			
			processBuilder.command("res/run.bat");
			break;
		case "Raspbean":
			;
			processBuilder.command(shellPath + "res/run.sh");
			break;

			

		default:
			break;
		}

	}
	
	private void writeToFile()
	{
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter("res/log.txt",true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			Date date = Calendar.getInstance().getTime();
			String dateStr = DateFormat.getDateTimeInstance().format(date);
			System.out.println("WRITE");
			printWriter.printf("%s%n",dateStr );
			printWriter.printf("%s %s%n","Known: " +Integer.toString(knownOccupnacyCount),"Predicted: "+  Integer.toString(predOccupancyCount) );
			for(Object person : copyOccuModel.toArray())
			{
				printWriter.printf("%s%n",person.toString());
			} 
			printWriter.printf("%n" );
			printWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	
	}

}

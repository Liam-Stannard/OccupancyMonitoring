package uk.ac.newcastle.stannard.liam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	private int predictedOccupancyCount = 0;
	private int knownOccupancyCount = 0;
	private int presentAddressCount = 0;
	private PersonStore personStore;
	private int round = 0;
	
	private final String router_mac_address;

	/**
	 * Constructor for objects of class ShellWorker
	 */
	public ShellWorker(DefaultListModel<String> addrModel, DefaultListModel<String> occuModel, JTextField tStatus,
			JTextField tPredCount, JTextField tKnownCount, PersonStore p,String router_mac_address) {
		processBuilder = new ProcessBuilder();

		
		this.router_mac_address = router_mac_address;
		copyOccuModel = occuModel;
		copyAddrModel = addrModel;
		tfStatus = tStatus;
		tfPredCount = tPredCount;
		tfKnownCount = tKnownCount;
		personStore = p;

	}

	@Override
	public Set<String> doInBackground() {
		while (!this.isCancelled()) {
			round ++;
			System.out.println("Start - " + round);
			try {
				runCommand();
		
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
					System.out.println(line);
					macAddresses.add(line);
					copyAddrModel.addElement(line);
					}
				}
				macAddresses.remove(router_mac_address);
				macAddresses.remove("eth.src");
				System.out.println("List:");
				for(String string : macAddresses)
					System.out.println(string);
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
			PersonStore peoplePresent = new PersonStore();
			predictedOccupancyCount = 0;
			knownOccupancyCount = 0;
			presentAddressCount = 0;
			copyAddrModel.clear();
			copyOccuModel.clear();
			
			System.out.println("Addresses:");
			for(String adr : macAddresses)
				System.out.println(adr);
			
			for (String str : macAddresses) 
			{
				System.out.println("Current Address:" + str);
				for (Person p : personStore.getPersonList())
				{
					System.out.println("---------------------------------");
					System.out.println(p);
					
					for (String addr : p.getAddresses()) 
					{
						System.out.println(addr);
						if (addr.equals(str)) 
						{
							if (addr.equals(p.getPrimaryAddress())) 
							{
								
								copyOccuModel.addElement(p.getName());
								knownOccupancyCount++;
								peoplePresent.addPerson(p);
							}
							
							presentAddressCount++;
						}
					}
					
					System.out.println("---------------------------------");
				}

			}
			predictedOccupancyCount = macAddresses.size() - presentAddressCount;
			System.out.println("\nAddresses:");
			for(String adr : macAddresses)
				System.out.println(adr);
			
			System.out.println("People Present:");
			for (Person p : peoplePresent.getPersonList())
				System.out.println(p);
			
			System.out.println("Known: " + knownOccupancyCount);
			System.out.println("Pred: " + predictedOccupancyCount);


			tfPredCount.setText(Integer.toString(predictedOccupancyCount));
			tfKnownCount.setText(Integer.toString(knownOccupancyCount));
		} catch (Exception ignore) {
		}
	}

	private String getOperatingSystem() {
		System.out.println(System.getProperty("os.name"));
		return System.getProperty("os.name");
	}

	private void runCommand() throws IOException {
		Process p;
		switch (getOperatingSystem()) {
		case "Windows 10":
			
			processBuilder.command("res/run.bat");
			process = processBuilder.start();
			processBuilder.command("res/ping_windows10.bat");
			p = processBuilder.start();
			break;
		case "Raspbian":

			processBuilder.command("res/run.bat");
			process = processBuilder.start();
			processBuilder.command("res/ping_Raspbian.bat");
			 p = processBuilder.start();
			break;

			

		default:
			break;
		}

	}
	
	private void writeToFile()
	{
		FileWriter fileWriter;
		try {
			File logDir = new File("logs");
			if(!logDir.exists())
				logDir.mkdirs();
				
				
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = Calendar.getInstance().getTime();
			System.out.println("log"+ sdf.format(date).toString()+".txt");
			fileWriter = new FileWriter("logs/log_"+sdf.format(date).toString()+".txt",true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			
			String dateStr = DateFormat.getDateTimeInstance().format(date);
			System.out.println("WRITE");
			printWriter.printf("%s%n",dateStr );
			printWriter.printf("%s %s%n","Known: " +Integer.toString(knownOccupancyCount),"Predicted: "+  Integer.toString(predictedOccupancyCount) );
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

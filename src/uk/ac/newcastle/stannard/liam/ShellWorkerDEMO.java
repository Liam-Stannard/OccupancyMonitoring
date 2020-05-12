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

public class ShellWorkerDEMO extends SwingWorker<Set<String>, Set<String>> {
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
	private String[] macArray = {
			"12:4a:6c:3b:7d:4d",
			"15:4a:6c:3b:7d:4d",
			"16:4a:6c:3b:7d:4d",
			"17:4a:6c:3b:7d:4d",
			"18:4a:6c:3b:7d:4d",
			"19:4a:6c:3b:7d:4d",
			"11:4a:6c:3b:7d:4d",
			"12:43:6c:3b:7d:4d",
			"12:4a:63:3b:7d:4d",
			"12:4a:6a:3b:7d:4d",
			"12:4a:6d:3d:7d:4d",
			"12:4a:6c:3c:7d:4d",
			"12:4a:6c:35:7d:4d",
			"12:4a:6c:3b:76:4d",
			"12:4a:66:3b:7d:4d",
			"12:4a:6c:3b:7d:4d",
			"12:4a:d5:3b:7d:4d",
			"12:4a:6c:3b:7d:4d",
			"12:45:6c:3b:7d:4d",
			"12:fa:6c:3b:7d:4d",
			"12:6a:6c:3b:7d:4d",
			"12:4a:6c:6b:7d:4d",
			"12:4a:6c:3b:7d:4d",
			"12:4a:77:3b:7d:4d",
			"12:4a:6c:77:7d:4d",
			"12:4a:88:3b:7d:4d",
			"12:4a:99:3b:7d:4d",
			"99:4a:6c:3b:7d:4d",
			"12:4a:6c:3b:7d:4d",
			"12:99:6c:3b:7d:4d",
			"12:4a:6c:3b:7d:4d",
			"12:4a:8c:3b:7d:4a",
			"12:4a:6c:3b:7d:4b",
			"12:4a:12:3b:7d:4c",
			"12:4a:6c:3b:7d:4d",
			"12:4a:6c:3b:7d:4e",
			"12:4a:6c:3b:7d:41",
			"12:4a:6c:3b:7d:42",
			"12:4a:6c:3b:7d:43",
			"12:4a:6c:3b:7d:44",
			"12:4a:6c:3b:7d:45",
			"12:4a:6c:3b:7d:46",
			"12:4a:6c:3b:7d:47",
			"12:4a:6c:3b:7d:48",
			"12:4a:6c:3b:7d:49",
			"12:4a:6c:3b:7d:51",
			"12:4a:6c:3b:7d:52",
			"12:4a:6c:3b:7d:53",
			"12:4a:6c:3b:7d:54",
			"12:4a:6c:3b:7d:55",
			"12:4a:6c:3b:7d:56",
			
	};
	
	private final String router_mac_address;

	/**
	 * Constructor for objects of class ShellWorker
	 */
	public ShellWorkerDEMO(DefaultListModel<String> addrModel, DefaultListModel<String> occuModel, JTextField tStatus,
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
	public Set<String> doInBackground() 
	{
		while (!this.isCancelled()) 
		{
			round ++;
			System.out.println("Start - " + round);
			macAddresses = generateMacSet();
			writeToFile();
			updateGUI();
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
				copyAddrModel.addElement(adr);
			
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

	
	
	private Set<String> generateMacSet() {
		Set<String> macSet = new HashSet<String>();
		
		for(int i = 0;i<5 ;i++)
		{
		
			macSet.add(macArray[new Random().nextInt(macArray.length-1)]);
			
		}
		int j = 0;
		int k =0;
		int x = 0;
		while(x<Integer.MAX_VALUE)
		{
			while (j<Integer.MAX_VALUE)
			{
				
				while (k<Integer.MAX_VALUE)
					k++;
				j++;
			}
			x++;
		}
		
		return macSet;
		
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

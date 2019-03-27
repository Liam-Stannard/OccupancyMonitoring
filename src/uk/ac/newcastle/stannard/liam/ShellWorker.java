package uk.ac.newcastle.stannard.liam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.util.*;
import javax.swing.SwingWorker;
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
	private Set<String> macAddresses = new HashSet<String>();
	private DefaultListModel<String> copyAddrModel;
	private DefaultListModel<String> copyOccuModel;
	private int predOccupancyCount = 0;
	private int knownOccupnacyCount = 0;
	private PersonStore personStoreCopy;

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

			System.out.println("Start!");
			try {

				tfStatus.setText("Running");
				process = processBuilder.start();
				output = new StringBuilder();
				File log = new File("log");
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

				String line;
				output.append("MAC ADDRESSES: \n");
				while ((line = reader.readLine()) != null && !macAddresses.contains(line)) {
					output.append(line + "\n");
					macAddresses.add(line);
				}

				int exitVal = process.waitFor();
				if (exitVal == 0) {
					System.out.println("Success!");
					System.out.println(output);
					macAddresses.remove("eth.src");
					updateGUI();

				} else {

					// abnormal...
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Finish!");

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

				for (Person p : personStoreCopy.getPersonList()) {
					System.out.println(p.getName());
					for (String addr : p.getAddresses()) {
						if (addr.equals(str)) {
							knownOccupnacyCount++;
							copyOccuModel.addElement(p.getName());
						}
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
			System.out.println(this.getClass().getResource("res/run.bat").toString());
			processBuilder.command("cmd /c start \"\"this.getClass().getResource(\"res/run.bat\").toString()");
			break;
		case "Raspbean":
			;
			processBuilder.command(shellPath + "/run.sh");
			break;

			

		default:
			break;
		}

	}

}

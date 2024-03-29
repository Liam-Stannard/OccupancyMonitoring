package uk.ac.newcastle.stannard.liam;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.*;

/**
 * @author  Liam Stannard This is the user interface class which allows for: -The personStore to be manipulated - The monitoring to be stopped and started - The occupancy counts to be displayed
 */

public class Interface {

	private final boolean DEMO_MODE = true;
	private JFrame f;
	private JTabbedPane tabbedPane;
	PersonStore personStore;
	PersonStore demoStore = new PersonStore();
	ObjectSerialiser<PersonStore> personSerialiser;
	private final String routerAddressPath = "res/routerAddress";
	private final String personStorePath = "res/personStore";

	// monitor panel

	private JTextField tfStatus;
	private JPanel monitorPanel;
	private JTextField tfPredCount;
	private JTextField tfKnownCount;
	private JButton b;
	private JList<String> addrList;
	private DefaultListModel<String> addrModel = new DefaultListModel<String>();
	private JList<String> occuList;
	private DefaultListModel<String> occuModel = new DefaultListModel<String>();
	private JPanel monitorEastPanel;
	private JPanel montiorWestPanel;
	private JPanel monitorCentrePanel;
	private JPanel monitorSouthPanel;
	private JPanel monitorCentreLeftPanel;
	private JPanel monitorCentreRightPanel;
	private JTextField tfrouterAddress;
	private JButton routerUpdateButton;

	private boolean executing;
	private String router_mac_address;

	// person panel
	private JPanel personPanel;

	private JPanel personEastPanel;

	private JPanel personWestPanel;
	private JLabel nameLabel;
	private JLabel addressesLabel;
	private JLabel primaryAddressLabel;

	private JPanel personCenterPanel;
	private JTextField tfName;
	private JTextField tfPrimaryAddress;
	private JTextField tfAddresses;
	private JButton submitButton;

	final int TEXT_FIELD_WIDTH = 150;
	final int TEXT_FIELD_HEIGHT = 25;

	// edit panel
	private JPanel editPanel;
	private JPanel editWestPanel;
	private JPanel editCenterPanel;
	private JPanel editEastPanel;

	private JTextField editNameTf;
	private JTextField editPrimaryAddressTf;
	private JTextField editAddressesTf;

	private JList<String> editAddressList;
	private DefaultListModel<String> editAddressListModel;

	private JList<Person> editPersonList;
	private DefaultListModel<Person> editPersonListModel;
	private JButton editDeletePersonButton;
	private JButton editDeleteAddressButton;
	private JButton editUpdateButton;
	

	public Interface() {
		personSerialiser = new ObjectSerialiser<PersonStore>(personStorePath );
		PersonStore tempStore = personSerialiser.load();
		if(tempStore != null) {
			personStore = tempStore;
		}else {
			personStore = new PersonStore();
		}
		if(DEMO_MODE)
		{
			demoStore.addPerson(new Person("bob", "12:4a:6c:3b:7d:4d"));
			demoStore.addPerson(new Person("mary", "15:4a:6c:3b:7d:4d"));
			demoStore.addPerson(new Person("alan", "16:4a:6c:3b:7d:4d"));
			demoStore.addPerson(new Person("sue", "17:4a:6c:3b:7d:4d"));
			demoStore.addPerson(new Person("charlie", "18:4a:6c:3b:7d:4d"));
			demoStore.addPerson(new Person("joe", "19:4a:6c:3b:7d:4d"));
			demoStore.addPerson(new Person("saskia", "11:4a:6c:3b:7d:4d"));
			demoStore.addPerson(new Person("liam", "12:43:6c:3b:7d:4d"));
			demoStore.addPerson(new Person("max", "12:4a:63:3b:7d:4d"));
			demoStore.addPerson(new Person("paddy", "12:4a:6a:3b:7d:4d"));
			personStore = demoStore;
		}
		
		setup();
	}

	/**
	 * Setup for the user interface and all of its components
	 * 
	 */
	private void setup() {
		f = new JFrame();
		tabbedPane = new JTabbedPane();
		ObjectSerialiser<String> oSerialiser = new ObjectSerialiser<String>(routerAddressPath);
		router_mac_address = oSerialiser.load();
		
		setupMonitorPanel();
		setupAddPersonPanel();
		setupEditPersonPanel();
		setupCloseFrameListener();
		f.add(tabbedPane);
		f.setSize(800, 600);
		f.setVisible(true);
	}

	public PersonStore getPersonStore() {
		return personStore;
	}

	/**
	 * Sets up the monitor panel which is the parent component for the monitor tab
	 */
	private void setupMonitorPanel() {
		b = new JButton("Start");
		monitorPanel = new JPanel();
		tfStatus = new JTextField();
		tfPredCount = new JTextField();
		tfKnownCount = new JTextField();
		tfrouterAddress = new JTextField();
		tfrouterAddress.setText(router_mac_address);
		addrList = new JList<>();
		occuList = new JList<>();
		monitorEastPanel = new JPanel(new BorderLayout());
		montiorWestPanel = new JPanel(new BorderLayout());
		monitorCentrePanel = new JPanel();
		monitorSouthPanel = new JPanel(new BorderLayout());
		monitorCentreLeftPanel = new JPanel(new BorderLayout());
		monitorCentreRightPanel = new JPanel(new BorderLayout());

		monitorCentrePanel.add(monitorCentreLeftPanel);
		monitorCentrePanel.add(monitorCentreRightPanel);

		tfPredCount.setBounds(0, 0, 50, 50);
		tfKnownCount.setBounds(0, 0, 50, 50);
		addrList.setBounds(0, 0, 200, 800);
		occuList.setBounds(0, 0, 200, 800);
		tfStatus.setBounds(10, 10, 100, 30);
		tfStatus.setText("Idle");
		addrList.setModel(addrModel);
		occuList.setModel(occuModel);

		montiorWestPanel.add(new JLabel("Address List:"), BorderLayout.NORTH);
		montiorWestPanel.add(addrList, BorderLayout.CENTER);

		monitorEastPanel.add(new JLabel("Occupant List:"), BorderLayout.NORTH);
		monitorEastPanel.add(occuList, BorderLayout.CENTER);

		monitorCentreLeftPanel.add(new JLabel("Predicted Count:"), BorderLayout.NORTH);
		monitorCentreRightPanel.add(new JLabel("Known Count:"), BorderLayout.NORTH);
		monitorCentreLeftPanel.add(tfPredCount, BorderLayout.CENTER);
		monitorCentreRightPanel.add(tfKnownCount, BorderLayout.CENTER);

		monitorPanel.setSize(600, 400);
		monitorPanel.setLayout(new BorderLayout());
		monitorPanel.add(montiorWestPanel, BorderLayout.WEST);
		monitorSouthPanel.add(b, BorderLayout.EAST);
		
		monitorPanel.add(monitorCentrePanel);
		monitorPanel.add(monitorSouthPanel,BorderLayout.SOUTH);
		monitorPanel.add(monitorEastPanel, BorderLayout.EAST);
		monitorPanel.add(tfStatus, BorderLayout.NORTH);

		class ShellListener implements ActionListener {
			private ShellWorker sw = null;
			private ShellWorkerDEMO swdDemo;
			@Override
			public void actionPerformed(ActionEvent ae) {

				if (ae.getActionCommand().equals("Start")) {
					if (sw == null || sw.isDone()) {
						b.setText("Stop");
						executing = true;
						routerUpdateButton.setEnabled(false);
						tfrouterAddress.setEnabled(false);
						if(DEMO_MODE)
						{
							swdDemo = new ShellWorkerDEMO(addrModel, occuModel, tfStatus, tfPredCount, tfKnownCount, personStore,router_mac_address);
							swdDemo.execute();
						}
						else 
						{
							sw = new ShellWorker(addrModel, occuModel, tfStatus, tfPredCount, tfKnownCount, personStore,router_mac_address);
							sw.execute();
						}
					
						

					} else {
						b.setText("Start");
						executing = false;
						if(DEMO_MODE)
						{
							swdDemo.cancel(true);
						}
						else {
							sw.cancel(true);
						}
						
						routerUpdateButton.setEnabled(true);
						tfrouterAddress.setEnabled(true);
					}
				}
			}
		}

		b.addActionListener(new ShellListener());
		b.setActionCommand("Start");
		b.setVisible(true);
		setupMonitorUpdateRouterButton();
		monitorSouthPanel.add(routerUpdateButton, BorderLayout.WEST);
		monitorSouthPanel.add(tfrouterAddress, BorderLayout.CENTER);
		tabbedPane.add("Monitor", monitorPanel);

	}

	/**
	 * Sets up the add person panel which is the parent component for the add person
	 * tab
	 */
	private void setupAddPersonPanel() {
		personPanel = new JPanel(new BorderLayout());
		personCenterPanel = new JPanel(new BorderLayout());
		personWestPanel = new JPanel(new BorderLayout());
		setupSubmitButton();

		tfName = new JTextField();
		tfPrimaryAddress = new JTextField();
		tfAddresses = new JTextField();
		tfName.setText("Name");
		tfAddresses.setText("Insert Mac Addresses here in format xx:xx:xx:xx:xx:xx,yy:yy:yy:yy:yy:yy");
		tfPrimaryAddress.setText("Insert Primary Mac Addresses here in format xx:xx:xx:xx:xx:xx");
		nameLabel = new JLabel("Name:");
		addressesLabel = new JLabel("Addresses:");
		primaryAddressLabel = new JLabel("Primary Address:");

		tfName.setSize(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
		tfAddresses.setSize(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
		tfPrimaryAddress.setSize(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

		// center panel
		personCenterPanel.add(tfName, BorderLayout.NORTH);
		personCenterPanel.add(tfAddresses, BorderLayout.CENTER);
		personCenterPanel.add(tfPrimaryAddress, BorderLayout.SOUTH);

		// west panel
		personWestPanel.add(nameLabel, BorderLayout.NORTH);
		personWestPanel.add(addressesLabel, BorderLayout.CENTER);
		personWestPanel.add(primaryAddressLabel, BorderLayout.SOUTH);

		// main panel
		personPanel.add(submitButton, BorderLayout.SOUTH);
		personPanel.add(personWestPanel, BorderLayout.WEST);
		personPanel.add(personCenterPanel, BorderLayout.CENTER);

		tabbedPane.add("Add Person", personPanel);
	}

	/**
	 * Sets up the edit person panel which is the parent component for the edit
	 * person tab
	 */
	private void setupEditPersonPanel() {
		editPanel = new JPanel(new BorderLayout());
		editCenterPanel = new JPanel(new BorderLayout());
		editWestPanel = new JPanel(new BorderLayout());
		editEastPanel = new JPanel(new BorderLayout());
		editPersonListModel = new DefaultListModel<Person>();
		editPersonList = new JList<Person>();

		editAddressListModel = new DefaultListModel<String>();
		editAddressList = new JList<String>();
		editAddressListSelectionListener();
		editPersonListSelectionListener();

		editAddressesTf = new JTextField();
		editAddressesTf.setText("Add addition addresses here in the format xx:xx:xx:xx:xx:xx, yy:yy:yy:yy:yy:yy");
		nameLabel = new JLabel("Name:");

		setupEditDeletePersonButton();
		setupEditDeleteAddressButton();
		setupEditUpdateButton();
		editAddressList.setModel(editAddressListModel);
		editPersonList.setModel(editPersonListModel);
		for (Person p : personStore.getPersonList()) {
			editPersonListModel.addElement(p);
		}
		editWestPanel.setSize(200, 600);
		editPersonList.setSize(200, 600);

		// center panel
		editCenterPanel.add(addressesLabel, BorderLayout.NORTH);
		editCenterPanel.add(editAddressesTf, BorderLayout.CENTER);
		editCenterPanel.add(editUpdateButton, BorderLayout.SOUTH);
		// west panel
		editWestPanel.add(new JLabel("People:\t\t\t\t"), BorderLayout.NORTH);
		editWestPanel.add(editPersonList, BorderLayout.CENTER);
		editWestPanel.add(editDeletePersonButton, BorderLayout.SOUTH);

		// east panel
		editEastPanel.add(addressesLabel, BorderLayout.NORTH);
		editEastPanel.add(editAddressList, BorderLayout.CENTER);
		editEastPanel.add(editDeleteAddressButton, BorderLayout.SOUTH);

		// main panel
		editPanel.add(editWestPanel, BorderLayout.WEST);
		editPanel.add(editCenterPanel, BorderLayout.CENTER);
		editPanel.add(editEastPanel, BorderLayout.EAST);

		tabbedPane.add("Edit Person", editPanel);
	}

	/**
	 * Sets up the sumbit button used on the monitor panel. Adds an action listener
	 * which creates a new person from the strings found in tfName, tfAddresses and
	 * tfPrimaryAddress
	 */
	private void setupSubmitButton() {

		submitButton = new JButton("Sumbit");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> tempArrayList = new ArrayList<String>(
						Arrays.asList(tfAddresses.getText().split(",")));
				Person tempPerson = new Person(tfName.getText(), tfPrimaryAddress.getText(), tempArrayList);
				personStore.addPerson(tempPerson);
				editPersonListModel.addElement(tempPerson);
				System.out.println(tempPerson);
			}
		});
	}

	/**
	 * Sets up the editDeletePersonButtin used on the edit person panel. Adds an
	 * action listener which removes the correct person from both the PersonStore
	 * and the editPersonListModel
	 */
	private void setupEditDeletePersonButton() {

		editDeletePersonButton = new JButton("Delete Person");
		editDeletePersonButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = editPersonList.getSelectedIndex();
				if (index >= 0) {
					personStore.getPersonList()
							.remove(personStore.getPersonList().indexOf(editPersonListModel.get(index)));
					editPersonListModel.remove(index);
				}
			}
		});
		editDeletePersonButton.setEnabled(false);
	}

	/**
	 * Sets up the editDeletePersonButtin used on the edit person panel. Adds an
	 * action listener which removes the correct address from the Person and the
	 * editAddressListModel
	 */
	private void setupEditDeleteAddressButton() {

		editDeleteAddressButton = new JButton("Delete Address");
		editDeleteAddressButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int addressindex = editAddressList.getSelectedIndex();
				int personIndex = editPersonList.getSelectedIndex();
				if (addressindex >= 0 && personIndex >= 0) {
					personStore.getPersonList()
							.get(personStore.getPersonList().indexOf(editPersonListModel.get(personIndex)))
							.getAddresses().remove(addressindex);
					editAddressListModel.remove(addressindex);
				}
			}
		});
		editDeleteAddressButton.setEnabled(false);
	}

	/**
	 * Sets up the update person. Adds an action listener which adds the addresses
	 * to the person selected in the editPersonList
	 */
	private void setupEditUpdateButton() {
		editUpdateButton = new JButton("Update Person");
		editUpdateButton.setEnabled(false);
		editUpdateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = editPersonList.getSelectedIndex();
				ArrayList<String> tempAddressList = new ArrayList<String>();
				if (index >= 0) {
					tempAddressList = personStore.getPersonList()
							.get(personStore.getPersonList().indexOf(editPersonListModel.get(index))).getAddresses();

					for (String adr : Arrays.asList(editAddressesTf.getText().split(","))) {
						tempAddressList.add(adr);
						System.out.println(adr);
					}

					personStore.getPersonList().get(personStore.getPersonList().indexOf(editPersonListModel.get(index)))
							.setAddresses(tempAddressList);

				}
			}
		});
	}

	/**
	 * Adds a ListSelection listener to the editAddressList which simply
	 * enables/disables buttons based on if an element is selected or not.
	 */

	private void editAddressListSelectionListener() {

		editAddressList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if (!lse.getValueIsAdjusting()) {
					int addressIndex = editAddressList.getSelectedIndex();
					if (addressIndex == -1) {
						// No selection, disable fire button.
						editDeleteAddressButton.setEnabled(false);

					} else {
						// Selection, enable the fire button.
						editDeleteAddressButton.setEnabled(true);
					}
				}
			}
		});

	}

	/**
	 * Adds a ListSelection listener to the editPersonList which simply
	 * enables/disables buttons based on if an element is selected or not. If a
	 * person is selected it also updates the editAddressListModel with the
	 * addresses of the person selected.
	 */
	private void editPersonListSelectionListener() {
		editPersonList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if (!lse.getValueIsAdjusting()) {
					int personIndex = editPersonList.getSelectedIndex();
					if (personIndex == -1) {
						// No selection, disable fire button.
						editDeletePersonButton.setEnabled(false);
						editDeleteAddressButton.setEnabled(false);
						editUpdateButton.setEnabled(false);

					} else {
						// Selection, enable the fire button.
						editDeletePersonButton.setEnabled(true);
						editUpdateButton.setEnabled(true);
						editAddressListModel.clear();
						for (String adr : personStore.getPerson(personIndex).getAddresses()) {
							editAddressListModel.addElement(adr);
						}

					}
				}
			}
		});
	}

	public void updateEditPersonList() {

		editPersonListModel.clear();
		for (Person p : personStore.getPersonList())
			editPersonListModel.addElement(p);

	}
	
	public void setupCloseFrameListener() {
		f.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	personSerialiser.save(personStore);
		            System.exit(0);
		        
		    }
		});
		
		
		
	
	}
	
	private void setupMonitorUpdateRouterButton() {

		routerUpdateButton = new JButton("Update Address");
		routerUpdateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				router_mac_address = tfrouterAddress.getText();
				ObjectSerialiser<String> oSerialiser = new ObjectSerialiser<String>(routerAddressPath);
				oSerialiser.save(router_mac_address);
				
			}
		});
		
	
	}
}

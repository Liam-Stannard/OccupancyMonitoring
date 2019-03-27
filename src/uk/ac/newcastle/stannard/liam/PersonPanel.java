package uk.ac.newcastle.stannard.liam;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PersonPanel extends JPanel 
{
	
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
	
	public PersonPanel() {
		setupPersonPanel();
		this.setLayout(new BorderLayout());
	}
	
	private void setupPersonPanel() {
		
		personCenterPanel = new JPanel(new BorderLayout());
		personWestPanel = new JPanel(new BorderLayout());
		
		
		tfName = new JTextField();
		tfPrimaryAddress = new JTextField();
		tfAddresses = new JTextField();

		nameLabel = new JLabel("Name:");
		addressesLabel = new JLabel("Addresses:");
		primaryAddressLabel = new JLabel("Primary Address:");
		submitButton = new JButton("Sumbit");
		
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
		this.add(submitButton,BorderLayout.SOUTH);
		this.add(personWestPanel, BorderLayout.WEST);
		this.add(personCenterPanel, BorderLayout.CENTER);

		
	}
	
	
	
	public JTextField getTfName() {
		return tfName;
	}



	public JTextField getTfPrimaryAddress() {
		return tfPrimaryAddress;
	}



	public JTextField getTfAddresses() {
		return tfAddresses;
	}



	public JButton getSubmitButton()
	{
		return submitButton;
	}

}

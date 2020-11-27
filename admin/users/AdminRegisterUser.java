package ca.java.admin.users;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import ca.java.admin.top.AdminTop;
import ca.java.db.connect.ConnectToUsersDatabases;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;

public class AdminRegisterUser {

	private JFrame frame;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField emailField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminRegisterUser window = new AdminRegisterUser();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminRegisterUser() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		ConnectToUsersDatabases dao = new ConnectToUsersDatabases();
		
		frame = new JFrame();
		frame.setTitle("Register a User");
		frame.setBounds(700, 450, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		firstNameField = new JTextField();
		firstNameField.setBounds(227, 37, 130, 26);
		frame.getContentPane().add(firstNameField);
		firstNameField.setColumns(10);
		
		JLabel lblFirstNameLabel = new JLabel("First Name");
		lblFirstNameLabel.setBounds(83, 42, 95, 16);
		frame.getContentPane().add(lblFirstNameLabel);
		
		JLabel lblLastNameLabel = new JLabel("Last Name");
		lblLastNameLabel.setBounds(83, 80, 95, 16);
		frame.getContentPane().add(lblLastNameLabel);
		
		lastNameField = new JTextField();
		lastNameField.setBounds(227, 75, 130, 26);
		frame.getContentPane().add(lastNameField);
		lastNameField.setColumns(10);
		
		JLabel lblEmailLabel = new JLabel("Email");
		lblEmailLabel.setBounds(83, 123, 95, 16);
		frame.getContentPane().add(lblEmailLabel);
		
		emailField = new JTextField();
		emailField.setBounds(227, 118, 130, 26);
		frame.getContentPane().add(emailField);
		emailField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(227, 156, 130, 26);
		frame.getContentPane().add(passwordField);
		
		JLabel lblPasswordLabel = new JLabel("Password");
		lblPasswordLabel.setBounds(83, 161, 61, 16);
		frame.getContentPane().add(lblPasswordLabel);
		
		JRadioButton rdbtnIsAdminRadioButton = new JRadioButton("     Register as an Admin ?");
		rdbtnIsAdminRadioButton.setBounds(110, 194, 194, 23);
		frame.getContentPane().add(rdbtnIsAdminRadioButton);
		
		JButton btnRegisterButton = new JButton("Register");
		btnRegisterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (firstNameField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill out First Name box");
					return;
				}
				if (lastNameField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill out Last Name box");
					return;
				}
				if (emailField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill out Email box");
					return;
				}
				if (passwordField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill out Password box");
					return;
				}
				
				boolean isSuccess = dao.insertQuery(firstNameField.getText(), lastNameField.getText(), emailField.getText(), passwordField.getText(), rdbtnIsAdminRadioButton.isSelected());
				if (isSuccess) {
					AdminTop.main(null);
					JOptionPane.showMessageDialog(null, "Success insert a record");
					frame.dispose();
				} else {
					AdminRegisterUser.main(null);
					JOptionPane.showMessageDialog(null, "Not Sccess insert a record");
					frame.dispose();
				}
			}
		});
		btnRegisterButton.setBounds(150, 229, 117, 29);
		frame.getContentPane().add(btnRegisterButton);
		
		JButton btnBackButton = new JButton("Back");
		btnBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminTop.main(null);
				frame.dispose();
			}
		});
		btnBackButton.setBounds(357, 229, 61, 29);
		frame.getContentPane().add(btnBackButton);
		
	}
}

package ca.java.common.login;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ca.java.admin.top.AdminTop;
import ca.java.db.connect.ConnectToUsersDatabases;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class LoginForm {

	JFrame frame;
	private JTextField emaiilField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm window = new LoginForm();
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
	public LoginForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		ConnectToUsersDatabases dao = new ConnectToUsersDatabases();
		
		frame = new JFrame();
		frame.setTitle("Admin Login Form");
		frame.setBounds(700, 450, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblEmailLabel = new JLabel("Emaiil");
		lblEmailLabel.setBounds(91, 71, 61, 16);
		frame.getContentPane().add(lblEmailLabel);
		
		JLabel lblPasswordLabel = new JLabel("Password");
		lblPasswordLabel.setBounds(91, 152, 61, 16);
		frame.getContentPane().add(lblPasswordLabel);
		
		emaiilField = new JTextField();
		emaiilField.setBounds(198, 66, 130, 26);
		frame.getContentPane().add(emaiilField);
		emaiilField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(198, 147, 130, 26);
		frame.getContentPane().add(passwordField);
		
		JButton btnLoginButton = new JButton("Login");
		btnLoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (emaiilField.getText().isEmpty() && passwordField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill out Email and Password boxes");
					return;
				}
				if (emaiilField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill out Email box");
					return;
				}
				if (passwordField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill out Password box");
					return;
				}
				
				int isUsers = dao.selectIsUsersQuery(emaiilField.getText(), passwordField.getText());
				if (isUsers == 1) {
					LoginForm.main(null);
					JOptionPane.showMessageDialog(null, "This is User Info.\nPlease use admin user.");
					frame.dispose();
					return;
				} else if (isUsers == 2){
					AdminTop.main(null);
					JOptionPane.showMessageDialog(null, "Login as an Admin");
					frame.dispose();
				} else if (isUsers == 3) {
					LoginForm.main(null);
					JOptionPane.showMessageDialog(null, "User doesn't exsist");
					frame.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Something Wrong. Please retry later");
					frame.dispose();
				}
			}
		});
		btnLoginButton.setBounds(147, 212, 117, 29);
		frame.getContentPane().add(btnLoginButton);
		
		JButton btnUserLoginButton = new JButton("Go to User");
		btnUserLoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UserSignInForm.main(null);
					frame.dispose();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnUserLoginButton.setBounds(340, 16, 93, 29);
		frame.getContentPane().add(btnUserLoginButton);
	}
}

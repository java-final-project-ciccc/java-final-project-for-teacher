package ca.java.common.login;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ca.java.admin.top.AdminTop;
import ca.java.db.connect.ConnectToUsersDatabases;
import ca.java.db.environment.DbEnvironments;
import ca.java.users.UserBooksView;

import java.awt.Color;
import javax.swing.JPasswordField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.JSpinner;
import javax.swing.JSlider;
import java.awt.Checkbox;
import javax.swing.JLabel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class UserSignInForm {

	private JFrame frame;
	private JTextField userEmailField;
	private JPasswordField userPasswordField;
	
	
	private static final String ALL_BOOKS_QUERY = "SELECT * FROM BOOKS";
	
	/**
	 * Launch the application.
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		//getConnection();
		//System.out.println("get conn works");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserSignInForm window = new UserSignInForm();
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
	public UserSignInForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setForeground(Color.DARK_GRAY);
		frame.setBackground(Color.DARK_GRAY);
		frame.setBounds(100, 100, 283, 380);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		userEmailField = new JTextField();
		userEmailField.setBackground(Color.LIGHT_GRAY);
		userEmailField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = userEmailField.getText();
				System.out.println(email);
			}
		});
		
		userEmailField.setBounds(49, 101, 179, 45);
		frame.getContentPane().add(userEmailField);
		userEmailField.setColumns(10);
		
		userPasswordField = new JPasswordField();
		userPasswordField.setBackground(Color.LIGHT_GRAY);
		/*
		userPasswordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userPassword = String.valueOf(userPasswordField.getPassword());
				System.out.println(userPassword);
			}
		});
		*/
		userPasswordField.setBounds(49, 174, 179, 45);
		frame.getContentPane().add(userPasswordField);
		
		JButton LogInButton = new JButton("Log in");
		LogInButton.setBackground(Color.LIGHT_GRAY);
		LogInButton.setForeground(Color.BLACK);
		LogInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = userEmailField.getText();
				String userPassword = String.valueOf(userPasswordField.getPassword());
				
				Connection con = null;
				Statement stmt = null;
				ResultSet rs = null;
				
				if(email.equals("")) {
					JOptionPane.showMessageDialog(null, "Enter email");
				} else if (userPassword.equals("")) {
					JOptionPane.showMessageDialog(null, "Enter password");
				} else {
						try {
							con = getConnection();
							stmt = con.createStatement();
							stmt.executeQuery("USE LIBRARY_MGMT");
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						String st = ("SELECT * FROM USERS WHERE EMAIL='"+email+"' AND PASSWORD='"+userPassword+"'");
						
						try {
							
							rs = stmt.executeQuery(st);
							if (rs.next() == false) {
								System.out.println("No user");
								JOptionPane.showMessageDialog(null, "Wrong username or password, try again");
							} else {
								/* ---(Begin) Add function which user is admin or user ---*/
								ConnectToUsersDatabases dao = new ConnectToUsersDatabases();
								
								int isUsers = dao.selectIsUsersQuery(email, userPassword);
								
								if (isUsers == 1) {
									JOptionPane.showMessageDialog(null, "Login as user.");
									UserBooksView userBookView = new UserBooksView();
									userBookView.setVisible(true);	
									frame.dispose();
									return;
								} else if (isUsers == 2){
									UserSignInForm.main(null);
									JOptionPane.showMessageDialog(null, "This is Admin Info.\nPlease use user info.");
									frame.dispose();
								} else if (isUsers == 3) {
									LoginForm.main(null);
									JOptionPane.showMessageDialog(null, "User doesn't exsist");
									return;
								} else {
									JOptionPane.showMessageDialog(null, "Something Wrong. Please retry later");
									frame.dispose();
								}
								/* ---(END) Add function which user is admin or user ---*/

							} 
							
						} catch (SQLException e1) {
							
							// TODO Auto-generated catch block
							e1.printStackTrace();
							
						
						}
				} 
			}	
			});
		
		LogInButton.setBounds(80, 256, 117, 51);
		frame.getContentPane().add(LogInButton);
		
		Checkbox adminCheckbox = new Checkbox("Admin");
		adminCheckbox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {	
				frame.dispose();
				frame.getContentPane().setVisible(false);
				LoginForm window = new LoginForm();
				window.frame.setVisible(true);
			}
		});
		
		adminCheckbox.setBackground(Color.DARK_GRAY);
		adminCheckbox.setForeground(Color.LIGHT_GRAY);
		adminCheckbox.setBounds(156, 10, 117, 23);
		frame.getContentPane().add(adminCheckbox);
		
		JLabel lblNewLabel = new JLabel("Email:");
		lblNewLabel.setForeground(Color.LIGHT_GRAY);
		lblNewLabel.setBounds(32, 84, 61, 16);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel passwLabel = new JLabel("Password:");
		passwLabel.setForeground(Color.LIGHT_GRAY);
		passwLabel.setBounds(32, 158, 106, 16);
		frame.getContentPane().add(passwLabel);
		
		JLabel userLoginLabel = new JLabel("LOG IN AS A USER BELOW");
		userLoginLabel.setForeground(Color.WHITE);
		userLoginLabel.setBackground(Color.DARK_GRAY);
		userLoginLabel.setBounds(32, 56, 151, 16);
		frame.getContentPane().add(userLoginLabel);
	}
	
	public static Connection getConnection() throws SQLException {
		Connection conn = DriverManager.
				getConnection(DbEnvironments.URL, DbEnvironments.USER, DbEnvironments.PASSWORD);
		return conn;
	}
	
	public static ResultSet getData(Connection conn, String query) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}
	
	/*
	public static void printBooks(Connection conn, String query, JFrame frame) throws SQLException {
		System.out.println("You are connected");
		Statement stmt = conn.createStatement();
		ResultSet rsBooks = stmt.executeQuery(query);
		DefaultListModel<String> lm = new DefaultListModel<>();
		while (rsBooks.next()) {
			lm.addElement(rsBooks.getInt("book_id") + " " + rsBooks.getString("title") + " "
					      + rsBooks.getString("author") + " " + (rsBooks.getBoolean("issued") ? "yes" : "no"));
		}
		JList<String> list = new JList<>(lm);
		list.setBounds(100, 100, 100, 100);
		frame.getContentPane().add(list);
		frame.getContentPane().setVisible(true);
	}
	*/
}

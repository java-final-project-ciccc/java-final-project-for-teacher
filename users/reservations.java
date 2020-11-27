package ca.java.users;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTable;

import ca.java.db.environment.DbEnvironments;

public class reservations {

	private JFrame frame;
	private JTable selTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					reservations window = new reservations();
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
	public reservations() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Connection con;
//		con = getConnection();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(29, 18, 392, 105);
		frame.getContentPane().add(textArea);
		
		
		selTable = new JTable();
		selTable.setBounds(29, 41, 388, 75);
		frame.getContentPane().add(selTable);
		
//		UserBooksView.printBooks(con).mouseClicked();
		
	}
	
	public static Connection getConnection() throws SQLException {
		Connection conn = DriverManager.
				getConnection(DbEnvironments.URL, DbEnvironments.USER, DbEnvironments.PASSWORD);
		return conn;
	}
}

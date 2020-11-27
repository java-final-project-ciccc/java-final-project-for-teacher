package ca.java.users;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.layout.RowSpec;

import ca.java.db.connect.ConnectToUsersDatabases;
import ca.java.db.environment.DbEnvironments;

//import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import java.awt.Color;
import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
//import net.miginfocom.swing.MigLayout;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserBooksView {

	private JFrame frmBooksLibrary;
	private JTable booksTable;
	
	
	String[] toolNames = {"Book ID", "Title", "Author", "Available"};

	//ConnectToUsersDatabases dao = new ConnectToUsersDatabases();
	private static final String ALL_BOOKS_QUERY = "SELECT * FROM BOOKS";
	private static final String RQS_BOOKS_QUERY = "INSERT INTO reserved_books(id, title, author, issued) VALUES (?, ?, ?, ?)"; 
	//String st = ("SELECT * FROM USERS WHERE EMAIL='"+email+"' AND PASSWORD='"+userPassword+"'");
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserBooksView window = new UserBooksView();
					window.frmBooksLibrary.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserBooksView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBooksLibrary = new JFrame();
		frmBooksLibrary.getContentPane().setBackground(Color.DARK_GRAY);
		frmBooksLibrary.setTitle("Books library");
		frmBooksLibrary.setBounds(100, 100, 511, 311);
		frmBooksLibrary.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBooksLibrary.getContentPane().setLayout(null);
		
		Connection con;
		
		try {
			con = getConnection();
			booksTable = printBooks(con, ALL_BOOKS_QUERY);
			frmBooksLibrary.getContentPane().add(booksTable);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JScrollPane scrollPane = new JScrollPane(booksTable);
		scrollPane.setBounds(16, 6, 476, 189);
		frmBooksLibrary.getContentPane().add(scrollPane);
		
		JButton requestButton = new JButton("Request the book");
		requestButton.setBackground(Color.LIGHT_GRAY);
		requestButton.setForeground(Color.BLACK);
		requestButton.setBounds(324, 207, 168, 29);
		
		booksTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ArrayList<Object[]> select = new ArrayList<Object[]>();
				Object[] sRow;
				sRow = new Object[4];
				sRow[0] = booksTable.getValueAt(booksTable.getSelectedRowCount(), 0).toString();
				sRow[1] = booksTable.getValueAt(booksTable.getSelectedRowCount(), 1).toString();
				sRow[2] = booksTable.getValueAt(booksTable.getSelectedRowCount(), 2).toString();
				sRow[3] = booksTable.getValueAt(booksTable.getSelectedRowCount(), 3);
				select.add(sRow);
				
				requestButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						Connection con;
						try {
							con = getConnection();
							
							PreparedStatement prst = null;
							prst = con.prepareStatement(RQS_BOOKS_QUERY);
							prst.setObject(1, sRow[0]);
							prst.setObject(2, sRow[1]);
							prst.setObject(3, sRow[2]);
							prst.setObject(4, sRow[3]);
							//prst.executeQuery();
							prst.executeUpdate();
							
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.out.println("request button");
						
						JOptionPane.showMessageDialog(null, "The book was requested");
					}
				});
			}
		});
		
		
		
		frmBooksLibrary.getContentPane().add(requestButton);
		
		JButton logOutButton = new JButton("Log out");
		logOutButton.setBackground(Color.LIGHT_GRAY);
		logOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmBooksLibrary.dispose();
				JOptionPane.showMessageDialog(null, "Successfully log out. See you later.");
				frmBooksLibrary.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			}
		});
		logOutButton.setForeground(Color.BLACK);
		logOutButton.setBounds(390, 241, 102, 29);
		frmBooksLibrary.getContentPane().add(logOutButton);
		
		
		
	}
	
	
	
	public JTable printBooks(Connection conn, String query) throws SQLException {
		//System.out.println("You are connected");
		Statement stmt = conn.createStatement();
		ResultSet rsBooks = stmt.executeQuery(query);
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		Object[] row;
		int count = 0;
		while (rsBooks.next()) {
			row = new Object[4];
			row[0] = rsBooks.getInt("id");
			row[1] = rsBooks.getString("title");
			row[2] = rsBooks.getString("author");
			row[3] = (rsBooks.getBoolean("status")) ? "No" : "Yes";
			rows.add(row);
			count += 1;
		}
		Object[][] aLBooks = new Object[count][];
		for (int i = 0; i < count; i++) {
			aLBooks[i] = rows.get(i);
		}
		String[] columns = {"Book ID", "Title", "Author", "Available"};
		booksTable = new JTable(aLBooks, columns);
		
		booksTable.setFillsViewportHeight(true);
		booksTable.setBackground(Color.DARK_GRAY);
		booksTable.setForeground(Color.WHITE);
		booksTable.setBounds(2, 2, 460, 126);
		return booksTable; 	
		
	}
	
	/*
	private Object selected(Object book_id, Object title, Object author, Object issued) throws SQLException {
		Connection con = getConnection();
		printBooks(con, ALL_BOOKS_QUERY);
		ArrayList<Object[]> selec = new ArrayList<Object[]>();
		/*selec(booksTable.getValueAt(booksTable.getSelectedRow(), 0),
				 booksTable.getValueAt(booksTable.getSelectedRow(), 1),
				 booksTable.getValueAt(booksTable.getSelectedRow(), 2),
				 booksTable.getValueAt(booksTable.getSelectedRow(), 3));
				 
		
		return selec;
	} 
	 */

	public static ResultSet getData (Connection conn, String query) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		return rs;
		//ArrayList<Object[]> rows = new ArrayList<Object[]>();
		//Object[] row;
		
		
				/*
				getValueAt(booksTable.getSelectedRow(), 0);
		booksTable.getValueAt(booksTable.getSelectedRow(), 1);
		booksTable.getValueAt(booksTable.getSelectedRow(), 2);
		booksTable.getValueAt(booksTable.getSelectedRow(), 3);
		System.out.println(booksTable);
		//String bookId = stmt.get
		
		//		(String)table.getValueAt(table.getSelectedRow(), 0),
		*/
		

	}
	
	//public JTable requestedBooks (Connection conn, String query) {	
	//}
	
	public static Connection getConnection() throws SQLException {
		Connection conn = DriverManager.
				getConnection(DbEnvironments.URL, DbEnvironments.USER, DbEnvironments.PASSWORD);
		return conn;
	}

	public void setVisible(boolean b) {
		UserBooksView window = new UserBooksView();
		window.frmBooksLibrary.setVisible(true);	
	}
}

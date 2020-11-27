package ca.java.admin.books;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import ca.java.admin.top.AdminTop;
import ca.java.db.connect.ConnectToBooksDatabases;

@SuppressWarnings("serial")
public class AdminReservedBookStatusLists extends JFrame {
	
  Object[][] data;
  
  String[] columns = {"Book ID", "User ID", "FirstName", "Title", "Author", "Status"};
  
  public AdminReservedBookStatusLists() {
	  
	ConnectToBooksDatabases dao = new ConnectToBooksDatabases(); 
	
	data = dao.selectAllUsersBookStatusQuery();
	if (data == null) {
		JOptionPane.showMessageDialog(null, "Reserved book doesn't exist yet");
		AdminTop.main(null);
		dispose();
	}
	
	setTitle("Users Reserved Books List");
	setBounds(700, 450, 450, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(null);

    JTable table = new JTable(data, columns);
    table.setGridColor(Color.BLACK);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBounds(0, 0, 450, 200);
    
    getContentPane().add(scrollPane);
    
    JButton btnBackButton = new JButton("Go Back");
    btnBackButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		AdminTop.main(null);
    		dispose();
    	}
    });
    btnBackButton.setBounds(164, 226, 117, 29);
    getContentPane().add(btnBackButton);
    
  }
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        AdminReservedBookStatusLists app = new AdminReservedBookStatusLists();
        app.setVisible(true);
      }
    });
  }
}
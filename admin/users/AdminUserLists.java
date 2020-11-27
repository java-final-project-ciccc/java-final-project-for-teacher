package ca.java.admin.users;

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
import ca.java.db.connect.ConnectToUsersDatabases;

@SuppressWarnings("serial")
public class AdminUserLists extends JFrame {
	
  Object[][] data;
  
  String[] columns = {"User ID", "First Name", "Last Name", "Email", "Admin"};
  
  public AdminUserLists() {
	  
	ConnectToUsersDatabases dao = new ConnectToUsersDatabases();
	
	data = dao.selectAllUsersForTableQuery();
	
	setTitle("User Lists");
	setBounds(700, 450, 450, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(null);

    JTable table = new JTable(data, columns);
    table.setGridColor(Color.BLACK);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBounds(0, 0, 450, 200);
    
    getContentPane().add(scrollPane);
    
    JButton btnDeleteButton = new JButton("Delete User");
    btnDeleteButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		boolean isSuccess = dao.deleteQuery((String)table.getValueAt(table.getSelectedRow(), 2));
    		if (isSuccess) {
    			AdminUserLists.main(null);
    			JOptionPane.showMessageDialog(null, "Success delete the record");
        		dispose();
    		} else {
    			AdminUserLists.main(null);
    			JOptionPane.showMessageDialog(null, "Not Success delete the record");
        		dispose();
    		}
    		
    	}
    });
    btnDeleteButton.setBounds(162, 223, 117, 29);
    getContentPane().add(btnDeleteButton);
    
    JButton btnUpdateButton = new JButton("Update Name");
    btnUpdateButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		boolean isSuccess = dao.updateUsersInfoQuery(
    				(String)table.getValueAt(table.getSelectedRow(), 0),
    				(String)table.getValueAt(table.getSelectedRow(), 1),
    				(String)table.getValueAt(table.getSelectedRow(), 2));
    		if (isSuccess) {
    			AdminUserLists.main(null);
    			JOptionPane.showMessageDialog(null, "Success update Name in the record");
        		dispose();
    		} else {
    			AdminUserLists.main(null);
    			JOptionPane.showMessageDialog(null, "Not Success update Name in the record");
        		dispose();
    		}
    		
    	}
    });
    btnUpdateButton.setBounds(25, 223, 117, 29);
    getContentPane().add(btnUpdateButton);
    
    JButton btnBackButton = new JButton("Go Back");
    btnBackButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		AdminTop.main(null);
    		dispose();
    	}
    });
    btnBackButton.setBounds(306, 223, 117, 29);
    getContentPane().add(btnBackButton);
    
  }
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        AdminUserLists app = new AdminUserLists();
        app.setVisible(true);
      }
    });
  }
}
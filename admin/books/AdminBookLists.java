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
public class AdminBookLists extends JFrame {
	
  Object[][] data;
  
  String[] columns = {"Book ID", "Title", "Author", "Issue", "Status"};
  
  public AdminBookLists() {
	  
	ConnectToBooksDatabases dao = new ConnectToBooksDatabases();
	
	data = dao.selectAllBooksForTableQuery();
	
	setTitle("Book Lists");
	setBounds(700, 450, 450, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(null);

    JTable table = new JTable(data, columns);
    table.setGridColor(Color.BLACK);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBounds(0, 0, 450, 200);
    
    getContentPane().add(scrollPane);
    
    JButton btnDeleteButton = new JButton("Delete Book");
    btnDeleteButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		boolean isSuccess = dao.deleteQuery((String)table.getValueAt(table.getSelectedRow(), 0));
    		if (isSuccess) {
    			AdminBookLists.main(null);
    			JOptionPane.showMessageDialog(null, "Success delete the record");
        		dispose();
    		} else {
    			AdminBookLists.main(null);
    			JOptionPane.showMessageDialog(null, "Not Success delete the record");
        		dispose();
    		}
    		
    	}
    });
    btnDeleteButton.setBounds(38, 243, 117, 29);
    getContentPane().add(btnDeleteButton);
    
    JButton btnUpdateButton = new JButton("Update a book info");
    btnUpdateButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		boolean isSuccess = dao.updateBooksInfoQuery(
    				(String)table.getValueAt(table.getSelectedRow(), 1),
    				(String)table.getValueAt(table.getSelectedRow(), 2),
    				(String)table.getValueAt(table.getSelectedRow(), 3),
    				(String)table.getValueAt(table.getSelectedRow(), 0));
    		if (isSuccess) {
    			AdminBookLists.main(null);
    			JOptionPane.showMessageDialog(null, "Success update info in the record");
        		dispose();
    		} else {
    			AdminBookLists.main(null);
    			JOptionPane.showMessageDialog(null, "Not Success update info in the record");
        		dispose();
    		}
    		
    	}
    });
    btnUpdateButton.setBounds(38, 212, 154, 29);
    getContentPane().add(btnUpdateButton);
    
    JButton btnBackButton = new JButton("Go Back");
    btnBackButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		AdminTop.main(null);
    		dispose();
    	}
    });
    btnBackButton.setBounds(276, 243, 117, 29);
    getContentPane().add(btnBackButton);
    
    JButton btnUpdateStatusButton = new JButton("Update Status");
    btnUpdateStatusButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		String inputUserId = JOptionPane.showInputDialog("Please input User Id (who borrow this book?)");
    		try {
    			if (inputUserId.isEmpty()) {
        			JOptionPane.showMessageDialog(null, "Empty! Please try again.");
        			return;
        		}
    			if (!(dao.selectIsUserQuery(inputUserId) == 1)) {
    				JOptionPane.showMessageDialog(null, "User doesn't exist");
    				return;
    			}
    			if (!(dao.selectReservedUserIsCorrectQuery(inputUserId, (String)table.getValueAt(table.getSelectedRow(), 0)) == 1)) {
    				if (!table.getValueAt(table.getSelectedRow(), 4).equals("")) {
    					JOptionPane.showMessageDialog(null, "Incorrect user! Please check user id again.");
        				return;
    	    		}
    			}
    		} catch (NullPointerException ex) {
    			System.out.println(ex + " AdminBookLists.java: Input dialog is null. Please input user id.");
    			return;
    		}
    		
    		boolean hasStatus = false;
    		if (table.getValueAt(table.getSelectedRow(), 4).equals("")) {
    			hasStatus = true;
    		}
   
    		boolean isSuccess = dao.updateBooksStatusQuery(hasStatus, (String)table.getValueAt(table.getSelectedRow(), 0));
    		if (isSuccess) {
    			if (hasStatus) {
    				dao.insertBookRecordQuery(inputUserId, (String)table.getValueAt(table.getSelectedRow(), 0));
    			} else {
    				dao.deleteBookRecordQuery();
    			}
    			
    			AdminBookLists.main(null);
    			JOptionPane.showMessageDialog(null, "Success update book status");
        		dispose();
    		} else {
    			AdminBookLists.main(null);
    			JOptionPane.showMessageDialog(null, "Not Success update book status");
        		dispose();
    		}
    	}
    });
    btnUpdateStatusButton.setBounds(276, 212, 117, 29);
    getContentPane().add(btnUpdateStatusButton);
    
  }
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        AdminBookLists app = new AdminBookLists();
        app.setVisible(true);
      }
    });
  }
}
package ca.java.admin.books;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import ca.java.admin.top.AdminTop;
import ca.java.db.connect.ConnectToBooksDatabases;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminRegisterBook {

	private JFrame frame;
	private JTextField titleField;
	private JTextField authorField;
	private JTextField issueField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminRegisterBook window = new AdminRegisterBook();
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
	public AdminRegisterBook() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		ConnectToBooksDatabases dao = new ConnectToBooksDatabases();
		
		frame = new JFrame();
		frame.setTitle("Register a Book");
		frame.setBounds(700, 450, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		titleField = new JTextField();
		titleField.setBounds(229, 69, 130, 26);
		frame.getContentPane().add(titleField);
		titleField.setColumns(10);
		
		JLabel lblTitleLabel = new JLabel("Title");
		lblTitleLabel.setBounds(85, 74, 95, 16);
		frame.getContentPane().add(lblTitleLabel);
		
		JLabel lblAuthorLabel = new JLabel("Author");
		lblAuthorLabel.setBounds(85, 112, 95, 16);
		frame.getContentPane().add(lblAuthorLabel);
		
		authorField = new JTextField();
		authorField.setBounds(229, 107, 130, 26);
		frame.getContentPane().add(authorField);
		authorField.setColumns(10);
		
		JLabel lblIssueLabel = new JLabel("Issue");
		lblIssueLabel.setBounds(85, 155, 95, 16);
		frame.getContentPane().add(lblIssueLabel);
		
		issueField = new JTextField();
		issueField.setBounds(229, 150, 130, 26);
		frame.getContentPane().add(issueField);
		issueField.setColumns(10);
		
		JButton btnRegisterButton = new JButton("Register");
		btnRegisterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (titleField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill out Title box");
					return;
				}
				if (authorField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill out Author box");
					return;
				}
				if (issueField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill out Issue box");
					return;
				}
				
				boolean isSuccess = dao.insertQuery(titleField.getText(), authorField.getText(), issueField.getText());
				if (isSuccess) {
					AdminTop.main(null);
					JOptionPane.showMessageDialog(null, "Success insert a record");
					frame.dispose();
				} else {
					AdminRegisterBook.main(null);
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
		btnBackButton.setBounds(353, 229, 61, 29);
		frame.getContentPane().add(btnBackButton);
		
	}
}

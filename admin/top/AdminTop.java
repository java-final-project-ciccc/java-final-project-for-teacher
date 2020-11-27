package ca.java.admin.top;

import java.awt.EventQueue;

import javax.swing.JFrame;

import ca.java.admin.books.AdminBookLists;
import ca.java.admin.books.AdminRegisterBook;
import ca.java.admin.books.AdminReservedBookStatusLists;
import ca.java.admin.users.AdminRegisterUser;
import ca.java.admin.users.AdminUserLists;
import ca.java.common.login.LoginForm;
import ca.java.db.connect.ConnectToBooksDatabases;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminTop {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminTop window = new AdminTop();
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
	public AdminTop() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setTitle("Admin Top");
		frame.setBounds(700, 450, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnUserListButton = new JButton("User list");
		btnUserListButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminUserLists.main(null);
				frame.dispose();
			}
		});
		btnUserListButton.setBounds(44, 43, 117, 29);
		frame.getContentPane().add(btnUserListButton);
		
		JButton btnRegisterButton = new JButton("User Register");
		btnRegisterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminRegisterUser.main(null);
				frame.dispose();
			}
		});
		btnRegisterButton.setBounds(44, 105, 117, 29);
		frame.getContentPane().add(btnRegisterButton);
		
		JButton btnGoBackButton = new JButton("Go Back");
		btnGoBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginForm.main(null);
				frame.dispose();
			}
		});
		btnGoBackButton.setBounds(157, 217, 117, 29);
		frame.getContentPane().add(btnGoBackButton);
		
		JButton btnBookListButton = new JButton("Book list");
		btnBookListButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminBookLists.main(null);
				frame.dispose();
			}
		});
		btnBookListButton.setBounds(254, 43, 117, 29);
		frame.getContentPane().add(btnBookListButton);
		
		JButton btnBookRegisterButton = new JButton("Book Register");
		btnBookRegisterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminRegisterBook.main(null);
				frame.dispose();
			}
		});
		btnBookRegisterButton.setBounds(254, 105, 117, 29);
		frame.getContentPane().add(btnBookRegisterButton);
		
		JButton btnReservedBookInfoButton = new JButton("Reseved Book Info");
		btnReservedBookInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminReservedBookStatusLists.main(null);
				frame.dispose();
			}
		});
		btnReservedBookInfoButton.setBounds(254, 160, 159, 29);
		frame.getContentPane().add(btnReservedBookInfoButton);
	}
}

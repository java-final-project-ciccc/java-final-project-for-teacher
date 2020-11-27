package ca.java.db.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ca.java.db.environment.DbEnvironments;

public class ConnectToBooksDatabases {
	
	private final String ALL_BOOK_SELECT_QUERY = "SELECT * FROM books";
	private final String ONE_BOOK_SELECT_QUERY = "SELECT * FROM books WHERE title = ? OR author = ?";
	private final String ONE_COUNT_USER_QUERY = "SELECT COUNT(*) FROM users WHERE id = ?";
	private final String BOOK_COUNT_QUERY = "SELECT COUNT(*) FROM books";
	private final String BOOK_STATUS_COUNT_QUERY = "SELECT COUNT(*) FROM books WHERE books.status = true";
	private final String One_BOOK_USER_STATUS_COUNT_QUERY = "SELECT COUNT(*) FROM book_records, books, users WHERE book_records.user_id = users.id AND book_records.book_id = books.id AND status = true AND users.id = ? AND books.id = ?";
	private final String ADD_INSERT_QUERY = "INSERT INTO books (title, author, issue) VALUES (?, ?, ?)";
	private final String ADD_INSERT_BOOK_RECORD_QUERY = "INSERT INTO book_records (user_id, book_id) VALUES (?, ?)";
	private final String UPDATE_BOOK_INFO_QUERY = "UPDATE books SET title = ?, author = ?, issue = ? WHERE id = ?";
	private final String UPDATE_BOOK_STATUS_QUERY = "UPDATE books SET status = ? WHERE id = ?";
	private final String DELETE_QUERY = "DELETE FROM books WHERE id = ?";
	private final String DELETE_BOOK_RECORD_QUERY = "DELETE FROM book_records WHERE EXISTS (SELECT status FROM books WHERE book_records.book_id = books.id AND status = false);";
	
	private final String ALL_USER_STATUS_QUERY = "SELECT books.id, users.id, first_name, title, author, status" +
											" FROM users, books, book_records" +
											" WHERE book_records.user_id = users.id" +
											" AND book_records.book_id = books.id";
	
	private Connection conn = null;
	private PreparedStatement ps, psCount = null;
	private ResultSet rs, rsCount = null;
	
	private boolean isUpdateSuccess = false;
	
	private Connection connection() throws SQLException {
		Connection conn = DriverManager.getConnection(DbEnvironments.URL, DbEnvironments.USER, DbEnvironments.PASSWORD);
		return conn;
	};
	
	
	private void disconnection() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void selectAllQuery() {
		try {
			conn = connection();
			ps = conn.prepareStatement(ALL_BOOK_SELECT_QUERY);
			rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println("Id: " + rs.getString("id") +
								   " Title: " + rs.getString("title") +
								   " Author: " + rs.getString("author") +
								   " Issue: " + rs.getString("issue") +
								   " Status: " + rs.getString("status"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection();
		}
	}
	
	
	public int selectReservedUserIsCorrectQuery(String userId, String bookId) {
		int isCorrectUser = 0;
		
		try {
			conn = connection();
			ps = conn.prepareStatement(One_BOOK_USER_STATUS_COUNT_QUERY);
			ps.setString(1, userId);
			ps.setString(2, bookId);
			rs = ps.executeQuery();
			
			rs.next();
			isCorrectUser = rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection();
		}
		return isCorrectUser;
		
	}
	
	
	public Object[][] selectAllBooksForTableQuery() {
		Object[][] data = null;
		
		try {
			conn = connection();
			
			psCount = conn.prepareStatement(BOOK_COUNT_QUERY);
			rsCount = psCount.executeQuery();
			rsCount.next();
			int limit = rsCount.getInt(1);
			
			ps = conn.prepareStatement(ALL_BOOK_SELECT_QUERY);
			rs = ps.executeQuery();
			while (rs.next()) {
				data = new Object[limit][];
		        for(int i = 0; i < limit; i++) {
		        	data[i] = new Object[5];
		        }
		        
		        for(int i = 0; i < limit; i++) {
		            for(int j = 0; j < 5; j++) {
		            	if (j == 0) {
		            		data[i][j] = rs.getString("id");
		            	} else if (j == 1) {
		            		data[i][j] = rs.getString("title");
		            	} else if (j == 2) {
		            		data[i][j] = rs.getString("author");
		            	} else if (j == 3) {
		            		data[i][j] = rs.getString("issue");
		            	} else {
		            		Boolean isAdmin = rs.getBoolean("status");
		            		data[i][j] = isAdmin ? "Borrowed" : "";            	
		            		rs.next();
		            	}
		            }
		        }
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rsCount != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (psCount != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			disconnection();
		}
		return data;
	}
	
	
	public Object[][] selectAllUsersBookStatusQuery() {
		Object[][] data = null;
		
		try {
			conn = connection();
			
			psCount = conn.prepareStatement(BOOK_STATUS_COUNT_QUERY);
			rsCount = psCount.executeQuery();
			rsCount.next();
			int limit = rsCount.getInt(1);
			
			ps = conn.prepareStatement(ALL_USER_STATUS_QUERY);
			rs = ps.executeQuery();
			while (rs.next()) {
				data = new Object[limit][];
		        for(int i = 0; i < limit; i++) {
		        	data[i] = new Object[6];
		        }
		        
		        for(int i = 0; i < limit; i++) {
		            for(int j = 0; j < 6; j++) {
		            	if (j == 0) {
		            		data[i][j] = rs.getString("books.id");
		            	} else if (j == 1) {
		            		data[i][j] = rs.getString("users.id");
		            	} else if (j == 2) {
		            		data[i][j] = rs.getString("first_name");
		            	} else if (j == 3) {
		            		data[i][j] = rs.getString("title");
		            	} else if (j == 4) {
		            		data[i][j] = rs.getString("author");
		            	} else {
		            		Boolean isAdmin = rs.getBoolean("status");
		            		data[i][j] = isAdmin ? "Borrowed" : "";            	
		            		rs.next();
		            	}
		            }
		        }
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rsCount != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (psCount != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			disconnection();
		}
		return data;
	}
	
	
	public void selectOneQuery(String title, String author) {
		try {
			conn = connection();
			ps = conn.prepareStatement(ONE_BOOK_SELECT_QUERY);
			ps.setString(1, title);
			ps.setString(2, author);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				if (rs.getString("email").equals(title) || rs.getString("password").equals(author)) {
					System.out.println("Id: " + rs.getString("id") +
							   " First Name: " + rs.getString("first_name") +
							   " Last Name: " + rs.getString("last_name") +
							   " Email: " + rs.getString("email"));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection();
		}
		
	}
	
	public int selectIsUserQuery(String id) {
		int isUser = 0;
		
		try {
			conn = connection();
			ps = conn.prepareStatement(ONE_COUNT_USER_QUERY);
			ps.setString(1, id);
			rs = ps.executeQuery();
			rs.next();
			isUser = rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection();
		}
		return isUser;
	}
	
	
	public boolean insertQuery(String title, String author, String issue) {
		try {
			conn = connection();
			ps = conn.prepareStatement(ADD_INSERT_QUERY);
			ps.setString(1, title);
			ps.setString(2, author);
			ps.setString(3, issue);
			int result = ps.executeUpdate();
			if (result == 1) {
				System.out.println("Success insert record");
				isUpdateSuccess = true;
			} else {
				System.err.println("Something wrong");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection();
		}
		return isUpdateSuccess;
	}
	
	
	public boolean insertBookRecordQuery(String userId, String bookId) {
		try {
			conn = connection();
			ps = conn.prepareStatement(ADD_INSERT_BOOK_RECORD_QUERY);
			ps.setString(1, userId);
			ps.setString(2, bookId);
			int result = ps.executeUpdate();
			if (result == 1) {
				System.out.println("Success insert book record");
				isUpdateSuccess = true;
			} else {
				System.err.println("Something wrong");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection();
		}
		return isUpdateSuccess;
	}
	
	
	
	public boolean updateBooksInfoQuery(String title, String author, String issue, String id) {
		try {
			conn = connection();
			ps = conn.prepareStatement(UPDATE_BOOK_INFO_QUERY);
			ps.setString(1, title);
			ps.setString(2, author);
			ps.setString(3, issue);
			ps.setString(4, id);
			int result = ps.executeUpdate();
			System.out.println(result);
			if (result == 1) {
				System.out.println("Success update book info");
				isUpdateSuccess = true;
			} else {
				System.err.println("Something wrong");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection();
		}
		return isUpdateSuccess;
	}
	
	
	public boolean updateBooksStatusQuery(Boolean status, String id) {
		try {
			conn = connection();
			ps = conn.prepareStatement(UPDATE_BOOK_STATUS_QUERY);
			ps.setBoolean(1, status);
			ps.setString(2, id);
			int result = ps.executeUpdate();
			System.out.println(result);
			if (result == 1) {
				System.out.println("Success update book info");
				isUpdateSuccess = true;
			} else {
				System.err.println("Something wrong");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection();
		}
		return isUpdateSuccess;
	}
	
	
	public boolean deleteQuery(String id) {
		try {
			conn = connection();
			ps = conn.prepareStatement(DELETE_QUERY);
			ps.setString(1, id);
			int result = ps.executeUpdate();
			if (result == 1) {
				System.out.println("Success delete record");
				isUpdateSuccess = true;
			} else {
				System.err.println("Something wrong");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection();
		}
		return isUpdateSuccess;
	}
	
	
	public boolean deleteBookRecordQuery() {
		try {
			conn = connection();
			ps = conn.prepareStatement(DELETE_BOOK_RECORD_QUERY);
			int result = ps.executeUpdate();
			if (result == 1) {
				System.out.println("Success delete book record");
				isUpdateSuccess = true;
			} else {
				System.err.println("Something wrong");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection();
		}
		return isUpdateSuccess;
	}
	
}

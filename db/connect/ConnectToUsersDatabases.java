package ca.java.db.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ca.java.db.environment.DbEnvironments;

public class ConnectToUsersDatabases {
	
	private final String ALL_USER_SELECT_QUERY = "SELECT * FROM users";
	private final String ONE_USER_SELECT_QUERY = "SELECT * FROM users WHERE email = ? and password = ?";
	private final String USER_COUNT_QUERY = "SELECT COUNT(*) FROM users";
	private final String ADD_INSERT_QUERY = "INSERT INTO users (first_name, last_name, email, password, admin_flag) VALUES (?, ?, ?, ?, ?)";
	private final String UPDATE_QUERY = "UPDATE users SET password = ? WHERE email = ? and password = ?";
	private final String UPDATE_USERD_INFO_QUERY = "UPDATE users SET first_name = ?, last_name = ? WHERE email = ?";
	private final String DELETE_QUERY = "DELETE FROM users WHERE email = ?";
	
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
			ps = conn.prepareStatement(ALL_USER_SELECT_QUERY);
			rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println("Id: " + rs.getString("id") +
								   " First Name: " + rs.getString("first_name") +
								   " Last Name: " + rs.getString("last_name") +
								   " Email: " + rs.getString("email"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection();
		}
	}
	
	
	public Object[][] selectAllUsersForTableQuery() {
		Object[][] data = null;
		
		try {
			conn = connection();
			
			psCount = conn.prepareStatement(USER_COUNT_QUERY);
			rsCount = psCount.executeQuery();
			rsCount.next();
			int limit = rsCount.getInt(1);
			
			ps = conn.prepareStatement(ALL_USER_SELECT_QUERY);
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
		            		data[i][j] = rs.getString("first_name");
		            	} else if (j == 2) {
		            		data[i][j] = rs.getString("last_name");
		            	}else if (j == 3) {
		            		data[i][j] = rs.getString("email");
		            	} else {
		            		Boolean isAdmin = rs.getBoolean("admin_flag");
		            		data[i][j] = isAdmin ? "Yes" : "";
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
	
	public void selectOneQuery(String email, String password) {
		try {
			conn = connection();
			ps = conn.prepareStatement(ONE_USER_SELECT_QUERY);
			ps.setString(1, email);
			ps.setString(2, password);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				if (rs.getString("email").equals(email) && rs.getString("password").equals(password)) {
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
	
	
	public int selectIsUsersQuery(String email, String password) {
		int temp = 0;
		boolean isAdmin = false;
		try {
			conn = connection();
			ps = conn.prepareStatement(ONE_USER_SELECT_QUERY);
			ps.setString(1, email);
			ps.setString(2, password);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				if (rs.getString("email").equals(email) && rs.getString("password").equals(password)) {
					System.out.println("Id: " + rs.getString("id") +
							   " First Name: " + rs.getString("first_name") +
							   " Last Name: " + rs.getString("last_name") +
							   " Email: " + rs.getString("email"));
					isAdmin = rs.getBoolean("admin_flag");
				}
			} else {
				System.out.println("Record does not exist");
				temp = -1;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("err");
		} finally {
			disconnection();
		}
		
		if (temp == -1) {
			return 3;
		} else if (isAdmin == true) {
			temp = 2;
		} else if (isAdmin == false) {
			temp = 1;
		}
		return temp;
	}
	
	
	public boolean insertQuery(String first_name, String last_name, String email, String password, boolean adminFlag) {
		try {
			conn = connection();
			ps = conn.prepareStatement(ADD_INSERT_QUERY);
			ps.setString(1, first_name);
			ps.setString(2, last_name);
			ps.setString(3, email);
			ps.setString(4, password);
			ps.setBoolean(5, adminFlag);
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
	
	
	public boolean updateQuery(String SetNewPassword, String email, String password) {
		try {
			conn = connection();
			ps = conn.prepareStatement(UPDATE_QUERY);
			ps.setString(1, SetNewPassword);
			ps.setString(2, email);
			ps.setString(3, password);
			int result = ps.executeUpdate();
			System.out.println(result);
			if (result == 1) {
				System.out.println("Success update password");
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
	
	
	public boolean updateUsersInfoQuery(String firstName, String lastName, String email) {
		try {
			conn = connection();
			ps = conn.prepareStatement(UPDATE_USERD_INFO_QUERY);
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setString(3, email);
			int result = ps.executeUpdate();
			System.out.println(result);
			if (result == 1) {
				System.out.println("Success update user info");
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
	
	
	public boolean deleteQuery(String email) {
		try {
			conn = connection();
			ps = conn.prepareStatement(DELETE_QUERY);
			ps.setString(1, email);
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
	
}

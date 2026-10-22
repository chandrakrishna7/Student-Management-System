package STUDENTS;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.Scanner;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class STUDENT_MANAGEMET {
	

	public static void main(String[] args) throws Exception {

	    Class.forName("com.mysql.cj.jdbc.Driver");

	    HikariConfig config = new HikariConfig();

	    String password = System.getenv("DB_PASSWORD");

	    if (password == null || password.isBlank()) {
	        throw new IllegalStateException("DB_PASSWORD is missing");
	    }

	    config.setJdbcUrl("jdbc:mysql://localhost:3306/student_management");
	    config.setUsername("root");
	    config.setPassword(password);
	    config.setMaximumPoolSize(3);

	    HikariDataSource hd = new HikariDataSource(config);
	    Connection con = hd.getConnection();

	    Scanner sc = new Scanner(System.in);

	    int choice;

	    do {
	        System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
	        System.out.println("1. Add Student");
	        System.out.println("2. View All Students");
	        System.out.println("3. Search Student by ID");
	        System.out.println("4. Update Student");
	        System.out.println("5. Delete Student");
	        System.out.println("6. Course Details");
	        System.out.println("7.  View Course ");
	        System.out.println("8.  Course Enrollment ");
	        System.out.println("9.  View Enrollments ");
	        System.out.println("10.  mark attendence ");
	        System.out.println("11.  View attendences ");
	        System.out.println("12.  Add marks ");
	        System.out.println("13.  update marks ");
	        System.out.println("14.  View marks ");
	        System.out.println("15.  Generate Result ");
	        System.out.println("0. Exit");

	        System.out.print("Enter your choice: ");
	        choice = Integer.parseInt(sc.nextLine());

	        switch (choice) {
	            case 1:
	                addstudent(con, sc);
	                break;

	            case 2:
	                viewStudents(con);
	                break;

	            case 3:
	            	searchstudent(con,sc);
	                break;

	            case 4:
	                updatestudent(con,sc);
	                break;

	            case 5:
	                deletestudent(con,sc);
	                break;
	               
	            case 6:
	            	addcourse(con, sc);
	            	break;
	            	
	            case 7:
	            	viewcourse(con);
	            	break;
	            	
	            case 8:
	            	enrollstudent(con,sc);
	            	break;
	            case 9:
	            	viewenrollments(con);
	            	break;
	            case 10:
	            	markAttendance(con, sc);
	            	break;
	            case 11:
	            	viewAttendance(con);
	            	break;
	            case 12:
	            	addMarks(con, sc);
	            	break;
	            case 13:
	            	updateMarks(con, sc);
	            	break;
	            case 14:
	                viewMarks(con);
	                break;
	            case 15:
	            	generateResult(con,sc);
	            	break;
	            case 0:
	                System.out.println("Exiting Student Management System...");
	                break;

	            default:
	                System.out.println("Invalid choice! Please try again.");
	        }

	    } while (choice != 0);

	    con.close();
	    hd.close();
	    sc.close();
	}
	
	public static void addstudent(Connection con,Scanner sc) throws  Exception{
		System.out.print("Enter student name: ");
        String name = sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter phone: ");
        String phone = sc.nextLine();

        System.out.print("Enter gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter admission date (YYYY-MM-DD): ");
        String dateInput = sc.nextLine();
        
		 String sql="INSERT INTO students (student_name, email, phone, gender, admission_date) VALUES(?,?,? ,?,?)";
		 
		 PreparedStatement st = con.prepareStatement(sql);
		 st.setString(1, name);
		 st.setString(2, email);
		 st.setString(3, phone);
		 st.setString(4, gender);
		 st.setDate(5, Date.valueOf(LocalDate.parse(dateInput)));
		 
		   int rows = st.executeUpdate();
		   
		   if(rows>0) {
			   System.out.println("inserted data sucessfully "+rows);
		   }
		   else {
			   System.out.println("failed");
		   }
		  
		   
	}
	
	public static void viewStudents(Connection con) throws Exception {
		
		String sql="select * from students";
		
		PreparedStatement statement = con.prepareStatement(sql);
		
		ResultSet rs = statement.executeQuery();
		
		while(rs.next()) {
			
			System.out.println("STUDENT ID : "
						+rs.getInt("student_id"));
			
			System.out.println("STUDENT NAME : "
						+rs.getString("student_name"));
			
			System.out.println("STUDENT EMAIL : "
						+rs.getString("email"));
			
			 System.out.println("Phone: "
		                + rs.getString("phone"));

		        System.out.println("Gender: "
		                + rs.getString("gender"));

		        System.out.println("Admission Date: "
		                + rs.getDate("admission_date"));

		        System.out.println("--------------------------");
		}
		rs.close();
		statement.close();
	}
	
	public static void  searchstudent(Connection con,Scanner sc) throws Exception{
		System.out.print("Enter the student id :");
		int id=Integer.parseInt(sc.nextLine());
		
		String sql="select * from students where student_id=?";
		
		PreparedStatement st = con.prepareStatement(sql);
		st.setInt(1,id);
		
		ResultSet rs = st.executeQuery();
		
		if (rs.next()) {
	        System.out.println("Student ID: " + rs.getInt("student_id"));
	        System.out.println("Name: " + rs.getString("student_name"));
	        System.out.println("Email: " + rs.getString("email"));
	        System.out.println("Phone: " + rs.getString("phone"));
	        System.out.println("Gender: " + rs.getString("gender"));
	        System.out.println("Admission Date: " + rs.getDate("admission_date"));
	    } else {
	        System.out.println("Student not found!");
	    }
		
		st.close();
		rs.close();
	}
	
	public static void updatestudent(Connection con,Scanner sc) throws SQLException {
		System.out.println("1. Update Name");
		System.out.println("2. Update Email");
		System.out.println("3. Update Phone");
		System.out.println("4. Update Gender");

		System.out.print("Choose the field to update: ");
		int choice = Integer.parseInt(sc.nextLine());
		int id;
		int rows;
		switch(choice) {
		case 1:
			System.out.print("Enter the student id :");
			 id=Integer.parseInt(sc.nextLine());
			
			System.out.print("Enter student Name :");
			String name=sc.nextLine();
			
			String sql="update students set student_name=? where student_id=?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1,name);
			statement.setInt(2, id);
			 rows = statement.executeUpdate();
			if(rows>0) {
				 System.out.println("Student name updated successfully!");
		    } else {
		        System.out.println("Student ID not found!");
		    
			}
			statement.close();
			break;
		case 2:
			System.out.print("Enter the student id :");
			 id=Integer.parseInt(sc.nextLine());
			
			System.out.print("Enter student email :");
			String email=sc.nextLine();
			
			String sql1="update students set email=? where student_id=?";
			PreparedStatement statements = con.prepareStatement(sql1);
			statements.setString(1,email);
			statements.setInt(2, id);
			 rows = statements.executeUpdate();
			if(rows>0) {
				 System.out.println("Student email updated successfully!");
		    } else {
		        System.out.println("Student ID not found!");
		    
			}
			statements.close();
			break;
		case 3:
			System.out.print("Enter the student id :");
			 id=Integer.parseInt(sc.nextLine());
			
			System.out.print("Enter student phone :");
			String phone=sc.nextLine();
			
			String sql2="update students set phone=? where student_id=?";
			PreparedStatement statement1 = con.prepareStatement(sql2);
			statement1.setString(1,phone);
			statement1.setInt(2, id);
			 rows = statement1.executeUpdate();
			if(rows>0) {
				 System.out.println("Student phone updated successfully!");
		    } else {
		        System.out.println("Student ID not found!");
		    
			}
			statement1.close();
			break;
		case 4:
			System.out.print("Enter the student id :");
			 id=Integer.parseInt(sc.nextLine());
			
			System.out.print("Enter student gender :");
			String gender=sc.nextLine();
			
			String sql3="update students set gender=? where student_id=?";
			PreparedStatement statement2 = con.prepareStatement(sql3);
			statement2.setString(1,gender);
			statement2.setInt(2, id);
			 rows = statement2.executeUpdate();
			if(rows>0) {
				 System.out.println("Student gender updated successfully!");
		    } else {
		        System.out.println("Student ID not found!");
		    
			}
			statement2.close();
			break;
		default:
			System.out.println("!Inavlid input");
		}	
		
	}
	public static void deletestudent(Connection con,Scanner sc) throws SQLException {
		
		System.out.print("Enter the student id :");
		int id=Integer.parseInt(sc.nextLine());
		
		System.out.print("Are you sure to delete "+id+" :");
		
		String sure=sc.nextLine();
		
		if(sure.equalsIgnoreCase("yes")) {
			String sql="delete from students where student_id=?";
			
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, id);
			
			int rows = st.executeUpdate();
			if(rows>0) {
				System.out.println("sucessfully deleted");
			}
			else {
				System.out.println("invalid data");
			}
		}
		else if(sure.equalsIgnoreCase("no")) {
			System.out.println("deletion cancelled");
		}
			
	}
	public static void addcourse(Connection con,Scanner sc) throws SQLException {
		
		System.out.print("Enter student course name: ");
        String cname = sc.nextLine();

        System.out.print("Enter course fee: ");
        double cfee = Double.parseDouble(sc.nextLine());

        System.out.print("Enter course duration in months : ");
        int duration = Integer.parseInt(sc.nextLine());
		
		String sql="insert into courses(course_name,course_fee,duration_months) values(?,?,?)";
		
		PreparedStatement statement = con.prepareStatement(sql);
		
		statement.setString(1, cname);
		statement.setDouble(2, cfee);
		statement.setInt(3, duration);
		
		int rows = statement.executeUpdate();
		
		if(rows>0) {
			System.out.println("Data Inserted sucessful");
		}
		else {
			System.out.println("Not Inserted");
		}
	}
	public static void viewcourse(Connection con) throws SQLException {
		
		String sql="select * from courses";
		
		PreparedStatement st = con.prepareStatement(sql);
		
		ResultSet rs = st.executeQuery();
		
		while(rs.next()) {
			System.out.println("course_id :"+rs.getInt("course_id"));
			System.out.println("course_name :"+rs.getString("course_name"));
			System.out.println("course_fee :"+rs.getDouble("course_fee"));
			System.out.println("duration in months :"+rs.getInt("duration_months"));
		}
	}
	

public static void enrollstudent(Connection con, Scanner sc)
        throws SQLException {

    System.out.print("Enter the student id: ");
    int stuid = Integer.parseInt(sc.nextLine());

    System.out.print("Enter the course id: ");
    int couid = Integer.parseInt(sc.nextLine());

    boolean oldAutoCommit = con.getAutoCommit();

    try {
        con.setAutoCommit(false);

        String sql = "INSERT INTO enrollments "
                + "(student_id, course_id, enrollment_date) "
                + "VALUES (?, ?, ?)";

        PreparedStatement statement = con.prepareStatement(sql);

        statement.setInt(1, stuid);
        statement.setInt(2, couid);
        statement.setDate(3, Date.valueOf(LocalDate.now()));

        int rows = statement.executeUpdate();

        con.commit();

        if (rows > 0) {
            System.out.println("Enrollment successful!");
        }

        statement.close();

    } catch (SQLException e) {
        con.rollback();
        System.out.println("Enrollment failed. Changes rolled back.");
        throw e;

    } finally {
        con.setAutoCommit(oldAutoCommit);
    }

	}
		public static void viewenrollments(Connection con) throws SQLException {
			
			String sql="SELECT\r\n"
					+ "    e.enrollment_id,\r\n"
					+ "    s.student_name,\r\n"
					+ "    c.course_name,\r\n"
					+ "    e.enrollment_date\r\n"
					+ "FROM enrollments e\r\n"
					+ "JOIN students s ON e.student_id = s.student_id\r\n"
					+ "JOIN courses c ON e.course_id = c.course_id";
			
			PreparedStatement statement = con.prepareStatement(sql);
			
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {
				System.out.println("Enrollment id :"+rs.getInt("enrollment_id"));
				System.out.println("student name :"+rs.getString("student_name"));
				System.out.println("course name :"+rs.getString("course_name"));
				System.out.println("Enrollment date :"+rs.getDate("enrollment_date"));
			}
			rs.close();
			statement.close();
			
		}
		public static void markAttendance(Connection con, Scanner sc)
		        throws SQLException {
		    System.out.print("Enrollment id :");
		    int Id = Integer.parseInt(sc.nextLine());
		    String checkSql =
		            "SELECT enrollment_id FROM enrollments WHERE enrollment_id = ?";

		    PreparedStatement checkSt = con.prepareStatement(checkSql);
		    checkSt.setInt(1, Id);

		    ResultSet rs = checkSt.executeQuery();

		    if (!rs.next()) {
		        System.out.println("Invalid enrollment ID!");
		        rs.close();
		        checkSt.close();
		        return;
		    }

		    rs.close();
		    checkSt.close();
		    System.out.print("Enter attendance status (Present/Absent): ");
		    String status = sc.nextLine();
		  
		    if (!status.equalsIgnoreCase("Present")
		            && !status.equalsIgnoreCase("Absent")) {
		        System.out.println("Invalid status! Enter Present or Absent.");
		        return;
		    }
		   
		    String sql="INSERT INTO attendance (enrollment_id, attendance_date, status)\r\n"
		    		+ "VALUES (?, ?, ?)";
		    PreparedStatement st = con.prepareStatement(sql);
		    
		    st.setInt(1, Id);
		    st.setDate(2,Date.valueOf(LocalDate.now()));
		    st.setString(3, status);
		    try {
		        int rows = st.executeUpdate();

		        if (rows > 0) {
		            System.out.println("Attendance marked successfully!");
		        }
		    } catch (SQLIntegrityConstraintViolationException e) {
		        System.out.println("Attendance has already been marked for this enrollment today!");
		    } 
		    finally {
		        st.close();
		    }
		}
		public static void viewAttendance(Connection con)
		        throws SQLException {
		    String sql="SELECT\r\n"
		    		+ "    s.student_name,\r\n"
		    		+ "    c.course_name,\r\n"
		    		+ "    a.attendance_date,\r\n"
		    		+ "    a.status\r\n"
		    		+ "FROM attendance a\r\n"
		    		+ "JOIN enrollments e\r\n"
		    		+ "    ON a.enrollment_id = e.enrollment_id\r\n"
		    		+ "JOIN students s\r\n"
		    		+ "    ON e.student_id = s.student_id\r\n"
		    		+ "JOIN courses c\r\n"
		    		+ "    ON e.course_id = c.course_id";
		    
		    PreparedStatement st = con.prepareStatement(sql);
		    ResultSet rs = st.executeQuery();
		    while(rs.next()) {
		    	System.out.println("student name :"+rs.getString("student_name"));
		    	System.out.println("course name :"+rs.getString("course_name"));
		    	System.out.println("attendence date :"+rs.getDate("attendance_date"));
		    	System.out.println("status :"+rs.getString("status"));
		    }
		    rs.close();
		    st.close();
		}
		public static void addMarks(Connection con, Scanner sc)
		        throws SQLException {
			System.out.print("Enrollment Id :");
			int Id = Integer.parseInt(sc.nextLine());

			System.out.print("Subject Name :");
			String name = sc.nextLine();

			System.out.print("Marks Obtained :");
			int marks = Integer.parseInt(sc.nextLine());

			System.out.print("Max Marks :");
			int maxmarks = Integer.parseInt(sc.nextLine());
		   
		   String sql="INSERT INTO marks\r\n"
		   		+ "    (enrollment_id, subject_name, marks_obtained, maximum_marks)\r\n"
		   		+ "VALUES (?, ?, ?, ?)";
		   
		   PreparedStatement st = con.prepareStatement(sql);
		   
		   st.setInt(1, Id);
		   st.setString(2, name);
		   st.setInt(3, marks);
		   st.setInt(4, maxmarks);
		   try {
			   int rows = st.executeUpdate();
		 
		  
		   if(rows>0) {
			   System.out.println("succesfully Inserted");
		   }
		   }
		   catch (SQLIntegrityConstraintViolationException e) {
			    System.out.println("Marks already entered for this subject and enrollment!");
			}
		  finally {
			  st.close();
		}
	  
		}
		public static void updateMarks(Connection con, Scanner sc)
		        throws SQLException {
			System.out.print("Enrollment Id :");
			int Id = Integer.parseInt(sc.nextLine());
			System.out.print("Subject Name :");
			String name = sc.nextLine();
			System.out.print("New Marks :");
			int newmarks = Integer.parseInt(sc.nextLine());
			System.out.print("Max Marks :");
			int maxmarks = Integer.parseInt(sc.nextLine());
			
			
			String sql="UPDATE marks\r\n"
					+ "SET marks_obtained = ?, maximum_marks = ?\r\n"
					+ "WHERE enrollment_id = ? AND subject_name = ?";
			
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, newmarks);
			st.setInt(2, maxmarks);
			st.setInt(3, Id);
			st.setString(4, name);
			if (newmarks < 0 || maxmarks <= 0 || newmarks > maxmarks) {
			    System.out.println("Invalid marks! Please check the values.");
			    st.close();
			    return;
			}
			int rows = st.executeUpdate();
			if (rows > 0) {
			    System.out.println("Marks updated successfully!");
			} else {
			    System.out.println("No marks found for this enrollment and subject.");
			}

			st.close();
			
		}
		public static void viewMarks(Connection con) throws SQLException {

		    String sql = "SELECT s.student_name, c.course_name, "
		            + "m.subject_name, m.marks_obtained, m.maximum_marks "
		            + "FROM marks m "
		            + "JOIN enrollments e ON m.enrollment_id = e.enrollment_id "
		            + "JOIN students s ON e.student_id = s.student_id "
		            + "JOIN courses c ON e.course_id = c.course_id";

		    PreparedStatement st = con.prepareStatement(sql);
		    ResultSet rs = st.executeQuery();

		    while (rs.next()) {
		        System.out.println("Student Name: " + rs.getString("student_name"));
		        System.out.println("Course Name: " + rs.getString("course_name"));
		        System.out.println("Subject: " + rs.getString("subject_name"));
		        System.out.println("Marks Obtained: " + rs.getInt("marks_obtained"));
		        System.out.println("Maximum Marks: " + rs.getInt("maximum_marks"));
		        System.out.println("-----------------------------");
		    }

		    rs.close();
		    st.close();
		}

public static void generateResult(Connection con,Scanner sc) throws SQLException {
	System.out.print("Enter Student ID: ");
	int id = Integer.parseInt(sc.nextLine());
	String sql = "SELECT s.student_id, s.student_name, "
	        + "m.subject_name, m.marks_obtained, m.maximum_marks "
	        + "FROM marks m "
	        + "JOIN enrollments e ON m.enrollment_id = e.enrollment_id "
	        + "JOIN students s ON e.student_id = s.student_id "
	        + "WHERE s.student_id = ?";

    PreparedStatement st = con.prepareStatement(sql);
    st.setInt(1, id);
    ResultSet rs = st.executeQuery();

    int total = 0;
    int max = 0;
    boolean pass = true;
    boolean found = false;

    while (rs.next()) {
        found = true;

        String name = rs.getString("student_name");
        String subject = rs.getString("subject_name");
        int marks = rs.getInt("marks_obtained");
        int maximum = rs.getInt("maximum_marks");

        System.out.println("Student: " + name);
        System.out.println("Subject: " + subject);
        System.out.println("Marks: " + marks + "/" + maximum);

        total += marks;
        max += maximum;

        if (marks * 100.0 / maximum < 40) {
            pass = false;
        }
    }

    if (found) {
        System.out.println("Total Marks: " + total + "/" + max);
        System.out.println("Percentage: " + (total * 100.0 / max) + "%");
        System.out.println("Result: " + (pass ? "PASS" : "FAIL"));
    } else {
        System.out.println("No marks found!");
    }

    rs.close();
    st.close();
}

}

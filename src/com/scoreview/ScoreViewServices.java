package com.scoreview;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@Path("/scoreview/")
public class ScoreViewServices {
	private Session setEmailSession(String email)
	{
		final String username = "hongloc2206@gmail.com";//Change to your email
		final String password = "kimthuong1125";//Change to your email password

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
		return session;
	}
	public String OTP(int leng)
	{
		// Using numeric values 
        String numbers = "0123456789"; 
  
        // Using random method 
        Random rndm_method = new Random(); 
  
        char[] otp = new char[leng]; 
  
        for (int i = 0; i < leng; i++) 
        { 
            // Use of charAt() method : to get character value 
            // Use of nextInt() as it is scanning the value as int 
        	otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length())); 
        }
        
        return String.valueOf(otp); 
	}
	public void sendOTP2(String email)
	{
		Session temp = setEmailSession(email);
		if(temp != null)
		{	
			String otpCode = OTP(5);
			try {
				try {
				Connection conn = new ConnectDB().getConnection();
				String sql = "insert into otp values(?,?)";
				PreparedStatement stm = conn.prepareStatement(sql);
				stm.setString(1, otpCode);
				stm.setInt(2,0);
				stm.execute();
				conn.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				Message message = new MimeMessage(temp);
				message.setFrom(new InternetAddress("hongloc2206@gmail.com"));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
				message.setSubject("OTP verify code");
				message.setText("Dear " + email + ","
						+ "\n\n Your verify code is " + otpCode + " ."
						+ "\n\nRegards,"
						+ "\niBanking.");
				
				Transport.send(message);
				System.out.println("Done");
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}	
		}
	}
	public boolean checkOTP(String otp) {
		try {
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				conn = new ConnectDB().getConnection();
				stmt = conn.createStatement();
				String sql = "select *"
						+ "from otp";
				rs = stmt.executeQuery(sql);
				if(otp.equals(rs.getString(1))) {
					conn.close();
					return true;
				}
				conn.close();
				return false;
			}
			finally {
				try { if (rs!=null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if (stmt!=null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if (conn!=null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean checkStudent(String email, String name) {
		try {
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				conn = new ConnectDB().getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT *"
						+ "FROM student"
								);
				System.out.println("check valid user:");
				while (rs.next())
				{
					if(rs.getString(3).equals(email)&&rs.getString(4).equals(name))
					{
						System.out.println("Not valid");
						conn.close();
						return false;
					}
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Valid");
		return true;
	}
	@POST
	@Path("/sign-in/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean signIn(@FormParam("username") String username, @FormParam("password") String password)
	{	
			try {
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				try {
					conn = new ConnectDB().getConnection();
					stmt = conn.createStatement();
					rs = stmt.executeQuery("SELECT * FROM student");
					while (rs.next())
					{
						// Write your code here
						String u = rs.getString("student_id");
						String p = rs.getString("password");
						if(username.equals(u) && password.equals(p))
						{
							return true;
						}
					}
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
					try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
					try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
	}
	@POST
	@Path("/sign-up")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean signUp(@FormParam("username") String username, @FormParam("password") String password,
			@FormParam("email") String email, @FormParam("name") String name) {
		if(checkStudent(email,name)) {
			try {
				Connection conn = null;
				try {
					conn = new ConnectDB().getConnection();
					PreparedStatement stm = conn.prepareStatement("INSERT INTO student VALUES(?,?,?,?)");
					stm.setString(1, username);
					stm.setString(2, password);
					stm.setString(3, name);
					stm.setString(4, email);
					stm.executeUpdate();
					conn.close();
					sendOTP2(email);
					return true;
				}
				catch (Exception e){
					e.printStackTrace();
				}
				finally {
					try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	return false;
	}
	@POST
	@Path("/get-data/{username}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public StudentSubject getData(@PathParam("username") String id) {
		StudentSubject studentSubject = new StudentSubject();
		try {
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				conn = new ConnectDB().getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT *"
						+ "FROM student_subject"
								);
				while (rs.next())
				{			
					if(id.equals(rs.getString(1)))
					{	
						studentSubject.setId(rs.getString("student_id"));
						studentSubject.setSubid(rs.getString("subj_id"));
						return studentSubject;
					}
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(id);
		
		return studentSubject;
	}
	@POST
	@Path("/get-data2/{subid}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Result getData2(@PathParam("subid") String subid) {
		Result studentResult = new Result();
		try {
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				conn = new ConnectDB().getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT *"
						+ "FROM result"
								);
				while (rs.next())
				{			
					if(subid.equals(rs.getString(1)))
					{	
						
						studentResult.setScore(rs.getInt(2));
						studentResult.setSubid(rs.getString(1));
						studentResult.setDate(rs.getDate(3));
						return studentResult;
					}
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
				try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return studentResult;
	}
	@POST
	@Path("/verifyOTP/{otpcode}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String verifyOTP(@PathParam("otpcode") String otpcode) {
		try {
			Connection conn = null;
			try {
				conn = new ConnectDB().getConnection();
				PreparedStatement stm = conn.prepareStatement("select * from otp where otp_code='"+otpcode+"'");
				ResultSet rs = stm.executeQuery();
				while(rs.next())
				{	
					if(rs.getString(1).equals(otpcode)) {
						stm.executeUpdate("UPDATE otp "
								+ "SET status = 1 "
								+ "WHERE otp_code='"+otpcode+"';");
						conn.close();
						return "Success verify otp";
					}
				}
			}finally {
				if (conn!=null) conn.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "OTP Khong dung";
	}
	@POST
	@Path("/verifyOTP/{otpcode}/{username}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String studentVerify(@PathParam("otpcode") String otpcode, @PathParam("username") String username) {
		try {
			Connection conn = null;
			try {
				conn = new ConnectDB().getConnection();
				PreparedStatement stm = conn.prepareStatement("insert into student_otp_verify values(?,?)");
				stm.setString(1, otpcode);
				stm.setString(2, username);
				stm.executeUpdate();
				return "Okay";
			}finally {
				if (conn!=null) conn.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "Not okay";
	}
	@POST
	@Path("/application-form/{subid}/{stuid}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String getApplicationForm(@PathParam("subid") String subid, @PathParam("stuid") String stuid) {
		try {
			Connection conn = new ConnectDB().getConnection();
			PreparedStatement stm = conn.prepareStatement("insert into dpk values(?,?,?)");
			stm.setString(3,subid);
			stm.setString(2, stuid);
			stm.setString(1, "1");
			stm.executeUpdate();
			stm = conn.prepareStatement("select * from student where student_id='"+stuid+"'");
			ResultSet rs = stm.executeQuery();
			String email = null;
			while(rs.next()) {
				if(rs.getString(1).equals(stuid)) {
					email = rs.getString(4);
					System.out.println(email);
				}
			}
			conn.close();
			return "Nhan duoc don";
		}catch (Exception e){
			e.printStackTrace();
		}
		return "Khong nhan duoc don";
	}
}


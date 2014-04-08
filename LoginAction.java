package com.gwu.action.login;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailException;
import org.apache.struts2.ServletActionContext;

import com.gwu.common.GwuParameters;
import com.gwu.common.GwuTimeZone;
import com.gwu.common.Logs;
import com.gwu.dao.ScoreBoard.ScoreBoardDAO;
import com.gwu.dao.contentlibrary.NewContentLibraryDAO;
import com.gwu.dao.course.CourseDAO;
import com.gwu.dao.student.StudentDAO;
import com.gwu.dao.tutor.TutorDAO;
import com.gwu.dao.user.UserDAO;
import com.gwu.entities.ScoreBoard.TblScoreBoard;
import com.gwu.entities.student.TblStudent;
import com.gwu.entities.tutor.TblTutor;
import com.gwu.entities.user.TblLoginHistory;
import com.gwu.entities.user.TblUser;
import com.gwu.entities.user.TblUserType;
import com.gwu.mail.MailService;
import com.gwu.security.AccountService;
import com.gwu.security.EncryptDecrypt;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String className = "LoginAction";
	private String userName;
	private String password;
	private String email;
	private String sendto;
	private int userId;
	private String oldPassword;
	private String timeZoneOffset;
	private String timezone;
	

	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	// login action
	public String doLogin() throws Exception {
		
		Logs.printInfoLog(className, "Inside doLogin() method");
		HttpServletRequest request = ServletActionContext.getRequest();
		UserDAO userD = new UserDAO();

		int isFacebookId = 0;
		Map<String, Integer> map = null;
		int iUserID = 0;
		int isActive = 0;
		short isBlocked = 0;
		int iTypeId = 0;
		GwuParameters parameters = new GwuParameters();
		HttpSession session = ServletActionContext.getRequest().getSession();

		int offset=Integer.parseInt(timeZoneOffset);
		GwuTimeZone gwuTimeZone=new GwuTimeZone();
		String timeZoneId=gwuTimeZone.clientTimeZoneID(offset);
		
		try {
			Logs.printInfoLog(className, "Inside try block of doLogin() method");
			ServletContext context=session.getServletContext();
			map = userD.checkLogin(userName,EncryptDecrypt.getMessageDigest(password.getBytes()));
			
			if (map != null & map.size() > 0) {			
				Logs.printInfoLog(className, "Correct username and password");
				iUserID = map.get("iUserId");
				isActive = map.get("isActive");
				iTypeId = map.get("iTypeId");
				System.out.println("iUserId: " + iUserID + " isActive: " + isActive + " iTypeId: " + iTypeId);
				parameters.initializeParapeters(iUserID, iTypeId, timezone);
				@SuppressWarnings("unchecked")
				ArrayList<String> activeUser=(ArrayList<String>) context.getAttribute("activeUser");
				activeUser.add(new Integer(iUserID).toString());
				
				session.setAttribute("parameters", parameters);
			}
			
			
			
			if (iTypeId > 0 & isActive == 1) {
				if (iTypeId == TblUserType.USER_ADMIN){
					request.setAttribute("UserType", 1);
					updateLoginHistory( iUserID,timeZoneId);
				}
				else if (iTypeId == TblUserType.USER_TUTOR){
					String name = "";
					TblTutor tutor = new TblTutor();
					request.setAttribute("UserType", 2);
					int tId = parameters.getTutorId();
					TutorDAO dao = new TutorDAO();
					Map mapTC = null;
					mapTC = dao.getTutorCourses(tId);

					ArrayList<Integer> tCourseIds = (ArrayList<Integer>)mapTC.get("tCourseIds");
					if(tCourseIds.size() > 0){
						int courseId = tCourseIds.get(0);
						CourseDAO courseDAO = new CourseDAO();
						String course = courseDAO.getCourseNameById(courseId);
						request.setAttribute("course", course);
					}
					try {
						tutor = dao.getTutorData(tId);
						
						if(tutor.getsFacebookId() != null && tutor.getsFacebookId() != "")
							isFacebookId = 1;
						
						name = tutor.getsFirstName()==null?"":tutor.getsFirstName();
						isBlocked = tutor.getiIsActive();
						
						if(isBlocked == 1){
							request.setAttribute("ErrorType",3);
						}
						/*else if(!userD.checkUserAvailability(iUserID)){
							String code = EncryptDecrypt.encodeString(parameters.getEmail());
							request.setAttribute("Step", 1);
							request.setAttribute("code", code);							
						}*/
						else if(name.equals("")){
							request.setAttribute("Step",2);
						}
						else {
							request.setAttribute("Step",3);
							updateLoginHistory( iUserID,timeZoneId);
						}
					} catch (Exception ex) {
						Logs.printErrorLog(className, ex.getMessage());
						addActionError("Login failed. Please try again!");
						return INPUT;
					}
				}
				else if (iTypeId == TblUserType.USER_STUDENT){
					
					
					String clLink = "index.jsp?opencl=yes";
					if(request.getServerPort()==80 || request.getServerPort()==443){
						clLink=request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/"+clLink;
					}else{
						clLink=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+""+request.getContextPath()+"/"+clLink;
					}
					String name = "";
					request.setAttribute("UserType", 3);
					request.setAttribute("clLink", clLink);
					int sId = parameters.getStudentId();
					StudentDAO dao = new StudentDAO();
					TblStudent student = new TblStudent();
					int courseId = student.getiCourseId();
					CourseDAO courseDAO = new CourseDAO();
					String course = courseDAO.getCourseNameById(courseId);
					request.setAttribute("course", course);
					
					
					
					try {
						student = dao.getStudentData(sId);

						if(student.getsFacebookId() != null && student.getsFacebookId() != "")
							isFacebookId = 1;
						
						name = student.getsFirstName()==null?"":student.getsFirstName();
						isBlocked = student.getiIsActive();
						
						/*if(isBlocked == 1){
							request.setAttribute("ErrorType",3);
						}
						else if(!userD.checkUserAvailability(iUserID)){
							String code = EncryptDecrypt.encodeString(parameters.getEmail());
							request.setAttribute("Step", 1);
							request.setAttribute("code", code);	
						}
						else if(name.equals("")){
							request.setAttribute("Step",2);
						}
						else {
							request.setAttribute("Step",3);
							updateLoginHistory( iUserID,timeZoneId);
						}*/
						request.setAttribute("Step",3);
						//updateLoginHistory( iUserID,timeZoneId);
					} catch (Exception ex) {
						Logs.printErrorLog(className, ex.getMessage());
						addActionError("Login failed. Please try again!");
						return INPUT;
					}
					
				}
				
				
			}else if (iTypeId > 0 & isActive == 0) {
				/*addActionMessage("Your Account is not verified! Please verify your account.");*/
				request.setAttribute("accStatus",1);
			} else {
				boolean exists = userD.isUserExist(userName);
				if(exists){
					//user already signuped with facebook
					if( isFacebookId == 1)
					{
						/*addActionError("Oops. Username and password do not match. Please retry.");*/
						request.setAttribute("ErrorType",10);

					}
					else
					{
						/*addActionError("Oops. Username and password do not match. Please retry.");*/
						request.setAttribute("ErrorType",1);
					}


				}else{
					/*addActionError("Please sign up before login");*/
					request.setAttribute("ErrorType",2);
				}
			}

		} catch (Exception e) {
			/*addActionError("Login Failed. Retry...");*/
			request.setAttribute("ErrorType",0);
			Logs.printErrorLog(className, e.getMessage());
			return SUCCESS;
		}
		
		return SUCCESS;
	}
	
	// direct login after saving availability
	public String doDirectLogin(){
		Logs.printInfoLog(className, "Inside doDirectLogin() method");
		HttpServletRequest request = ServletActionContext.getRequest();
		UserDAO userD = new UserDAO();
		int isFacebookId = 0;
		
		int iUserID = 0;
		
		GwuParameters parameters = new GwuParameters();
		HttpSession session = ServletActionContext.getRequest().getSession();

		try {
			iUserID = userD.getUserID(userName);
			TblUser user = new TblUser();
			user = userD.getUserById(iUserID);
			
			
			int isActive = user.getIsActive();
			short isBlocked = 0;
			int iTypeId = user.getiTypeId();
			
			Logs.printInfoLog(className, "Inside try block of doDirectLogin() method");
			ServletContext context=session.getServletContext();
			parameters.initializeParapeters(iUserID, iTypeId, "");
			ArrayList<String> activeUser=(ArrayList<String>) context.getAttribute("activeUser");
			activeUser.add(new Integer(iUserID).toString());
			session.setAttribute("parameters", parameters);			
			
			if (iTypeId > 0 & isActive == 1) {
				if (iTypeId == TblUserType.USER_TUTOR){
					String name = "";
					TblTutor tutor = new TblTutor();
					request.setAttribute("UserType", 2);
					int tId = parameters.getTutorId();
					TutorDAO dao = new TutorDAO();
					Map mapTC = null;
					mapTC = dao.getTutorCourses(tId);
					
					

					ArrayList<Integer> tCourseIds = (ArrayList<Integer>)mapTC.get("tCourseIds");
					if(tCourseIds.size() > 0){
						int courseId = tCourseIds.get(0);
						CourseDAO courseDAO = new CourseDAO();
						String course = courseDAO.getCourseNameById(courseId);
						request.setAttribute("course", course);
					}
					try {
						tutor = dao.getTutorData(tId);
						
						if(tutor.getsFacebookId() != null && tutor.getsFacebookId() != "")
							isFacebookId = 1;
				
						
						name = tutor.getsFirstName()==null?"":tutor.getsFirstName();
						isBlocked = tutor.getiIsActive();
						if(isBlocked == 1){
							request.setAttribute("ErrorType",3);
						}
						else if(name.equals("")){
							request.setAttribute("Step",2);
						}
						else {
							request.setAttribute("Step",3);
						}
					} catch (Exception ex) {
						Logs.printErrorLog(className, ex.getMessage());
						addActionError("Login failed. Please try again!");
						return INPUT;
					}
				}
				else if (iTypeId == TblUserType.USER_STUDENT){
					String name = "";
					request.setAttribute("UserType", 3);
					int sId = parameters.getStudentId();
					StudentDAO dao = new StudentDAO();
					TblStudent student = new TblStudent();
					int courseId = student.getiCourseId();
					CourseDAO courseDAO = new CourseDAO();
					String course = courseDAO.getCourseNameById(courseId);
					request.setAttribute("course", course);
					
					
					
					try {
						student = dao.getStudentData(sId);
						
						if(student.getsFacebookId() != null && student.getsFacebookId() != "")
							isFacebookId = 1;
				
						
						name = student.getsFirstName()==null?"":student.getsFirstName();
						isBlocked = student.getiIsActive();
						if(isBlocked == 1){
							request.setAttribute("ErrorType",3);
						}
						/*else if(name.equals("")){
							request.setAttribute("Step",2);
						}*/
						else {
							request.setAttribute("Step",3);
						}
					} catch (Exception ex) {
						Logs.printErrorLog(className, ex.getMessage());
						addActionError("Login failed. Please try again!");
						return INPUT;
					}
					
				}
				
				
			}else if (iTypeId > 0 & isActive == 0) {
				/*addActionMessage("Your Account is not verified! Please verify your account.");*/
				request.setAttribute("accStatus",1);
			} else {
				boolean exists = userD.isUserExist(userName);
				if(exists){
					
					//user already signuped with facebook
					if( isFacebookId == 1)
					{
						/*addActionError("Oops. Username and password do not match. Please retry.");*/
						request.setAttribute("ErrorType",10);

					}
					else
					{
						/*addActionError("Oops. Username and password do not match. Please retry.");*/
						request.setAttribute("ErrorType",1);
					}

				}else{
					/*addActionError("Please sign up before login");*/
					request.setAttribute("ErrorType",2);
				}
			}

		} catch (Exception e) {
			/*addActionError("Login Failed. Retry...");*/
			request.setAttribute("ErrorType",0);
			Logs.printErrorLog(className, e.getMessage());
			return SUCCESS;
		}
		return SUCCESS;
	}

	// Forgot Password
	public String forgotPassowd() throws Exception {
		return SUCCESS;
	}

	// Change Password Request
	public String changePassowdReq() throws Exception {
		HttpServletRequest request = (HttpServletRequest) ActionContext
				.getContext().get(ServletActionContext.HTTP_REQUEST);
		UserDAO dao = new UserDAO();
		if (dao.isUserExist(email)) {
			if (dao.isUserActive(email)) {
				AccountService service = new AccountService();
				service.setPasswordChangeLink(request, email);
				final String message = service.getMessageBodyForPassword(email);
				
				new Thread(new Runnable() {
					public void run() {
						MailService service2 = new MailService();
						try {
							service2.sendAccountVerificationMail("Change Password Link", email, message, true);
						} catch (EmailException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();		
				
				addActionError("Reset Password link has been sent to your registered email address.");
				return SUCCESS;
			} else {
				addActionError("Your are registered with CultureAlley but your account is not activated.");
				return SUCCESS;
			}

		} else {
			addActionError("This email id is not registered with CultureAlley yet!");
			return SUCCESS;
		}

	}

	// Update Password
	public String updatePassowd() throws Exception {
		UserDAO dao = new UserDAO();
		HttpServletRequest request=(HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
		boolean flag = dao.updatePassword(email, password,dao.getUserID(email));
		
		if (flag) {
			request.setAttribute("status","1");
			addActionMessage("Password changed successfully!");
			return SUCCESS;
		} else {
			request.setAttribute("status","2");
			addActionError("Unable to change password! Try later.");
			return SUCCESS;
		}

	}

	// Verify Change password Request

	public String verifyChangePassword() throws Exception {
		HttpServletRequest request = (HttpServletRequest) ActionContext
				.getContext().get(ServletActionContext.HTTP_REQUEST);
		String code = request.getParameter("code");
		boolean flag = false;
		if (code != null) {
			UserDAO dao = new UserDAO();
			flag = dao.isUserExist(EncryptDecrypt.decodeString(code));
		}
		if (flag) {
			request.setAttribute("email", EncryptDecrypt.decodeString(code));
			return SUCCESS;
		} else {
			return "failure";
		}
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setTimeZoneOffset(String timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public String getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setSendto(String sendto) {
		this.sendto = sendto;
	}

	public String getSendto() {
		return sendto;
	}

	
	public String adminHome()throws Exception{
		GwuParameters parameters = (GwuParameters)ServletActionContext.getRequest().getSession().getAttribute("parameters");
		if(parameters.getUserType() == TblUserType.USER_ADMIN){
			return SUCCESS;
		}
		else
			return ERROR;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}
	
	
public String resetPassword()throws Exception{
		
		HttpServletRequest request=ServletActionContext.getRequest();
		GwuParameters parameters=(GwuParameters)request.getSession().getAttribute("parameters");
		UserDAO userDAO=new UserDAO();
		TblUser user=userDAO.getUserById(userId);
		
		if(parameters!=null){
			if(parameters.getUserType()==TblUserType.USER_ADMIN){
				request.setAttribute("action", "home.action");
			}else if(parameters.getUserType()==TblUserType.USER_TUTOR||parameters.getUserType()==TblUserType.USER_STUDENT){
				request.setAttribute("action", "dashboardprofile.action");
			}
			
		}else{
			request.setAttribute("action", "index.jsp");
		}
		
		if(user!=null){
			if (user.getsPassword().equals(EncryptDecrypt.getMessageDigest(oldPassword.getBytes()))) {
				boolean flag=userDAO.updatePassword(user.getsUserName(),password, userId);
				if(flag){
					
					request.setAttribute("status", "1");
					return SUCCESS;
				}else{
					
					request.setAttribute("status", "2");
					return SUCCESS;
				}
				
				
				
			}else{
				request.setAttribute("status", "3");
				return SUCCESS;
			}
		}
		
		return SUCCESS;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}
	
	
	private void updateLoginHistory(int userId,String timeZone){
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		String browserType = request.getHeader("User-Agent");
		String sessionId=session.getId();
		String clientIp=request.getRemoteAddr();
		
		TblLoginHistory history=new TblLoginHistory();
		history.setBrowserName(browserType);
		history.setClientIp(clientIp);
		history.setSessionId(sessionId);
		history.setUserId(userId);
		history.setTimeZone(timeZone);
		history.setLoginTime(GwuTimeZone.currentSystemTimeInGMT());
		
		UserDAO userDAO=new UserDAO();
		int flag = 0;
		for(int i=0; i<10; i++){
			if(flag == 1)
				break;
			else{
				try{
					Logs.printInfoLog(className, "Inside try block of updateLoginHistory() method");
					userDAO.setLoginDetails(history);
					flag = 1;
				}catch(Exception e){
					Logs.printErrorLog(className, "Inside catch block of updateLoginHistory() method:\n" + e.getCause());
				}
			}	
		}
	}
	
	public String userLogin(){
		Logs.printInfoLog(className, "Inside userLogin() method");
		HttpSession session = ServletActionContext.getRequest().getSession();
		GwuParameters parameters=(GwuParameters)session.getAttribute("parameters");
		if(parameters!=null&& parameters.getUserType()==TblUserType.USER_ADMIN){
			return SUCCESS;
		}else{
			return ERROR;
		}
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getTimezone() {
		return timezone;
	}
	
}

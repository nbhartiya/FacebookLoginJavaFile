package com.gwu.action.login;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.gwu.action.invites.InviteDAO;
import com.gwu.common.GwuParameters;
import com.gwu.common.GwuTimeZone;
import com.gwu.common.Logs;
import com.gwu.dao.ScoreBoard.ScoreBoardDAO;
import com.gwu.dao.course.CourseDAO;
import com.gwu.dao.student.StudentDAO;
import com.gwu.dao.tutor.TutorDAO;
import com.gwu.dao.user.UserDAO;
import com.gwu.entities.student.TblStudent;
import com.gwu.entities.tutor.TblTutor;
import com.gwu.entities.user.TblLoginHistory;
import com.gwu.entities.user.TblUser;
import com.gwu.entities.user.TblUserType;
import com.gwu.entities.ScoreBoard.TblScoreBoard;
import com.gwu.security.EncryptDecrypt;
import com.opensymphony.xwork2.ActionSupport;

public class FacebookLoginAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private final static String className = "FacebookLoginAction";
	private String emailId;
	private String firstName;
	private String lastName;
	private String fbtz;


	public String twitterSignIn() throws Exception {
		return SUCCESS;

	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getEmailId() {
		return emailId;
	}



	public String updateFacebookId() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String direct = request.getParameter("direct")==null?"":request.getParameter("direct");
		String facebookId = request.getParameter("facebookId");
		UserDAO dao = new UserDAO();
		String siteUrl = "";
		String clLink = "index.jsp?opencl=yes";
		String cLinkFromCookie = request.getParameter("cLinkFromCookie");
		String courseValue = "not defined";
		
		if(request.getParameter("courseValue") != null)
			courseValue = request.getParameter("courseValue");
		
		if(cLinkFromCookie != null)
			clLink = cLinkFromCookie;

		System.out.println(clLink);

		if(request.getServerPort()==80 || request.getServerPort()==443){
			siteUrl = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
			clLink=siteUrl+"/"+clLink;
		}else{
			siteUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
			clLink=siteUrl+"/"+clLink;
		}
		
		Map<String, Integer> map = null;
		try{
			map = dao.updateFacebookId(emailId, facebookId, firstName, lastName);
		}catch (Exception e) {
			map = dao.updateFacebookId(emailId, facebookId, "", "");
		}
		int iUserID = 0;
		int isActive = 0;
		int iTypeId = 0;
		short isBlocked = 0;
		GwuParameters parameters = new GwuParameters();
		HttpSession session = ServletActionContext.getRequest().getSession();
		
		String timeZoneOffset = request.getParameter("timeZoneOffset")==null?"":request.getParameter("timeZoneOffset");
		int offset=0;
		if(!timeZoneOffset.equals(""))
			offset = Integer.parseInt(timeZoneOffset);
		GwuTimeZone gwuTimeZone=new GwuTimeZone();
		String timeZoneId=gwuTimeZone.clientTimeZoneID(offset);

		try {
			ServletContext context=session.getServletContext();
			if (map != null & map.size() > 0) {
				iUserID = map.get("iUserId");
				isActive = map.get("isActive");
				iTypeId = map.get("iTypeId");
				System.out.println("updateFacebookId(), iUserId: " + iUserID + " isActive: " + isActive + " iTypeId: " + iTypeId);
				parameters.initializeParapeters(iUserID, iTypeId, fbtz);
				@SuppressWarnings("unchecked")
				ArrayList<String> activeUser=(ArrayList<String>) context.getAttribute("activeUser");
				activeUser.add(new Integer(iUserID).toString());
				session.setAttribute("parameters", parameters);
			}

			if (iTypeId > 0) {
				
				if (iTypeId == TblUserType.USER_TUTOR){
					request.setAttribute("clLink", clLink);
					String name = "";
					TblTutor tutor = new TblTutor();
					request.setAttribute("UserType", 2);
					int tId = parameters.getTutorId();
					TutorDAO dao2 = new TutorDAO();
					Map mapTC = null;
					mapTC = dao2.getTutorCourses(tId);

					ArrayList<Integer> tCourseIds = (ArrayList<Integer>)mapTC.get("tCourseIds");
					if(tCourseIds.size() > 0){
						int courseId = tCourseIds.get(0);
						CourseDAO courseDAO = new CourseDAO();
						String course = courseDAO.getCourseNameById(courseId);
						request.setAttribute("course", course);
					}
					try {
						tutor = dao2.getTutorData(tId);
						name = tutor.getsFirstName()==null?"":tutor.getsFirstName();
						isBlocked = tutor.getiIsActive();
						
						/*if(isBlocked == 1){
							request.setAttribute("ErrorType",3);
						}
						else if(!dao.checkUserAvailability(iUserID)){
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
						request.setAttribute("FBLogin",0);
						request.setAttribute("facebookId", facebookId);
						request.setAttribute("emailId",emailId);
						request.setAttribute("firstName",firstName);
						request.setAttribute("lastName",lastName);
						updateLoginHistory( iUserID,timeZoneId);
					} catch (Exception ex) {
						Logs.printErrorLog(className, ex.getMessage());
						request.setAttribute("FBSignUp",0);
						addActionError("Login failed. Please try again!");
						return INPUT;
					}
				}else if (iTypeId == TblUserType.USER_STUDENT){
					request.setAttribute("clLink", clLink);
					String name = "";
					request.setAttribute("UserType", 3);
					int sId = parameters.getStudentId();
					StudentDAO dao2 = new StudentDAO();
					TblStudent student = new TblStudent();
					int courseId = student.getiCourseId();
					CourseDAO courseDAO = new CourseDAO();
					String course = courseDAO.getCourseNameById(courseId);
					request.setAttribute("course", course);
					
					
					try {
						student = dao2.getStudentData(sId);
						name = student.getsFirstName()==null?"":student.getsFirstName();
						isBlocked = student.getiIsActive();
						/*if(isBlocked == 1){
							request.setAttribute("ErrorType",3);
						}
						else if(!dao.checkUserAvailability(iUserID)){
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
						request.setAttribute("FBLogin",1);
						request.setAttribute("facebookId", facebookId);
						request.setAttribute("emailId",emailId);
						request.setAttribute("firstName",firstName);
						request.setAttribute("lastName",lastName);
						updateLoginHistory( iUserID,timeZoneId);
					} catch (Exception ex) {
						Logs.printErrorLog(className, ex.getMessage());
						addActionError("Login failed. Please try again!");
						return INPUT;
					}
					
				}
			}else {
				boolean exists = dao.isUserExist(emailId);
				if(exists){
					/*addActionError("Oops. Username and password do not match. Please retry.");*/
					request.setAttribute("ErrorType",1);

				}else{
					StudentDAO dao2 = new StudentDAO();
					ScoreBoardDAO scoreBoardDAO = new ScoreBoardDAO();
					InviteDAO inviteDAO = new InviteDAO();
					int courseId = 0;
					
					courseId = Integer.parseInt(request.getParameter("courseId"));
					
					String contains = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
					String password1 = EncryptDecrypt.getMessageDigest(EncryptDecrypt.getRandomAlphaNumericString(contains, 6).getBytes());
					session.setAttribute("newregistration", emailId);
					String name = "";
					try{
						TblUser user = new TblUser(emailId, password1, 1,TblUserType.USER_STUDENT);
						TblStudent student = new TblStudent(emailId, user, courseId, firstName, lastName, facebookId, fbtz);
						name = (firstName + " " + lastName).trim();
						dao2.add(student);
						
						TblScoreBoard tblScoreBoard = new TblScoreBoard();
						tblScoreBoard.setUserId(user.getiUserId());
						scoreBoardDAO.add(tblScoreBoard);
					}catch (Exception e) {
						
						
						System.out.println("First name and last name are not readable");
						TblUser user = new TblUser(emailId, password1, 1,TblUserType.USER_STUDENT);
						
						TblStudent student = new TblStudent(emailId, user, courseId, "", "", facebookId, fbtz);
						dao2.add(student);
						
						TblScoreBoard tblScoreBoard = new TblScoreBoard();
						tblScoreBoard.setUserId(user.getiUserId());
						scoreBoardDAO.add(tblScoreBoard);
						name = emailId;
					}
					
					iUserID = dao.getUserID(emailId);
					inviteDAO.updateReferralStatus(1, emailId, siteUrl, name);
					parameters.initializeParapeters(iUserID, TblUserType.USER_STUDENT, fbtz);
					ArrayList<String> activeUser=(ArrayList<String>) context.getAttribute("activeUser");
					activeUser.add(new Integer(iUserID).toString());
					session.setAttribute("parameters", parameters);
					request.setAttribute("Step",3);
					request.setAttribute("UserType", 3);
					request.setAttribute("clLink", clLink);
					request.setAttribute("FBSignUp",1);
					request.setAttribute("courseValue", courseValue);
					request.setAttribute("facebookId", facebookId);
					request.setAttribute("emailId",emailId);
					request.setAttribute("firstName",firstName);
					request.setAttribute("lastName",lastName);
					updateLoginHistory( iUserID,timeZoneId);
				}
			}

		} catch (Exception e) {
			/*addActionError("Login Failed. Retry...");*/
			request.setAttribute("ErrorType",0);
			Logs.printErrorLog(className, e.getMessage());
			if(direct.equals("direct"))
				return "SUCCESSFB";
			else
				return SUCCESS;
		}
		if(direct.equals("direct"))
			return "SUCCESSFB";
		else
			return SUCCESS;
	}

	public String doLoginByFacebookId() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String clLink = "index.jsp?opencl=yes";
		String cLinkFromCookie = request.getParameter("cLinkFromCookie");
		
		if(cLinkFromCookie != null)
			clLink = cLinkFromCookie;
		
		if(request.getServerPort()==80 || request.getServerPort()==443){
			clLink=request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/"+clLink;
		}else{
			clLink=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+""+request.getContextPath()+"/"+clLink;
		}
		UserDAO dao = new UserDAO();
		Map<String, Integer> map = dao.doLoginByFacebookId((String)request.getParameter("facebookId"), firstName, lastName);
		String fbtimezone=request.getParameter("fbTimeZone");
		
		String timeZoneId=GwuTimeZone.getCustomTimeZone(Double.parseDouble(fbtimezone));
		int iUserID = 0;
		int isActive = 0;
		int iTypeId = 0;
		short isBlocked = 0;
		GwuParameters parameters = new GwuParameters();
		HttpSession session = ServletActionContext.getRequest().getSession();

		try {
			ServletContext context=session.getServletContext();
			if (map != null & map.size() > 0) {
				iUserID = map.get("iUserId");
				isActive = map.get("isActive");
				iTypeId = map.get("iTypeId");
				System.out.println("updateFacebookId(), iUserId: " + iUserID + " isActive: " + isActive + " iTypeId: " + iTypeId);
				parameters.initializeParapeters(iUserID, iTypeId, fbtz);
				@SuppressWarnings("unchecked")
				ArrayList<String> activeUser=(ArrayList<String>) context.getAttribute("activeUser");
				activeUser.add(new Integer(iUserID).toString());
				session.setAttribute("parameters", parameters);
			}

			if (iTypeId > 0) {
				
				if (iTypeId == TblUserType.USER_TUTOR){
					request.setAttribute("clLink", clLink);
					String name = "";
					TblTutor tutor = new TblTutor();
					request.setAttribute("UserType", 2);
					int tId = parameters.getTutorId();
					TutorDAO dao2 = new TutorDAO();
					Map mapTC = null;
					mapTC = dao2.getTutorCourses(tId);

					ArrayList<Integer> tCourseIds = (ArrayList<Integer>)mapTC.get("tCourseIds");
					if(tCourseIds.size() > 0){
						int courseId = tCourseIds.get(0);
						CourseDAO courseDAO = new CourseDAO();
						String course = courseDAO.getCourseNameById(courseId);
						request.setAttribute("course", course);
					}
					try {
						tutor = dao2.getTutorData(tId);
						name = tutor.getsFirstName()==null?"":tutor.getsFirstName();
						isBlocked = tutor.getiIsActive();
						
						request.setAttribute("Step",3);
						request.setAttribute("FBLogin",0);
						updateLoginHistory( iUserID,timeZoneId);
					} catch (Exception ex) {
						Logs.printErrorLog(className, ex.getMessage());
						addActionError("Login failed. Please try again!");
						return INPUT;
					}
				}else if (iTypeId == TblUserType.USER_STUDENT){
					request.setAttribute("clLink", clLink);
					String name = "";
					request.setAttribute("UserType", 3);
					int sId = parameters.getStudentId();
					StudentDAO dao2 = new StudentDAO();
					TblStudent student = new TblStudent();
					int courseId = student.getiCourseId();
					CourseDAO courseDAO = new CourseDAO();
					String course = courseDAO.getCourseNameById(courseId);
					request.setAttribute("course", course);
					
					
					try {
						student = dao2.getStudentData(sId);
						name = student.getsFirstName()==null?"":student.getsFirstName();
						isBlocked = student.getiIsActive();
						
						request.setAttribute("Step",3);
						request.setAttribute("FBLogin",1);
						updateLoginHistory( iUserID,timeZoneId);
					} catch (Exception ex) {
						Logs.printErrorLog(className, ex.getMessage());
						addActionError("Login failed. Please try again!");
						return INPUT;
					}
					
				}
			}else {
				boolean exists = dao.isUserExist(emailId);
				if(exists){
					/*addActionError("Oops. Username and password do not match. Please retry.");*/
					request.setAttribute("ErrorType",1);

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

	public String facebookLoginFailure()throws Exception{
		
		addActionError("There is a problem in getting data from facebook, so please login from here.");
		return INPUT;
		
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

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFbtz(String fbtz) {
		this.fbtz = fbtz;
	}

	public String getFbtz() {
		return fbtz;
	}
}

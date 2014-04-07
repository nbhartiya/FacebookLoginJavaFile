package com.gwu.action.login;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.gwu.common.GwuParameters;
import com.gwu.common.Logs;
import com.gwu.dao.contentlibrary.WordListDAO;
import com.gwu.dao.course.CourseDAO;
import com.gwu.dao.games.GameRequestDao;
import com.gwu.dao.user.UserDAO;

/**
 * Servlet implementation class FacebookLoginServlet
 */
public class FacebookLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String className = "FacebookLoginServlet";
	private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
	private static final Token EMPTY_TOKEN = null;
	private String facebookId;
	private String timeZone;
	private String firstName;
	private String lastName;
	private String email;
	private String cLinkFromCookie;

	public FacebookLoginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String reqType = request.getParameter("req");
		if (reqType.endsWith("fblogin")) {
			facebookAuthentication(request, response);
		} else if (reqType.endsWith("fbsignin")) {
			try {
				facebookSignIn(request, response);
			} catch (ParseException e) {
				Logs.printErrorLog(className, e.getMessage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Logs.printErrorLog(className, e.getMessage());
			}
		} 
		
		else if(reqType.endsWith("fbgamerequest")){
			fbGameAuthentication(request, response);
		}
		else if(reqType.endsWith("fbloginforgame")){
			fbLoginForGame(request, response);
		}else if(reqType.endsWith("gotCourse")){
			try {
				gotCourseId(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(reqType.endsWith("gotEmailId")){
			try {
				gotEmailId(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	protected void facebookAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String timezone = request.getParameter("timezone")==null?"":request.getParameter("timezone");
		cLinkFromCookie = request.getParameter("clLink")==null?"":request.getParameter("clLink");
		
		ResourceBundle bundle = ResourceBundle.getBundle("facebook_config");
		String apikey = bundle.getString("app.key");
		String secret = bundle.getString("app.secret");
		String scope = "email";
		String url = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath()
				+ "/" + bundle.getString("app.callback");
		
		try {
			OAuthService service = new ServiceBuilder()
					.provider(FacebookApi.class).apiKey(apikey)
					.apiSecret(secret).callback(url).scope(scope).build();

			String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);

			HttpSession session = request.getSession();

			session.setAttribute("facebookService", service);
			session.setAttribute("fbtz", timezone);
			response.sendRedirect(authorizationUrl);
			
			
		} catch (NullPointerException e) {
			response.sendRedirect("facebooksigninAuthfailure.action");
		}

	}

	protected void facebookSignIn(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html");
		
		HttpSession session = request.getSession();
		
		String code = request.getParameter("code")==null?"":request.getParameter("code");
		try {
			if(code.equals("")){
				RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp?isFBSignFail=userPermission");
				dispatcher.forward(request, response);
			}
			else{
				
				OAuthService service = null;
				
				if(request.getSession() == null){
					System.out.println("Session is null");
				}else if(request.getSession().getAttribute("facebookService") == null){
					System.out.println("facebook service is null");
		
					ResourceBundle bundle = ResourceBundle.getBundle("facebook_config");
					String apikey = bundle.getString("app.key");
					String secret = bundle.getString("app.secret");
					String scope = "email";

					String url = request.getScheme() + "://" + request.getServerName()
							+ ":" + request.getServerPort() + request.getContextPath()
							+ "/" + bundle.getString("app.callback");

					
					service = new ServiceBuilder()
					.provider(FacebookApi.class).apiKey(apikey)
					.apiSecret(secret).callback(url).scope(scope).build();

					
				}else{					
					service = (OAuthService) request.getSession()
							.getAttribute("facebookService");
				}
				
				if(service == null)
					service = (OAuthService) request.getSession()
					.getAttribute("facebookService");

				
				Verifier verifier = new Verifier(code);
								
				Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
				OAuthRequest req = new OAuthRequest(Verb.GET,
						PROTECTED_RESOURCE_URL);
				service.signRequest(accessToken, req);
				Response res = req.send();
				System.out.println("facebookSignIn response body: " + res.getBody());
				// response.getWriter().write(res.getBody()+"<hr/>");
		
				JSONArray array = new JSONArray("[" + res.getBody() + "]");
		
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					
					if(jsonObject.has("username"))
							facebookId = jsonObject.getString("username");
					else 
						facebookId = "";
					
					timeZone = jsonObject.getString("timezone");
					
					String fbid = jsonObject.getString("id")==null?"":jsonObject.getString("id");
					
					firstName = jsonObject.getString("first_name")==null?"":jsonObject.getString("first_name");
					
					lastName = jsonObject.getString("last_name")==null?"":jsonObject.getString("last_name");
					
					if(jsonObject.has("email"))
						email = jsonObject.getString("email")==null?"":jsonObject.getString("email");
					else 
						email = null;
										
				}
		
				
				request.getSession().removeAttribute("facebookService");	
				
				String fbtz = request.getSession().getAttribute("fbtz")==null?"":request.getSession().getAttribute("fbtz").toString();
				request.getSession().removeAttribute("fbtz");
				if (email != null) {
					UserDAO dao = new UserDAO();
					//Map<String, Integer> map = dao
					//		.isUserFacebookIdExist(facebookId);
		
					if (dao.isUserExist(email)) {
						if(email.equals("")){
							request.setAttribute("firstName", firstName);
							request.setAttribute("lastName", lastName);
							request.setAttribute("facebookId", facebookId);
							request.setAttribute("faceBookLogin", "Yes");
							RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
							dispatcher.forward(request, response);
						}
						else{
													
							response.sendRedirect("loginbyfacebook.action?facebookId="
									+ facebookId + "&fbTimeZone=" + timeZone + "&emailId=" + email
									+ "&firstName=" + firstName + "&lastName=" + lastName + "&direct=direct&fbtz="+fbtz+"&cLinkFromCookie=" + cLinkFromCookie);
						
						}
					} else {
						
						
						session.setAttribute("facebookId",facebookId);
						session.setAttribute("fbTimeZone", timeZone);
						session.setAttribute("emailId", email);
						session.setAttribute("firstName", firstName);
						session.setAttribute("lastName", lastName);
						session.setAttribute("fbtz", fbtz);
						session.setAttribute("cLinkFromCookie", cLinkFromCookie);
						
						response.sendRedirect("chooseLanguageForSignUp.jsp?chooseCourse=Facebook");	
						
					}
				} else {
					
					System.out.println("*************************************************************");
					System.out.println("-----------------Tried facebook login------------------------");
					System.out.println("-----------------But no email found------------------------");
					System.out.println("*************************************************************");
					
					session.setAttribute("facebookId",facebookId);
					session.setAttribute("fbTimeZone", timeZone);
					session.setAttribute("firstName", firstName);
					session.setAttribute("lastName", lastName);
					session.setAttribute("fbtz", fbtz);
					session.setAttribute("cLinkFromCookie", cLinkFromCookie);

					
					response.sendRedirect("getEmail.jsp");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("facebooksigninfailure.action");
		}

	}
	
	protected void gotCourseId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		try{
			
			HttpSession session = request.getSession();
			
			String facebookId = session.getAttribute("facebookId").toString();
			String timeZone = session.getAttribute("fbTimeZone").toString();
			String email = session.getAttribute("emailId").toString();
			String firstName = session.getAttribute("firstName").toString();
			String lastName = session.getAttribute("lastName").toString();
			String fbtz = session.getAttribute("fbtz").toString();
			String cLinkFromCookie = session.getAttribute("cLinkFromCookie").toString();
			
			int courseId=0;
			
			String courseValue = "not defined";			
			//getting course ID
			courseId = Integer.parseInt(request.getParameter("courseId").toString());
			
			if(courseId > 0){
				CourseDAO courseDao = new CourseDAO();
				try {
					courseValue = courseDao.getCourseNameById(courseId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			
			session.setAttribute("facebook_courseValue", courseValue);
			
			
			response.sendRedirect("updatefacebookId.action?facebookId="
					+ facebookId + "&fbTimeZone=" + timeZone + "&emailId=" + email
					+ "&firstName=" + firstName + "&lastName=" + lastName + "&direct=direct&fbtz="+fbtz+"&cLinkFromCookie=" + cLinkFromCookie+"&courseId="+courseId+"&&courseValue="+courseValue);
			 			
		} catch (Exception e) {
			e.printStackTrace();
		
		}
	}
	
	protected void gotEmailId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		try{
			
			HttpSession session = request.getSession();
			
			String facebookId = "";
			
			if(session.getAttribute("facebookId") != null)
				facebookId = session.getAttribute("facebookId").toString();
			else {
				response.sendRedirect("index.jsp?notValidRequest");
				return;
			}

			String timeZone = session.getAttribute("fbTimeZone").toString();
			String firstName = session.getAttribute("firstName").toString();
			String lastName = session.getAttribute("lastName").toString();
			String fbtz = session.getAttribute("fbtz").toString();
			String cLinkFromCookie = session.getAttribute("cLinkFromCookie").toString();
			
			String email=null;
			
			//getting Email ID
			if(request.getParameter("emailId") != null)
				email = request.getParameter("emailId").toString();
			else
				email = null;
			
			if (email != null) {
				UserDAO dao = new UserDAO();
	
				if (dao.isUserExist(email)) {
					if(email.equals("")){
						request.setAttribute("firstName", firstName);
						request.setAttribute("lastName", lastName);
						request.setAttribute("facebookId", facebookId);
						request.setAttribute("faceBookLogin", "Yes");
						RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
						dispatcher.forward(request, response);
					}
					else{												
						response.sendRedirect("loginbyfacebook.action?facebookId="
								+ facebookId + "&fbTimeZone=" + timeZone + "&emailId=" + email
								+ "&firstName=" + firstName + "&lastName=" + lastName + "&direct=direct&fbtz="+fbtz+"&cLinkFromCookie=" + cLinkFromCookie);
					
					}
				} else {
					
					
					session.setAttribute("facebookId",facebookId);
					session.setAttribute("fbTimeZone", timeZone);
					session.setAttribute("emailId", email);
					session.setAttribute("firstName", firstName);
					session.setAttribute("lastName", lastName);
					session.setAttribute("fbtz", fbtz);
					session.setAttribute("cLinkFromCookie", cLinkFromCookie);
					
					response.sendRedirect("chooseLanguageForSignUp.jsp?chooseCourse=Facebook");	
					
				}
			} else {
				
				response.sendRedirect("index.jsp?facebookSigninFailure=noEmail");
			}
			 			
		} catch (Exception e) {
			e.printStackTrace();
		
		}
	}
	
	
	protected void fbGameAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ResourceBundle bundle = ResourceBundle.getBundle("facebook_config");
		String apikey = bundle.getString("app.hangman.key");
		String secret = bundle.getString("app.hangman.secret");
		String scope = "email";
		String url = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath()
				+ "/FacebookLoginServlet?req=fbloginforgame";
		
		try {
			OAuthService service = new ServiceBuilder()
					.provider(FacebookApi.class).apiKey(apikey)
					.apiSecret(secret).callback(url).scope(scope).build();

			String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
			
			System.out.println("authorizationUrl: " + authorizationUrl);

			HttpSession session = request.getSession();

			session.setAttribute("facebookService", service);
			response.sendRedirect(authorizationUrl);
		} catch (NullPointerException e) {
			response.sendRedirect("index.jsp");
		}

	}
	
	protected void fbLoginForGame(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		String code = request.getParameter("code")==null?"":request.getParameter("code");
		try {
			if(code.equals("")){
				RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
				dispatcher.forward(request, response);
			}
			else{
				OAuthService service = (OAuthService) request.getSession()
				.getAttribute("facebookService");
				Verifier verifier = new Verifier(code);
				Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
				System.out.println("accessToken: " + accessToken.toString());
				OAuthRequest req = new OAuthRequest(Verb.GET,
						PROTECTED_RESOURCE_URL);
				service.signRequest(accessToken, req);
				Response res = req.send();
				JSONArray array = new JSONArray("[" + res.getBody() + "]");
				String fbid = "";
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					facebookId = jsonObject.getString("username");
					timeZone = jsonObject.getString("timezone");
					
					System.out.println("facebook details: ");
					fbid = jsonObject.getString("id")==null?"":jsonObject.getString("id");
					System.out.println("fbid: " + fbid);
					
					firstName = jsonObject.getString("first_name")==null?"":jsonObject.getString("first_name");
					System.out.println("firstName: " + firstName);
					
					lastName = jsonObject.getString("last_name")==null?"":jsonObject.getString("last_name");
					System.out.println("lastName: " + lastName);
					
					email = jsonObject.getString("email")==null?"":jsonObject.getString("email");
					System.out.println("email: " + email);
					
				}
		
				HttpSession httpSession=request.getSession();
				httpSession.removeAttribute("facebookService");
				System.out.println("fb username: " + facebookId);	
				
				long grId = 0l;
				String req_id = "";
				
				if(httpSession.getAttribute("grId") != null)
					grId = (Long)httpSession.getAttribute("grId");
				
				if(httpSession.getAttribute("req_id") != null)
					req_id = (String)httpSession.getAttribute("req_id");
				
				if(grId > 0 || !req_id.equals("")){
					GameRequestDao dao = new GameRequestDao();
					int count = dao.updateGameRequest(email, firstName + " " + lastName, fbid, grId, request.getSession().getId(), req_id);
					
					
					GwuParameters parameters = (GwuParameters)httpSession.getAttribute("parameters");
					WordListDAO wlDao = new WordListDAO();
					int userId = 0;
					if(parameters != null){
						userId = parameters.getUserId();
					}
					int clid = 0;
					if(grId > 0)
						clid = 13;
					else
						clid = 14;
					wlDao.saveLessonIdInTotalRead(clid, userId, httpSession.getId());
					
					httpSession.setAttribute("fb_id", fbid);
					response.sendRedirect("PlayGameTogether/game.jsp");
					
					/*request.setAttribute("fb_id", fbid);
					RequestDispatcher dispatcher = request.getRequestDispatcher("PlayGameTogether/game.jsp");
					dispatcher.forward(request, response);*/
				}else{
					response.sendRedirect("index.jsp");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("index.jsp");
		}
	}

}

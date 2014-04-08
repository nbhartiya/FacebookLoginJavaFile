package com.gwu.action.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;

import com.gwu.common.GwuParameters;
import com.gwu.common.Logs;
import com.gwu.dao.contentlibrary.NewCLPaymentDAO;
import com.gwu.dao.contentlibrary.NewContentLibraryDAO;
import com.gwu.dao.contentlibrary.WordListDAO;
import com.gwu.entities.contentlibrary.TblCLPayment;
import com.gwu.entities.contentlibrary.TblContentLibrary1;
import com.gwu.entities.user.TblUserType;

/**
 * Servlet implementation class LoginStatusServlet
 */
public class LoginStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String className = "LoginStatusServlet";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginStatusServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		checkLoginStatus(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		checkLoginStatus(request, response);
	}

	protected void checkLoginStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userType=0;
		int ft = 0;
		String ftUrl = "";
		String wlStatus = "";
		String clLink = request.getAttribute("clLink")==null?"":request.getAttribute("clLink").toString();
		
		String course = request.getAttribute("course")==null?"":request.getAttribute("course").toString();
		if(request.getAttribute("UserType")!=null){
			userType=(Integer)request.getAttribute("UserType");
		}
		int errorType=0;
		if(request.getAttribute("ErrorType")!=null){
			errorType=(Integer)request.getAttribute("ErrorType");
		}
		int accStatus=0;
		if(request.getAttribute("accStatus")!=null){
			accStatus=(Integer)request.getAttribute("accStatus");
		}
		int step = 0;
		String code = "";
		if(request.getAttribute("Step")!=null){
			step=(Integer)request.getAttribute("Step");
			if(step == 1)
				code = request.getAttribute("code").toString();
		}
		HttpSession session=request.getSession();
		GwuParameters parameters=(GwuParameters)session.getAttribute("parameters");
		PrintWriter out=response.getWriter();
		
		if(parameters!=null){
			if(session.getAttribute("ftcourse") != null){
				if((Integer)session.getAttribute("ftcourse") > 0){
					ft = 1;
					ftUrl = session.getAttribute("fturl")==null?"":(String)session.getAttribute("fturl");
					ftUrl = URLDecoder.decode(ftUrl,"utf-8");
					ftUrl = ftUrl.replace("studentId=0", "studentId="+parameters.getStudentId());
					
					String slotIds = session.getAttribute("SLOT_IDS")==null?"":(String)session.getAttribute("SLOT_IDS");
					
					ftUrl+="&sslotIds="+slotIds;
					System.out.println("fturl: " + ftUrl);
				}
			}else if(session.getAttribute("comment")!=null&&!session.getAttribute("comment").equals("")&&session.getAttribute("comment").equals("newcomment")){
				ft =2;
			}else if(session.getAttribute("secbooking")!=null&&session.getAttribute("secbooking").equals("yes")){
				int courseId = session.getAttribute("secbookcourseid")==null?0:(Integer)session.getAttribute("secbookcourseid");
				ft = 1;
				ftUrl = "seccourseslots.action?courseId=" + courseId + "&timezone=" + parameters.getTimeZone();
			}else if(session.getAttribute("loginurl")!=null){
				ft = 1;
				ftUrl = (String)session.getAttribute("loginurl");
				ftUrl = URLDecoder.decode(ftUrl,"utf-8");
				ftUrl = ftUrl.replace("studentId=0", "studentId="+parameters.getStudentId());
				session.removeAttribute("loginurl");
				String siteHome = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
				if(request.getServerPort()==80 || request.getServerPort()==443){
					siteHome = request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/";
				}
				if(siteHome.equals(ftUrl) || ftUrl.equals(siteHome+"index.jsp")){
					ftUrl = siteHome + "index.jsp?opencl=yes";
				}							
			}
			if(session.getAttribute("tocart")!=null && !((String)session.getAttribute("tocart")).equals("")){
				String siteHome = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
				if(request.getServerPort()==80 || request.getServerPort()==443){
					siteHome = request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/";
				}
				ft = 1;
				if(((String)session.getAttribute("tocart")).equals("yes"))
					ftUrl = siteHome + "shoppingcart.action";
				else if(((String)session.getAttribute("tocart")).equals("refer"))
					ftUrl = siteHome + "dashboardreferral.action";
			}
			
			
			if(accStatus>0||errorType>0||step==1){
				session.removeAttribute("parameters");
			}
			
			
			boolean flag=false;
			
			System.out.println("email: " + parameters.getEmail());
		
		
			if(session.getAttribute("tocart")!=null && !((String)session.getAttribute("tocart")).equals("")){
				String cartclids = session.getAttribute("cartclids")==null?"":(String)session.getAttribute("cartclids");
				if(!cartclids.equals("") && parameters.getUserType()!=TblUserType.USER_ADMIN){
					try{
						cartclids = cartclids.substring(0, (cartclids.length()-1));
						String cartClIds[] = cartclids.split("_");
						for(int i=1; i<cartClIds.length; i++){
							int cartclid = Integer.parseInt(cartClIds[i]);
							if(cartclid > 0){
								int userId = parameters.getUserId();
								NewContentLibraryDAO newCLDao = new NewContentLibraryDAO();
								NewCLPaymentDAO clPaymentDAO = new NewCLPaymentDAO();
								int userPaid = clPaymentDAO.isUserCartContainsLesson(userId, cartclid, false, false);
								if(userPaid == 0){
									TblContentLibrary1 contentLibrary1 = newCLDao.getContentLibrary(cartclid);
									TblCLPayment clPayment = new TblCLPayment();
									clPayment.setiClid(cartclid);
									clPayment.setiEntryBy(userId);
									clPayment.setiUserId(userId);
									clPayment.setPrice(contentLibrary1.getPrice());
									clPaymentDAO.addCLPayment(clPayment);
								}
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				session.removeAttribute("tocart");
				session.removeAttribute("cartclids");
			}
			
			if(session.getAttribute("wordlist")!=null && !session.getAttribute("wordlist").equals("")){
				String wordlist = session.getAttribute("wordlist").toString();
				
				@SuppressWarnings("unchecked")
				ArrayList<String> userWlIds=(ArrayList<String>) session.getAttribute("userWlIds");
				if(userWlIds==null){
					userWlIds=new ArrayList<String>();
					
					userWlIds.add(wordlist);
					session.setAttribute("userWlIds",userWlIds);
					
					int clid=0;
					if(wordlist!=null&&NumberUtils.isDigits(wordlist)){
						clid=Integer.parseInt(wordlist);
					}
					
					WordListDAO listDAO=new WordListDAO();
					try{
						listDAO.saveLessonIdInTotalRead(clid,parameters.getUserId(),"");;
						wlStatus = "Word list saved";
					}catch(Exception e){
						e.printStackTrace();
						Logs.printErrorLog(className, "inside catch block of saveUserWordList() method: " + e.getCause());
						wlStatus = "Word list could not be saved due to internal server error";
					}
					
				}else{
					if(userWlIds.contains(wordlist)){
						wlStatus = "Word list already saved";

					}else{
						int clid=0;
						if(wordlist!=null&&NumberUtils.isDigits(wordlist)){
							clid=Integer.parseInt(wordlist);
						}
						
						WordListDAO listDAO=new WordListDAO();
						try{
							listDAO.saveLessonIdInTotalRead(clid,parameters.getUserId(),"");;
							wlStatus = "Word list saved";
						}catch(Exception e){
							e.printStackTrace();
							Logs.printErrorLog(className, "inside catch block of saveUserWordList() method: " + e.getCause());
							wlStatus = "Word list could not be saved due to internal server error";
						}
					}
				}
				
				
				
			}
			session.removeAttribute("wordlist");
		}
		

		String isFacebook=request.getParameter("isFacebook");
		
		if(isFacebook!=null&&isFacebook.equals("Yes")){
			
			//System.
			
			ft=3;
			response.setContentType("text/plain");
			StringBuilder builder=new StringBuilder();

			builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			builder.append("<user>");
			
			builder.append("<userType>"+userType+"</userType>");
			builder.append("<accStatus>"+accStatus+"</accStatus>");
			builder.append("<errorType>"+errorType+"</errorType>");
			builder.append("<course>"+course+"</course>");
			builder.append("<step>"+step+"</step>");
			builder.append("<code>"+code+"</code>");
			builder.append("<ft>"+ft+"</ft>");
			builder.append("<fturl>"+ftUrl+"</fturl>");
			builder.append("<wlStatus>"+wlStatus+"</wlStatus>");
			builder.append("<clLink>"+clLink+"</clLink>");
			
			if(request.getParameter("FBLogin") != null)
				builder.append("<FBLogin>"+request.getParameter("FBLogin")+"</FBLogin>");
			else
				builder.append("<FBLogin>"+"-1"+"</FBLogin>");
			
			if(request.getParameter("FBSignUp") != null)
				builder.append("<FBSignUp>"+request.getParameter("FBSignUp")+"</FBSignUp>");
			else
				builder.append("<FBSignUp>"+"-1"+"</FBSignUp>");
			
			if(request.getParameter("emailId") != null)
				builder.append("<emailId>"+request.getParameter("emailId")+"</emailId>");
			if(request.getParameter("firstName") != null)
				builder.append("<firstName>"+request.getParameter("firstName")+"</firstName>");
			if(request.getParameter("lastName") != null)
				builder.append("<lastName>"+request.getParameter("lastName")+"</lastName>");
			
			builder.append("</user>");
			
			RequestDispatcher dispatcher=request.getRequestDispatcher("facebooklogin.jsp");
			request.setAttribute("loginstatus", builder.toString());
			dispatcher.forward(request, response);
			
		}else{
			
			response.setContentType("text/xml");
			
			StringBuilder builder=new StringBuilder();

			builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			builder.append("<user>");
			
			builder.append("<userType>"+userType+"</userType>");
			builder.append("<accStatus>"+accStatus+"</accStatus>");
			builder.append("<errorType>"+errorType+"</errorType>");
			builder.append("<course>"+course+"</course>");
			builder.append("<step>"+step+"</step>");
			builder.append("<code>"+code+"</code>");
			builder.append("<ft>"+ft+"</ft>");
			builder.append("<fturl><![CDATA["+ftUrl+"]]></fturl>");
			builder.append("<wlStatus>"+wlStatus+"</wlStatus>");
			builder.append("<clLink><![CDATA["+clLink+"]]></clLink>");
			builder.append("</user>");
			
			out.write(builder.toString());
		}
		
		
	}
}
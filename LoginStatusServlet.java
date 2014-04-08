package com.gwu.action.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gwu.common.Logs;
import com.gwu.dao.student.StudentDAO;
import com.gwu.dao.tutor.TutorDAO;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String className = "LoginServlet";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String reqType=request.getParameter("reqType");
		if(reqType!=null&&reqType.equals("2")){
			getTutorIds(request, response);
		}else if(reqType!=null&&reqType.equals("3")){
			getStudentIds(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String reqType=request.getParameter("reqType");
		if(reqType!=null&&reqType.equals("2")){
			getTutorIds(request, response);
		}else if(reqType!=null&&reqType.equals("3")){
			getStudentIds(request, response);
		}
	}
	@SuppressWarnings("unchecked")
	protected void getStudentIds(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logs.printInfoLog(className, "Inside getStudentIds() method");
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		StudentDAO studentDAO=new StudentDAO();
		ArrayList<String> emails = null;
		try {
			
			HashMap<Object, ArrayList<?>> map = (HashMap<Object, ArrayList<?>>) studentDAO.getActiveStudentsList();
			emails=(ArrayList<String>) map.get("emails");
		} catch (Exception e) {
			Logs.printErrorLog(className, "Inside catch block of getStudentIds() method:\n" + e.getCause());
			e.printStackTrace();
		}
		if(emails!=null){
			out.write("<option value=''>--Select--</option>");
			for(int i=0;i<emails.size();i++){
				out.write("<option value='"+emails.get(i)+"'>"+emails.get(i)+"</option>");
			}
		}
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	protected void getTutorIds(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logs.printInfoLog(className, "Inside getTutorIds() method");
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		TutorDAO tutorDAO=new TutorDAO();
		ArrayList<String> emails = null;
		ArrayList<Short> isActive = null;
		try {
			
			HashMap<Object, ArrayList<?>> map = (HashMap<Object, ArrayList<?>>) tutorDAO.getActiveTutorsList();
			emails=(ArrayList<String>) map.get("emails");
			isActive=(ArrayList<Short>) map.get("isActive");
		} catch (Exception e) {
			Logs.printErrorLog(className, "Inside catch block of getStudentIds() method:\n" + e.getCause());
			e.printStackTrace();
		}
		
		if(emails!=null){
			out.write("<option value=''>--Select--</option>");
			for(int i=0;i<emails.size();i++){
				if(isActive.get(i)==0)
					out.write("<option value='"+emails.get(i)+"'>"+emails.get(i)+"</option>");
			}
		}
	}

}
package inquiry;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.Course;

import util.HtmlUtils;
import util.HttpUtils;

/**
 * Servlet implementation class CourseInquiry
 */
@WebServlet({ "/CourseInquiry", "/inquiry/CourseInquiry" })
public class CourseInquiry extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CourseInquiry() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("doPost");
		requestData(request,response);
	}
	
	private synchronized void requestData(HttpServletRequest request,HttpServletResponse response) throws IOException{
		List<String> list=new ArrayList<>();
		TreeMap<String, List<Course>> treeMap=new TreeMap<>();
		String account=request.getParameter("account");
		System.out.println(account);
		String cookie=request.getParameter("cookie");
		System.out.println(cookie);
		String name=request.getParameter("name");
		System.out.println(name);
		String viewState=HttpUtils.requestFirstCourse(list,account,cookie,name);
		System.out.println(list);
		HttpUtils.requestNotFirstCourseGet(treeMap, account, cookie, name);
		System.out.println(treeMap);
		for(int i=1;i<list.size();i++){
			String option=list.get(i);
			if(list.get(i-1).substring(9).equals(option.substring(9)))
				HttpUtils.requestNotFirstCoursePost(treeMap,account, cookie, name, "xnd", viewState, option.substring(0,9), option.substring(9));
			else
				HttpUtils.requestNotFirstCoursePost(treeMap,account, cookie, name, "xqd", viewState, option.substring(0,9), option.substring(9));
			System.out.println(treeMap);
		}
		String jsonResult=HtmlUtils.analyzeAllCourse(treeMap);
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer=response.getWriter();
		writer.print(jsonResult);
	}

}

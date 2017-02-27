package inquiry;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.HttpUtils;

/**
 * Servlet implementation class ScoreInquiry
 */
@WebServlet({ "/ScoreInquiry", "/inquiry/ScoreInquiry" })
public class ScoreInquiry extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScoreInquiry() {
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
		String account=request.getParameter("account");
		System.out.println(account);
		String cookie=request.getParameter("cookie");
		System.out.println(cookie);
		String name=request.getParameter("name");
		System.out.println(name);
		String jsonResult=HttpUtils.requestScore(account,cookie,name);
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer=response.getWriter();
		writer.print(jsonResult);
	}

}

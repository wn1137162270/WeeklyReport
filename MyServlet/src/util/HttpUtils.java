package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import json.Course;
import json.MyJSONObject;
import json.Verification;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

public class HttpUtils {
	private static final String LOGIN_URL="http://222.24.62.120/default2.aspx";
    private static final String VERIFICATION_URL="http://222.24.62.120/CheckCode.aspx";
    private static final String HOST_URL="http://222.24.62.120/";
    private static final String MAIN_URL="http://222.24.62.120/xs_main.aspx?xh=";
    private static final String MIDDLE_URL="&xm=";
    private static final String INFO_URL="xsgrxx.aspx?xh=";
    private static final String INFO_REAR="&gnmkdm=N121501";
    private static final String SCORE_URL="xscjcx.aspx?xh=";
    private static final String SCORE_REAR="&gnmkdm=N121605";
    private static final String COURSE_URL="xskbcx.aspx?xh=";
    private static final String COURSE_REAR="&gnmkdm=N121603";
    private static final String PLAN_URL="pyjh.aspx?xh=";
    private static final String PLAN_REAR="&gnmkdm=N121607";
    private static final String NEWS_URL="lw_ckgg.aspx?xh=";
    private static final String NEWS_REAR="&gnmkdm=N121901";
	
	public static String requestVerification(){
		byte[] bytes=null;
		String jsonResult=null;
		HttpClient httpClient=new DefaultHttpClient();
		try {
	        HttpGet verifyGet=new HttpGet(VERIFICATION_URL);
	        HttpResponse httpResponse=httpClient.execute(verifyGet);
	        String cookie=httpResponse.getFirstHeader("Set-Cookie").getValue();
	        System.out.println("cookie:"+cookie);
	        if(httpResponse.getStatusLine().getStatusCode()==200)
	        {
	            HttpEntity httpEntity=httpResponse.getEntity();
	            bytes=EntityUtils.toByteArray(httpEntity);
	            System.out.println(bytes);
	            jsonResult=HtmlUtils.analyzeVerification(cookie, bytes);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return jsonResult;
	}
	
	public static String studentLogin(String account,String password,String verifyCode,String cookie){
		String jsonResult=null;
		HttpClient httpClient=new DefaultHttpClient();
        HttpPost loginPost=new HttpPost(LOGIN_URL);
        loginPost.setHeader("Cookie", cookie);
        ArrayList<NameValuePair> pairs=new ArrayList<>();
        pairs.add(new BasicNameValuePair("__VIEWSTATE",
                "dDwtNTE2MjI4MTQ7Oz5O9kSeYykjfN0r53Yqhqckbvd83A=="));
        pairs.add(new BasicNameValuePair("Button1",""));
        pairs.add(new BasicNameValuePair("hidPdrs",""));
        pairs.add(new BasicNameValuePair("hidsc",""));
        pairs.add(new BasicNameValuePair("lbLanguage",""));
        pairs.add(new BasicNameValuePair("RadioButtonList1","%D1%A7%C9%FA"));
        pairs.add(new BasicNameValuePair("Textbox1",""));
        pairs.add(new BasicNameValuePair("TextBox2",password));
        pairs.add(new BasicNameValuePair("txtSecretCode",verifyCode));
        pairs.add(new BasicNameValuePair("txtUserName",account));
        try {
            HttpEntity requestEntity=new UrlEncodedFormEntity(pairs,HTTP.UTF_8);
            loginPost.setEntity(requestEntity);
            HttpResponse httpResponse=httpClient.execute(loginPost);
            System.out.println(httpResponse.getStatusLine().getStatusCode());
            if(httpResponse.getStatusLine().getStatusCode()==302)
            {
            	HttpClient mainClient=new DefaultHttpClient();
            	HttpGet mainGet=new HttpGet(MAIN_URL+account);
            	mainGet.setHeader("Cookie", cookie);
            	mainGet.setHeader("Referer", LOGIN_URL);
            	HttpResponse mainResponse=mainClient.execute(mainGet);
            	HttpEntity mainEntity=mainResponse.getEntity();
            	String loginResult=EntityUtils.toString(mainEntity);
                //System.out.println(loginResult);
                jsonResult=HtmlUtils.analyzeLogin(loginResult);
            }
            else if(httpResponse.getStatusLine().getStatusCode()==200){
            	HttpEntity httpEntity=httpResponse.getEntity();
            	String loginResult=EntityUtils.toString(httpEntity);
            	//System.out.println(loginResult);
            	jsonResult=HtmlUtils.analyzeLogin(loginResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResult;
	}
	
	public static String requestScore(String account,String cookie,String name){
		String jsonResult=null;
		String linkURL=SCORE_URL+account+MIDDLE_URL+name+SCORE_REAR;
		String viewState=getViewState(HOST_URL+linkURL,MAIN_URL+account,cookie);
		HttpClient httpClient=new DefaultHttpClient();
        String url=HOST_URL+linkURL;
        ArrayList<NameValuePair> pairs=new ArrayList<>();
        pairs.add(new BasicNameValuePair("__EVENTARGUMENT",""));
        pairs.add(new BasicNameValuePair("__EVENTTARGET",""));
        pairs.add(new BasicNameValuePair("__VIEWSTATE",viewState));
        pairs.add(new BasicNameValuePair("btn_zcj","%C0%FA%C4%EA%B3%C9%BC%A8"));
        pairs.add(new BasicNameValuePair("ddlXN",""));
        pairs.add(new BasicNameValuePair("ddlXQ",""));
        pairs.add(new BasicNameValuePair("ddl_kcxz",""));
        pairs.add(new BasicNameValuePair("hidLanguage",""));
        try {
            HttpEntity requestEntity=new UrlEncodedFormEntity(pairs);
            HttpPost scorePost=new HttpPost(url);
            scorePost.setEntity(requestEntity);
            scorePost.setHeader("Cookie",cookie);
            scorePost.setHeader("Referer",MAIN_URL+account);
            HttpResponse httpResponse=httpClient.execute(scorePost);
            System.out.println(httpResponse.getStatusLine().getStatusCode());
            if(httpResponse.getStatusLine().getStatusCode()==200)
            {
                HttpEntity httpEntity=httpResponse.getEntity();
                String allScoreResult= EntityUtils.toString(httpEntity);
                jsonResult=HtmlUtils.analyzeScore(allScoreResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResult;
	}
	
	public static String requestFirstCourse(List<String> list,String account,String cookie,String name){
		String courseFirstResult=requestCourseGet(account,cookie,name);
        String couresViewState=HtmlUtils.analyzeFirstCourse(list,courseFirstResult); 
        return couresViewState;
	}
	
	public static void requestNotFirstCourseGet(TreeMap<String,List<Course>> hashMap,String account,String cookie,String name){
		String courseFirstResult=requestCourseGet(account,cookie,name);
        HtmlUtils.analyzeCourse(hashMap,courseFirstResult); 
	}
	
	public static String requestCourseGet(String account,String cookie,String name){
		String courseResultGet=null;
		String linkURL=COURSE_URL+account+MIDDLE_URL+name+COURSE_REAR;
		String url=HOST_URL+linkURL;
		HttpClient httpClient=new DefaultHttpClient();
        HttpGet courseFirstGet=new HttpGet(url);
        courseFirstGet.setHeader("Cookie",cookie);
        courseFirstGet.setHeader("Referer",MAIN_URL+account);
        try {
            HttpResponse httpResponse=httpClient.execute(courseFirstGet);
            if(httpResponse.getStatusLine().getStatusCode()==200)
            {
                HttpEntity httpEntity=httpResponse.getEntity();
                courseResultGet=EntityUtils.toString(httpEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courseResultGet;
	}
	
	public static void requestNotFirstCoursePost(TreeMap<String,List<Course>> hashMap,String account,String cookie,String name,String yos,String courseViewState,String year,String semester){
		String courseFirstResult=requestCoursePost(account,cookie,name,yos,courseViewState,year,semester);
        HtmlUtils.analyzeCourse(hashMap,courseFirstResult); 
	}
	
	public static String requestCoursePost(String account,String cookie,String name,String yos,String courseViewState,String year,String semester){
		String courseResultPost=null;
		String linkURL=COURSE_URL+account+MIDDLE_URL+name+COURSE_REAR;
		String url=HOST_URL+linkURL;
		HttpClient httpClient=new DefaultHttpClient();
        ArrayList<NameValuePair> pairs=new ArrayList<>();
        pairs.add(new BasicNameValuePair("__EVENTARGUMENT",""));
        pairs.add(new BasicNameValuePair("__EVENTTARGET",yos));
        pairs.add(new BasicNameValuePair("__VIEWSTATE",courseViewState));
        pairs.add(new BasicNameValuePair("xnd",year));
        pairs.add(new BasicNameValuePair("xqd",semester));
        try {
            HttpEntity requestEntity=new UrlEncodedFormEntity(pairs);
            HttpPost coursePost=new HttpPost(url);
            coursePost.setEntity(requestEntity);
            coursePost.setHeader("Cookie",cookie);
            coursePost.setHeader("Referer",MAIN_URL+account);
            HttpResponse httpResponse=httpClient.execute(coursePost);
            if(httpResponse.getStatusLine().getStatusCode()==200)
            {
                HttpEntity httpEntity=httpResponse.getEntity();
                courseResultPost= EntityUtils.toString(httpEntity);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courseResultPost;
	}
	
	public static String getViewState(final String url,final String referer,final String cookie) {
        String viewState=null;
        HttpClient httpClient=new DefaultHttpClient();
        HttpGet viewStateGet=new HttpGet(url);
        viewStateGet.addHeader("Cookie", cookie);
        viewStateGet.addHeader("Referer", referer);
        try {
            HttpResponse httpResponse=httpClient.execute(viewStateGet);
            HttpEntity httpEntity= httpResponse.getEntity();
            String s=EntityUtils.toString(httpEntity);
            viewState=Jsoup.parse(s).select("input[name=__VIEWSTATE]").val();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return viewState;
    }
	
	public static String studentLogout(String account,String cookie){
		String logout;
		String jsonResult=null;
		HttpClient httpClient=new DefaultHttpClient();
        HttpPost logoutPost=new HttpPost(MAIN_URL+account);
        logoutPost.setHeader("Cookie", cookie);
        logoutPost.setHeader("Referer",MAIN_URL+account);
        ArrayList<NameValuePair> pairs=new ArrayList<>();
        pairs.add(new BasicNameValuePair("__EVENTARGUMENT",""));
        pairs.add(new BasicNameValuePair("__EVENTTARGET","likTc"));
        pairs.add(new BasicNameValuePair("__VIEWSTATE", 
        		"dDwxMjg4MjkxNjE4Ozs+mC/tArt0u1jmP1rzm5PGO18pVC4="));
        try {
            HttpEntity requestEntity=new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
            logoutPost.setEntity(requestEntity);
            HttpResponse httpResponse=httpClient.execute(logoutPost);
            if(httpResponse.getStatusLine().getStatusCode()==302){
            	logout="退出成功";
                jsonResult=HtmlUtils.analyzeLogout(logout);
            }
            else{
            	logout="退出失败";
                jsonResult=HtmlUtils.analyzeLogout(logout);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResult;
	}
}

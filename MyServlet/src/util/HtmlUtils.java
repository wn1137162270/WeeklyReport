package util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import json.Course;
import json.MyJSONObject;
import json.Verification;

public class HtmlUtils {
	public static String analyzeVerification(String cookie,byte[] bytes){
		String verifyCode=null;
		try {
			verifyCode = new String(bytes,"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Verification verification=new Verification(cookie,verifyCode);
        MyJSONObject myJsonObject=new MyJSONObject();
        myJsonObject.setItem(verification);
        myJsonObject.setCode("ok");
        myJsonObject.setMsg("请求成功");
        myJsonObject.setTime();
        String jsonResult=JSONUtils.toJSON(myJsonObject);
        return jsonResult;
	}
	
	public static String analyzeLogin(String loginResult){
		String login=null;
		boolean flag=false;
		
		Document doc= Jsoup.parse(loginResult);
        Elements links=doc.select("a[href]");
        Elements alerts=doc.select("script[language]");

        for(Element link:links){
            if(link.text().equals("个人信息")){
                login="登录成功";
                flag=true;
            }
        }

        if(!flag){
        	for(Element alert:alerts){
                if(alert.data().contains("用户名不存在或未按照要求参加教学活动")){
                    login="用户名不存在或未按照要求参加教学活动";
                }
                else if(alert.data().contains("密码错误")){
                    login="密码错误";
                }
                else if(alert.data().contains("验证码不正确")){
                    login="验证码不正确";
                }
                else if(alert.data().contains("用户名不能为空")){
                	login="用户名不能为空";
                }
                else if(alert.data().contains("密码不能为空")){
                	login="密码不能为空";
                }
                else if(alert.data().contains("验证码不能为空，如看不清请刷新")){
                	login="验证码不能为空，如看不清请刷新";
                }
            }
        }
        MyJSONObject myJsonObject=new MyJSONObject();
        myJsonObject.setItem(login);
        myJsonObject.setCode("ok");
        myJsonObject.setMsg("请求成功");
        myJsonObject.setTime();
        String jsonResult=JSONUtils.toJSON(myJsonObject);
        System.out.println(jsonResult);
        return jsonResult;
	}
	
	public static String analyzeScore(String scoreResult){
		Map<String,List<String[]>> childName=new HashMap();
		Elements td= Jsoup.parse(scoreResult).select("td");
        ArrayList<String[]> scores=new ArrayList<>();
        for(int i=23;i<td.size();i=i+15){
            int j=i;
            if(td.get(j).text()==null||td.get(j).text().equals(""))
                break;
            String[] scoreInfo=new String[13];
            for(j=i;j<i+15;j++){
                if((j-23)%15!=13&&(j-23)%15!=14)
                    scoreInfo[(j-23)%15]=td.get(j).text();
            }
            scores.add(scoreInfo);
        }
        List<String[]> list=new ArrayList<>();
        for(int i=0;i<scores.size();i++){
            String[] scoreInfo=scores.get(i);
            if(i==0||scores.get(i)[1].equals(scores.get(i-1)[1]))
                list.add(scoreInfo);
            else {
                childName.put(list.get(0)[0]+list.get(0)[1],list);
                list=new ArrayList<>();
                list.add(scoreInfo);
            }
        }
        childName.put(list.get(0)[0]+list.get(0)[1],list);
        MyJSONObject myJsonObject=new MyJSONObject();
        myJsonObject.setItem(childName);
        myJsonObject.setCode("ok");
        myJsonObject.setMsg("请求成功");
        myJsonObject.setTime();
        String jsonResult=JSONUtils.toJSON(myJsonObject);
        System.out.println(jsonResult);
        return jsonResult;
	}
	
	public static String analyzeFirstCourse(List<String> list,String courseFirstResult){
		Document doc=Jsoup.parse(courseFirstResult);
		String courseViewState=doc.select("input[name=__VIEWSTATE]").val();
		Elements optionSelected=doc.select("option[selected]");
		String chooseYear=optionSelected.get(0).text();
		String chooseSemester=optionSelected.get(1).text();
        String chooseSelectedStr=chooseYear+chooseSemester;
		Elements option=doc.select("option");
		if(chooseSemester.equals("1")){
			for(int i=0;option.get(i).text().contains("-");i++){
				list.add(option.get(i).text()+"1");
			}
			for(int i=1;option.get(i).text().contains("-");i++){
				list.add(option.get(i).text()+"2");
			}
		}
		else{
			for(int i=0;option.get(i).text().contains("-");i++){
				list.add(option.get(i).text()+"2");
			}
			for(int i=0;option.get(i).text().contains("-");i++){
				list.add(option.get(i).text()+"1");
			}
		}
		return courseViewState;
	}
	
	public static void analyzeCourse(TreeMap<String,List<Course>> hashMap,String courseResult){
		List<Course> list=new ArrayList<>();
		Document doc=Jsoup.parse(courseResult);
        Elements optionSelected=doc.select("option[selected]");
        String chooseSelectedStr=optionSelected.get(0).text()+optionSelected.get(1).text();
        Elements td=doc.select("td");
        list.clear();
        for(int i=0;i<td.size();i++){
            String tdStr=td.get(i).text();
            if(tdStr.equals("第1节")||tdStr.equals("第3节")||tdStr.equals("第5节")||
                    tdStr.equals("第7节")||tdStr.equals("第9节")){
                int k=0;
                for(int j=i+1;j<=i+7;j++){
                    String ts=td.get(j).text();
                    if(ts.length()>1) {
                        int end = ts.indexOf(' ');
                        Course course = new Course();
                        course.setDay(k + 1);
                        course.setSection(Integer.parseInt(tdStr.substring(1, 2)));
                        course.setCourseName(ts.substring(0, end));
                        course.setCourseDetail(ts);
                        if (!td.get(j).attr("rowspan").equals("2"))
                            course.setCourseLong(Integer.parseInt(td.get(j).attr("rowspan")));
                        list.add(course);
                    }
                    k++;
                }
                i=i+7;
            }
        }
        hashMap.put(chooseSelectedStr, list);
	}
	
	public static String analyzeAllCourse(TreeMap<String, List<Course>> treeMap){
        MyJSONObject myJsonObject=new MyJSONObject();
        myJsonObject.setItem(treeMap);
        myJsonObject.setCode("ok");
        myJsonObject.setMsg("请求成功");
        myJsonObject.setTime();
        String jsonResult=JSONUtils.toJSON(myJsonObject);
        return jsonResult;
	}
	
	public static String analyzeLogout(String logout){
        MyJSONObject myJsonObject=new MyJSONObject();
        myJsonObject.setItem(logout);
        myJsonObject.setCode("ok");
        myJsonObject.setMsg("请求成功");
        myJsonObject.setTime();
        String jsonResult=JSONUtils.toJSON(myJsonObject);
        return jsonResult;
	}
	
}

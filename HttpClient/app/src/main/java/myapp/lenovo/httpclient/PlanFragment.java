package myapp.lenovo.httpclient;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlanFragment extends Fragment {
    private ListView listView;

    private String planViewStateStr;
    private String semesterStr;
    private String currentPageStr;
    private String pageSizeStr;
    private Handler handler;
    private List<String[]> planList;

    private static final String HOST_URL="http://222.24.62.120/";
    private static final String MAIN_URL="http://222.24.62.120/xs_main.aspx?xh=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_plan, container, false);
        listView= (ListView) view.findViewById(R.id.plan_lv);

        planList=new ArrayList<>();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1==8){
                    MyListViewAdapter myListViewAdapter=new MyListViewAdapter(planList,getContext());
                    listView.setAdapter(myListViewAdapter);
                }
            }
        };

        String linkURL=IOUtils.analyzeURL(MainActivity.loginResult,"培养计划");
        MyPlanFirstThread myPlanFirstThread=new MyPlanFirstThread(linkURL);
        myPlanFirstThread.start();

        return view;
    }

    class MyPlanFirstThread extends Thread{
        private String linkURL;

        MyPlanFirstThread(String linkURL){
            this.linkURL=linkURL;
        }

        @Override
        public void run() {
            HttpClient httpClient=new DefaultHttpClient();
            String url=HOST_URL+linkURL;
            HttpGet planFirstGet=new HttpGet(url);
            planFirstGet.setHeader("Cookie",MainActivity.cookie);
            planFirstGet.setHeader("Referer",MAIN_URL+MainActivity.accountStr);
            try {
                HttpResponse httpResponse=httpClient.execute(planFirstGet);
                if(httpResponse.getStatusLine().getStatusCode()==200)
                {
                    HttpEntity httpEntity=httpResponse.getEntity();
                    String planFirstResult= EntityUtils.toString(httpEntity);
                    //Log.d("planFirstResult",planFirstResult);
                    Document doc= Jsoup.parse(planFirstResult);
                    planViewStateStr =doc.select("input[name=__VIEWSTATE]").val();
                    Elements td=doc.select("td");
                    Elements selected=doc.select("option[selected]");
                    Element pageSize=doc.getElementById("dpDBGrid_lblTotalPages");
                    Element currentPage=doc.getElementById("dpDBGrid_lblCurrentPage");
                    //for(int i=0;i<td.size();i++){
                    //    Log.d("td:"+i,td.get(i).text());
                    //}
                    int i=22;
                    while (!td.get(i).text().equals("课程代码")) {
                        String[] plans = new String[15];
                        for (int j = 0; j < 15; j++) {
                            plans[j] = td.get(i + j).text();
                        }
                        i=i+16;
                        planList.add(plans);
                    }
                    for(int j=0;j<planList.size();j++){
                        for(String aplan:planList.get(j))
                            Log.d("plan",aplan);
                    }
                    semesterStr=selected.get(3).text();
                    Log.d("semester",semesterStr);
                    pageSizeStr=pageSize.text();
                    Log.d("pageSize",pageSizeStr);
                    currentPageStr=currentPage.text();
                    Log.d("currentPageStr",currentPageStr);
                    Message msg=handler.obtainMessage(1,"");
                    msg.arg1=8;
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.run();
        }
    }

    class MyPlanThread extends Thread{
        private String linkURL;

        MyPlanThread(String linkURL){
            this.linkURL=linkURL;
        }

        @Override
        public void run() {
            String target;
            if(Integer.parseInt(currentPageStr)>1)
                target="dpDBGrid:txtChoosePage";
            else
                target="xq";
            HttpClient httpClient=new DefaultHttpClient();
            String url=HOST_URL+linkURL;
            ArrayList<NameValuePair> pairs=new ArrayList<>();
            pairs.add(new BasicNameValuePair("__EVENTARGUMENT",""));
            pairs.add(new BasicNameValuePair("__EVENTTARGET",target));
            pairs.add(new BasicNameValuePair("__VIEWSTATE",planViewStateStr));
            pairs.add(new BasicNameValuePair("dpDBGrid:txtChoosePage",currentPageStr));
            pairs.add(new BasicNameValuePair("dpDBGrid:txtPageSize","10"));
            pairs.add(new BasicNameValuePair("kcxz","全部"));
            pairs.add(new BasicNameValuePair("xq",semesterStr));
            try {
                HttpEntity requestEntity=new UrlEncodedFormEntity(pairs);
                //String urlUTF=new String(url.getBytes("UTF-8"),"iso-8859-1");
                HttpPost planPost=new HttpPost(url);
                planPost.setEntity(requestEntity);
                planPost.setHeader("Cookie",MainActivity.cookie);
                planPost.setHeader("Referer",url);
                HttpResponse httpResponse=httpClient.execute(planPost);
                if(httpResponse.getStatusLine().getStatusCode()==200)
                {
                    HttpEntity httpEntity=httpResponse.getEntity();
                    String planResult=EntityUtils.toString(httpEntity);
                    //Log.d("planResult",planResult);
                    Document doc=Jsoup.parse(planResult);
                    planViewStateStr =doc.select("input[name=__VIEWSTATE]").val();
                    Elements td=doc.select("td");
                    Element pageSize=doc.getElementById("dpDBGrid_lblTotalPages");
                    Element currentPage=doc.getElementById("dpDBGrid_lblCurrentPage");
                    //for(int i=0;i<td.size();i++){
                    //    Log.d("td:"+i,td.get(i).text());
                    //}
                    int i=22;
                    while (!td.get(i).text().equals("课程代码")) {
                        String[] plans = new String[15];
                        for (int j = 0; j < 15; j++) {
                            plans[j] = td.get(i + j).text();
                        }
                        i=i+16;
                        planList.add(plans);
                    }
                    pageSizeStr=pageSize.text();
                    Log.d("pageSize",pageSizeStr);
                    currentPageStr=currentPage.text();
                    Log.d("currentPageStr",currentPageStr);
                    //if(Integer.parseInt(pageSizeStr)>1)
                    //Log.d("allScoreResult",allScoreResult);
                    Message msg=handler.obtainMessage(1,"");
                    msg.arg1=9;
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.run();
        }
    }

}

package myapp.lenovo.httpclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class LoginActivity extends Activity {
    private EditText account;
    private EditText password;
    private EditText confirm;
    private ImageView verification;
    private Bitmap verifyBitmap;
    private Handler handler;

    private String cookie;
    private String loginResult;
    private String accountStr;

    private static final String LOGIN_URL="http://222.24.62.120/default2.aspx";
    private static final String VERIFICATION_URL="http://222.24.62.120/CheckCode.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        account = (EditText) findViewById(R.id.account_et);
        password = (EditText) findViewById(R.id.password_et);
        confirm= (EditText) findViewById(R.id.verification_et);
        Button login = (Button) findViewById(R.id.login_btn);
        verification= (ImageView) findViewById(R.id.identifying_code_iv);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.arg1)
                {
                    case 0:verification.setImageBitmap(verifyBitmap);break;
                    case 1:
                        Toast.makeText(LoginActivity.this, "登录成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("loginResult",loginResult);
                        intent.putExtra("cookie",cookie);
                        intent.putExtra("accountStr",accountStr);
                        startActivity(intent);break;
                    case 2:Toast.makeText(LoginActivity.this, "用户名不存在或未按照要求参加教学活动",
                            Toast.LENGTH_SHORT).show();break;
                    case 3:Toast.makeText(LoginActivity.this, "密码错误",
                            Toast.LENGTH_SHORT).show();break;
                    case 4:Toast.makeText(LoginActivity.this, "验证码不正确",
                            Toast.LENGTH_SHORT).show();break;
                    case 5:Toast.makeText(LoginActivity.this, "用户名不能为空",
                            Toast.LENGTH_SHORT).show();break;
                    case 6:Toast.makeText(LoginActivity.this, "密码不能为空",
                            Toast.LENGTH_SHORT).show();break;
                    case 7:Toast.makeText(LoginActivity.this, "验证码不能为空，如看不清请刷新",
                            Toast.LENGTH_SHORT).show();break;

                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountStr=account.getText().toString().trim();
                String passwordStr=password.getText().toString().trim();
                String confirmStr=confirm.getText().toString().trim();

                MyLoginThread myloginThread=new MyLoginThread(accountStr,passwordStr,confirmStr);
                myloginThread.start();
            }
        });
    }

    @Override
    protected void onResume() {
        loadVerification();
        super.onResume();
    }

    class MyVerifyThread extends Thread{
        @Override
        public void run() {
            Log.d("haha","haha");
            HttpClient httpClient=new DefaultHttpClient();
            //HttpGet hostGet=new HttpGet(LOGIN_URL);
            try {
                //httpClient.execute(hostGet);
                HttpGet verifyGet=new HttpGet(VERIFICATION_URL);
                HttpResponse httpResponse=httpClient.execute(verifyGet);
                cookie=httpResponse.getFirstHeader("Set-Cookie").getValue();
                if(httpResponse.getStatusLine().getStatusCode()==200)
                {
                    HttpEntity httpEntity=httpResponse.getEntity();
                    byte[] bytes=EntityUtils.toByteArray(httpEntity);
                    verifyBitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    Message msg=handler.obtainMessage(1,"");
                    msg.arg1=0;
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.run();
        }
    }

    class MyLoginThread extends Thread{
        private String accountStr;
        private String passwordStr;
        private String verificationStr;

        MyLoginThread(String accountStr, String passwordStr ,String verificationStr){
            this.accountStr=accountStr;
            this.passwordStr=passwordStr;
            this.verificationStr=verificationStr;
        }

        @Override
        public void run() {
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
            pairs.add(new BasicNameValuePair("TextBox2",passwordStr));
            pairs.add(new BasicNameValuePair("txtSecretCode",verificationStr));
            pairs.add(new BasicNameValuePair("txtUserName",accountStr));
            try {
                HttpEntity requestEntity=new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
                loginPost.setEntity(requestEntity);
                HttpResponse httpResponse=httpClient.execute(loginPost);
                if(httpResponse.getStatusLine().getStatusCode()==200)
                {
                    HttpEntity httpEntity=httpResponse.getEntity();
                    loginResult= EntityUtils.toString(httpEntity);
                    //Log.d("loginResult",loginResult);
                    analyzeLogin();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.run();
        }
    }

    public void loadVerification(){
        MyVerifyThread myVerifyThread=new MyVerifyThread();
        myVerifyThread.start();
    }

    public void analyzeLogin(){
        Document doc= Jsoup.parse(loginResult);
        Elements links=doc.select("a[href]");
        Elements alerts=doc.select("script[language]");

        for(Element link:links){
            Message msg=handler.obtainMessage(1,"");
            if(link.text().equals("选体育课")){
                msg.arg1=1;
                handler.sendMessage(msg);
                return;
            }
        }

        for(Element alert:alerts){
            Message msg=handler.obtainMessage(1,"");
            if(alert.data().contains("用户名不存在或未按照要求参加教学活动")){
                msg.arg1=2;
                handler.sendMessage(msg);
            }
            else if(alert.data().contains("密码错误")){
                msg.arg1=3;
                handler.sendMessage(msg);
            }
            else if(alert.data().contains("验证码不正确")){
                msg.arg1=4;
                handler.sendMessage(msg);
            }
            else if(alert.data().contains("用户名不能为空")){
                msg.arg1=5;
                handler.sendMessage(msg);
            }
            else if(alert.data().contains("密码不能为空")){
                msg.arg1=6;
                handler.sendMessage(msg);
            }
            else if(alert.data().contains("验证码不能为空，如看不清请刷新")){
                msg.arg1=7;
                handler.sendMessage(msg);
            }
        }
    }
}

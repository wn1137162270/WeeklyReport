package webview.imooc.lenovo.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private String url="http://2014.qq.com/";
    private WebView webWiew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Uri uri=Uri.parse(url);
        //Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        //startActivity(intent);
        init();
    }

    private void init(){
        webWiew= (WebView) findViewById(R.id.web_view);
        webWiew.loadUrl("http://115.159.205.168/ocr_php/public/");
        webWiew.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings webSettings=webWiew.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(webWiew.canGoBack()){
                webWiew.goBack();
            }
            else{
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

package myapp.lenovo.ocr2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText account;
    private EditText password;
    private Button register;
    private Button entry;
    private CheckBox savePassword;

    private String registerAccount;
    private String registerPassword;
    private String loadResult;
    private boolean isResumeLoad;
    private Intent intentDbs;
    private Intent intentRgt;
    private MyReviver myReviver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isResumeLoad=false;

        account= (EditText) findViewById(R.id.account_et);
        password= (EditText) findViewById(R.id.password_et);
        register= (Button) findViewById(R.id.register_btn);
        entry= (Button) findViewById(R.id.entry_btn);
        savePassword= (CheckBox) findViewById(R.id.save_password_cb);

        register.setOnClickListener(new MyOnClickListener());
        entry.setOnClickListener(new MyOnClickListener());

        myReviver=new MyReviver();
        intentFilter=new IntentFilter();
        intentFilter.addAction("loadResult");
        registerReceiver(myReviver,intentFilter);
    }

    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.register_btn:
                    intentRgt=new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(intentRgt);
                    break;
                case R.id.entry_btn:
                    String entryAccount=account.getText().toString().trim();
                    String entryPassword=password.getText().toString().trim();
                    intentDbs=new Intent(MainActivity.this,DatabaseService.class);
                    intentDbs.putExtra("isEntry",true);
                    intentDbs.putExtra("entryAccount",entryAccount);
                    intentDbs.putExtra("entryPassword",entryPassword);
                    startService(intentDbs);
            }
        }
    }

    public class MyReviver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("loadResult")){
                Log.i("1","1");
                loadResult=intent.getStringExtra("loadResult");
            }
            Log.i("result",loadResult);
            switch (loadResult) {
                case "succeedLoad":
                    unregisterReceiver(myReviver);
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intentOpe = new Intent(MainActivity.this, OperateActivity.class);
                    startActivity(intentOpe);
                    break;
                case "errorPassword":
                    Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    account.setText("");
                    password.setText("");
                    break;
                case "noneAccount":
                    Toast.makeText(MainActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                    account.setText("");
                    password.setText("");
                    break;
            }
        }
    }
}

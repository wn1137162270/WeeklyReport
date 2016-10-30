package myapp.lenovo.ocr2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
    private EditText account;
    private EditText password;
    private Button confirm;
    private Button cancel;

    private Intent intentDbs;
    private boolean isResumeLoad;
    private String registerResult;
    private MyReviver myReviver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        isResumeLoad = true;

        account = (EditText) findViewById(R.id.account_et);
        password = (EditText) findViewById(R.id.password_et);
        confirm = (Button) findViewById(R.id.confirm_btn);
        cancel = (Button) findViewById(R.id.cancel_btn);

        confirm.setOnClickListener(new MyOnClickListener());
        cancel.setOnClickListener(new MyOnClickListener());

        myReviver=new MyReviver();
        intentFilter=new IntentFilter();
        intentFilter.addAction("registerResult");
        registerReceiver(myReviver,intentFilter);
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.confirm_btn:
                    String registerAccount = account.getText().toString().trim();
                    String registerPassword = password.getText().toString().trim();
                    intentDbs=new Intent(RegisterActivity.this,DatabaseService.class);
                    intentDbs.putExtra("registerAccount", registerAccount);
                    intentDbs.putExtra("registerPassword", registerPassword);
                    startService(intentDbs);
                    break;
                case R.id.cancel_btn:
                    RegisterActivity.this.onStop();
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public class MyReviver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("registerResult")){
                registerResult=intent.getStringExtra("registerResult");
            }
            if(registerResult.equals("succeedRegister")){
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                onStop();
            }
            else if(registerResult.equals("errorPassword")){
                Toast.makeText(RegisterActivity.this, "密码位数有误", Toast.LENGTH_SHORT).show();
                account.setText("");
            }
            else if(registerResult.equals("haveRegistered")){
                Toast.makeText(RegisterActivity.this, "账号已注册", Toast.LENGTH_SHORT).show();
                account.setText("");
                password.setText("");
            }
        }
    }
}

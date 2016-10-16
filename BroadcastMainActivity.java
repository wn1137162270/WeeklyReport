package broadcast.imooc.lenovo.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button send1;
    private Button send2;
    private Button send3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send1= (Button) findViewById(R.id.send_broadcast_btn);
        send2= (Button) findViewById(R.id.send_order_broadcast_btn);
        send3= (Button) findViewById(R.id.send_sticky_broadcast_btn);
    }

    public void doClick(View v){
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.send_broadcast_btn:
                intent.setAction("broadcast.imooc.lenovo.broadcast.Broadcast");
                intent.putExtra("msg1","sendBroadcast");
                sendBroadcast(intent);
                break;
            case R.id.send_order_broadcast_btn:
                intent.setAction("broadcast.imooc.lenovo.broadcast.OrderedBroadcast");
                intent.putExtra("msg2","sendOrderedBroadcast");
                sendOrderedBroadcast(intent,);
                break;
            case R.id.send_sticky_broadcast_btn:
                intent.setAction("broadcast.imooc.lenovo.broadcast.StickyBroadcast");
                intent.putExtra("msg3","sendStickyBroadcast");
                sendStickyBroadcast(intent);
                break;
        }
    }
}

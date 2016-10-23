package myapp.lenovo.ocr;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class DatabaseService extends Service {
    private SQLiteDatabase db;
    private String loadResult;
    private String registerResult;
    private Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();

        db = openOrCreateDatabase("account.db", MODE_PRIVATE, null);
        db.execSQL("create table if not exists accounttb(_id integer primary key autoincrement," +
                "account text not null,password text not null)");
        Cursor cursorTest = db.query("accounttb", null, "_id>?", new String[]{"0"}, null, null, null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra("isEntry", false)) {
            String entryAccount = intent.getStringExtra("entryAccount");
            String entryPassword = intent.getStringExtra("entryPassword");
            Cursor cursor = db.query("accounttb", new String[]{"account", "password"}, "account=?", new String[]{entryAccount}, null, null, null);
            int j = cursor.getCount();
            Log.i("msg3", j + "");
            if (cursor != null) {
                if (cursor.getCount() != 0) {
                    cursor.moveToNext();
                    String passworddb = cursor.getString(cursor.getColumnIndex("password"));
                    if (passworddb.equals(entryPassword)) {
                        loadResult = "succeedLoad";
                    } else {
                        loadResult = "errorPassword";
                    }
                } else {
                    loadResult = "noneAccount";
                }
                Log.i("result", loadResult);
                intent = new Intent("loadResult");
                intent.putExtra("loadResult", loadResult);
                sendBroadcast(intent);
            }
        } else {
            String registerAccount = intent.getStringExtra("registerAccount");
            String registerPassword = intent.getStringExtra("registerPassword");
            Log.d("0", registerAccount);
            Log.d("1", registerPassword);
            Cursor cursorReg = db.query("accounttb", new String[]{"account"}, "account=?", new String[]{registerAccount}, null, null, null);
            if (cursorReg != null) {
                if (cursorReg.getCount() == 0) {
                    if(registerPassword.length()>=6&&registerPassword.length()<=18) {
                        registerResult="succeedRegister";
                        ContentValues cv = new ContentValues();
                        cv.put("account", registerAccount);
                        cv.put("password", registerPassword);
                        db.insert("accounttb", null, cv);
                    }
                    else{
                        registerResult="errorPassword";
                    }
                }
                else {
                    registerResult="haveRegistered";
                }
                intent = new Intent("registerResult");
                intent.putExtra("registerResult", registerResult);
                sendBroadcast(intent);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

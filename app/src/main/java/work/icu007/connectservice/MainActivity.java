package work.icu007.connectservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import work.icu007.connectservice.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private static final String TAG = "lcr add";
    private Intent intent;
    private TextView textView;
    private EditText editText;
    private MyService.Binder binder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent = new Intent(this,MyService.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.tvOut);
        findViewById(R.id.btnStartService).setOnClickListener(this);
        findViewById(R.id.btnStopService).setOnClickListener(this);
        findViewById(R.id.btnBindService).setOnClickListener(this);
        findViewById(R.id.btnUnbindService).setOnClickListener(this);
        findViewById(R.id.btnSyncData).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStartService:
                intent.putExtra("data",editText.getText().toString());
                startService(intent);
                break;
            case R.id.btnBindService:
                bindService(intent,this, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btnUnbindService:
                unbindService(this);
                break;
            case R.id.btnStopService:
                stopService(intent);
                break;
            case R.id.btnSyncData:
                if (binder != null){
                    binder.setData(editText.getText().toString());
                }

        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // ??????????????? ?????? ???????????? ????????????service ???????????????binder ????????????setCallback?????????
        binder = (MyService.Binder) service;
        binder.getService().setCallback(new MyService.Callback() {
            // ?????????????????? ???????????????str?????????textView??????
            @Override
            public void onDataChange(String str) {
//                textView.setText(str);
//                ???????????? setText ???????????????????????????????????????????????????????????????????????????????????????UI?????????????????????????????????UI?????????????????????????????????????????????UI??????????????????
                Message message = new Message();
                Bundle bundle = new Bundle();
                Log.d(TAG, "onDataChange: I am the str" + str);
//                Toast.makeText(MainActivity.this, "The string is" + str , Toast.LENGTH_SHORT).show();
                bundle.putString("data",str);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
//            msg.getData().getString("data");
            textView.setText(msg.getData().getString("data"));
        }
    };
}
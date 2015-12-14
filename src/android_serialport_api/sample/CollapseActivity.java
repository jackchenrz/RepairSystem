package android_serialport_api.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.activity.LoginActivity;

public class CollapseActivity extends Activity {
    private Button btnRestart, btnExit;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapse);
        setTitle("”¶”√±¿¿£¡À");
        btnRestart = (Button) findViewById(R.id.collapse_restart);
        btnExit = (Button) findViewById(R.id.collapse_exit);
        btnRestart.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        btnExit.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
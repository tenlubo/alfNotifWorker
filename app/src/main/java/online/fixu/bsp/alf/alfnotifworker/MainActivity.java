package online.fixu.bsp.alf.alfnotifworker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {

        Log.d(TAG, "onClick()");
        try {
            Intent k = new Intent(this, WorkFlowActivity.class);
            startActivity(k);
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}

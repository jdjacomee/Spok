package ec.edu.uce.spok;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin1;
    private Button btnRegistrarse1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin1 = (Button) findViewById(R.id.btnLogin);
        btnRegistrarse1 = (Button) findViewById(R.id.btnRegistrase);

        btnLogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnRegistrarse1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, RegistroActivity.class);
                startActivity(i);
            }
        });

    }
}

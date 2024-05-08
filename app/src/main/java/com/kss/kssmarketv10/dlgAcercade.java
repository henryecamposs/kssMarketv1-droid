package com.kss.kssmarketv10;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class dlgAcercade extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlg_acercade);
        final ImageView ivExit= (ImageView) findViewById(R.id.ivExit_Ace);
        final ImageButton ibHome = (ImageButton) findViewById(R.id.ibHome_Ace) ;
        ivExit.setOnClickListener(OnClickListener);
        ibHome.setOnClickListener(OnClickListener);


    }
    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivExit_Ace:
                    finish();
                    break;
                case R.id.ibHome_Ace:
                    finish();
                    break;

            }
        }
    };
}

package com.kss.kssmarketv10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.kss.kssmarketv10.R;

public class plantilla extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantilla);



    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvSave_Clie:
                    break;
                case R.id.tvCancel_Clie:
                    break;
                case R.id.ivExit_Conf:
                    break;
                case R.id.ibHome_Conf:
                    break;
                case R.id.tvZoom_Conf:
                    break;
                case R.id.ivTakeImage_Conf:
                    break;
            }
        }
    };
}

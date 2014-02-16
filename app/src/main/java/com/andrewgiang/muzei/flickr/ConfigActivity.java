package com.andrewgiang.muzei.flickr;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ConfigActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Typeface alegreyaBI = Typeface.createFromAsset(getAssets(),"Alegreya-BlackItalic.ttf");
        final TextView headerContent = (TextView)findViewById(R.id.header_content);
        headerContent.setTypeface(alegreyaBI);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.search_options)));
        if (savedInstanceState == null) {

        }
    }



}

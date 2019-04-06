package com.example.phonebooktry;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.ListView;

public class MainActivity extends Activity {

    private CardView lvphone;
    private ListView lvPhone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvphone = (CardView)findViewById(R.id.listphone);
        lvPhone = (ListView)findViewById(R.id.liphone);

        List<phonebook> listphonebook = new ArrayList<phonebook>();
        listphonebook.add(new phonebook(
                BitmapFactory.decodeResource(getResources(), R.drawable.wife),
                "                       Sudha", "                       1234567980", "                      Wife"));
        listphonebook.add(new phonebook(
                BitmapFactory.decodeResource(getResources(), R.drawable.daughter),
                "                       Subadhra", "                        6106300867", "                       Daughter"));
        listphonebook.add(new phonebook(
                BitmapFactory.decodeResource(getResources(), R.drawable.son),
                "                       Madhu", "                       2381490000", "                       Son"));
        listphonebook.add(new phonebook(
                BitmapFactory.decodeResource(getResources(), R.drawable.dil),
                "                       Lakshmi", "                       1234567890", "                       Daughter-in-law"));
        phonebookadapter adapter = new phonebookadapter(this, listphonebook);
        lvPhone.setAdapter(adapter);
    }
}

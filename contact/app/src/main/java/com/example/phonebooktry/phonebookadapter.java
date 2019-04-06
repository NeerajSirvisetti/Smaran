package com.example.phonebooktry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;
import android.support.v7.widget.CardView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class phonebookadapter extends BaseAdapter {

        private Context mContext;
        private List<phonebook> mListphonebook;

        public phonebookadapter(Context context, List<phonebook> list) {
            mContext = context;
            mListphonebook = list;

        }

        @Override
        public int getCount() {
            return mListphonebook.size();
        }

        @Override
        public Object getItem(int pos) {
            return mListphonebook.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            // get selected entry
            phonebook entry = mListphonebook.get(pos);

            // inflating list view layout if null
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.activity_phonebook, null);
            }

            // set avatar
            ImageView ivAvatar = (ImageView)convertView.findViewById(R.id.imgAvatar);
            ivAvatar.setImageBitmap(entry.getAvatar());

            // set name
            TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
            tvName.setText(entry.getName());

            // set phone
            TextView tvPhone = (TextView)convertView.findViewById(R.id.tvPhone);
            tvPhone.setText(entry.getPhone());

            // set email
            TextView tvEmail = (TextView)convertView.findViewById(R.id.tvEmail);
            tvEmail.setText(entry.getEmail());

            return convertView;
        }

}

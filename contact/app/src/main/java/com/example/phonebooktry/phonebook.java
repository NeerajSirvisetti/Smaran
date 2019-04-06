package com.example.phonebooktry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Bitmap;

public class phonebook {
        private Bitmap mAvatar;
        private String mName;
        private String mPhone;
        private String mEmail;

        public phonebook(Bitmap avatar, String name, String phone, String email) {
            mAvatar = avatar;
            mName = name;
            mPhone = phone;
            mEmail = email;
        }

        public void setAvatar(Bitmap avatar) {
            mAvatar = avatar;
        }
        public Bitmap getAvatar() {
            return mAvatar;
        }

        public void setName(String name) {
            mName = name;
        }
        public String getName() {
            return mName;
        }

        public void setPhone(String phone) {
            mPhone = phone;
        }
        public String getPhone() {
            return mPhone;
        }

        public void setEmail(String email) {
            mEmail = email;
        }
        public String getEmail() {
            return mEmail;
        }
}


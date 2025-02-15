package com.pro.shopfee.prefs;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pro.shopfee.model.User;
import com.pro.shopfee.utils.StringUtil;
import com.google.gson.Gson;

public class DataStoreManager {

    public static final String PREF_USER_INFOR = "PREF_USER_INFOR";
    private static DataStoreManager instance;
    private MySharedPreferences sharedPreferences;

    public static void init(Context context) {
        instance = new DataStoreManager();
        instance.sharedPreferences = new MySharedPreferences(context);
    }

    public static DataStoreManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }

    public static void setUser(@Nullable User user) {
        String jsonUser = "";
        if (user != null) {
            jsonUser = user.toJSon();
        }
        DataStoreManager.getInstance().sharedPreferences
                .putStringValue(PREF_USER_INFOR, jsonUser);
    }

    public static User getUser() {
        String jsonUser = DataStoreManager.getInstance()
                .sharedPreferences.getStringValue(PREF_USER_INFOR);
        if (!StringUtil.isEmpty(jsonUser)) {
            return new Gson().fromJson(jsonUser, User.class);
        }
        return new User();
    }

    public static void listenForRoleChanges(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String email = snapshot.child("email").getValue(String.class);
                    String role = snapshot.child("role").getValue(String.class);

                    Log.d("UserRole", "Dữ liệu lấy từ Firebase - Email: " + email + " | Role: " + role);

                    User user = new User();
                    user.setEmail(email);
                    user.setRole(role);

                    setUser(user); // Cập nhật vào SharedPreferences
                } else {
                    Log.d("UserRole", "Không tìm thấy dữ liệu user trên Firebase!");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("UserRole", "Lỗi lấy dữ liệu từ Firebase: " + error.getMessage());
            }
        });
    }

}
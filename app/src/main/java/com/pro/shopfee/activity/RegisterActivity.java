package com.pro.shopfee.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pro.shopfee.R;
import com.pro.shopfee.model.User;
import com.pro.shopfee.prefs.DataStoreManager;
import com.pro.shopfee.utils.GlobalFunction;
import com.pro.shopfee.utils.StringUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {

    private EditText edtEmail, edtPassword;
    private Button btnRegister;
    private LinearLayout layoutLogin;
    private boolean isEnableButtonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUi();
        initListener();
    }

    private void initUi() {
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnRegister = findViewById(R.id.btn_register);
        layoutLogin = findViewById(R.id.layout_login);
    }

    private void initListener() {
        edtEmail.addTextChangedListener(textWatcher);
        edtPassword.addTextChangedListener(textWatcher);
        layoutLogin.setOnClickListener(v -> finish());
        btnRegister.setOnClickListener(v -> onClickValidateRegister());
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            validateInput();
        }
    };

    private void validateInput() {
        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();

        isEnableButtonRegister = !StringUtil.isEmpty(strEmail) && !StringUtil.isEmpty(strPassword);
        btnRegister.setBackgroundResource(isEnableButtonRegister
                ? R.drawable.bg_button_enable_corner_16
                : R.drawable.bg_button_disable_corner_16);
    }

    private void onClickValidateRegister() {
        if (!isEnableButtonRegister) return;

        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();

        if (StringUtil.isEmpty(strEmail)) {
            showToastMessage(getString(R.string.msg_email_require));
        } else if (StringUtil.isEmpty(strPassword)) {
            showToastMessage(getString(R.string.msg_password_require));
        } else if (!StringUtil.isValidEmail(strEmail)) {
            showToastMessage(getString(R.string.msg_email_invalid));
        } else {
            registerUserFirebase(strEmail, strPassword);
        }
    }

    private void registerUserFirebase(String email, String password) {
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            saveUserToDatabase(user.getUid(), email);
                        }
                    } else {
                        showToastMessage(getString(R.string.msg_register_error));
                    }
                });
    }

    private void saveUserToDatabase(String uid, String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Chỉ lưu email và role, không lưu password
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("role", "user"); // Mặc định role là "user"

        databaseReference.child(uid).setValue(userMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Lưu email và role vào local storage nhưng không lưu password
                        User userObject = new User(email, "user");
                        DataStoreManager.setUser(userObject);
                        goToMainActivity();
                    } else {
                        showToastMessage(getString(R.string.msg_register_error));
                    }
                });
    }

    private void goToMainActivity() {
        GlobalFunction.startActivity(this, MainActivity.class);
        finishAffinity();
    }
}

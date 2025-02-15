package com.pro.shopfee.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.shopfee.R;
import com.pro.shopfee.activity.admin.AdminMainActivity;
import com.pro.shopfee.model.User;
import com.pro.shopfee.prefs.DataStoreManager;
import com.pro.shopfee.utils.GlobalFunction;
import com.pro.shopfee.utils.StringUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private LinearLayout layoutRegister;
    private TextView tvForgotPassword;
    private boolean isEnableButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initUi();
        initListener();
    }

    private void initUi() {
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        layoutRegister = findViewById(R.id.layout_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
    }

    private void initListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateInput();
            }
        };

        edtEmail.addTextChangedListener(textWatcher);
        edtPassword.addTextChangedListener(textWatcher);
        layoutRegister.setOnClickListener(v -> GlobalFunction.startActivity(this, RegisterActivity.class));
        btnLogin.setOnClickListener(v -> onClickValidateLogin());
        tvForgotPassword.setOnClickListener(v -> GlobalFunction.startActivity(this, ForgotPasswordActivity.class));
    }

    private void validateInput() {
        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();

        isEnableButtonLogin = !StringUtil.isEmpty(strEmail) && !StringUtil.isEmpty(strPassword);
        btnLogin.setBackgroundResource(isEnableButtonLogin
                ? R.drawable.bg_button_enable_corner_16
                : R.drawable.bg_button_disable_corner_16);
    }

    private void onClickValidateLogin() {
        if (!isEnableButtonLogin) return;

        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();

        if (StringUtil.isEmpty(strEmail)) {
            showToastMessage(getString(R.string.msg_email_require));
        } else if (StringUtil.isEmpty(strPassword)) {
            showToastMessage(getString(R.string.msg_password_require));
        } else if (!StringUtil.isValidEmail(strEmail)) {
            showToastMessage(getString(R.string.msg_email_invalid));
        } else {
            loginUserFirebase(strEmail, strPassword);
        }
    }

    private void loginUserFirebase(String email, String password) {
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            checkUserRole(user.getUid(), email);
                        }
                    } else {
                        showToastMessage(getString(R.string.msg_login_error));
                    }
                });
    }

    private void checkUserRole(String uid, String email) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot snapshot = task.getResult();
                String role = snapshot.child("role").getValue(String.class);

                if (role == null) {
                    showToastMessage("Không thể xác định vai trò người dùng!");
                    return;
                }

                // Chỉ lưu email và role, không lưu mật khẩu
                User userObject = new User(email, role);
                DataStoreManager.setUser(userObject);

                // GỌI HÀM ĐỂ THEO DÕI ROLE THAY ĐỔI SAU ĐĂNG NHẬP
                DataStoreManager.listenForRoleChanges(uid);

                if ("admin".equals(role)) {
                    GlobalFunction.startActivity(this, AdminMainActivity.class);
                } else {
                    GlobalFunction.startActivity(this, MainActivity.class);
                }

                finishAffinity();
            } else {
                showToastMessage("Không thể lấy dữ liệu người dùng!");
            }
        });
    }

}

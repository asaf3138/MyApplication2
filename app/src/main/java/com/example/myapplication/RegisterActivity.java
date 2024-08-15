package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    private FirebaseAuth firebaseAuth; // אובייקט לניהול אימות משתמשים ב-Firebase

    private ProgressDialog progressDialog; // דיאלוג התקדמות

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance(); // אתחול של אובייקט ה-FirebaseAuth

        progressDialog = new ProgressDialog(this); // אתחול של דיאלוג ההתקדמות
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        // ניהול לחצן חזרה למסך הקודם
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // ניהול לחצן הרשמה
        binding.siginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validData(); // אימות נתוני המשתמש
            }
        });
    }

    // פונקציה לאימות נתונים של המשתמש
    private String fname = "", lname = "", phone = "", email = "", password = "";
    private void validData() {
        fname = binding.firstnameET.getText().toString().trim(); // שם פרטי
        lname = binding.lastnameET.getText().toString().trim(); // שם משפחה
        email = binding.emailET.getText().toString().trim(); // דואר אלקטרוני
        phone = binding.phoneET.getText().toString().trim(); // מספר טלפון
        password = binding.passwordET.getText().toString().trim(); // סיסמה
        String cpassword = binding.confirmPasswordET.getText().toString().trim(); // אימות סיסמה

        // בדיקת שדות חובה
        if (TextUtils.isEmpty(fname)) {
            Toast.makeText(this, "Enter your First Name", Toast.LENGTH_SHORT).show();
            return;
        }

        else if (TextUtils.isEmpty(lname)) {
            Toast.makeText(this, "Enter your Last Name", Toast.LENGTH_SHORT).show();
            return;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }

        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        else if (TextUtils.isEmpty(cpassword)) {
            Toast.makeText(this, "Confirm Password", Toast.LENGTH_SHORT).show();
            return;
        }

        else if (!password.equals(cpassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        createUserAccount(); // יצירת חשבון משתמש
    }

    private void createUserAccount() {
        progressDialog.setMessage("Creating Account ...");
        progressDialog.show();

        // יצירת חשבון משתמש ב-Firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // חשבון נוצר בהצלחה, מעדכנים את פרטי המשתמש
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // יצירת החשבון נכשלה
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserInfo() {
        progressDialog.setMessage("Saving user Data ...");

        long timestamp = System.currentTimeMillis(); // תאריך וזמן הנוכחיים
        String uid = firebaseAuth.getUid(); // מזהה המשתמש

        // ארגון המידע ב-HashMap
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("firstName", fname);
        hashMap.put("lastName", lname);
        hashMap.put("phone", phone);
        hashMap.put("timestamp", timestamp);

        // הוספת המידע למסד הנתונים של Firebase
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // מידע נוסף בהצלחה למסד הנתונים
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Account created ...", Toast.LENGTH_SHORT).show();
                        // מעבר למסך הלוח הראשי של המשתמש
                        startActivity(new Intent(RegisterActivity.this, DashBoardUserActivity.class));
                        finish(); // סיום הפעולה הנוכחית
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // הוספת המידע למסד הנתונים נכשלה
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

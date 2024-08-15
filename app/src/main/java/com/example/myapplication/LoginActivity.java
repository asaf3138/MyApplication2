package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding; // משתנה להחזקת Binding של הקלטים במסך

    private FirebaseAuth firebaseAuth; // משתנה להחזקת פרטי ההתחברות עם Firebase

    private ProgressDialog progressDialog; // דיאלוג להתקדמות עבור התחברות

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater()); // קבלת binding של המסך
        setContentView(binding.getRoot()); // הגדרת ה-View של הפעילות

        firebaseAuth = FirebaseAuth.getInstance(); // אתחול של FirebaseAuth

        progressDialog = new ProgressDialog(this); // אתחול של דיאלוג ההתקדמות
        progressDialog.setTitle("Please wait..."); // כותרת לדיאלוג
        progressDialog.setCanceledOnTouchOutside(false); // דיאלוג לא ייסגר בלחיצה מחוץ לו

        // טיפול בלחיצה על "אין לך חשבון?" - מעבר למסך ההרשמה
        binding.noaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        // טיפול בלחיצה על כפתור ההתחברות - התחלת תהליך ההתחברות
        binding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData(); // קריאה לפונקציה לאימות הנתונים
            }
        });
    }

    private String email = "", password = ""; // משתנים לשמירת פרטי ההתחברות

    private void validateData() {
        // פונקציה לאימות הנתונים לפני התחברות
        email = binding.emailET.getText().toString().trim(); // קבלת המייל מהקלט
        password = binding.passwordET.getText().toString().trim(); // קבלת הסיסמה מהקלט

        // בדיקה אם המייל תקין
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }
        // בדיקה אם הסיסמה לא ריקה
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        } else {
            loginUser(); // קריאה לפונקציה להתחברות
        }
    }

    private void loginUser() {
        progressDialog.setMessage("Logging In ..."); // הצגת הודעה בדיאלוג
        progressDialog.show(); // הצגת דיאלוג ההתקדמות

        // ניסיון להתחבר עם פרטי המשתמש
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // התחברות הצליחה, נבדוק את סוג המשתמש
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss(); // סגירת דיאלוג ההתקדמות
                        Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show(); // הצגת הודעת שגיאה
                    }
                });
    }

    private void checkUser() {
        progressDialog.setMessage("Checking User ..."); // הצגת הודעה בדיאלוג

        // קבלת המשתמש הנוכחי וקריאה למסד הנתונים לבדוק את סוגו
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                progressDialog.dismiss(); // סגירת דיאלוג ההתקדמות
                // מעבר למסך הראשי של המשתמש (Dashboard)
                startActivity(new Intent(LoginActivity.this, DashBoardUserActivity.class));
                finish(); // סיום הפעילות הנוכחית
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // טיפול במקרה של שגיאה בקריאה למסד הנתונים (לא מופיע כאן אבל ניתן להוסיף טיפול שגיאות אם נדרש)
            }
        });
    }
}

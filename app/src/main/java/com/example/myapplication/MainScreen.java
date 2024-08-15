package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainScreen extends AppCompatActivity {

    private FirebaseAuth firebaseAuth; // משתנה להחזקת אינסטנס של FirebaseAuth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen); // קובע את ה-View של הפעילות

        firebaseAuth = FirebaseAuth.getInstance(); // מקבל את אינסטנס של FirebaseAuth

        // הצגת המסך הבא לאחר השהייה של 3 שניות
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser(); // בדיקת מצב המשתמש
                startActivity(new Intent(MainScreen.this, MainActivity.class)); // פתיחת מסך הבית
                finish(); // סיום הפעולה הנוכחית
            }
        }, 3000); // השהייה של 3000 מִילִישֵׁנִיוֹת (3 שניות)
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser(); // קבלת המשתמש הנוכחי
        if (firebaseUser == null) {
            // המשתמש לא נמצא, חזרה למסך ההתחברות
            startActivity(new Intent(MainScreen.this, MainActivity.class));
            finish(); // סיום הפעולה הנוכחית
        } else {
            // המשתמש מחובר, נוודא את סוג המשתמש
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // המשתמש קיים, פתיחת מסך לוח הבקרה של המשתמש
                    startActivity(new Intent(MainScreen.this, DashBoardUserActivity.class));
                    finish(); // סיום הפעולה הנוכחית
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // טיפול במקרה של ביטול הבקשה
                }
            });
        }
    }

}

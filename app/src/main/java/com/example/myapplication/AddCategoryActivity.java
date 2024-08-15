package com.example.myapplication; // מגדיר את המיקום של הקובץ בפרויקט

// ייבוא של ספריות הנדרשות לקוד
import android.app.ProgressDialog; // ייבוא עבור דיאלוג התקדמות
import android.os.Bundle; // ייבוא עבור אובייקט Bundle להעברת נתונים
import android.text.TextUtils; // ייבוא עבור בדיקות טקסט
import android.view.View; // ייבוא עבור תצוגות
import android.widget.Toast; // ייבוא עבור הודעות צפות

import androidx.appcompat.app.AppCompatActivity; // ייבוא עבור פעילויות עם תמיכה בעיצוב

import com.example.myapplication.databinding.ActivityAddCategoryBinding; // ייבוא עבור ה-binding של ActivityAddCategory
import com.google.android.gms.tasks.OnFailureListener; // ייבוא עבור מאזין למקרה של כישלון
import com.google.android.gms.tasks.OnSuccessListener; // ייבוא עבור מאזין למקרה של הצלחה
import com.google.firebase.auth.FirebaseAuth; // ייבוא עבור אימות משתמשים של Firebase
import com.google.firebase.database.DatabaseReference; // ייבוא עבור הפניה למסד נתונים של Firebase
import com.google.firebase.database.FirebaseDatabase; // ייבוא עבור מסד הנתונים של Firebase

import java.util.HashMap; // ייבוא עבור HashMap

public class AddCategoryActivity extends AppCompatActivity {
    private ActivityAddCategoryBinding binding; // משתנה עבור ה-binding של הפעילות
    private FirebaseAuth firebaseAuth; // משתנה עבור אימות משתמשים
    private ProgressDialog progressDialog; // משתנה עבור דיאלוג התקדמות

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // קריאה לבנאי של המחלקה האב
        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater()); // אתחול ה-binding
        setContentView(binding.getRoot()); // הגדרת התצוגה של הפעילות

        firebaseAuth = FirebaseAuth.getInstance(); // אתחול של FirebaseAuth

        // אתחול דיאלוג התקדמות
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait"); // כותרת הדיאלוג
        progressDialog.setCanceledOnTouchOutside(false); // לא מאפשר סגירה על ידי לחיצה מחוץ לדיאלוג

        // מאזין ללחיצה על כפתור החזרה
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // חוזר לפעילות הקודמת
            }
        });

        // מאזין ללחיצה על כפתור השמירה
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData(); // קורא לפונקציה לאימות הנתונים
            }
        });
    }

    private String category = ""; // משתנה לאחסון שם הקטגוריה

    // פונקציה לאימות הנתונים שהוזנו
    private void validateData() {
        category = binding.categoryEt.getText().toString().trim(); // מקבל את הטקסט שהוזן ושולל רווחים

        // אם השדה ריק
        if (TextUtils.isEmpty(category)) {
            Toast.makeText(this, "Please Enter To Do!", Toast.LENGTH_SHORT).show(); // מציג הודעת שגיאה
        } else {
            addDataToFirebase(); // אם הנתונים תקינים, קורא לפונקציה להוסיף נתונים למסד הנתונים
        }
    }

    // פונקציה להוספת נתונים למסד הנתונים של Firebase
    private void addDataToFirebase() {
        progressDialog.setMessage("Adding Data..."); // מציב הודעה בדיאלוג
        progressDialog.show(); // מציג את הדיאלוג

        long timestamp = System.currentTimeMillis(); // מקבל את הזמן הנוכחי

        // שינוי: הוספת UID של המשתמש הנוכחי
        String uid = firebaseAuth.getUid(); // מקבל את ה-UID של המשתמש הנוכחי

        // יצירת HashMap לאחסון הנתונים
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + timestamp); // מוסיף את ה-ID המבוסס על הזמן
        hashMap.put("category", category); // מוסיף את שם הקטגוריה
        hashMap.put("timestamp", timestamp); // מוסיף את הזמן
        hashMap.put("uid", uid); // מוסיף את ה-UID של המשתמש

        // שינוי: עדכון הנתיב במסד הנתונים
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ToDoLists").child(uid); // הפניה למסד הנתונים
        ref.child("" + timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss(); // סוגר את דיאלוג ההתקדמות
                Toast.makeText(AddCategoryActivity.this, "Data Added Successfully!", Toast.LENGTH_SHORT).show(); // הודעה על הצלחה
                binding.categoryEt.setText(""); // ריקון שדה הקטגוריה
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                progressDialog.dismiss(); // סוגר את דיאלוג ההתקדמות במקרה של שגיאה
                Toast.makeText(AddCategoryActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show(); // מציג הודעת שגיאה
            }
        });
    }
}

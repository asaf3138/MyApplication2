package com.example.myapplication; // מגדיר את המיקום של הקובץ בפרויקט

// ייבוא של ספריות הנדרשות לקוד
import android.content.Intent; // ייבוא עבור יצירת אינטנט להעברת מידע בין פעילויות
import android.os.Bundle; // ייבוא עבור אובייקט Bundle להעברת נתונים
import android.text.Editable; // ייבוא עבור טקסט שניתן לעריכה
import android.text.TextWatcher; // ייבוא עבור מאזין לשינויים בטקסט
import android.view.View; // ייבוא עבור תצוגות

import androidx.appcompat.app.AppCompatActivity; // ייבוא עבור פעילויות עם תמיכה בעיצוב

import com.example.myapplication.databinding.ActivityDashBoardUserBinding; // ייבוא עבור ה-binding של ActivityDashBoardUser
import com.google.firebase.auth.FirebaseAuth; // ייבוא עבור אימות משתמשים של Firebase
import com.google.firebase.auth.FirebaseUser; // ייבוא עבור משתמשים מאומתים של Firebase
import com.google.firebase.database.DataSnapshot; // ייבוא עבור Snapshot של נתוני Firebase
import com.google.firebase.database.DatabaseError; // ייבוא עבור שגיאות מסד נתונים
import com.google.firebase.database.DatabaseReference; // ייבוא עבור הפניה למסד נתונים של Firebase
import com.google.firebase.database.FirebaseDatabase; // ייבוא עבור מסד הנתונים של Firebase
import com.google.firebase.database.ValueEventListener; // ייבוא עבור מאזין לאירועים ב-Firebase

import java.util.ArrayList; // ייבוא עבור רשימות מסוג ArrayList

public class DashBoardUserActivity extends AppCompatActivity {
    private ActivityDashBoardUserBinding binding; // משתנה עבור ה-binding של הפעילות
    private FirebaseAuth firebaseAuth; // משתנה עבור אימות משתמשים
    private ArrayList<ModelCategory> categoryArrayList; // רשימה של קטגוריות
    private AdapterCategory adapterCategory; // משתנה עבור המתאם של הקטגוריות

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // קריאה לבנאי של המחלקה האב
        binding = ActivityDashBoardUserBinding.inflate(getLayoutInflater()); // אתחול ה-binding
        setContentView(binding.getRoot()); // הגדרת התצוגה של הפעילות

        firebaseAuth = FirebaseAuth.getInstance(); // אתחול של FirebaseAuth
        checkUser(); // בדיקת האם המשתמש מחובר
        loadCategories(); // טעינת הקטגוריות מהמסד נתונים

        // מאזין לשינויים בטקסט של שדה החיפוש
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // לא עושה דבר לפני שינוי הטקסט
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // סינון הרשימה בהתאם לטקסט שהוזן
                try {
                    adapterCategory.getFilter().filter(s); // מפעיל את המסנן
                } catch (Exception e) {
                    // לא עושה דבר במקרה של שגיאה
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // לא עושה דבר אחרי שינוי הטקסט
            }
        });

        // מאזין ללחיצה על כפתור ההתנתקות
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut(); // מתנתק מהמשתמש
                checkUser(); // בודק שוב את מצב המשתמש
            }
        });

        // מאזין ללחיצה על כפתור הוספת קטגוריה
        binding.addcategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מפעיל את Activity הוספת הקטגוריה
                startActivity(new Intent(DashBoardUserActivity.this, AddCategoryActivity.class));
            }
        });
    }

    // פונקציה לטעינת הקטגוריות מהמסד נתונים
    private void loadCategories() {
        categoryArrayList = new ArrayList<>(); // אתחול רשימת הקטגוריות

        // שינוי: טעינת משימות רק עבור המשתמש הנוכחי
        String uid = firebaseAuth.getUid(); // מקבל את ה-UID של המשתמש הנוכחי
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ToDoLists").child(uid); // הפניה למסד הנתונים של הקטגוריות
        ref.addValueEventListener(new ValueEventListener() { // מאזין לשינויים בנתונים
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                categoryArrayList.clear(); // ניקוי הרשימה הקיימת
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelCategory model = ds.getValue(ModelCategory.class); // המרת הנתונים למודל של קטגוריה
                    categoryArrayList.add(model); // הוספת הקטגוריה לרשימה
                }
                adapterCategory = new AdapterCategory(DashBoardUserActivity.this, categoryArrayList); // אתחול המתאם עם הרשימה
                binding.categoriesRv.setAdapter(adapterCategory); // הגדרת המתאם ל-RecyclerView
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // לא עושה דבר במקרה של שגיאה
            }
        });
    }

    // פונקציה לבדוק האם המשתמש מחובר
    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser(); // מקבל את המשתמש הנוכחי
        if (firebaseUser == null) {
            // אם המשתמש לא מחובר, מפעיל את MainActivity
            startActivity(new Intent(DashBoardUserActivity.this, MainActivity.class));
            finish(); // סוגר את הפעילות הנוכחית
        } else {
            // אם המשתמש מחובר, מציג את כתובת האימייל
            String email = firebaseUser.getEmail(); // מקבל את האימייל של המשתמש
            binding.subtitleTv.setText(email); // מציב את האימייל בטקסט בתצוגה
        }
    }
}

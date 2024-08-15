package com.example.myapplication; // הגדרת המיקום של הקובץ בפרויקט

// ייבוא של ספריות הנדרשות לקוד
import android.app.AlertDialog; // ייבוא עבור דיאלוגים
import android.content.Context; // ייבוא עבור הקשר (Context) של האפליקציה
import android.content.DialogInterface; // ייבוא עבור ממשק דיאלוגים
import android.view.LayoutInflater; // ייבוא עבור יצירת תצוגות
import android.view.View; // ייבוא עבור תצוגות
import android.view.ViewGroup; // ייבוא עבור קבוצות תצוגה
import android.widget.Filter; // ייבוא עבור סינון רשימות
import android.widget.Filterable; // ייבוא עבור ממשק סינון
import android.widget.ImageButton; // ייבוא עבור כפתורים מסוג ImageButton
import android.widget.TextView; // ייבוא עבור טקסטים
import android.widget.Toast; // ייבוא עבור הודעות צפות

import androidx.annotation.NonNull; // ייבוא עבור תוויות של ערכים שאינם יכולים להיות null
import androidx.recyclerview.widget.RecyclerView; // ייבוא עבור RecyclerView

import com.example.myapplication.databinding.RowCategoryBinding; // ייבוא עבור מחbinding של RowCategory
import com.google.android.gms.tasks.OnFailureListener; // ייבוא עבור מאזין למקרה של כישלון
import com.google.android.gms.tasks.OnSuccessListener; // ייבוא עבור מאזין למקרה של הצלחה
import com.google.firebase.auth.FirebaseAuth; // ייבוא עבור אימות משתמשים של Firebase
import com.google.firebase.database.DatabaseReference; // ייבוא עבור הפניה למסד נתונים של Firebase
import com.google.firebase.database.FirebaseDatabase; // ייבוא עבור מסד הנתונים של Firebase

import java.util.ArrayList; // ייבוא עבור רשימות מסוג ArrayList

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.HolderCategory> implements Filterable {
    private Context context; // משתנה לשמירת הקשר האפליקציה
    public ArrayList<ModelCategory> categoryArrayList, filterList; // רשימות שמכילות את המשימות
    private RowCategoryBinding binding; // משתנה עבור ה-binding של RowCategory
    private FilterCategory filter; // משתנה עבור מסנן המשימות

    // בנאי עבור מחלקת AdapterCategory
    public AdapterCategory(Context context, ArrayList<ModelCategory> categoryArrayList) {
        this.context = context; // אתחול של הקשר
        this.categoryArrayList = categoryArrayList; // אתחול של רשימת הקטגוריות
        this.filterList = categoryArrayList; // אתחול של רשימת הסינון
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(ViewGroup parent, int viewType) {
        // יוצר את ה-binding עבור תצוגת הקטגוריה
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderCategory(binding.getRoot()); // מחזיר את HolderCategory עם התצוגה שהתקבלה
    }

    @Override
    public void onBindViewHolder(HolderCategory holder, int position) {
        // מקבל את המודל של הקטגוריה מהרשימה לפי המיקום
        ModelCategory model = categoryArrayList.get(position);
        String id = model.getId(); // מקבל את ה-ID של הקטגוריה
        String category = model.getCategory(); // מקבל את שם הקטגוריה
        String uid = model.getUid(); // מקבל את ה-UID של המשתמש
        long timestamp = model.getTimestamp(); // מקבל את הזמן של הקטגוריה

        holder.categoryTv.setText(category); // מציב את שם הקטגוריה בתצוגה

        // מאזין לאירוע לחיצה על כפתור המחיקה
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // יצירת דיאלוג אישור מחיקה
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete") // כותרת הדיאלוג
                        .setMessage("Are you sure you want to delete this ?") // הודעה בדיאלוג
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // הודעה לצורך סימול מחיקה
                                Toast.makeText(context, "Deleting ...", Toast.LENGTH_SHORT).show();
                                deleteCategory(model, holder); // קריאה לפונקציית מחיקת הקטגוריה
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // סוגר את הדיאלוג
                            }
                        })
                        .show(); // מציג את הדיאלוג
            }
        });
    }

    // פונקציה למחיקת קטגוריה
    private void deleteCategory(ModelCategory model, HolderCategory holder) {
        String id = model.getId(); // מקבל את ה-ID של הקטגוריה
        // שינוי: מחיקת משימה רק עבור המשתמש הנוכחי
        String uid = FirebaseAuth.getInstance().getUid(); // מקבל את ה-UID של המשתמש הנוכחי
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ToDoLists").child(uid); // מקבל הפניה למסד הנתונים של הקטגוריות
        ref.child(id).removeValue() // מבצע מחיקת ערך לפי ה-ID
                .addOnSuccessListener(new OnSuccessListener<Void>() { // מאזין להצלחה
                    @Override
                    public void onSuccess(Void unused) {
                        // הודעה כאשר המחיקה מצליחה
                        Toast.makeText(context, "Successfully deleted ...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() { // מאזין לכישלון
                    @Override
                    public void onFailure(Exception e) {
                        // הודעה כאשר המחיקה נכשלה
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size(); // מחזיר את מספר הקטגוריות ברשימה
    }

    @Override
    public Filter getFilter() {
        // אם אין מסנן קיים, יוצר חדש
        if (filter == null) {
            filter = new FilterCategory(filterList, this); // אתחול המסנן
        }
        return filter; // מחזיר את המסנן
    }

    // מחלקה פנימית עבור החזקת תצוגת הקטגוריה
    class HolderCategory extends RecyclerView.ViewHolder {
        TextView categoryTv; // משתנה עבור טקסט הקטגוריה
        ImageButton deleteBtn; // משתנה עבור כפתור המחיקה

        public HolderCategory(View itemView) {
            super(itemView); // קריאה לבנאי של המחלקה האב
            categoryTv = binding.categoryTv; // אתחול של טקסט הקטגוריה
            deleteBtn = binding.deleteBtn; // אתחול של כפתור המחיקה
        }
    }
}

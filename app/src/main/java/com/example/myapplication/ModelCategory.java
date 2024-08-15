package com.example.myapplication;

public class ModelCategory {
    // לוודא ששמות המשתנים תואמים לשמות המשתנים במסד הנתונים Firebase
    String id; // מזהה הקטגוריה
    String Category; // שם הקטגוריה
    String uid; // מזהה המשתמש
    long timestamp; // תאריך וזמן הוספת הקטגוריה

    // קונסטרקטור ברירת מחדל נדרש לפעולה עם Firebase
    public ModelCategory() {
    }

    // קונסטרקטור עם פרמטרים
    public ModelCategory(String id, String category, String uid, long timestamp) {
        this.id = id;
        Category = category;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    // פונקציה לקבלת מזהה הקטגוריה
    public String getId() {
        return id;
    }

    // פונקציה להגדרת מזהה הקטגוריה
    public void setId(String id) {
        this.id = id;
    }

    // פונקציה לקבלת שם הקטגוריה
    public String getCategory() {
        return Category;
    }

    // פונקציה להגדלת שם הקטגוריה
    public void setCategory(String category) {
        Category = category;
    }

    // פונקציה לקבלת מזהה המשתמש
    public String getUid() {
        return uid;
    }

    // פונקציה להגדלת מזהה המשתמש
    public void setUid(String uid) {
        this.uid = uid;
    }

    // פונקציה לקבלת תאריך וזמן
    public long getTimestamp() {
        return timestamp;
    }

    // פונקציה להגדלת תאריך וזמן
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

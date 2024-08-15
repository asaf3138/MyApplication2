package com.example.myapplication;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterCategory extends Filter {

    // רשימת הקטגוריות בהן נרצה לחפש
    private ArrayList<ModelCategory> filterList;

    // האדאפטר בו צריך להחיל את הסינון
    private AdapterCategory adapterCategory;

    // קונסטרוקטור
    public FilterCategory(ArrayList<ModelCategory> filterList, AdapterCategory adapterCategory) {
        this.filterList = filterList;
        this.adapterCategory = adapterCategory;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        // לבדוק אם הערך לא NULL ולא ריק
        if (constraint != null && constraint.length() > 0) {
            // שינוי לערכים באותיות רישיות או קטנות כדי למנוע רגישות לאותיות רישיות
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelCategory> filterModels = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                // לבדוק אם הקטגוריה מכילה את המילה שהוזנה
                if (filterList.get(i).getCategory().toUpperCase().contains(constraint)) {
                    // הוספת קטגוריה לרשימה המסוננת
                    filterModels.add(filterList.get(i));
                }
            }
            // הגדרת התוצאות
            results.count = filterModels.size();
            results.values = filterModels;
        } else {
            // במקרה שאין ערך חיפוש, להחזיר את כל הרשימה המקורית
            results.count = filterList.size();
            results.values = filterList;
        }
        return results; // החזרת התוצאות
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        // החלת השינויים על האדאפטר
        adapterCategory.categoryArrayList = (ArrayList<ModelCategory>) results.values;

        // לעדכן את האדאפטר על השינויים
        adapterCategory.notifyDataSetChanged();
    }
}

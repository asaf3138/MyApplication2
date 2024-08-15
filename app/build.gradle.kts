// הגדרת התוספים הדרושים לפרויקט
plugins {
    alias(libs.plugins.android.application) // תוסף לבניית אפליקציית אנדרואיד
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics) // תוסף לשירותים של גוגל (למשל Firebase)
}

android {
    namespace = "com.example.myapplication" // מרחב שמות של האפליקציה (package name)
    compileSdk = 34 // גרסת SDK בה האפליקציה תתומך

    defaultConfig {
        applicationId = "com.example.myapplication" // מזהה האפליקציה
        minSdk = 29 // הגדרת גרסת מינימלית של מערכת ההפעלה אנדרואיד בה האפליקציה תתמוך
        targetSdk = 34 // הגדרת גרסת היעד של מערכת ההפעלה אנדרואיד
        versionCode = 1 // מספר גרסה של האפליקציה
        versionName = "1.0" // שם גרסה של האפליקציה

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // הגדרת רץ לבדיקות היחידה
    }

    buildTypes {
        release {
            isMinifyEnabled = false // האם לצמצם קוד עבור גרסת שחרור
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro") // קבצי ProGuard לניהול צמצום והגנה על הקוד
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // תאימות קוד מקור לגרסה 1.8 של Java
        targetCompatibility = JavaVersion.VERSION_1_8 // תאימות לגרסה 1.8 של Java
    }

    buildFeatures {
        viewBinding = true // הפעלת ViewBinding, שמפשטת את ההתייחסות לתצוגות בקוד
    }
}

dependencies {
    implementation(libs.appcompat) // תוסף תואם לאפליקציות אנדרואיד (תמיכה בכפתורים ו-UI)
    implementation(libs.material) // ספרייה לחומרים ועיצובים של Google Material
    implementation(libs.activity) // ספרייה לניהול פעילויות באפליקציה
    implementation(libs.constraintlayout) // ספריית Layout עם Constraint Layout (עימוד גמיש)


    implementation(libs.firebase.auth) // Firebase Authentication לתמיכה בהרשמה ואימות משתמשים
    implementation(libs.firebase.analytics)// מוסיף את Firebase Analytics, כלי לניתוח נתוני שימוש באפליקציה ולקבלת תובנות על התנהגות המשתמשים
    implementation(libs.firebase.database)// מוסיף את Firebase Realtime Database, שירות לניהול וקריאה/כתיבה של נתונים בזמן אמת
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics) // Firebase Analytics למעקב וניתוח של נתוני שימוש
    testImplementation(libs.junit) // תלות עבור בדיקות יחידה (JUnit)
    androidTestImplementation(libs.ext.junit) // תלות עבור בדיקות יחידה לאנדרואיד (JUnit)
    androidTestImplementation(libs.espresso.core) // תלות עבור בדיקות UI באפליקציה (Espresso)
}

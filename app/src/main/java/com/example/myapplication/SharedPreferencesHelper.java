package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class SharedPreferencesHelper {

    private static final String PREFS_NAME_RANKS = "RanksPrefs";
    private static final String PREFS_NAME_COURSES = "CoursesPrefs";

    public static void saveRanks(Context context,String listName, List<Rank> ranks) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(ranks);

            byte[] serializedBytes = byteArrayOutputStream.toByteArray();
            String base64Encoded = Base64.encodeToString(serializedBytes, Base64.DEFAULT);

            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_RANKS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(listName, base64Encoded);
            editor.apply();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Rank> loadRanks(Context context,String listName) {
        List<Rank> ranks = null;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_RANKS, Context.MODE_PRIVATE);
        String base64Encoded = prefs.getString(listName, null);

        if (base64Encoded != null) {
            try {
                byte[] serializedBytes = Base64.decode(base64Encoded, Base64.DEFAULT);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedBytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                ranks = (List<Rank>) objectInputStream.readObject();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return ranks;
    }
    public static void saveCourses(Context context,String listName, List<List<Course>> courses) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(courses);

            byte[] serializedBytes = byteArrayOutputStream.toByteArray();
            String base64Encoded = Base64.encodeToString(serializedBytes, Base64.DEFAULT);

            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_COURSES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(listName, base64Encoded);
            editor.apply();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<List<Course>> loadCourses(Context context,String listName) {
        List<List<Course>> courses = null;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_COURSES, Context.MODE_PRIVATE);
        String base64Encoded = prefs.getString(listName, null);

        if (base64Encoded != null) {
            try {
                byte[] serializedBytes = Base64.decode(base64Encoded, Base64.DEFAULT);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedBytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                courses = (List<List<Course>>) objectInputStream.readObject();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return courses;
    }
    public static void saveCurrentCourses(Context context,String listName, List<List<CurrentCourse>> currentCourses) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(currentCourses);

            byte[] serializedBytes = byteArrayOutputStream.toByteArray();
            String base64Encoded = Base64.encodeToString(serializedBytes, Base64.DEFAULT);

            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_COURSES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(listName, base64Encoded);
            editor.apply();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<List<CurrentCourse>> loadCurrentCourses(Context context,String listName) {
        List<List<CurrentCourse>> courses = null;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_COURSES, Context.MODE_PRIVATE);
        String base64Encoded = prefs.getString(listName, null);

        if (base64Encoded != null) {
            try {
                byte[] serializedBytes = Base64.decode(base64Encoded, Base64.DEFAULT);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedBytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                courses = (List<List<CurrentCourse>>) objectInputStream.readObject();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return courses;
    }
    //删除所有保存数据
    public static void deleteAll(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_RANKS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        prefs = context.getSharedPreferences(PREFS_NAME_COURSES, Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}

package com.example.myapplication;

import java.util.Map;

public class CurrentCourse {
    private String courseName;
    private int size;
    private String[] courseTime;
    private String[] courseRoom;

    public CurrentCourse(String courseName, String[] courseTime, String[] courseRoom) {
        this.courseName = courseName;
        this.courseTime = courseTime;
        this.courseRoom = courseRoom;
        size = Math.min(courseRoom.length, courseTime.length);
    }
    public String getCourseName() {
        return courseName;
    }

    public String[] getCourseTime() {
        return courseTime;
    }
    public String[] getCourseRoom() {
        return courseRoom;
    }
    public int getSize() {
        return size;
    }
}

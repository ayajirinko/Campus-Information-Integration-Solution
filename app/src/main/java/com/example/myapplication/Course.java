package com.example.myapplication;

import java.io.Serializable;

public class Course implements Serializable {
    public String courseName;
    public String zypjcj;
    public String kccj;

    public Course(String courseName, String zypjcj, String kccj) {
        this.courseName = "课程名："+courseName;
        this.zypjcj = "专业平均成绩：" + zypjcj;
        this.kccj = "你的成绩：" + kccj;
    }

}

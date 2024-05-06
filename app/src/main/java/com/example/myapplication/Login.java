package com.example.myapplication;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

public class Login {
    private String username = "";
    private String password = "";
    private String[] params = new String[6];
    private String[] bjpms = new String[6];
    private String[] zypms = new String[6];
    private List<List<String>> xscjs = new ArrayList<>();
    public static void main(String[] args) throws IOException {
    }
    public void solve(String username,String password,List<List<Course>> xsCourses,List<Rank> bjRanks,List<Rank> zyRanks) throws IOException {
        this.username = username;
        this.password = password;
        int grade = Integer.parseInt("2" + username.substring(1, 4));
        params[0] = "{\"xnd\":\""+grade+"-"+(grade+1)+"\",\"xq\":\"1\"}";
        params[1] = "{\"xnd\":\""+grade+"-"+(grade+1)+"\",\"xq\":\"2\"}";
        params[2] = "{\"xnd\":\""+(grade+1)+"-"+(grade+2)+"\",\"xq\":\"1\"}";
        params[3] = "{\"xnd\":\""+(grade+1)+"-"+(grade+2)+"\",\"xq\":\"2\"}";
        params[4] = "{\"xnd\":\""+(grade+2)+"-"+(grade+3)+"\",\"xq\":\"1\"}";
        params[5] = "{\"xnd\":\""+(grade+2)+"-"+(grade+3)+"\",\"xq\":\"2\"}";
        Connection.Response response = Jsoup.connect("https://one.ahu.edu.cn/")
            .method(Connection.Method.GET)
            .execute();
        Document doc = Jsoup.parse(response.body());
        Map<String, String> cookies = response.cookies();
        String lt = Objects.requireNonNull(doc.getElementById("lt")).attr("value");
        String execution = Objects.requireNonNull(
            doc.getElementsByAttributeValue("name", "execution").first()).attr("value");
        String _eventId = Objects.requireNonNull(
            doc.getElementsByAttributeValue("name", "_eventId").first()).attr("value");
        String loginURL = "https://wvpn.ahu.edu.cn" + Objects.requireNonNull(
            doc.getElementById("loginForm")).attr("action");
        Map<String, String> data = new Encrypt().encrypt(username, password, lt);
        data.put("lt", lt);
        data.put("execution", execution);
        data.put("_eventId", _eventId);
        response = Jsoup.connect(loginURL)
            .data(data)
            .method(Connection.Method.POST)
            .cookies(cookies)
            .execute();


        response = Jsoup.connect(
            "https://wvpn.ahu.edu.cn/https/77726476706e69737468656265737421fff944d226387d1e7b0c9ce29b5b/idc/view?m=idc"
            )
            .method(Connection.Method.GET)
            .cookies(cookies)
            .execute();
        //班级排名
        for (int i = 0; i < 6;i++){
            response = Jsoup.connect(
                    "https://wvpn.ahu.edu.cn/https/77726476706e69737468656265737421fff944d226387d1e7b0c9ce29b5b/idc//idc/idcHome/studentHome/getBjpm"
                )
                .method(Connection.Method.POST)
                .header("Content-Type", "application/json;charset=UTF-8")
                .cookies(cookies)
                .requestBody(params[i])//参数
                .ignoreContentType(true)
                .execute();
            doc = Jsoup.parse(response.body());
            bjpms[i] = doc.body().html();
        }
        for (int i = 0; i < 6;i++){
            response = Jsoup.connect(
                    "https://wvpn.ahu.edu.cn/https/77726476706e69737468656265737421fff944d226387d1e7b0c9ce29b5b/idc//idc/idcHome/studentHome/getZypm"
                )
                .method(Connection.Method.POST)
                .header("Content-Type", "application/json;charset=UTF-8")
                .cookies(cookies)
                .requestBody(params[i])//参数
                .ignoreContentType(true)
                .execute();
            doc = Jsoup.parse(response.body());
            zypms[i] = doc.body().html();
        }
        for (int i = 0; i < 6;i++){
            response = Jsoup.connect(
                    "https://wvpn.ahu.edu.cn/https/77726476706e69737468656265737421fff944d226387d1e7b0c9ce29b5b/idc//idc/idcHome/studentHome/getSxqcjlist"
                )
                .method(Connection.Method.POST)
                .header("Content-Type", "application/json;charset=UTF-8")
                .cookies(cookies)
                .requestBody(params[i])//参数
                .ignoreContentType(true)
                .execute();
            doc = Jsoup.parse(response.body());
            String html = doc.body().html();
            if(html.equals("[]")) continue;
            html = html.substring(1,html.length()-1);
            List<String> list = new ArrayList<>();
            while(html.length()>9){
                int k = html.indexOf("}");
                String s = html.substring(0,k+1);
                list.add(s);
                if(k==html.length()-1) break;
                html = html.substring(k+2);
            }
            xscjs.add(list);
        }

        for (String zypm : zypms) {
            Rank rank = new Rank(zypm.substring(zypm.indexOf("{\"PM\":")+6,zypm.indexOf(",\"PM2\"")),zypm.substring(zypm.indexOf("\"ZYRS\":")+7,zypm.indexOf(",\"LABELNAME\"")));
            zyRanks.add(rank);
        }
        for (String bjpm : bjpms) {
            Rank rank = new Rank(bjpm.substring(bjpm.indexOf("{\"PM\":")+6,bjpm.indexOf(",\"ID_TYPE\"")),bjpm.substring(bjpm.indexOf("\"BJRS\":")+7,bjpm.indexOf("}")));
            bjRanks.add(rank);
        }
        for (List<String> list : xscjs) {
            List<Course> courses = new ArrayList<>();
            for (String s : list) {
                if(s.length()<9) continue;
                Course course = new Course(
                    s.substring(s.indexOf("\"KCMC\":\"")+8,s.lastIndexOf("\"")),
                    s.substring(s.indexOf("\"ZYPJCJ\":")+9,s.lastIndexOf("\"ZYPJCJ\":")+13),
                    s.substring(s.indexOf("\"KCCJ\":")+7,s.indexOf(",\"KCMC\""))
                );
                courses.add(course);
            }
            xsCourses.add(courses);
        }
    }
}

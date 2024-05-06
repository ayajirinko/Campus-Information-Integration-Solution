package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Course;
import com.example.myapplication.SharedPreferencesHelper;
import com.example.myapplication.databinding.FragmentCjcxBinding;
import com.example.myapplication.ui.cjcx.CourseAdapter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_cjcx#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_cjcx extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean isZddl = false;
    private int lastPosition = 0;
    private List<List<Course>> xsCourses;
    private FragmentCjcxBinding binding;
    Map<String, String> cookies;
    String host;

    public Fragment_cjcx() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_cjcx.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_cjcx newInstance(String param1, String param2,Map<String, String> cookies,String host,boolean isZddl) {
        Fragment_cjcx fragment = new Fragment_cjcx();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putSerializable("cookies", (Serializable) cookies);
        args.putBoolean("isZddl",isZddl);
        args.putString("host",host);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            cookies = (Map<String, String>) getArguments().getSerializable("cookies");
            isZddl = getArguments().getBoolean("isZddl");
            host = getArguments().getString("host");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCjcxBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init(root);
        return root;
    }
    @SuppressLint("NotifyDataSetChanged")
    private void init(View root) {
        final Spinner spinnerTerm = binding.spinnerTerm;
        final RecyclerView recyclerView = binding.recyclerViewCjcx;
        final ConstraintLayout constraintLayoutData = binding.constraintLayoutData;
        final ConstraintLayout constraintLayoutProgress = binding.constraintLayoutProgress;
        final TextView textViewProgressCjcx = binding.textViewProgressCjcx;
        Handler processHandler = new Handler(Looper.getMainLooper());
        textViewProgressCjcx.setText("正在获取数据");

        final HandlerThread handlerThread = new HandlerThread("StudentDataFetcher");
        handlerThread.start();
        Handler backgroundHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 12) {
                    int current = msg.arg1;
                    int total = msg.arg2;
                    // 更新UI\
                    processHandler.post(() -> {
                        String s = "正在获取在校成绩 " + current + "/" + total;
                        textViewProgressCjcx.setText(s);
                    });
                }
                if (msg.what == 13) {
                    // 更新UI
                    processHandler.post(() -> {
                        String s = "请稍等";
                        textViewProgressCjcx.setText(s);
                    });
                }
                if (msg.what == 3) {
                    xsCourses = (List<List<Course>>) msg.obj;
                    SharedPreferencesHelper.saveCourses(root.getContext(), "xsCourses", xsCourses);
                }

                if (msg.what == 4) {
                    if (msg.arg1 == 100){
                        processHandler.post(() -> {
                            String s = "正在从缓存获取数据 ";
                            textViewProgressCjcx.setText(s);
                        });
                        if (xsCourses == null) {
                            xsCourses = SharedPreferencesHelper.loadCourses(root.getContext(), "xsCourses");
                        }
                    }
                    processHandler.post(() -> {
                        constraintLayoutProgress.setVisibility(View.GONE);
                        constraintLayoutData.setVisibility(View.VISIBLE);
                        int grade = Integer.parseInt("20" + mParam1.substring(2, 4));
                        String[] params = new String[6];
                        params[0] = "学年:"+grade+"-"+(grade+1)+",第1学期";
                        params[1] = "学年:"+grade+"-"+(grade+1)+",第2学期";
                        params[2] = "学年:"+(grade+1)+"-"+(grade+2)+",第1学期";
                        params[3] = "学年:"+(grade+1)+"-"+(grade+2)+",第2学期";
                        params[4] = "学年:"+(grade+2)+"-"+(grade+3)+",第1学期";
                        params[5] = "学年:"+(grade+2)+"-"+(grade+3)+",第2学期";

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(root.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,params);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerTerm.setAdapter(adapter);


                        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        CourseAdapter courseAdapter = new CourseAdapter(new ArrayList<>());
                        recyclerView.setAdapter(courseAdapter);
                        spinnerTerm.setSelection(xsCourses.size()-1);
                        courseAdapter.updateData(xsCourses.get(spinnerTerm.getSelectedItemPosition()));

                        spinnerTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String itemSelected = (String) parent.getItemAtPosition(position);
                                if (position <= xsCourses.size() - 1) {
                                    lastPosition = position;
                                    courseAdapter.updateData(xsCourses.get(position));
                                    //Toast.makeText(root.getContext(), "已选择:" + itemSelected, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(root.getContext(), "未来可期", Toast.LENGTH_SHORT).show();
                                    parent.setSelection(lastPosition);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // 当没有项目被选中时执行的操作
                            }
                        });
                    });
                }
            }
        };

        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                // 在这里执行你的后台任务
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isZddl){
                            Message message = Message.obtain(backgroundHandler, 4,100,0);
                            message.sendToTarget();
                        }else {
                            String[] params = new String[6];
                            String[] bjpms = new String[6];
                            String[] zypms = new String[6];
                            List<List<String>> xscjs = new ArrayList<>();
                            xsCourses = new ArrayList<>();
                            Message message;
                            int grade = Integer.parseInt("20" + mParam1.substring(2, 4));
                            params[0] = "{\"xnd\":\"" + grade + "-" + (grade + 1) + "\",\"xq\":\"1\"}";
                            params[1] = "{\"xnd\":\"" + grade + "-" + (grade + 1) + "\",\"xq\":\"2\"}";
                            params[2] = "{\"xnd\":\"" + (grade + 1) + "-" + (grade + 2) + "\",\"xq\":\"1\"}";
                            params[3] = "{\"xnd\":\"" + (grade + 1) + "-" + (grade + 2) + "\",\"xq\":\"2\"}";
                            params[4] = "{\"xnd\":\"" + (grade + 2) + "-" + (grade + 3) + "\",\"xq\":\"1\"}";
                            params[5] = "{\"xnd\":\"" + (grade + 2) + "-" + (grade + 3) + "\",\"xq\":\"2\"}";

                            String rankURL = "https://wvpn.ahu.edu.cn/https/77726476706e69737468656265737421fff944d226387d1e7b0c9ce29b5b";
//                            if (host.contains("one.ahu.edu.cn")) rankURL = "https://one.ahu.edu.cn";

                            try {
                                Connection.Response response;
                                Document doc;
                                for (int i = 0; i < 6; i++) {
                                    response = Jsoup.connect(
                                                    rankURL+"/idc//idc/idcHome/studentHome/getSxqcjlist"
                                            )
                                            .method(Connection.Method.POST)
                                            .header("Content-Type", "application/json;charset=UTF-8")
                                            .cookies(cookies)
                                            .requestBody(params[i])//参数
                                            .ignoreContentType(true)
                                            .execute();
                                    doc = Jsoup.parse(response.body());
                                    String html = doc.body().html();
                                    if (html.equals("[]")) continue;
                                    html = html.substring(1, html.length() - 1);
                                    List<String> list = new ArrayList<>();
                                    while (html.length() > 9) {
                                        int k = html.indexOf("}");
                                        String s = html.substring(0, k + 1);
                                        list.add(s);
                                        if (k == html.length() - 1) break;
                                        html = html.substring(k + 2);
                                    }
                                    xscjs.add(list);
                                    message = Message.obtain(backgroundHandler, 12, i + 1, 6); // 用Message携带数据
                                    message.sendToTarget();
                                }
                                message = Message.obtain(backgroundHandler, 13); // 用Message携带数据
                                message.sendToTarget();
                                for (List<String> list : xscjs) {
                                    List<Course> courses = new ArrayList<>();
                                    for (String s : list) {
                                        if (s.length() < 9) continue;
                                        Course course = new Course(
                                                s.substring(s.indexOf("\"KCMC\":\"") + 8, s.lastIndexOf("\"")),
                                                s.substring(s.indexOf("\"ZYPJCJ\":") + 9, s.lastIndexOf("\"ZYPJCJ\":") + 13),
                                                s.substring(s.indexOf("\"KCCJ\":") + 7, s.indexOf(",\"KCMC\""))
                                        );
                                        if (!Objects.equals(course.kccj, "你的成绩：" + "0"))
                                            courses.add(course);
                                    }
                                    xsCourses.add(courses);
                                }
                                message = Message.obtain(backgroundHandler, 3, xsCourses); // 用Message携带数据
                                message.sendToTarget();
                                message = Message.obtain(backgroundHandler, 4,0,0);
                                message.sendToTarget();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
                thread.start();
            }
        });
    }
}

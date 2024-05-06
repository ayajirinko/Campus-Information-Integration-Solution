package com.example.myapplication.fragment;

import android.os.Bundle;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CurrentCourse;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreferencesHelper;
import com.example.myapplication.databinding.FragmentOtherBinding;
import com.example.myapplication.ui.cjcx.CurrentCourseAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_other#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_other extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private FragmentOtherBinding binding;
    private String userName;
    private String mParam2;
    private int lastPosition = 0;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private ConstraintLayout constraintLayout;
    List<List<CurrentCourse>> currentCourses;
    boolean isZddl = false;

    public Fragment_other() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_other.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_other newInstance(String param1, String param2) {
        Fragment_other fragment = new Fragment_other();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_other, container, false);
        binding = FragmentOtherBinding.bind(root);
        init(root);
        return root;
    }
    public void init(View root) {
        Spinner spinner = binding.spinner2;
        recyclerView = binding.recyclerViewXskb;
        constraintLayout = binding.constraintLayoutProgress;
        ConstraintLayout constraintLayoutProgress = binding.constraintLayoutProgress;
        ConstraintLayout constraintLayoutData = binding.constraintLayoutData;
        Spinner spinnerDay = binding.spinner2;

        Handler processHandler = new Handler(Looper.getMainLooper());

        final HandlerThread handlerThread = new HandlerThread("StudentDataFetcher");
        handlerThread.start();
        Handler backgroundHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 3) {
                    currentCourses = (List<List<CurrentCourse>>) msg.obj;
                    SharedPreferencesHelper.saveCurrentCourses(root.getContext(), "currentCourses", currentCourses);
                }

                if (msg.what == 4) {
                    if (msg.arg1 == 100){
                        if (currentCourses == null) {
                            currentCourses = SharedPreferencesHelper.loadCurrentCourses(root.getContext(), "currentCourses");
                        }
                    }
                    processHandler.post(() -> {
                        constraintLayoutProgress.setVisibility(View.GONE);
                        constraintLayoutData.setVisibility(View.VISIBLE);
                        String[] params = new String[8];
                        params[0] = "星期一";
                        params[1] = "星期二";
                        params[2] = "星期三";
                        params[3] = "星期四";
                        params[4] = "星期五";
                        params[5] = "星期六";
                        params[6] = "星期日";
                        params[7] = "全部";

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(root.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,params);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        CurrentCourseAdapter currentCourseAdapter = new CurrentCourseAdapter(new ArrayList<>());
                        recyclerView.setAdapter(currentCourseAdapter);
                        spinnerDay.setSelection(currentCourses.size()-1);
                        currentCourseAdapter.updateData(currentCourses.get(spinnerDay.getSelectedItemPosition()));

                        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String itemSelected = (String) parent.getItemAtPosition(position);
                                if (!currentCourses.get(position).isEmpty()) {
                                    lastPosition = position;
                                    currentCourseAdapter.updateData(currentCourses.get(position));
                                    //Toast.makeText(root.getContext(), "已选择:" + itemSelected, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(root.getContext(), "所选日期无课", Toast.LENGTH_SHORT).show();
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
                            try {
                                Document doc = Jsoup.connect("http://kskw.ahu.edu.cn/kbcx.asp?xh=" + userName)

                                        .get();
                                String[] strings = doc.body().html().split("<br>");
                                int i = 0;
                                for (; !strings[i].contains("1:") && i < strings.length - 1; i++) ;
                                List<List<CurrentCourse>> currentCourses = new ArrayList<>();
                                List<CurrentCourse> Monday = new ArrayList<>();
                                List<CurrentCourse> Tuesday = new ArrayList<>();
                                List<CurrentCourse> Wednesday = new ArrayList<>();
                                List<CurrentCourse> Thursday = new ArrayList<>();
                                List<CurrentCourse> Friday = new ArrayList<>();
                                List<CurrentCourse> Saturday = new ArrayList<>();
                                List<CurrentCourse> Sunday = new ArrayList<>();
                                List<CurrentCourse> All = new ArrayList<>();
                                currentCourses.add(Monday);
                                currentCourses.add(Tuesday);
                                currentCourses.add(Wednesday);
                                currentCourses.add(Thursday);
                                currentCourses.add(Friday);
                                currentCourses.add(Saturday);
                                currentCourses.add(Sunday);
                                currentCourses.add(All);
                                for (int j = i; j + 2 < strings.length; j += 4) {
                                    String courseName = strings[j].trim();
                                    String[] courseTime = strings[j + 1].trim().split(";");
                                    String[] courseRoom = strings[j + 2].trim().split(";");
                                    CurrentCourse currentCourse = new CurrentCourse(courseName, courseTime, courseRoom);
                                    for(int k = 0; k < courseTime.length; k++){
                                        String time = !courseTime[k].isEmpty() ?courseTime[k]:"未安排";
                                        String room = !courseRoom[k].isEmpty() ?courseRoom[k]:"未安排";
                                        CurrentCourse currentCourse1 = new CurrentCourse(courseName.substring(2), new String[]{time}, new String[]{room});
                                        switch (time.charAt(1)) {
                                            case '一':
                                                Monday.add(currentCourse1);
                                                All.add(currentCourse1);
                                                break;
                                            case '二':
                                                Tuesday.add(currentCourse1);
                                                All.add(currentCourse1);
                                                break;
                                            case '三':
                                                Wednesday.add(currentCourse1);
                                                All.add(currentCourse1);
                                                break;
                                            case '四':
                                                Thursday.add(currentCourse1);
                                                All.add(currentCourse1);
                                                break;
                                            case '五':
                                                Friday.add(currentCourse1);
                                                All.add(currentCourse1);
                                                break;
                                            case '六':
                                                Saturday.add(currentCourse1);
                                                All.add(currentCourse1);
                                                break;
                                            case '日':
                                                Sunday.add(currentCourse1);
                                                All.add(currentCourse1);
                                                break;
                                            default:
                                                All.add(currentCourse1);
                                                break;
                                        }
                                    }
                                }

                                Message message = Message.obtain(backgroundHandler, 3, currentCourses); // 用Message携带数据
                                message.sendToTarget();
                                message = Message.obtain(backgroundHandler, 4, 0, 0);
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
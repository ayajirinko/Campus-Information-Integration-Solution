package com.example.myapplication.fragment;

import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Rank;
import com.example.myapplication.SharedPreferencesHelper;
import com.example.myapplication.databinding.FragmentPmcxBinding;

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
 * Use the {@link Fragment_pmcx#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_pmcx extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int lastPosition = 0;

    Map<String, String> cookies;
    List<Rank> zyRanks;
    List<Rank> bjRanks;
    SharedPreferences sp;
    boolean isZddl = false;
    String host = "";

    private @NonNull FragmentPmcxBinding binding;


    public Fragment_pmcx() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_pmcx.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_pmcx newInstance(String param1, String param2, Map<String, String> cookies,String host,boolean isZddl) {
        Fragment_pmcx fragment = new Fragment_pmcx();
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

        binding = FragmentPmcxBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sp = root.getContext().getSharedPreferences("data", 0);
        init(root);
        return root;
    }

    private void init(View root) {
        final Spinner spinner = binding.spinner;
        final TextView zypm = binding.textViewZypm;
        final TextView bjpm = binding.textViewBjpm;
        final TextView zyzrs = binding.textViewZyzrs;
        final TextView bjzrs = binding.textViewBjzrs;
        final ConstraintLayout constraintLayoutData = binding.constraintLayoutData;
        final ConstraintLayout constraintLayoutProgress = binding.constraintLayoutProgress;
        final TextView textViewProgressPmcx = binding.textViewProgressPmcx;
        Handler processHandler = new Handler(Looper.getMainLooper());
        textViewProgressPmcx.setText("正在获取数据");
        final HandlerThread handlerThread = new HandlerThread("StudentDataFetcher");
        handlerThread.start();
        Handler backgroundHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {

                if (msg.what == 10) {
                    int current = msg.arg1;
                    int total = msg.arg2;
                    processHandler.post(() -> {
                        String s = "正在获取班级排名 " + current + "/" + total;
                        textViewProgressPmcx.setText(s);
                    });
                }
                if (msg.what == 11) {
                    int current = msg.arg1;
                    int total = msg.arg2;
                    // 更新UI
                    processHandler.post(() -> {
                        String s = "正在获取专业排名 " + current + "/" + total;
                        textViewProgressPmcx.setText(s);
                    });
                }
                if (msg.what == 1) {
                    // 更新UI
                    zyRanks = (List<Rank>) msg.obj;
                    SharedPreferencesHelper.saveRanks(root.getContext(),"zyRanks",zyRanks);
                }
                if (msg.what == 2) {
                    // 更新UI
                    bjRanks = (List<Rank>) msg.obj;
                    SharedPreferencesHelper.saveRanks(root.getContext(),"bjRanks",bjRanks);
                }
                if (msg.what == 4) {
                    if (msg.arg1 == 100){
                        processHandler.post(() -> {
                            String s = "正在从缓存获取数据 ";
                            textViewProgressPmcx.setText(s);
                        });
                        if (zyRanks == null) {
                            zyRanks = (List<Rank>) SharedPreferencesHelper.loadRanks(root.getContext(),"zyRanks");
                        }
                        if (bjRanks == null) {
                            bjRanks = (List<Rank>) SharedPreferencesHelper.loadRanks(root.getContext(),"bjRanks");
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
                        spinner.setAdapter(adapter);
                        spinner.setSelection(Math.min(zyRanks.size(), bjRanks.size())-1);
                        zypm.setText(zyRanks.get(spinner.getSelectedItemPosition()).getRank());
                        bjpm.setText(bjRanks.get(spinner.getSelectedItemPosition()).getRank());
                        zyzrs.setText(zyRanks.get(spinner.getSelectedItemPosition()).getZrs());
                        bjzrs.setText(bjRanks.get(spinner.getSelectedItemPosition()).getZrs());
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position<zyRanks.size() && position<bjRanks.size()){
                                    lastPosition = position;
                                    zypm.setText(zyRanks.get(position).getRank());
                                    bjpm.setText(bjRanks.get(position).getRank());
                                    zyzrs.setText(zyRanks.get(position).getZrs());
                                    bjzrs.setText(bjRanks.get(position).getZrs());
                                    String itemSelected = (String) parent.getItemAtPosition(position);
                                    //Toast.makeText(root.getContext(), "已选择:" + itemSelected, Toast.LENGTH_SHORT).show();
                                }else {
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
                            Message message = Message.obtain(backgroundHandler, 4,100,0); // 用Message携带数据
                            message.sendToTarget();
                        }
                        else {
                            zyRanks = new ArrayList<>();
                            bjRanks = new ArrayList<>();
                            String[] params = new String[6];
                            String[] bjpms = new String[6];
                            String[] zypms = new String[6];
                            List<List<String>> xscjs = new ArrayList<>();
                            Message message;
                            int grade = Integer.parseInt("20" + mParam1.substring(2, 4));
                            params[0] = "{\"xnd\":\"" + grade + "-" + (grade + 1) + "\",\"xq\":\"1\"}";
                            params[1] = "{\"xnd\":\"" + grade + "-" + (grade + 1) + "\",\"xq\":\"2\"}";
                            params[2] = "{\"xnd\":\"" + (grade + 1) + "-" + (grade + 2) + "\",\"xq\":\"1\"}";
                            params[3] = "{\"xnd\":\"" + (grade + 1) + "-" + (grade + 2) + "\",\"xq\":\"2\"}";
                            params[4] = "{\"xnd\":\"" + (grade + 2) + "-" + (grade + 3) + "\",\"xq\":\"1\"}";
                            params[5] = "{\"xnd\":\"" + (grade + 2) + "-" + (grade + 3) + "\",\"xq\":\"2\"}";

                            String rankURL = "https://wvpn.ahu.edu.cn/https/77726476706e69737468656265737421fff944d226387d1e7b0c9ce29b5b";
                            //if (host.contains("one.ahu.edu.cn")) rankURL = "https://one.ahu.edu.cn";

                            try {
                                //班级排名
                                Connection.Response response;
                                Document doc = null;
                                for (int i = 0; i < 6; i++) {
                                    response = Jsoup.connect(
                                                    rankURL+"/idc/idc/idcHome/studentHome/getBjpm"
                                            )
                                            .method(Connection.Method.POST)
                                            .header("Content-Type", "application/json;charset=UTF-8")
                                            .cookies(cookies)
                                            .requestBody(params[i])//参数
                                            .ignoreContentType(true)
                                            .execute();
                                    doc = Jsoup.parse(response.body());
                                    bjpms[i] = doc.body().html();
                                    message = Message.obtain(backgroundHandler, 10, i + 1, 6); // 用Message携带数据
                                    message.sendToTarget();
                                }

                                //专业排名
                                for (int i = 0; i < 6; i++) {
                                    response = Jsoup.connect(
                                                    rankURL+"/idc/idc/idcHome/studentHome/getZypm"
                                            )
                                            .method(Connection.Method.POST)
                                            .header("Content-Type", "application/json;charset=UTF-8")
                                            .cookies(cookies)
                                            .requestBody(params[i])//参数
                                            .ignoreContentType(true)
                                            .execute();
                                    doc = Jsoup.parse(response.body());
                                    zypms[i] = doc.body().html();
                                    message = Message.obtain(backgroundHandler, 11, i + 1, 6); // 用Message携带数据
                                    message.sendToTarget();
                                }

                                for (String zypm : zypms) {
                                    Rank rank = new Rank(zypm.substring(zypm.indexOf("{\"PM\":") + 6, zypm.indexOf(",\"PM2\"")), zypm.substring(zypm.indexOf("\"ZYRS\":") + 7, zypm.indexOf(",\"LABELNAME\"")));
                                    if (rank.getRank().startsWith("\"")) {
                                        rank.setRank(rank.getRank().substring(1, rank.getRank().length() - 1));
                                    }
                                    if(Objects.equals(rank.getRank(), "0") || Objects.equals(rank.getRank(), "无")) continue;
                                    zyRanks.add(rank);
                                }
                                message = Message.obtain(backgroundHandler, 1, zyRanks); // 用Message携带数据
                                message.sendToTarget(); // 发送消息到Handler
                                for (String bjpm : bjpms) {
                                    Rank rank = new Rank(bjpm.substring(bjpm.indexOf("{\"PM\":") + 6, bjpm.indexOf(",\"ID_TYPE\"")), bjpm.substring(bjpm.indexOf("\"BJRS\":") + 7, bjpm.indexOf("}")));
                                    if (rank.getRank().startsWith("\"")) {
                                        rank.setRank(rank.getRank().substring(1, rank.getRank().length() - 1));
                                    }
                                    bjRanks.add(rank);
                                }
                                message = Message.obtain(backgroundHandler, 2, bjRanks); // 用Message携带数据
                                message.sendToTarget(); // 发送消息到Handler
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
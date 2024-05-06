package com.example.myapplication.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentXskbBinding;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_xskb#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_xskb extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    FragmentXskbBinding binding;
    private String mParam1;
    private String mParam2;

    public Fragment_xskb() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_xskb.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_xskb newInstance(String param1, String param2) {
        Fragment_xskb fragment = new Fragment_xskb();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_xskb, container, false);
        binding = FragmentXskbBinding.bind(root);

        return root;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(View root){
        final RecyclerView recyclerView = binding.recyclerViewXskb;
        LocalDate date = LocalDate.now();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        dayOfWeek.getValue();

    }
}
package com.example.myapplication.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.Course;
import com.example.myapplication.Rank;
import com.example.myapplication.fragment.Fragment_cjcx;
import com.example.myapplication.fragment.Fragment_other;
import com.example.myapplication.fragment.Fragment_pmcx;
import com.example.myapplication.R;

import java.util.List;
import java.util.Map;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    Map<String, String> cookies;
    String p1,p2;
    boolean isZddl;
    String host;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String p1,
                                String p2,
                                Map<String, String> cookies,
                                boolean isZddl,
                                String host) {
        super(fm);
        mContext = context;
        this.p1 = p1;
        this.p2 = p2;
        this.cookies = cookies;
        this.isZddl = isZddl;
        this.host = host;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment.
        switch (position) {
            case 2:
                return Fragment_cjcx.newInstance(p1,p2,cookies,host,isZddl);
            case 1:
                return Fragment_pmcx.newInstance(p1,p2,cookies,host,isZddl);
            case 0:
                return Fragment_other.newInstance(p1,p2);
            // 根据需要添加更多Fragment
            default:
                return PlaceholderFragment.newInstance(position + 1);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 2:
                return "成绩查询";
            case 1:
                return "排名查询";
            case 0:
                return "学生课表";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void refreshData(){

    }
}
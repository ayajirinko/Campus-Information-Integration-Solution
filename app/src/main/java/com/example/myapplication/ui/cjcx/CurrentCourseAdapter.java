package com.example.myapplication.ui.cjcx;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Course;
import com.example.myapplication.CurrentCourse;
import com.example.myapplication.R;

import java.util.Arrays;
import java.util.List;

public class CurrentCourseAdapter extends RecyclerView.Adapter<CurrentCourseAdapter.CourseViewHolder> {

    private List<CurrentCourse> coursesList;

    public CurrentCourseAdapter(List<CurrentCourse> coursesList) {
        this.coursesList = coursesList;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xskb, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        CurrentCourse course = coursesList.get(position);
        holder.cNameView.setText("课程名："+course.getCourseName());
        holder.cTimeView.setText("时间："+Arrays.toString(course.getCourseTime()));
        holder.cRoomView.setText("地点："+Arrays.toString(course.getCourseRoom()));
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView cNameView;
        TextView cTimeView;
        TextView cRoomView;

        public CourseViewHolder(View itemView) {
            super(itemView);

            cNameView = itemView.findViewById(R.id.text_view_cname);
            cTimeView = itemView.findViewById(R.id.text_view_ctime);
            cRoomView = itemView.findViewById(R.id.text_view_croom);
        }
    }
    public void updateData(List<CurrentCourse> newCoursesList) {
        this.coursesList.clear();
        this.coursesList.addAll(newCoursesList);
        notifyDataSetChanged();
    }

}

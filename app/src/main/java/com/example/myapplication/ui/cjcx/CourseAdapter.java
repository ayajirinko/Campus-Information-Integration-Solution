package com.example.myapplication.ui.cjcx;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Course;
import com.example.myapplication.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> coursesList;

    public CourseAdapter(List<Course> coursesList) {
        this.coursesList = coursesList;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cjcx, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Course course = coursesList.get(position);

        holder.titleTextView.setText(course.courseName);
        holder.teacherTextView.setText(course.zypjcj);
        holder.dateTextView.setText(course.kccj);
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView teacherTextView;
        TextView dateTextView;

        public CourseViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.text_view_cname);
            teacherTextView = itemView.findViewById(R.id.text_view_ctime);
            dateTextView = itemView.findViewById(R.id.text_view_croom);
        }
    }
    public void updateData(List<Course> newCoursesList) {
        this.coursesList.clear();
        this.coursesList.addAll(newCoursesList);
        notifyDataSetChanged();
    }

}

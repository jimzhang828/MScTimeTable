package cs.hku.zwj.timetable_test;

import android.app.Application;

import java.util.ArrayList;

public class MyApplication extends Application {
    private ArrayList<String> courseList = null;
    private String[] courseSelected = {
            "COMP7103B",
            "COMP7305B",
            "COMP7309",
            "COMP7404D",
            "COMP7405",
            "COMP7407",
            "COMP7408",
            "COMP7506A",
            "COMP7506B",
            "COMP7606A",
            "COMP7606B",
            "COMP7606C",
            "COMP7801",
            "COMP7901",
            "COMP7904"
    };

    public ArrayList<String> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<String> newList) {
        courseList = newList;
    }
}
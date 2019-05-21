package com.example.hw2.tasks;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TaskListContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Task> ITEMS = new ArrayList<Task>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Task> ITEM_MAP = new HashMap<String, Task>();


    public static void addItem(Task item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.title, item);
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class Task implements Parcelable {
        public String picPath;
        public final String title;
        public final String director;
        public final String release_date;

        public Task(String title, String director, String release_date) {
            this.picPath = "";
            this.title = title;
            this.director = director;
            this.release_date = release_date;
        }
        public Task(String picPath, String title, String director, String release_date) {
            this.picPath = picPath;
            this.title = title;
            this.director = director;
            this.release_date = release_date;
        }

        protected Task(Parcel in) {
            picPath = in.readString();
            title = in.readString();
            director = in.readString();
            release_date = in.readString();
        }

        public static final Creator<Task> CREATOR = new Creator<Task>() {
            @Override
            public Task createFromParcel(Parcel in) {
                return new Task(in);
            }

            @Override
            public Task[] newArray(int size) {
                return new Task[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(picPath);
            dest.writeString(title);
            dest.writeString(director);
            dest.writeString(release_date);
        }

        public void setPicPath(String path){
            this.picPath = path;
        }
    }
    public static void removeItem(int position){
        ITEMS.remove(position);
        ITEM_MAP.remove(position);
    }

    public static void clearList(){
        ITEMS.clear();
        ITEM_MAP.clear();
    }

}

package com.example.hw2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.hw2.tasks.TaskListContent;

import java.util.Random;

public class addelement extends AppCompatActivity {
    private final String TASKS_SHARED_PREFS = "TasksSharedPrefs";
    private final String NUM_TASKS = "NumOfTasks";
    private final String TITLE = "mod_";
    private final String RELEASE_DATE = "desc_";
    private final String PIC = "pic_";
    private final String DIRECTOR = "col_";
    EditText newTitleTxt;
    EditText newDirectorTxt;
    EditText newReleaseDateTxt;
    String newTitle;
    String newDirector;
    String newReleaseDate;;
    Intent data;
    String image;
    Random generator= new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addelement);
       newTitleTxt = findViewById(R.id.title);
       newDirectorTxt = findViewById(R.id.director);
       newReleaseDateTxt = findViewById(R.id.description);
       data= getIntent();
       image = data.getStringExtra("new_image");
    }

    public void addCar(View view) {
        newTitle = newTitleTxt.getText().toString();
        newDirector = newDirectorTxt.getText().toString();
        newReleaseDate = newReleaseDateTxt.getText().toString();
        if(newTitle.isEmpty() && newDirector.isEmpty() && newReleaseDate.isEmpty()){
            TaskListContent.addItem(new TaskListContent.Task("drawable "+(generator.nextInt(3)+1)+"", "title",
                    "DIRECTOR","release_date"));
        }
        else{
            if(newTitle.isEmpty())
                newTitle = "title";
            if(newDirector.isEmpty())
                newDirector ="director";
            if(newReleaseDate.isEmpty())
                newReleaseDate ="release_date";
            if(image==null)
                TaskListContent.addItem(new TaskListContent.Task("drawable "+(generator.nextInt(3)+1)+"", newTitle, newDirector, newReleaseDate));
            else
                TaskListContent.addItem(new TaskListContent.Task(""+image, newTitle, newDirector, newReleaseDate));
        }

        newTitleTxt.setText("");
        newDirectorTxt.setText("");
        newReleaseDateTxt.setText("");

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);

        SharedPreferences tasks = getSharedPreferences(TASKS_SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = tasks.edit();

        editor.clear();

        editor.putInt(NUM_TASKS, TaskListContent.ITEMS.size());
        for(int i=0;i<TaskListContent.ITEMS.size();i++){
            TaskListContent.Task task = TaskListContent.ITEMS.get(i);
            editor.putString(TITLE + i, task.title);
            editor.putString(DIRECTOR + i, task.director);
            editor.putString(RELEASE_DATE + i, task.release_date);
            editor.putString(PIC + i, task.picPath);
        }
        editor.apply();
        addelement.this.finish();
    }

}


package com.example.hw2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.hw2.DeleteDialog.OnDeleteDialogInteractionListener;
import com.example.hw2.tasks.TaskListContent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements TaskFragment.OnListFragmentInteractionListener, OnDeleteDialogInteractionListener {
    private final String TASKS_SHARED_PREFS = "TasksSharedPrefs";
    private final String NUM_TASKS = "NumOfTasks";
    private final String TITLE = "tit_";
    private final String RELEASE_DATE = "reld_";
    private final String PIC = "pic_";
    private final String DIRECTOR = "dir_";
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private int currentItemPosition = -1;
    public static final String taskExtra = "taskExtra";
    private TaskListContent.Task currentTask;
    private final String CURRENT_TASK_KEY = "CurrentTask";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            currentTask = savedInstanceState.getParcelable(CURRENT_TASK_KEY);
        }
        FloatingActionButton fab = findViewById(R.id.addelements);
        FloatingActionButton fab_2 = findViewById(R.id.camera);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddElement();
            }
        });
        fab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {

                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getString(R.string.myFileprovider), photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
        restoreTitlesFromSharedPreferences();
    }

    public void openActivityAddElement() {
        Intent intent = new Intent(this, addelement.class);
        startActivityForResult(intent, 1);
    }

    public void openActivityInfo(TaskListContent.Task task, int position) {
        Intent intent = new Intent(this, TaskInfoActivity.class);
        intent.putExtra(taskExtra, task);
        startActivity(intent);
    }

    private void restoreTitlesFromSharedPreferences() {
//        getSharedPreferences(TASKS_SHARED_PREFS, 0).edit().clear().commit();
        SharedPreferences tasks = getSharedPreferences(TASKS_SHARED_PREFS, MODE_PRIVATE);
        int numOfTasks = tasks.getInt(NUM_TASKS, 0);
        if (numOfTasks != 0) {
            TaskListContent.ITEMS.clear();

            for (int i = 0; i < numOfTasks; i++) {
                String model = tasks.getString(TITLE + i, "0");
                String color = tasks.getString(DIRECTOR + i, "0");
                String detail = tasks.getString(RELEASE_DATE + i, "0");
                String picPath = tasks.getString(PIC + i, "0");
                TaskListContent.addItem(new TaskListContent.Task(picPath, model, color, detail));
            }
        }
    }

    @Override
    public void onListFragmentClickInteraction(TaskListContent.Task task, int position) {
        currentTask = task;
        Toast.makeText(this, getString(R.string.item_selected_msg) + " " + position, Toast.LENGTH_SHORT).show();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dipslayTaskInFragment(task);
        } else {
            openActivityInfo(task, position);
        }
    }

    @Override
    public void onListFragmentLongClickInteraction(int position) {

    }

    @Override
    public void deleteFromList(int position) {
        Toast.makeText(this, getString(R.string.long_click_msg) + " " + position, Toast.LENGTH_SHORT).show();
        showDeleteDialog();
        currentItemPosition = position;
    }


    private void showDeleteDialog() {
        DeleteDialog.newInstance().show(getSupportFragmentManager(), getString(R.string.delete_dialog_tag));
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (currentItemPosition != -1 && currentItemPosition < TaskListContent.ITEMS.size()) {
            TaskListContent.removeItem(currentItemPosition);
            SharedPreferences tasks = getSharedPreferences(TASKS_SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = tasks.edit();

            editor.clear();

            editor.putInt(NUM_TASKS, TaskListContent.ITEMS.size());
            for (int i = 0; i < TaskListContent.ITEMS.size(); i++) {
                TaskListContent.Task task = TaskListContent.ITEMS.get(i);
                editor.putString(TITLE + i, task.title);
                editor.putString(DIRECTOR + i, task.director);
                editor.putString(RELEASE_DATE + i, task.release_date);
                editor.putString(PIC + i, task.picPath);
            }
            editor.apply();
            ((TaskFragment) getSupportFragmentManager().findFragmentById(R.id.taskFragment)).notifyDataChange();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        View v = findViewById(R.id.addButton);
        if (v != null) {
            Snackbar.make(v, getString(R.string.delete_cancel_msg), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.retry_msg), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDeleteDialog();
                        }
                    }).show();
        }
    }

    private void dipslayTaskInFragment(TaskListContent.Task task) {
        TaskInfoFragment taskInfoFragment = ((TaskInfoFragment) getSupportFragmentManager().findFragmentById(R.id.displayFragment));
        if (taskInfoFragment != null) {
            taskInfoFragment.displayTask(task);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (currentTask != null) {
            outState.putParcelable(CURRENT_TASK_KEY, currentTask);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (currentTask != null) {
                dipslayTaskInFragment(currentTask);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "new_model" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(this, addelement.class);
            intent.putExtra("new_image", mCurrentPhotoPath);
            startActivity(intent);
        }
    }
}


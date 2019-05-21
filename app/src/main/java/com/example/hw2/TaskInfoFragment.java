package com.example.hw2;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hw2.tasks.TaskListContent;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskInfoFragment extends Fragment{
    private TaskListContent.Task mDisplayedTask;

    public TaskInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_info, container, false);
    }
    public void displayTask(TaskListContent.Task task) {
        FragmentActivity activity = getActivity();
        (activity.findViewById(R.id.displayFragment)).setVisibility(View.VISIBLE);
        TextView InfoTitle = activity.findViewById(R.id.InfoTitle);
        TextView InfoDirector = activity.findViewById(R.id.infoDirector);
        TextView InfoTitle2 = activity.findViewById(R.id.infoModel);
        TextView InfoReleaseDate = activity.findViewById(R.id.infoReleaseDate);
        final ImageView InfoImage = activity.findViewById(R.id.InfoImage);
        Context context = getContext();
        InfoTitle.setText("Ekran wyboru dla filmu: "+task.title +"");
        InfoDirector.setText(task.director);
        InfoTitle2.setText(task.title);
        InfoReleaseDate.setText(task.release_date);
        if (task.picPath!=null && !task.picPath.isEmpty()) {
            if (task.picPath.contains("drawable")){
                Drawable taskDrawable;
                switch (task.picPath){
                    case "drawable 1":
                        taskDrawable= ContextCompat.getDrawable(context,R.drawable.avatar_1);
                        break;
                    case "drawable 2":
                        taskDrawable= ContextCompat.getDrawable(context,R.drawable.avatar_2);
                        break;
                    case "drawable 3":
                        taskDrawable= ContextCompat.getDrawable(context,R.drawable.avatar_3);
                        break;
                    default:
                        taskDrawable= ContextCompat.getDrawable(context,R.drawable.avatar_4);
                }
               InfoImage.setImageDrawable(taskDrawable);
            }
            else {
                Handler handler = new Handler();

                InfoImage.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InfoImage.setVisibility(View.VISIBLE);

                        Bitmap cameraImage = PicUtils.decodePic(mDisplayedTask.picPath,
                                InfoImage.getWidth(),
                                InfoImage.getHeight());
                        InfoImage.setImageBitmap(cameraImage);
                    }
                }, 200);
            }
            }else{
                InfoImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.avatar_4));
            }
            mDisplayedTask = task;
        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if(intent!= null){
            TaskListContent.Task receivedTask = intent.getParcelableExtra(MainActivity.taskExtra);
            if(receivedTask!=null){
                displayTask(receivedTask);
            }
        }
    }
}
class PicUtils{
    static Bitmap decodePic(String pPath, int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pPath,options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pPath,options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if(height> reqHeight || width>reqWidth){
            final int halfHeight = height / 2 ;
            final int halfWidth = width / 2;

            while ((halfHeight/ inSampleSize)>= reqHeight && (halfWidth/inSampleSize)>=reqWidth){
                inSampleSize *=2;
            }
        }
        return inSampleSize;
    }
}

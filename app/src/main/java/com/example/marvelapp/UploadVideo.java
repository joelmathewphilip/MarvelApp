package com.example.marvelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class UploadVideo extends AppCompatActivity {
    Button upload_btn;
    VideoView videoView;
    StorageReference video_reference;
    DatabaseReference databaseReference;
    EditText edit;
    String mobile_number;
    Uri videoUri;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        videoUri=getIntent().getParcelableExtra("Uri");

        upload_btn=findViewById(R.id.button_video_upload);
        videoView=findViewById(R.id.camera_video_view);
        edit=findViewById(R.id.Video_Name);

        videoView.setVideoURI(videoUri);
            mobile_number=getIntent().getStringExtra("Mobile Number");


        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(videoView.isPlaying())
                {
                    videoView.pause();
                    position=videoView.getCurrentPosition();
                    return false;
                }
                else
                {
                    videoView.seekTo(position);
                    videoView.start();
                    return false;
                }

            }
        });
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    upload_file();
            }
        });
    }

    public void upload_file()
    {
        try {
            if(edit.getText().toString().trim().length()==0)
            {
                edit.setError("Required");
                edit.requestFocus();
                return;
            }
            if (videoUri != null) {
                final long system_time=System.currentTimeMillis();
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.show();
                progressDialog.setCancelable(false);
                video_reference = FirebaseStorage.getInstance().getReference().child("Video_Files/"+mobile_number+"/"+system_time+".mp4");
                videoView.setVideoURI(videoUri);

                video_reference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> new_uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!new_uri.isComplete()) ;
                        Uri uri2 = new_uri.getResult();
                        databaseReference= FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Users").child(mobile_number).child("Videos").child(String.valueOf(system_time)).child("Link").setValue(uri2.toString());
                        databaseReference.child("Users").child(mobile_number).child("Videos").child(String.valueOf(system_time)).child("Name").setValue(edit.getText().toString().trim());
                        Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = 100.0 * (taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded:" + (int) progress + "%");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "Nothing to upload!!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }



}
package com.example.marvelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

public class Profile extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    VideoView videoView;
    String mobile_number="123456789";
    Uri videoUri;
    ProgressDialog progressDialog;
    private StorageReference video_reference;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Button upload,library;
        final DataBaseHelper dataBaseHelper=new DataBaseHelper(this);

        Button logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.delete_all_data();
                Cursor res=dataBaseHelper.get_All_Data();
            if(res.getCount()==0)
            {
                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
            }
        });

        upload=findViewById(R.id.upload_video_btn);

        library=findViewById(R.id.video_library_btn);

        mobile_number=getIntent().getStringExtra("Mobile Number");
        Toast.makeText(getApplicationContext(),mobile_number,Toast.LENGTH_SHORT).show();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }
            }
        });

        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(getApplicationContext(),Library.class);
                intent3.putExtra("Mobile Number",mobile_number);
                startActivity(intent3);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = intent.getData();
            if(videoUri==null)
            {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
            Intent intent2=new Intent(getApplicationContext(),UploadVideo.class);
            intent2.putExtra("Uri", videoUri);
            intent2.putExtra("Mobile Number",mobile_number);
            startActivity(intent2);


        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
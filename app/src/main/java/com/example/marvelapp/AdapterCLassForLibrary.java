package com.example.marvelapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;


public class AdapterCLassForLibrary extends RecyclerView.Adapter<AdapterCLassForLibrary.MyViewHolder> {

    ArrayList<DataClassForLibrary> apps;
    StorageReference storageReference;
    Context context;
    String location;
    AdapterCLassForLibrary(Context context, ArrayList<DataClassForLibrary> obj)
    {
        this.context=context;
        apps=obj;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.library_recycler_view_template,parent,false);
        return new MyViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(apps.get(position).getName());
        final String url=apps.get(position).getUrl();
        try {
            holder.name.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    try {
                        download_file(apps.get(position).getUrl());
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
{
    TextView name;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.library_recycler_view_name);
    }
}


public void download_file(String link) throws IOException {

    storageReference=FirebaseStorage.getInstance().getReference().child("Video_Files/1.mp4");
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(Uri.parse(link), "video/*");
    intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
    context.startActivity(intent);


   /*storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
       @Override
       public void onSuccess(Uri uri) {
           Toast.makeText(context,"Downloaded"+uri,Toast.LENGTH_LONG).show();

       }
   });*/
}

}




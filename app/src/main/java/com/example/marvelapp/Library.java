package com.example.marvelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Library extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;

    String mobile;
    AdapterCLassForLibrary adapterCLassForLibrary;
    ArrayList apps=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        mobile=getIntent().getStringExtra("Mobile Number");
        recyclerView=findViewById(R.id.library_recycler_view);

        get_data();

    }
        public void get_data()
        {
            final ProgressDialog progressDialog=new ProgressDialog(getApplicationContext());
            progressDialog.setMessage("Loading..Please Wait");
            databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    apps.clear();
                    for(DataSnapshot item:dataSnapshot.getChildren())
                    {

                        if (item.getKey().toString().equals(mobile))
                        {
                            for (DataSnapshot item2 : item.child("Videos").getChildren()) {
                                String name = item2.child("Name").getValue().toString();
                                String url=item2.child("Link").getValue().toString();
                                apps.add(new DataClassForLibrary(name,url));

                            }
                        }
                    }

                    adapterCLassForLibrary=new AdapterCLassForLibrary(getApplicationContext(),apps);
                    recyclerView.setAdapter(adapterCLassForLibrary);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


}
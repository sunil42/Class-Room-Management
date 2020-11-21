package com.example.classroommanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Viewtimetable extends AppCompatActivity {

    ListView listView;
    StorageReference storageReference;
    List<timetablegettersetter> uploadList; //forview


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewtimetable);
        storageReference = FirebaseStorage.getInstance().getReference();
        listView = findViewById(R.id.timetablelist);
        uploadList = new ArrayList<>();

        viewAllFiles();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                timetablegettersetter timetablegettersetter = uploadList.get(position);
                String name = timetablegettersetter.getName();
                String link = timetablegettersetter.getUrl();
                Intent intent = new Intent(Viewtimetable.this, pdfviewer.class);
                intent.putExtra("file", link);
                intent.putExtra("pdf", name);
//                intent.setType(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(timetablegettersetter.getUrl()));
//                intent.setDataAndType(Uri.fromFile(file), "aplication/pdf");
                startActivity(intent);
                finish();
            }
        });
    }

    private void viewAllFiles()
    {
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference listRef = storageReference.child("Time-Table");
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult)
            {
                List<StorageReference> prefixes = listResult.getPrefixes();
                List<StorageReference> items = listResult.getItems();
                System.out.println(items.get(1).getName());

                for (StorageReference prefix : listResult.getPrefixes())
                {
//                    System.out.println(prefix.listAll());
                }
                for (StorageReference item : listResult.getItems())
                {
                    Log.d("url", item.getPath());
                    timetablegettersetter timetablegettersetter = new timetablegettersetter(item.getName(),item.getDownloadUrl().toString());
                    uploadList.add(timetablegettersetter);
                }
                String[] upload = new String[uploadList.size()];

                for(int i=0;i<upload.length;i++)
                {
                    upload[i] = uploadList.get(i).getName();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,upload)
                {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent)
                    {
                        View view = super.getView(position, convertView, parent);
                        TextView text = (TextView) view.findViewById(android.R.id.text1);
                        text.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                listView.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e)
            {
                Toast.makeText(Viewtimetable.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }//viewAllFiles
}
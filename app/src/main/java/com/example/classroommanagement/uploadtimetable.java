package com.example.classroommanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class uploadtimetable extends AppCompatActivity {

    private Uri filepath;
    EditText pdf;
    TextView textstatus;
    Button uploadpdf, choosepdf;
    ProgressDialog progressDialog;
    StorageReference storageReference; //forupload

    ListView listView;
    List<timetablegettersetter> uploadList; //forview

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadtimetable);

        pdf = findViewById(R.id.pdfname);
        uploadpdf = findViewById(R.id.uploadpdf);
        choosepdf = findViewById(R.id.choosepdf);
        textstatus = findViewById(R.id.textstatus);
        storageReference = FirebaseStorage.getInstance().getReference();

        listView = findViewById(R.id.listview);
        uploadList = new ArrayList<>();

        viewAllFiles();

        choosepdf.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                selectPDFfile();
            }
        });

        uploadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                uploadPDFFile();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                timetablegettersetter timetablegettersetter = uploadList.get(position);
                String name = timetablegettersetter.getName();
                String link = timetablegettersetter.getUrl();
                Intent intent = new Intent(uploadtimetable.this,pdfviewer.class);
                intent.putExtra("file",link);
                intent.putExtra("pdf",name);
                Log.d("PDF LINK",link);
//                intent.setType(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(timetablegettersetter.getUrl()));
//                intent.setDataAndType(Uri.fromFile(file), "aplication/pdf");
                startActivity(intent);
                finish();
            }
        });
    }

    private void selectPDFfile()
    {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF File"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filepath = data.getData();
            textstatus.setText("File Selected");
        }
    }

    private void uploadPDFFile()
    {
        if (filepath!= null)
        {
            hideKeyBoard();
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Time-Table");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            StorageReference reference = storageReference.child("Time-Table/" + pdf.getText().toString() + ".pdf");
            reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    progressDialog.dismiss();
                    textstatus.setText("File Uploaded Successfully");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage((int)progress+"% " + " Uploaded");
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please Select File", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyBoard()
    {
        if (getCurrentFocus() != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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
                System.out.println(items.get(1).getDownloadUrl());

                for (StorageReference prefix : listResult.getPrefixes())
                {
//                    System.out.println(prefix.listAll());
                }
                for (final StorageReference item : listResult.getItems())
                {
                    item.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            Log.d("PDF URL", task.getResult().toString());
                            timetablegettersetter timetablegettersetter = new timetablegettersetter(item.getName(),task.getResult().toString());
                            uploadList.add(timetablegettersetter);
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
                    });
                }
//                String[] upload = new String[uploadList.size()];
//
//                for(int i=0;i<upload.length;i++)
//                {
//                    upload[i] = uploadList.get(i).getName();
//                }
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,upload)
//                {
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent)
//                    {
//                        View view = super.getView(position, convertView, parent);
//                        TextView text = (TextView) view.findViewById(android.R.id.text1);
//                        text.setTextColor(Color.BLACK);
//                        return view;
//                    }
//                };
//                listView.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e)
            {
                Toast.makeText(uploadtimetable.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

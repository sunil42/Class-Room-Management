package com.example.classroommanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NotesupFragment extends Fragment
{
    private Uri filepath;
    EditText pdf;
    TextView textstatus;
    Button uploadpdf, choosepdf;
    ProgressDialog progressDialog;
    StorageReference storageReference; //forupload

    ListView listView;
    List<notesgettersetter> uploadList; //forview

    public NotesupFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("Notes");
        View view = inflater.inflate(R.layout.fragment_notesup, container, false);

        pdf = view.findViewById(R.id.pdfname);
        uploadpdf = view.findViewById(R.id.uploadpdf);
        choosepdf = view.findViewById(R.id.choosepdf);
        textstatus = view.findViewById(R.id.textstatus);
        storageReference = FirebaseStorage.getInstance().getReference();

        listView = view.findViewById(R.id.listview);
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

                notesgettersetter notesgettersetter = uploadList.get(position);
                String name = notesgettersetter.getName();
                String link = notesgettersetter.getUrl();

                Intent intent = new Intent(getActivity(),pdfviewer.class);
                intent.putExtra("file",link);
                intent.putExtra("pdf",name);
//                intent.setType(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(notesgettersetter.getUrl()));
//                intent.setDataAndType(Uri.fromFile(file), "aplication/pdf");
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void selectPDFfile()
    {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select File"),1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
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
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading Notes");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            StorageReference reference = storageReference.child("Notes/" + pdf.getText().toString() + ".pdf");
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
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Please Select File", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewAllFiles()
    {
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference listRef = storageReference.child("Notes");
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult)
            {
                List<StorageReference> prefixes = listResult.getPrefixes();
                List<StorageReference> items = listResult.getItems();

                for (StorageReference prefix : listResult.getPrefixes())
                {
//                    System.out.println(prefix.listAll());
                }
                for (final StorageReference item : listResult.getItems())
                {
                    item.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            notesgettersetter notesgettersetter = new notesgettersetter(item.getName(),task.getResult().toString());
                            uploadList.add(notesgettersetter);
                            String[] upload = new String[uploadList.size()];

                            for(int i=0;i<upload.length;i++)
                            {
                                upload[i] = uploadList.get(i).getName();
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,upload)
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
//                    notesgettersetter notesgettersetter = new notesgettersetter(item.getName(),item.getDownloadUrl().toString());
//                    uploadList.add(notesgettersetter);
                }
//                String[] upload = new String[uploadList.size()];
//
//                for(int i=0;i<upload.length;i++)
//                {
//                    upload[i] = uploadList.get(i).getName();
//                }
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,upload)
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
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
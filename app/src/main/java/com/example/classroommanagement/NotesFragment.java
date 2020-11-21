package com.example.classroommanagement;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {

    ListView noteslistView;
    //  DatabaseReference databaseReference;
    List<timetablegettersetter> uploadPDF;

    public NotesFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("Notes");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        noteslistView = view.findViewById(R.id.noteslist);
        uploadPDF = new ArrayList<>();

//      viewAllFiles();

        noteslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                timetablegettersetter timetablegettersetter = uploadPDF.get(position);

                Intent intent = new Intent();
//                intent.setType(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(uploadPDF.getUrl()));
                startActivity(intent);
            }
        });
    }
//
//    private void viewAllFiles()
//    {
//        private void viewAllFiles()
//         {
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
//        databaseReference.addValueEventListener(new ValueEventListener()
//            {
//                @Override
//                public void onDataChange( DataSnapshot snapshot) {
//
//                    for (DataSnapshot postSnapshot : snapshot.getChildren())
//                    {
//                        timetablegettersetter timetablegettersetter = postSnapshot.getValue(timetablegettersetter.class);
//                        uploadPDF.add(timetablegettersetter);
//                    }
//
//                    String[] uploads = new String[uploadPDF.size()];
//
//                    for(int i=0;i<uploads.length;i++)
//                    {
//                        uploads[i] = uploadPDF.get(i).getName();
//                    }
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads)
//                    {
//                        @Override
//                        public View getView(int position, View convertView, ViewGroup parent)
//                        {
//                            View view = super.getView(position, convertView, parent);
//                            TextView textView = view.findViewById(android.R.id.text1);
//                            textView.setTextColor(Color.BLACK);
//                            return view;
//                        }
//                    };
//                    pdflistView.setAdapter(adapter);
//                }
//            });
//         }
//    }//viewAllFiles
}
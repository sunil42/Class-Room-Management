package com.example.classroommanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class studentadapter extends RecyclerView.Adapter<studentadapter.studentdetailsViewHolder>
{
    private String[] name,enroll;
    public studentadapter(String[] studentname, String[] studentenroll)
    {
        this.name=studentname;
        this.enroll=studentenroll;
    }

    @Override
    public studentdetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.studentlist,parent,false);
        return new studentdetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(studentdetailsViewHolder holder, int position)
    {
        String sname = name[position];
        String sroll = enroll[position];
        holder.sname.setText(sname);
        holder.sroll.setText(sroll);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Call", Toast.LENGTH_SHORT).show();
            }
        });
        holder.mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Mail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return enroll.length;
    }

    public class studentdetailsViewHolder extends RecyclerView.ViewHolder
    {
        TextView sname,sroll;
        ImageButton call,mail;
        public studentdetailsViewHolder(View itemView)
        {
            super(itemView);
            sname = itemView.findViewById(R.id.sname);
            sroll = itemView.findViewById(R.id.enrollmentnumber);
            call = itemView.findViewById(R.id.call);
            mail = itemView.findViewById(R.id.mail);

        }
    }
}

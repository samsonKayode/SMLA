package com.smla.sessions;

/**
 * Created by SAMSON KAYODE on 12-Dec-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smla.model.StudentListModel;
import com.unesco.smla.Achievements;
import com.unesco.smla.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Nihal on 1/24/2017.
 */

public class MyCustomAdapter extends ArrayAdapter{
    private Context context;
    public static String STID=null;
    private ArrayList<StudentListModel> SLM;

    Utilities utx = new Utilities();

    public MyCustomAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context,textViewResourceId, objects);

        this.context= context;
        SLM=objects;

    }

    private class ViewHolder
    {
        TextView title;
        TextView student_id;
        TextView student_status;
        ImageView list_image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder=null;
        if (convertView == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_row, null);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.student_id = (TextView) convertView.findViewById(R.id.student_id);
            holder.student_status=(TextView)convertView.findViewById(R.id.student_status);
            holder.list_image = (ImageView)convertView.findViewById(R.id.list_image);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final StudentListModel individualList= SLM.get(position);
        holder.title.setText(individualList.getName());
        holder.student_id.setText(individualList.getId());
        holder.student_status.setText(individualList.getType());
        holder.list_image.setImageBitmap(utx.getImage(individualList.getImage()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tv.setText("bla bla");
                STID = individualList.getId();
                String NAME = individualList.getName();

                System.out.println("ID --- "+STID);
                System.out.println("NAME --- "+NAME);

                //context.startActivity(new Intent(this, Achievements.class));

                Intent i=new Intent(context, Achievements.class);
                context.startActivity(i);
            }
        });

        return convertView;


    }

}

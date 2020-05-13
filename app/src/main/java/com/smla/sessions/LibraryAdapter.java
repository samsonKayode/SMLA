package com.smla.sessions;

/**
 * Created by SAMSON KAYODE on 14-Dec-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smla.model.LibraryModel;
import com.smla.model.StudentListModel;
import com.unesco.smla.Achievements;
import com.unesco.smla.LibraryImage;
import com.unesco.smla.LibraryVideo;
import com.unesco.smla.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nihal on 1/24/2017.
 */

public class LibraryAdapter extends ArrayAdapter{
    private Context context;
    public static String STID=null;
    private ArrayList<LibraryModel> SLM;

    private ArrayList<LibraryModel> worldpopulationlist = null;
    private ArrayList<LibraryModel> arraylist;

    public static String TITLE="", WEXT="", DESC="";

    Utilities utx = new Utilities();

    public LibraryAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context,textViewResourceId, objects);

        this.context= context;
        SLM=objects;

        worldpopulationlist = objects;
        this.arraylist = new ArrayList<LibraryModel>();
        this.arraylist.addAll(SLM);

    }

    private class ViewHolder
    {
        TextView title;
        TextView subject;
        TextView content;
        TextView duration;
        ImageView list_image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder=null;
        if (convertView == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.library_row, null);

            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.title);
            holder.subject = convertView.findViewById(R.id.subject);
            holder.content= convertView.findViewById(R.id.content);
            holder.duration= convertView.findViewById(R.id.duration);
            holder.list_image = convertView.findViewById(R.id.list_image);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final LibraryModel individualList= SLM.get(position);
        holder.title.setText(individualList.getTitle());

        String nts="";

        if (individualList.getDescription().length()<100){
            holder.subject.setText(individualList.getDescription());
        }else{
            nts = individualList.getDescription().substring(0,100);
            holder.subject.setText(nts+"...");
        }


        holder.content.setText(individualList.getType());

        if(individualList.getType().toString().equals("Video")){
            holder.list_image.setImageResource(R.drawable.film1);
            holder.duration.setText("");
        }

        if(individualList.getType().toString().equals("Image")){

            holder.list_image.setImageResource(R.drawable.imagelib);
            holder.duration.setText("");
            //holder.list_image.setImageBitmap();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TITLE = individualList.getTitle();
                WEXT = individualList.getWithExtension();
                DESC = individualList.getDescription();

                String TYP = individualList.getType();

                if(TYP.equals("Image")){

                    Intent i=new Intent(context, LibraryImage.class);
                    context.startActivity(i);

                }
                if(TYP.equals("Video")){
                    Intent i=new Intent(context, LibraryVideo.class);
                    context.startActivity(i);
                }

                //System.out.println("ID --- "+STID);
                //System.out.println("NAME --- "+NAME);

                //context.startActivity(new Intent(this, Achievements.class));


            }
        });

        return convertView;


    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        SLM.clear();
        if (charText.length() == 0) {
            SLM.addAll(arraylist);
        }
        else
        {
            for (LibraryModel wp : arraylist)
            {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    SLM.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}

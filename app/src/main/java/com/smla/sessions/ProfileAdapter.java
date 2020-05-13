package com.smla.sessions;

/**
 * Created by SAMSON KAYODE on 20-May-2018.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smla.alerts.DialogBoxes;
import com.smla.database.DatabaseConn;
import com.smla.model.StudentListModel;
import com.unesco.smla.Achievements;
import com.unesco.smla.DashBoard;
import com.unesco.smla.LoginPage;
import com.unesco.smla.R;

import java.util.ArrayList;
import java.util.Locale;

import static android.R.attr.x;

public class ProfileAdapter extends ArrayAdapter{
    private Context context;
    public static String STID=null;
    private ArrayList<StudentListModel> SLM;

    private ArrayList<StudentListModel> arraylist;

    Utilities utx = new Utilities();

    DatabaseConn db = new DatabaseConn(getContext());
    DialogBoxes dbox = new DialogBoxes();



    public ProfileAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context,textViewResourceId, objects);

        this.context= context;
        SLM=objects;

    }

    private class ViewHolder
    {
        TextView fullname;
        TextView username, stID;
        TextView password;
        ImageView list_image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder=null;
        if (convertView == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.profile_row, null);

            holder = new ViewHolder();
            holder.fullname = (TextView) convertView.findViewById(R.id.fullname);
            holder.username = (TextView) convertView.findViewById(R.id.username);
            holder.password=(TextView)convertView.findViewById(R.id.password);
            holder.stID=(TextView)convertView.findViewById(R.id.studentID);
            holder.list_image = (ImageView)convertView.findViewById(R.id.list_image);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final StudentListModel individualList= SLM.get(position);
        holder.fullname.setText(individualList.getName());
        holder.username.setText(individualList.getUsername());
        holder.password.setText(individualList.getPassword());
        holder.stID.setText(individualList.getId());
        holder.list_image.setImageBitmap(utx.getImage(individualList.getImage()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STID = individualList.getId();
                String NAME = individualList.getUsername();

                System.out.println("ID --- "+STID);
                System.out.println("USERNAME --- "+NAME);

                new AlertDialog.Builder(getContext())
                        .setTitle(context.getString(R.string.confirm))
                        .setMessage(context.getString(R.string.delete_profile))
                        .setIcon(R.drawable.confirm_icon)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Delete user..

                                if(LoginPage.uname.equals(individualList.getUsername())){

                                    dbox.showMessageDialog(context, context.getString(R.string.dialog_title_error),
                                            context.getString(R.string.you_are_in), R.drawable.error_icon);
                                    return;

                                }else{

                                    int x = db.deleteProfile( individualList.getUsername(), STID);

                                    System.out.println("DELETE X iS::: "+x);

                                    if(x>=0){
                                        //deleted..
                                        dbox.showMessageDialog(context, context.getString(R.string.info),
                                                getContext().getString(R.string.profile_removed), R.drawable.info_icon);

                                        Intent i=new Intent(context, DashBoard.class);
                                        context.startActivity(i);

                                    }else{
                                        //not deleted..
                                        dbox.showMessageDialog(context, context.getString(R.string.dialog_title_error),
                                                context.getString(R.string.error_deleting_profile), R.drawable.error_icon);
                                    }
                                }


                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

        return convertView;
    }


}

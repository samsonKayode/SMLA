package com.smla.filebrowser;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList; 
import java.util.Collections;
import java.util.List;
import java.text.DateFormat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.smla.alerts.DialogBoxes;
import com.unesco.smla.R;

public class FileChooser extends ListActivity {

	private File currentDir;
    private FileArrayAdapter adapter;
    DialogBoxes dbox = new DialogBoxes();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        //currentDir = new File("/sdcard/");
		currentDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        fill(currentDir); 
    }
    private void fill(File f)
    {
    	File[]dirs = f.listFiles(); 
		 this.setTitle("Current Dir: "+f.getName());
		 List<Item>dir = new ArrayList<Item>();
		 List<Item>fls = new ArrayList<Item>();
		 try{
			 for(File ff: dirs)
			 { 
				Date lastModDate = new Date(ff.lastModified()); 
				DateFormat formater = DateFormat.getDateTimeInstance();
				String date_modify = formater.format(lastModDate);
				if(ff.isDirectory()){
					
					
					File[] fbuf = ff.listFiles(); 
					int buf = 0;
					if(fbuf != null){ 
						buf = fbuf.length;
					} 
					else buf = 0; 
					String num_item = String.valueOf(buf);
					if(buf == 0) num_item = num_item + " item";
					else num_item = num_item + " items";
					
					//String formated = lastModDate.toString();
					dir.add(new Item(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"ic_folder_black_24dp"));
				}
				else
				{
					
					fls.add(new Item(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"ic_insert_drive_file_black_24dp"));
				}
			 }
		 }catch(Exception e)
		 {    
			 
		 }
		 Collections.sort(dir);
		 Collections.sort(fls);
		 dir.addAll(fls);
		 if(!f.getName().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()))
			 dir.add(0,new Item("..","Parent Directory","",f.getParent(),"ic_arrow_upward_black_24dp"));
		 adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_view,dir);
		 this.setListAdapter(adapter); 
    }
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		final Item o = adapter.getItem(position);
		if(o.getImage().equalsIgnoreCase("ic_folder_black_24dp")||o.getImage().equalsIgnoreCase("ic_arrow_upward_black_24dp")){
				currentDir = new File(o.getPath());

            if(currentDir.isDirectory()){

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.selectFolderTitle))
                        .setMessage(getString(R.string.selectFolder))
                        .setIcon(R.drawable.confirm_icon)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                onFileClick(o);
                                dialog.dismiss();
                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                fill(currentDir);
                            }}).show();
            }

		}
		else
		{
			onFileClick(o);
		}
	}


    private void onFileClick(Item o)
    {
    	//Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent();
        intent.putExtra("GetPath",currentDir.toString());
        intent.putExtra("GetFileName",o.getName());
        setResult(RESULT_OK, intent);
        finish();
    }
}

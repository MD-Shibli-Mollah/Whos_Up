package com.example.student.hackathon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateInfoFragment extends Fragment {

    private View view;
    private TableLayout tl;

    public UpdateInfoFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_update_info, container, false);
        return this.view;
    }

   public void onResume()
   {
       super.onResume();

       Log.w("updateInfoFragment", MainActivity.userAccount.getAllClasses().toString());


       tl = (TableLayout) view.findViewById(R.id.tableInfo);

       //Setting the header of the table
       TableRow tr = new TableRow(view.getContext());
       tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

       TextView header = new TextView(view.getContext());
       header.setText("CRN");
       tr.addView(header);

       TextView header2 = new TextView(view.getContext());
       header2.setText("Class Name");
       tr.addView(header2);

       TextView header3 = new TextView(view.getContext());
       header3.setText("SMS" + "\t");
       tr.addView(header3);

       TextView header4 = new TextView(view.getContext());
       header4.setText("Video" + "\t");
       tr.addView(header4);

       TextView header5 = new TextView(view.getContext());
       header5.setText("Voice");
       tr.addView(header5);
       tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

       //populating the table with data from the user
       for (int i = 0; i < MainActivity.userAccount.getAllClasses().size(); i++)
       {

           ClassInfo currentClass = MainActivity.userAccount.getClass(i);
           Log.w("INFO_FRAGMENT", "ID " + currentClass.getId() + " name" + currentClass.getName());
           TableRow other = new TableRow(view.getContext());
           other.setLayoutParams(tr.getLayoutParams());

           TextView id = new TextView(view.getContext());
           id.setText(currentClass.getId() + "\t");

           TextView className = new TextView(view.getContext());
           if (currentClass.getName().length() >= 20)
           {
               className.setText(currentClass.getName().substring(0, 20) + "...\t");
           }
           else
           {
               className.setText(currentClass.getName() + "\t");
           }


           CheckBox sms = new CheckBox(view.getContext());
           sms.setClickable(true);
           sms.setChecked(currentClass.isShareSms());

           CheckBox video = new CheckBox(view.getContext());
           video.setClickable(true);
           video.setChecked(currentClass.isShareVideo() );

           CheckBox voice = new CheckBox(view.getContext());
           voice.setClickable(true);
           voice.setChecked(currentClass.isShareVoice());

           other.addView(id, 0);
           other.addView(className, 1);
           other.addView(sms, 2);
           other.addView(video, 3);
           other.addView(voice, 4);
           tl.addView(other, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
       }

       Button updateInfo = (Button) view.findViewById(R.id.updateInfoBtn);

       updateInfo.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {
                MainActivity.createToast("Updated account info");
           }
       });


   }

}

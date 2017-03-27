package com.example.student.hackathon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersOnlineFragment extends Fragment
{

    private View view;
    private TableLayout tl;

    public UsersOnlineFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_users_online, container, false);
        return view;
    }

    public void onResume()
    {
        super.onResume();

        //Ask the server for all the users online
        Command command = new Command();
        command.addCommand("usersOnline");
        NetworkThread.commandArrayList.add(command);


        while(MainActivity.userOnlineList == null)
        {

        }

        /*ClassInfo info = new ClassInfo("id1", "name", true, false, false);
        ArrayList<ClassInfo> list = new ArrayList<>();
        list.add(info);
        UserAccount a = new UserAccount(1, "lol", list);
        ArrayList<UserAccount> accountsList = new ArrayList<>();
        accountsList.add(a);
        userOnlineList = new UsersOnline(accountsList);*/

        TextView text = (TextView) view.findViewById(R.id.userOnlineText);
        text.append(MainActivity.userOnlineList.getUserOnlineCount() + "");

        tl = (TableLayout) view.findViewById(R.id.UserOnlineTable);
        for (int i = 0; i < MainActivity.userOnlineList.getUserOnlineCount(); i++)
        {
            TableRow tr = new TableRow(view.getContext());

            TextView name = new TextView(view.getContext());
            name.setText(MainActivity.userOnlineList.getUser(i).getUsername());

            final UserButton button = new UserButton(view.getContext() , "text", MainActivity.userOnlineList.getUser(i));
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    MainActivity.createToast(button.getUsername());
                }
            });

            tr.addView(name);
            tr.addView(button);
            tl.addView(tr);
        }




    }


}

package com.example.student.hackathon;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextFragment extends Fragment {

    private static View view;
    private static TextView text;
    private TableLayout tl;
    public static String roomID = "";
    private static UserAccount account;
    public static TextFragmentThread refreshUIThread;

    public static void updateText(final String text)
    {
        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                TextFragment.text.append(text);
            }
        };
        mHandler.sendMessage(new Message());


    }

    public TextFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_text, container, false);
        return view;
    }

    public void onResume()
    {
        super.onResume();
        new Thread(new TextFragmentThread()).start();
        text = (TextView) view.findViewById(R.id.roomText);
        tl = (TableLayout) view.findViewById(R.id.tableSms);
        Button button1 = (Button) view.findViewById(R.id.sendSms);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Command sendMessage = new Command();
                sendMessage.addCommand("sendMessage");
                sendMessage.addCommand(TextFragment.roomID);
                NetworkThread.commandArrayList.add(sendMessage);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Command com = new Command();
                com.addCommand("askMessage");
                com.addCommand(TextFragment.roomID);
                NetworkThread.commandArrayList.add(com);

            }
        });
        Command command = new Command();
        command.addCommand("usersOnline");
        NetworkThread.commandArrayList.add(command);

        while (MainActivity.userOnlineList == null)
        {

        }
        Log.w("TEXTFRAGMENT", "userOnline not null");

        for (int i = 0; i < MainActivity.userOnlineList.getUserOnlineCount(); i++)
        {
            account = MainActivity.userOnlineList.getUser(i);

            for (int j = 0; j < account.getAllClasses().size(); j++)
            {

                if (!account.getUsername().equals(MainActivity.userAccount.getUsername()))
                {
                    if (account.getClass(j).isShareSms())
                    {
                        Log.w("TEXTFRAGMENT" , "account share sms " + account.getUsername());
                        TableRow row = new TableRow(view.getContext());

                        TextView tv = new TextView(view.getContext());
                        tv.setText(account.getUsername());

                        final UserButton button = new UserButton(view.getContext(), "sms", account);
                        button.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Command askRoom = new Command();
                                askRoom.addCommand("createSmsRoom");
                                askRoom.addCommand(MainActivity.userAccount.getUsername());
                                askRoom.addCommand(account.getUsername());
                                NetworkThread.commandArrayList.add(askRoom);

                                //wait for room
                                while (roomID.equals(""))
                                {

                                }


                            }
                        });
                        row.addView(tv);
                        row.addView(button);
                        tl.addView(row);
                    }
                }
            }
        }
    }
}

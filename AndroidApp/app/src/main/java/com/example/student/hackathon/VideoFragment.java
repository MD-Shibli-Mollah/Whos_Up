package com.example.student.hackathon;


import android.app.Activity;
import android.os.Bundle;
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

import com.vidyo.VidyoClient.Connector.Connector;
import com.vidyo.VidyoClient.Connector.VidyoConnector;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment implements VidyoConnector.IConnect {

    private View view;
    private TableLayout tl;
    private static FrameLayout videoFrame;
    private VidyoConnector vc;


    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_video, container, false);
        return view;
    }

    public void onResume()
    {
        super.onResume();
        videoFrame = (FrameLayout) view.findViewById(R.id.videoFrame);
        tl = (TableLayout) view.findViewById(R.id.videoTable);
        Button changeCamera = (Button) view.findViewById(R.id.changeCamera);
        changeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vc.CycleCamera();
            }
        });
        Command command = new Command();
        command.addCommand("usersOnline");
        NetworkThread.commandArrayList.add(command);
        final Activity fragActivity = getActivity();

        while (MainActivity.userOnlineList == null)
        {

        }

        for (int i = 0; i < MainActivity.userOnlineList.getUserOnlineCount(); i++)
        {
            UserAccount account = MainActivity.userOnlineList.getUser(i);

            for (int j = 0; j < account.getAllClasses().size(); j++)
            {
                if (account.getClass(j).isShareVideo())
                {
                    //Log.w("VideoFragment", account.getUsername() + " is sharing video for class " + account.getClass(i).getName());

                    //so we can't send a video call to our self
                    if (!account.getUsername().equals(MainActivity.userAccount.getUsername()))
                    {
                        TableRow tr = new TableRow(view.getContext());

                        TextView info = new TextView(view.getContext());
                        info.setText(account.getUsername() + ":" + account.getClass(j).getName());

                        final UserButton userButton = new UserButton(view.getContext(), "video", account);
                        userButton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                MainActivity.createToast("Start video chat with " + userButton.getUsername());

                                Connector.SetApplicationUIContext(fragActivity);
                                Connector.Initialize();
                                Start();
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Connect();
                            }
                        });

                        tr.addView(info);
                        tr.addView(userButton);

                        tl.addView(tr);
                    }
                }
            }
        }

    }

    public void Start()
    {
        vc = new VidyoConnector(videoFrame, VidyoConnector.VidyoConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Default, 16, "", "", 0 );
        vc.ShowViewAt(videoFrame, 0, 0, videoFrame.getWidth(), videoFrame.getHeight());
    }

    public void Connect()
    {
        //https://developer.vidyo.io/documentation/4-1-9-5/getting-started#Tokens
                //java -jar generateToken.jar --key= --appID= --userName=user1 --expiresInSecs=100000
        String token = "token";
        vc.Connect("prod.vidyo.io", token, MainActivity.userAccount.getUsername(), "demoRoom", this);


    }

    public void Disconnect(View v)
    {
        vc.Disconnect();
    }

    @Override
    public void OnSuccess()
    {
        //MainActivity.createToast("OnSucess");
        Log.w("VideoFragment", "onSucess");
    }

    @Override
    public void OnFailure(VidyoConnector.VidyoConnectorFailReason vidyoConnectorFailReason) {
       // MainActivity.createToast("OnFailure");
        Log.w("VideoFragment", "onFailure");
    }

    @Override
    public void OnDisconnected(VidyoConnector.VidyoConnectorDisconnectReason vidyoConnectorDisconnectReason) {
        MainActivity.createToast("OnDisconect");
    }
}

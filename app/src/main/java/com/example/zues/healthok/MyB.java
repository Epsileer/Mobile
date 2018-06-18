package com.example.zues.healthok;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.zues.healthok.model.Db;
import com.example.zues.healthok.model.GCMRequestData;
import com.example.zues.healthok.service.RegistrationIntentService;

public class MyB extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(RegistrationIntentService.REGISTRATION_SUCCESS)){


            String token = intent.getStringExtra("token");
            //Displaying the token as toast
            Toast.makeText(context, "Registration token:" + token, Toast.LENGTH_LONG).show();
        }
        else if(intent.getAction().equals(RegistrationIntentService.REGISTRATION_ERROR)){
            Toast.makeText(context, "GCM registration error!", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(context, "Error occurred", Toast.LENGTH_LONG).show();
            String token = intent.getStringExtra("token");
            Toast.makeText(context, "Message:" + token, Toast.LENGTH_LONG).show();
            GCMRequestData g = GCMRequestData.getInstance();
            g.setMessage(token);
            Db d = new Db(context);
            d.addMessges(token);

        }

    }
    }


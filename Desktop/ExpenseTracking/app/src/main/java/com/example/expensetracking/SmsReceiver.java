package com.example.expensetracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by user on 18-02-2018.
 */

public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_BUNDLE = "pdus";
    String smsMessageStr;
    int flag, preflag, reCheck;
    double amt;
    String shopName="";

    public void onReceive(final Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
            }
            String[] words = smsMessageStr.split(" ");
            for(int k=0;k<words.length;k++)
            {
                if(words[k].equalsIgnoreCase("debited"))
                {
                    flag = 1;
                    break;
                }
            }
            String word="";

            if(flag==1)
            {
                //Getting amount from the message received
                for(int k=0;k<words.length;k++)
                {
                    if(words[k].contains("Rs."))
                    {
                        word=words[k].replaceAll(",","");
                        amt = Double.parseDouble(word.substring(3));
                        break;
                    }
                    else if(words[k].equalsIgnoreCase("INR")||words[k].equalsIgnoreCase("Rs"))
                    {
                        word=words[k+1].replaceAll(",","");
                        amt = Double.parseDouble(word);
                        break;
                    }


                }

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("dd/ MM / yyyy ");
                String strDate = " Current Date : " + mdformat.format(calendar.getTime());

                int k;
                //Getting place from the message received
                for(k=1;k<words.length;k++)
                {
                    if(words[k-1].equalsIgnoreCase("at")||words[k-1].equalsIgnoreCase("@")) {
                        preflag = 1;
                        reCheck = k;
                    }
                    for(;reCheck<words.length;reCheck++)
                    {
                        if(words[reCheck].equalsIgnoreCase("on"))
                        {
                            break;
                        }
                    }

                    if(preflag==1)
                        break;


                }

                for(int m=k;m<reCheck;m++)
                    shopName+=words[m];


                Toast.makeText(context,"Amount:"+ amt+" ShopName: "+shopName+strDate,Toast.LENGTH_SHORT).show();



            }
         //  Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();


        }
    }
}
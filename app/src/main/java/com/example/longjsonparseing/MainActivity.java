package com.example.longjsonparseing;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {

    private String fileSaveInPath = Environment.getExternalStorageDirectory().toString() + "/Prince/prince.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

//    209
//    35
//    244
//    255756 required length
//    256000 total length

    public void readAndParse(View view) {

        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(fileSaveInPath));
            JSONObject jsonObject = new JSONObject(new String(jsonData));
            JSONObject jsonSet = jsonObject.getJSONObject("set");
            JSONObject jsonList = jsonSet.getJSONObject("list");
            JSONObject jsonOrgSet = jsonList.getJSONObject("orgSet");
            JSONArray jsonArrayRecord = jsonOrgSet.getJSONArray("record");
            int tempL = 0;
            int temFlag = 0;
//            int reqL = 255756;
            int reqL = 150000;
            String tempString = "";

            for (int i = 0; i < jsonArrayRecord.length(); i++) {

                String subOrj = jsonArrayRecord.getString(i);
                temFlag = subOrj.length();
                boolean check = (reqL >= (tempL + temFlag)) ? true : false;
                if (check) {
                    tempL = tempL + temFlag;
                    if (tempString.length() == 0) {
                        tempString = "[" + tempString + subOrj;
                    } else {
                        tempString = tempString + "," + subOrj;
                    }
                } else {
                    JSONObject jsonObjectSplit = jsonArrayRecord.getJSONObject(i);
                    JSONArray jsonArrayForSplit = jsonObjectSplit.getJSONArray("orgJob");
                    int temJFlag = 0;
                    for (int j = 0; j < jsonArrayForSplit.length(); j++) {
                        String subOrjSplit = jsonArrayForSplit.getString(j);
                        temJFlag = subOrjSplit.length();
                        boolean check2 = (reqL >= (tempL + temJFlag)) ? true : false;
                        if (check2) {
                            tempL = tempL + temJFlag;
                            if (j==0){
                                tempString = tempString + " ,{\"orgJob\": [" + subOrjSplit;
                            }else {
                                tempString = tempString + "," + subOrjSplit;
                            }
//                            tempString = tempString + subOrjSplit;
                            Log.i("Prince Looping value of i "+ i, "Looping of j "+j + "Length is " + tempL);
                        } else {
                            tempString = tempString + "]}]";
                            Log.i("Prince Looping value of i " + i, "Looping of j " + j + "Length is " + tempL);
                            Log.i("Prince Required Temp Length ", "" + tempL);
                            Log.i("Prince Required TempString ", "" + tempString);
                            Log.i("Prince Test TempString Length " + i, "" + tempString.length());
                            tempString = "";
                            tempL = 0;

//                        send data to server
                        }

                    }
                }



            }
//            tempString = tempString + "]";
            System.out.println(tempString);


            Log.d("Read Json from file", "" + jsonData.length);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

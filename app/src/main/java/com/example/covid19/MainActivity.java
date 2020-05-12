package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ProgressDialog Dialog;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dialog  = new ProgressDialog(MainActivity.this);
        Dialog.setMessage("Fetching Appropriate data.... ");
        Dialog.show();
        TextView totaltext = (TextView) findViewById(R.id.totaltext);
    TextView totaltext2 =(TextView) findViewById(R.id.totaltext2);
    TextView totaltext3 =(TextView) findViewById(R.id.totaltext3);
    TextView totaltext5 =(TextView) findViewById(R.id.totaltext5);
    TextView totaltext6=(TextView) findViewById(R.id.totaltext6);
    button = (Button) findViewById(R.id.button);


    final TextView valuetotal = (TextView) findViewById(R.id.valueTotal);
    final TextView valueindian = (TextView) findViewById(R.id.valueconfirmIND);
    final TextView valuefrn = (TextView) findViewById(R.id.valueconfirmFRN);
    final TextView valuedischarge = (TextView) findViewById(R.id.valueDischarge);
    final TextView valuedeaths = (TextView) findViewById(R.id.valueDeaths);
   button.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Intent intent = new Intent(MainActivity.this, Main3Activity.class);
           startActivity(intent);       }
   });


    AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://api.rootnet.in/covid19-in/stats/latest", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header [] headers,JSONObject response)
            {
                String json = response.toString();
                Log.d("app", "onSuccess: " + json);
                try {
                    int Totalcases = response.getJSONObject("data").getJSONObject("summary").getInt("total");
                    int indians = response.getJSONObject("data").getJSONObject("summary").getInt("confirmedCasesIndian");
                    int forners = response.getJSONObject("data").getJSONObject("summary").getInt("confirmedCasesForeign");
                    int discharge = response.getJSONObject("data").getJSONObject("summary").getInt("discharged");
                    int death = response.getJSONObject("data").getJSONObject("summary").getInt("deaths");
                    int unknown = response.getJSONObject("data").getJSONObject("summary").getInt("confirmedButLocationUnidentified");
                    valuetotal.setText(String.valueOf(Totalcases));
                    valueindian.setText(String.valueOf(indians));
                    valuefrn.setText(String.valueOf(forners));
                    valuedischarge.setText(String.valueOf(discharge));
                    valuedeaths.setText(String.valueOf(death));
                    Dialog.dismiss();

                    JSONObject jsonresponse = new JSONObject(json);
                    JSONObject jsonObjectdata = jsonresponse.getJSONObject("data");
                    JSONArray jsonArray = jsonObjectdata.getJSONArray("regional");
                    final int max = jsonArray.length();
//
                    for (int i = 0; i < max; i++) {
                        JSONObject currentStatesResponse = jsonArray.getJSONObject(i);
                        String loca = currentStatesResponse.getString("loc");
                        int totalCase = currentStatesResponse.getInt("totalConfirmed");
//                        States states = new States(loca, totalCase);
//                        list.add(states);
                        Log.i("ajeet", "onSuccess: " +totalCase);
                    }


                        Log.d("app", "onSuccess: "+Totalcases);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
               //  called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("app", "Request fail! Status code: " + statusCode);
                Log.d("app", "Fail response: " + response);
                Log.e("app", e.toString());
            }
        });

    }

}

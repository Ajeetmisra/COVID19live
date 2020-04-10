package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView totaltext = (TextView) findViewById(R.id.totaltext);
    TextView totaltext2 =(TextView) findViewById(R.id.totaltext2);
    TextView totaltext3 =(TextView) findViewById(R.id.totaltext3);
    TextView totaltext5 =(TextView) findViewById(R.id.totaltext5);
    TextView totaltext6=(TextView) findViewById(R.id.totaltext6);
    TextView totaltext7 =(TextView) findViewById(R.id.totaltext7);

    final TextView valuetotal = (TextView) findViewById(R.id.valueTotal);
    final TextView valueindian = (TextView) findViewById(R.id.valueconfirmIND);
    final TextView valuefrn = (TextView) findViewById(R.id.valueconfirmFRN);
    final TextView valuedischarge = (TextView) findViewById(R.id.valueDischarge);
    final TextView valuedeaths = (TextView) findViewById(R.id.valueDeaths);
    final TextView valuenotfnd = (TextView) findViewById(R.id.valueNotfnd);

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
                    valuenotfnd.setText(String.valueOf(unknown));


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

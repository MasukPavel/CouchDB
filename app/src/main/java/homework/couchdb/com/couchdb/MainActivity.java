package homework.couchdb.com.couchdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends ActionBarActivity {
    EditText editId;
    static ArrayList<String> keys = new ArrayList<>();
    static ArrayList<String> values = new ArrayList<>();
    int i1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editId = (EditText) findViewById(R.id.editText);
    }

    public void get(View view) {
        EditText id = (EditText) findViewById(R.id.editText);
        final String url = "http://46.101.205.23:4444/test_db/" + id.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            JSONObject json = new JSONObject(response);
                            ArrayList<String> keys = new ArrayList<String>();
                            ArrayList<String> values = new ArrayList<String>();
                            Iterator<String> jsonKeys = json.keys();
                            while (jsonKeys.hasNext()) {
                                String key = jsonKeys.next();
                                keys.add(key);
                                values.add(json.getString(key));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Invalid ID!", Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
        Intent intent=new Intent(this, screen2.class);
        intent.putExtra("type","get");
        startActivity(intent);
    }

    public void create(View view) {
        keys.add("_id");
        keys.add("_rev");
        values.add("");
        values.add("");
    Intent intent = new Intent(MainActivity.this, screen2.class);
    intent.putExtra("type","create");
    MainActivity.this.

    startActivity(intent);

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

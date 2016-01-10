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
        // доделать!!!

        try {
            if (getIntent().getType().equals("afterCreate")) {
                ((EditText) findViewById(R.id.editText)).setText(getIntent().getStringExtra("id"));
            }
        }catch(NullPointerException n){

        }



        // ^
    }

    public void get(View view) {
        EditText id = (EditText) findViewById(R.id.editText);
        final String url = "http://46.101.205.23:4444/test_db/" + id.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        if(id.getText().toString().equals(""))
            Toast.makeText(this,"enter id",Toast.LENGTH_SHORT).show();
        else {
            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                Iterator<String> jsonKeys = json.keys();
                                while (jsonKeys.hasNext()) {
                                    String key = jsonKeys.next();
                                    keys.add(key);
                                    values.add(json.getString(key));
                                }

                                String temp = values.get(keys.indexOf("_id"));
                                values.set(keys.indexOf("_id"), values.get(0));
                                values.set(0, temp);
                                keys.set(keys.indexOf("_id"), keys.get(0));
                                keys.set(0, "_id");

                                temp = values.get(keys.indexOf("_rev"));
                                values.set(keys.indexOf("_rev"), values.get(1));
                                values.set(1, temp);
                                keys.set(keys.indexOf("_rev"), keys.get(1));
                                keys.set(1, "_rev");
                                Intent intent = new Intent(MainActivity.this, screen2.class);
                                intent.putStringArrayListExtra("keys", keys);
                                intent.putStringArrayListExtra("values", values);
                                intent.setType("get");
                                keys=new ArrayList<String>();
                                values=new ArrayList<String>();
                                finish();
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "incorrect id!", Toast.LENGTH_LONG).show();

                }
            });
            queue.add(stringRequest);

        }
    }

    public void create(View view) {
    Intent intent = new Intent(this, screen2.class);
    intent.setType("create");
    startActivity(intent);
        finish();

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

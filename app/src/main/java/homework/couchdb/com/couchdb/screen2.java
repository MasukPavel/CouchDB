package homework.couchdb.com.couchdb;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pasha on 29.10.15.
 */
public class screen2 extends ActionBarActivity {
    static int change=0;
    ListView list;
    ArrayList<String> keys=new ArrayList<>();
    ArrayList<String> values=new ArrayList<>();
    ArrayList<String> newKeys=new ArrayList<>();
    ArrayList<String> newValues=new ArrayList<>();
    LinearLayout linearLayout;

    public void addField(final int number,boolean isEmpty){
        final Context c=this;
        LayoutInflater lf=getLayoutInflater();
        final View item=lf.inflate(R.layout.item,linearLayout,false);
        final Context context=this;
        EditText key=(EditText)item.findViewById(R.id.editText2);
        key.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText key1=(EditText)v;
                TableRow tableRow=(TableRow)v.getParent();
                View item=(View)tableRow.getParent();
                EditText value1=(EditText)tableRow.getChildAt(1);
                if(key1.getText().toString().equals("") && value1.getText().toString().equals("") && linearLayout.indexOfChild(item)!=linearLayout.getChildCount()-1 && hasFocus==false){
                    linearLayout.removeView(item);
                }

                if(linearLayout.indexOfChild(item)==linearLayout.getChildCount()-1 && !key1.getText().toString().equals("")){
                    addField(0,true);
                }

                if(linearLayout.indexOfChild(item)==linearLayout.getChildCount()-1 && hasFocus==true){
                    addField(0,true);
                }
            }
        });
        EditText value=(EditText)item.findViewById(R.id.editText3);
        value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText value1=(EditText)v;
                TableRow tableRow=(TableRow)v.getParent();
                View item=(View)tableRow.getParent();
                EditText key1=(EditText)tableRow.getChildAt(0);
                if(key1.getText().toString().equals("") && value1.getText().toString().equals("") && linearLayout.indexOfChild(item)!=linearLayout.getChildCount()-1 && hasFocus==false){
                    linearLayout.removeView(item);
                }

                if(linearLayout.indexOfChild(item)==linearLayout.getChildCount()-1 && !value1.getText().toString().equals("")){
                    addField(0,true);
                }

                if(linearLayout.indexOfChild(item)==linearLayout.getChildCount()-1 && hasFocus==true){
                    addField(0,true);
                }
            }
        });
        if(isEmpty==false){
            key.setText(keys.get(number));
            value.setText(values.get(number));
        }
        item.getLayoutParams().width= ActionBar.LayoutParams.MATCH_PARENT;
        item.getLayoutParams().height= ActionBar.LayoutParams.WRAP_CONTENT;
        linearLayout.addView(item);

    }

    public void post(View view){



        final String url = "http://46.101.205.23:4444/test_db/";
        JSONObject json = new JSONObject();
        try {
            json.put("_id",((TextView)((TableRow)linearLayout.getChildAt(0)).getChildAt(1)).getText().toString());
            json.put("_rev",((TextView)((TableRow)linearLayout.getChildAt(1)).getChildAt(1)).getText().toString());
            for(int i=2;i<linearLayout.getChildCount();i++){
                json.put(((TextView)((TableRow)linearLayout.getChildAt(i)).getChildAt(0)).getText().toString(),((TextView)((TableRow)linearLayout.getChildAt(i)).getChildAt(1)).getText().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage() + " /er", Toast.LENGTH_SHORT).show();
            }

        });
        queue.add(jsonObjectRequest);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen2);
        linearLayout=(LinearLayout)findViewById(R.id.ll);
        int count=MainActivity.keys.size();

        for(int i=0;i<count;i++){
            keys.add(MainActivity.keys.get(i));
            values.add(MainActivity.values.get(i));
        }


        for(int i=0;i<count;i++){
            if(i<2){
                if(getIntent().getStringExtra("type").equals("get")) {
                    LayoutInflater lf = getLayoutInflater();
                    View firstitem = lf.inflate(R.layout.firstitem, linearLayout, false);
                    TextView key = (TextView) firstitem.findViewById(R.id.textView3);
                    TextView value = (TextView) firstitem.findViewById(R.id.textView4);
                    key.setText(keys.get(i));
                    value.setText(values.get(i));
                    firstitem.getLayoutParams().width = ActionBar.LayoutParams.MATCH_PARENT;
                    firstitem.getLayoutParams().height = ActionBar.LayoutParams.WRAP_CONTENT;
                    linearLayout.addView(firstitem);
                }else{
                    LayoutInflater lf = getLayoutInflater();
                    View firstitemcreate = lf.inflate(R.layout.firstitemcreate, linearLayout, false);
                    TextView key = (TextView) firstitemcreate.findViewById(R.id.textView6);
                    EditText value = (EditText) firstitemcreate.findViewById(R.id.editText5);
                    key.setText("_id");
                    value.setText("");
                    firstitemcreate.getLayoutParams().width = ActionBar.LayoutParams.MATCH_PARENT;
                    firstitemcreate.getLayoutParams().height = ActionBar.LayoutParams.WRAP_CONTENT;
                    linearLayout.addView(firstitemcreate);
                }
            }else {
                addField(i, false);
            }
        }addField(0, true);
    }





}

package homework.couchdb.com.couchdb;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pasha on 29.10.15.
 */
public class screen2 extends ActionBarActivity {
    static boolean change=true;
    ListView list;
    ArrayList<String> keys=new ArrayList<>();
    ArrayList<String> values=new ArrayList<>();
    ArrayList<String> imageNames=new ArrayList<String>();
    LinearLayout linearLayout;
    LinearLayout imgLayout;


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
        key.setWidth(this.getWindowManager().getDefaultDisplay().getWidth()/2);
        value.setWidth(this.getWindowManager().getDefaultDisplay().getWidth()/2);
        item.getLayoutParams().width= ActionBar.LayoutParams.MATCH_PARENT;
        item.getLayoutParams().height= ActionBar.LayoutParams.WRAP_CONTENT;
        if(!key.getText().toString().equals("_attachments"))
        linearLayout.addView(item);

    }




    public void post(View view){
        keys=new ArrayList<String>();
        values=new ArrayList<String>();
        final String url = "http://46.101.205.23:4444/test_db/";
        final JSONObject json = new JSONObject();
        try {
            if(change) {
                json.put("_id", ((TextView) (linearLayout.getChildAt(1).findViewById(R.id.textView4))).getText().toString());
            }else{
                if(!((EditText)(linearLayout.getChildAt(1).findViewById(R.id.editText5))).getText().toString().equals("")) {
                    json.put("_id", ((EditText) (linearLayout.getChildAt(1).findViewById(R.id.editText5))).getText().toString());
                }
            }

            if(change) {
                json.put("_rev", ((TextView) (linearLayout.getChildAt(2).findViewById(R.id.textView4))).getText().toString());
            }


            int i2=0;
            if(change){
                i2=2;
            }else{
                i2=1;
            }


            for(int i=i2;i<linearLayout.getChildCount()-1;i++){
                String s1=((EditText)linearLayout.getChildAt(i+1).findViewById(R.id.editText2)).getText().toString();
                String s2=((EditText)linearLayout.getChildAt(i+1).findViewById(R.id.editText3)).getText().toString();
                keys.add(s1);
                values.add(s2);
                if(!s1.equals(""))
                json.put(s1,s2);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(screen2.this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        if(imageNames.size()==0)
                        Toast.makeText(getApplicationContext(),"success!",Toast.LENGTH_SHORT).show();
                        if(!change) {
                            if (((EditText) (linearLayout.getChildAt(1).findViewById(R.id.editText5))).getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "id was generated automatically", Toast.LENGTH_SHORT).show();
                            }
                                String id = "";
                                try {
                                    id = jsonObject.getString("id").toString();
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.setType("afterCreate");
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        if(change) {
                            try {
                                ((TextView) linearLayout.getChildAt(2).findViewById(R.id.textView4)).setText(jsonObject.getString("rev"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(imageNames.size()!=0){
                            putImage(0,(((TextView)linearLayout.getChildAt(2).findViewById(R.id.textView4)).getText().toString()));
                        }




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"This id is already in use",Toast.LENGTH_LONG).show();


            }
        });
        queue.add(jsonObjectRequest);
    }

    public void putImage(int number,String rev){
        final int number2=number;
        String url="http://46.101.205.23:4444/test_db/"+((TextView) (linearLayout.getChildAt(1).findViewById(R.id.textView4))).getText().toString()
                +"/"+imageNames.get(number)+"?rev="+rev;
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading("+(number2+1)+"/"+imageNames.size()+")...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        try {
                            if(number2<imageNames.size()-1){
                                ((TextView)linearLayout.getChildAt(2).findViewById(R.id.textView4)).setText(new JSONObject(s).getString("rev"));
                                putImage(number2 + 1, new JSONObject(s).getString("rev"));
                            }else {
                                Toast.makeText(getApplicationContext(), "success!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        String url="http://46.101.205.23:4444/test_db/"+((TextView) (linearLayout.getChildAt(1).findViewById(R.id.textView4))).getText().toString();
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest str=new StringRequest(com.android.volley.Request.Method.GET,url,new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    ((TextView)linearLayout.getChildAt(2).findViewById(R.id.textView4)).setText(new JSONObject(s).getString("_rev"));
                                    if(number2<imageNames.size()-1)
                                        putImage(number2+1,new JSONObject(s).getString("_rev"));
                                    else
                                        Toast.makeText(getApplicationContext(),"success!",Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(getApplicationContext(),"bad",Toast.LENGTH_SHORT).show();

                            }
                        });
                        queue.add(str);

                    }
                }

        ){

            @Override
            public byte[] getBody() throws AuthFailureError {
                Bitmap bitmap=((BitmapDrawable)(((ImageView)(imgLayout.getChildAt(number2*3+1))).getDrawable())).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                return imageBytes;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen2);

        if(getIntent().getType().toString().equals("create")){
            change=false;

        }else{
            keys=getIntent().getStringArrayListExtra("keys");
            values=getIntent().getStringArrayListExtra("values");
            change=true;
        }

        linearLayout=(LinearLayout)findViewById(R.id.ll);
        imgLayout=(LinearLayout)findViewById(R.id.l2);
        int count=2;
        if(change)
        count=keys.size();


        for(int i=0;i<count;i++){
            if(i<2){
                if(change) {
                    LayoutInflater lf = getLayoutInflater();
                    View firstitem = lf.inflate(R.layout.firstitem, linearLayout, false);
                    TextView key = (TextView) firstitem.findViewById(R.id.textView3);
                    TextView value = (TextView) firstitem.findViewById(R.id.textView4);
                    key.setText(keys.get(i));
                    value.setText(values.get(i));
                    key.setWidth(this.getWindowManager().getDefaultDisplay().getWidth()/2);
                    value.setWidth(this.getWindowManager().getDefaultDisplay().getWidth()/2);
                    firstitem.getLayoutParams().width = ActionBar.LayoutParams.MATCH_PARENT;
                    firstitem.getLayoutParams().height = ActionBar.LayoutParams.WRAP_CONTENT;
                    linearLayout.addView(firstitem);
                }else{
                    if(i==0) {
                        LayoutInflater lf = getLayoutInflater();
                        View firstitemcreate = lf.inflate(R.layout.firstitemcreate, linearLayout, false);
                        TextView key = (TextView) firstitemcreate.findViewById(R.id.textView6);
                        EditText value = (EditText) firstitemcreate.findViewById(R.id.editText5);
                        if (i == 0)
                            key.setText("_id");
                        if (i == 1)
                            key.setText("_rev");
                        value.setText("");
                        key.setWidth(this.getWindowManager().getDefaultDisplay().getWidth()/2);
                        value.setWidth(this.getWindowManager().getDefaultDisplay().getWidth()/2);
                        firstitemcreate.getLayoutParams().width = ActionBar.LayoutParams.MATCH_PARENT;
                        firstitemcreate.getLayoutParams().height = ActionBar.LayoutParams.WRAP_CONTENT;
                        linearLayout.addView(firstitemcreate);
                    }
                }
            }else {
                if(change && !keys.get(i).equals("_attachments"))
                addField(i, false);
            }
        }addField(0, true);


        if (change) {
            int index = keys.indexOf("_attachments");
            if (index != -1) {

                String attachments = values.get(index);
                try {
                    for (int i = 0; i < new JSONObject(attachments).names().length(); i++) {
                        final ProgressDialog loading = ProgressDialog.show(this,"Downloading("+(i+1)+"/"+new JSONObject(attachments).names().length()+")...","Please wait...",false,false);
                        String title = new JSONObject(attachments).names().get(i).toString();
                        imageNames.add(title);
                        RequestQueue queue = Volley.newRequestQueue(this);
                        final URL url = new URL("http://46.101.205.23:4444/test_db/" + ((TextView)(linearLayout.getChildAt(1).findViewById(R.id.textView4))).getText().toString() + "/" + title);
                        final ImageView imgView = new ImageView(screen2.this);
                        ImageRequest iq = new ImageRequest(url.toString(),
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        loading.dismiss();
                                        imgView.setImageBitmap(bitmap);
                                        imgView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 750));
                                        final TableRow tr=new TableRow(getApplicationContext());
                                        tr.addView(new TextView(getApplicationContext()));
                                        final Button remove=new Button(getApplicationContext());
                                        remove.setText("Remove");
                                        remove.setBackgroundResource(R.color.removeButtonColor);
                                        remove.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                imgLayout.removeView(imgView);
                                                imgLayout.removeView(tr);
                                                imgLayout.removeView(remove);
                                                imageNames.remove(imgLayout.indexOfChild(remove)/3);
                                            }
                                        });
                                        imgLayout.addView(imgView);
                                        imgLayout.addView(remove);
                                        imgLayout.addView(tr);
                                    }
                                }, 0, 0, null,
                                new Response.ErrorListener() {
                                    public void onErrorResponse(VolleyError error) {
                                        loading.dismiss();
                                        Toast.makeText(getApplicationContext(), error.getMessage()+" error", Toast.LENGTH_SHORT);
                                    }
                                });
                        queue.add(iq);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
}






    public void SelectImage(View v){
        if(change) {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, 1);
        }else{
            Toast.makeText(this,"First, create a repository, and then upload the picture",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            imageNames.add(new File(picturePath).getName());
            cursor.close();
            final ImageView image;
            image=new ImageView(this);
            image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,750));
            image.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            final TableRow tr=new TableRow(getApplicationContext());
            tr.addView(new TextView(getApplicationContext()));
            final Button remove=new Button(getApplicationContext());
            remove.setText("Remove");
            remove.setBackgroundResource(R.color.removeButtonColor);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgLayout.removeView(image);
                    imgLayout.removeView(tr);
                    imgLayout.removeView(remove);
                    imageNames.remove(imgLayout.indexOfChild(remove)/3);
                }
            });
            imgLayout.addView(image);
            imgLayout.addView(remove);
            imgLayout.addView(tr);

        }


    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"press Cancel if you want to go back",Toast.LENGTH_SHORT).show();
    }

    public void cancel(View v){
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }


}

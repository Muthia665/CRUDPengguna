package com.example.user.crudpengguna;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<ModelPengguna> data = new ArrayList<>() ;
    AQuery aQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView =findViewById(R.id.ListView);
        aQuery = new AQuery(MainActivity.this);

        getDataPengguna();
        //di alt enter pilih yang pertama

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogInsert = new Dialog(MainActivity.this);
                dialogInsert.setContentView(R.layout.dialog_insert);

                final EditText addnama = dialogInsert.findViewById(R.id.insertnama);
                final EditText addalamat = dialogInsert.findViewById(R.id.insertalamat);
                final EditText addhp = dialogInsert.findViewById(R.id.inserthp);
                Button btnTambah = dialogInsert.findViewById(R.id.btntambah);

                btnTambah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String getNama = addnama.getText().toString();
                        String getAlamat = addalamat.getText().toString();
                        String getHp = addhp.getText().toString();
                        
                        ActionSend(getNama, getAlamat, getHp);//alt enter
                        
                        dialogInsert.dismiss();
                    }
                });
                dialogInsert.show();

            }
        });
    }

    private void ActionSend(String getNama, String getAlamat, String getHp) {
        String url = "http://192.168.100.4/ServerPengguna/index.php/api/insert_pengguna";

        HashMap<String,String> map = new HashMap<>();
        map.put("nama_pengguna", getNama);
        map.put("alamat_pengguna", getAlamat);
        map.put("hp_pengguna", getHp);

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading . . .");

        aQuery.progress(progressDialog).ajax(url, map, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                    if (object != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(object);
                            String pesan = jsonObject.getString("pesan");
                            String result = jsonObject.getString("sukses");

                            if (result.equalsIgnoreCase("true")) {
                                Toast.makeText(MainActivity.this, pesan, Toast.LENGTH_SHORT);
                                //refresh
                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            }
        });
    }

    private void getDataPengguna() {
        String url = "http://192.168.100.4/ServerPengguna/index.php/api/get_pengguna";
        //serverpengguna = folder di htdocs
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading . . .");

        aQuery.progress(progressDialog).ajax(url, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                Log.d("data pengguna", object);
                if (object != null){
                    try {

                        JSONObject jsonObject = new JSONObject(object);//alt enter pilih yang pertama
                        String result = jsonObject.getString("sukses");
                        String pesan = jsonObject.getString("pesan");

                        if (result.equalsIgnoreCase("true")){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject json = jsonArray.getJSONObject(i);

                                ModelPengguna mp = new ModelPengguna();
                                mp.setId_pengguna(json.getString("id_pengguna"));
                                mp.setNama_pengguna(json.getString("nama_pengguna"));
                                mp.setAlamat_pengguna(json.getString("alamat_pengguna"));
                                mp.setHp_pengguna(json.getString("hp_pengguna"));

                                data.add(mp);

                                setListView(data);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "gagal mengambil data", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setListView(final ArrayList<ModelPengguna> data) {
        ListViewAdapter adapter = new ListViewAdapter(this,data);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialogEdit = new Dialog(MainActivity.this);
                dialogEdit.setContentView(R.layout.dialog_edit);

                final EditText editid = dialogEdit.findViewById(R.id.editid);
                final EditText editnama = dialogEdit.findViewById(R.id.editnama);
                final EditText editalamat = dialogEdit.findViewById(R.id.editalamat);
                final EditText edithp = dialogEdit.findViewById(R.id.edithp);
                Button btnHapus = dialogEdit.findViewById(R.id.btnHapus);
                Button btnEdit = dialogEdit.findViewById(R.id.btnEdit);

                ModelPengguna modelPengguna = data.get(position);
                editid.setText(modelPengguna.getId_pengguna());
                editnama.setText(modelPengguna.getNama_pengguna());
                editalamat.setText(modelPengguna.getAlamat_pengguna());
                edithp.setText(modelPengguna.getHp_pengguna());

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String getId = editid.getText().toString();
                        String getNama = editnama.getText().toString();
                        String getAlamat = editalamat.getText().toString();
                        String getHp = edithp.getText().toString();

                        ActionEdit(getId, getNama, getAlamat,getHp);//alt enter
                        dialogEdit.dismiss();
                    }
                });

                btnHapus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String getId = editid.getText().toString();

                        ActionHapus(getId);// alt enter

                        dialogEdit.dismiss();
                    }
                });

                dialogEdit.show();
            }
        });
    }

    private void ActionHapus(String getId) {
        String url = "http://192.168.100.4/ServerPengguna/index.php/api/hapus_pengguna/"+getId;

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading . . .");

        aQuery.progress(progressDialog).ajax(url, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(object); //alt enter
                        String pesan = jsonObject.getString("pesan");
                        String result = jsonObject.getString("sukses");

                        if (result.equalsIgnoreCase("true")){
                            Toast.makeText(MainActivity.this, pesan, Toast.LENGTH_SHORT).show();
                            //refresh
                            startActivity(new Intent(MainActivity.this,MainActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void ActionEdit(String getId, String getNama, String getAlamat, String getHp) {
        String url = "http://192.168.100.4/ServerPengguna/index.php/api/update_pengguna";

        HashMap<String, String> map = new HashMap<>();
        map.put("id_pengguna", getId);
        map.put("nama_pengguna", getNama);
        map.put("alamat_pengguna", getAlamat);
        map.put("hp_pengguna", getHp);

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading . . .");
        aQuery.progress(progressDialog).ajax(url, map, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object != null){
                    try {

                        JSONObject jsonObject = new JSONObject(object);//alt enter add...
                        String pesan = jsonObject.getString("pesan");
                        String result =jsonObject.getString("sukses");

                        if (result.equalsIgnoreCase("true")){
                            Toast.makeText(MainActivity.this, pesan, Toast.LENGTH_SHORT).show();
                            //refresh
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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

    private class ListViewAdapter extends BaseAdapter {
        Activity activity;
        ArrayList<ModelPengguna> data;
        //generate constructor = pilih dua-duanya > ok

        public ListViewAdapter(Activity activity, ArrayList<ModelPengguna> data) {
            this.activity = activity;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem, null);

            TextView txtnama = convertView.findViewById(R.id.txtnama);
            TextView txtalamat = convertView.findViewById(R.id.txtalamat);
            TextView txthp = convertView.findViewById(R.id.txthp);

            ModelPengguna modelPengguna = data.get(position);
            txtnama.setText(modelPengguna.getNama_pengguna());
            txtalamat.setText(modelPengguna.getAlamat_pengguna());
            txthp.setText(modelPengguna.getHp_pengguna());

            return convertView;
        }
    }
}

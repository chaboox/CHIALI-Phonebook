package com.gsha.annuairegsh.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gsha.annuairegsh.Adapter.ContactAdapter;
import com.gsha.annuairegsh.Adapter.GridViewAdapter;
import com.gsha.annuairegsh.Manager.API_Manager;
import com.gsha.annuairegsh.Manager.AnimationManager;
import com.gsha.annuairegsh.Manager.MyPreferences;
import com.gsha.annuairegsh.Manager.RealmManager;
import com.gsha.annuairegsh.Model.Company;
import com.gsha.annuairegsh.Model.Constant;
import com.gsha.annuairegsh.Model.Contact;
import com.gsha.annuairegsh.R;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthUtil;

import static com.gsha.annuairegsh.Manager.PictureDecodeManager.decodeSampleBitmap;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    RealmResults<Company> companies;
    ArrayList<Contact> contacts;
    private String TAG = "HOME";
    private LinearLayout myrv;
    private RecyclerView contactsView;
    public static Handler handler;
    private String company;
    private String city;
    private EditText search;
    private ContactAdapter adapter;
    private ArrayList<String> picsHome ;

    private FastScrollRecyclerView recyclerView;
    private ImageView settingButton;
    private ProgressDialog progressDialog;
    private CardView cardTubes, cardAcademy, cardGroupe, cardServices, cardProfi, cardNawafid, cardTrading, cardAltim, cardHuile;
    private TextView name1, name2, name3, job1, job2, job3, id1, id2, id3;
    private ImageView img1, img2, img3;
    private CardView crd1, crd2, crd3;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_home2);


        Realm.init(getApplicationContext());
        initView();
        setListener();

       // search.setText("");
        contactsView.setVisibility(View.GONE);

            populateFromAd();
        try {
            initContactAdapter();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //populateCompany();


        if(!RealmManager.areContactsInCache()){
            progressDialog.show();
            handler.sendEmptyMessage(Constant.COMPANY);
        }
        else  if(!MyPreferences.getMyBool(getApplicationContext(),"HOMESEEN", false))
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {

                            handler.sendEmptyMessage(Constant.SEARCH_EXPLAIN);
                            MyPreferences.saveMyBool(getApplicationContext(), "HOMESEEN", true);


                        }
                    },
                    1000
            );
        populateRecent();
        checkUpdate();
        //Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        //startActivity(intent);
        //populateContact();
       // testFunction();
    }

    private void testFunction() {

        Account[] accounts = null;
        final List<String> l = new ArrayList();
        final List<String> listOfAccountTypes = new ArrayList();
        String possibleEmails = "" + "\n**************** Get All Registered Accounts *****************\n";
        if (!(AccountManager.get(HomeActivity.this) == null || AccountManager.get(HomeActivity.this).getAccounts() == null)) {
            accounts = AccountManager.get(HomeActivity.this).getAccounts();
        }
        l.clear();
        l.add("Téléphone");
        listOfAccountTypes.clear();
        listOfAccountTypes.add("Téléphone");
        String email_pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        for (Account account : accounts) {
            if (pattern.matcher(account.name).matches() && account.type.equalsIgnoreCase(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE)) {
                listOfAccountTypes.add(account.type);
                l.add(account.name);
                possibleEmails = possibleEmails + " --> " + account.name + " : " + account.type + " , \n";
            }
        }
        l.add("Fichier CSV ou vCard.");
        listOfAccountTypes.add("CSV_OR_vCARD");
        Log.d(TAG, "OMARUS " + l.toString());
    }

    private void populateRecent(){
        String cash1 = MyPreferences.getMyString(getApplicationContext(),"cash1","");
        String cash2 = MyPreferences.getMyString(getApplicationContext(),"cash2","");
        String cash3 = MyPreferences.getMyString(getApplicationContext(),"cash3","");
        if (cash1.length()>0) {
            Contact contact = RealmManager.getContactbyId(cash1);
            name1.setText(contact.getName());
            job1.setText(contact.getDescription());
            id1.setText(contact.getId());
            String pic =  contact.getPictureC();
            Bitmap bitmap = null;
            if(pic != null){
                if(!pic.equals("null")) {
                    bitmap = decodeSampleBitmap(Base64.decode(pic, Base64.DEFAULT), 60, 60);
                    img1.setImageBitmap(bitmap);
                }
                else {
                    img1.setImageResource(getResources().getIdentifier("user", "drawable", getPackageName()));
                }}
            else {
                img1.setImageResource(getResources().getIdentifier("user", "drawable", getPackageName()));
            }
        }
        if(cash2.length()>0){
            Contact contact2 = RealmManager.getContactbyId(cash2);
            name2.setText(contact2.getName());
            job2.setText(contact2.getDescription());
            id2.setText(contact2.getId());
            String pic =  contact2.getPictureC();
            Bitmap bitmap = null;
            if(pic != null){
                if(!pic.equals("null")) {
                    bitmap = decodeSampleBitmap(Base64.decode(pic, Base64.DEFAULT), 60, 60);
                    img2.setImageBitmap(bitmap);
                }
                else {
                    img2.setImageResource(getResources().getIdentifier("user", "drawable", getPackageName()));
                }}
            else {
                img2.setImageResource(getResources().getIdentifier("user", "drawable", getPackageName()));
            }
        }

        if(cash3.length()>0){
            Contact contact3 = RealmManager.getContactbyId(cash3);
            name3.setText(contact3.getName());
            job3.setText(contact3.getDescription());
            id3.setText(contact3.getId());
            String pic =  contact3.getPictureC();
            Bitmap bitmap = null;
            if(pic != null){
                if(!pic.equals("null") ) {
                    bitmap = decodeSampleBitmap(Base64.decode(pic, Base64.DEFAULT), 60, 60);
                    img3.setImageBitmap(bitmap);
                }
                else {
                    img3.setImageResource(getResources().getIdentifier("user", "drawable", getPackageName()));
                }}
            else {
                img3.setImageResource(getResources().getIdentifier("user", "drawable", getPackageName()));
            }
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause:KLK ");

        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop:KLK");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: KLK");
        super.onDestroy();
    }

    private void checkUpdate() {
        if((Integer.valueOf(MyPreferences.getMyString(getApplicationContext(), "sync_frequency", "3")) != -1)){
        long lastUpdate = MyPreferences.getMyLong(getApplicationContext(), Constant.LAST_UPDATE_TIME, System.currentTimeMillis());
       // long lastUpdate = 1551920483000L;
        long frequence = Integer.valueOf(MyPreferences.getMyString(getApplicationContext(), "sync_frequency", "3")) * 86400000;
        Log.d(TAG, "checkUpdate: " + frequence + "   :  " + System.currentTimeMillis() + " L   " + lastUpdate);
        if((lastUpdate + frequence) <= System.currentTimeMillis()){
            Log.d(TAG, "checkUpdate: WORKINGG !!!");
            handler.sendEmptyMessage(Constant.COMPANY);
        }
        }
    }

    private void setListener() {
        cardTubes.setOnClickListener(this);
        cardAcademy.setOnClickListener(this);
        cardGroupe.setOnClickListener(this);
        cardServices.setOnClickListener(this);
        cardProfi.setOnClickListener(this);
        cardNawafid.setOnClickListener(this);
        cardTrading.setOnClickListener(this);
        cardAltim.setOnClickListener(this);
        cardHuile.setOnClickListener(this);
        cardNawafid.setOnClickListener(this);

        crd1.setOnClickListener(this);
        crd2.setOnClickListener(this);
        crd3.setOnClickListener(this);

    }

    private void initContactAdapter() throws UnsupportedEncodingException {

        EditText editText = findViewById(R.id.search);
        try {
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_gray_24dp, 0, 0, 0);
        }
        catch (Exception e){
            Log.d(TAG, "initContactAdapter: ");
        }
       // ListContact listContact = (ListContact) getIntent().getSerializableExtra("contacts");
         recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new ContactAdapter(getApplicationContext(), contacts, this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.list_divider));
        recyclerView.addItemDecoration(itemDecoration);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},2);
        }


    }


    private void initView(){
        //contacts = new ArrayList<>();
        contacts = RealmManager.getContactsByName("Adam2");
        myrv = findViewById(R.id.recyclerview_id);
        settingButton = findViewById(R.id.setting_ics);
        handler = new HandlerHome();

        cardTubes = findViewById(R.id.cardview_tubes);
        cardAcademy = findViewById(R.id.cardview_academy);
        cardGroupe = findViewById(R.id.cardview_groupe);
        cardServices = findViewById(R.id.cardview_service);
        cardProfi = findViewById(R.id.cardview_profi);
        cardNawafid = findViewById(R.id.cardview_nawafid);
        cardTrading = findViewById(R.id.cardview_trading);
        cardAltim = findViewById(R.id.cardview_altim);
        cardHuile = findViewById(R.id.cardview_huile);

        crd1 = findViewById(R.id.cardview_r1);
        crd2 = findViewById(R.id.cardview_r2);
        crd3 = findViewById(R.id.cardview_r3);



        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        name3 = findViewById(R.id.name3);
        job1 = findViewById(R.id.job1);
        job2 = findViewById(R.id.job2);
        job3 = findViewById(R.id.job3);
        img1 = findViewById(R.id.image1);
        img2 = findViewById(R.id.image2);
        img3 = findViewById(R.id.image3);
        id1 = findViewById(R.id.id1);
        id2 = findViewById(R.id.id2);
        id3 = findViewById(R.id.id3);

        search = findViewById(R.id.search);
        contactsView = findViewById(R.id.recycler);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Synchronisation des contacts");
        progressDialog.setMessage("Patientez un instant...");




        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  NotifyUser("YO", "Message Bla bla bla", 32)
               // MyPreferences.deletePreference(Constant.SECRET);
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
              //  finish();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().length() == 0){

                    AnimationManager.setToInvisibleRight(contactsView);
                       AnimationManager.SetToVisibleLeft(myrv);
                }
                else if(s.toString().length() >  0 && (myrv.getVisibility() == View.VISIBLE )){

                    AnimationManager.SetToVisibleRight(contactsView);


                        AnimationManager.setToInvisibleLeft(myrv);
                }

                if(s.toString().length() == 0) {
                    contacts = null;
                    try {
                        adapter = new ContactAdapter(getApplicationContext(), new ArrayList<Contact>(), activity);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
                else {
                    contacts = RealmManager.getContactsByName(s.toString());
                    //API_Manager.getPicById(contacts.get(0).getId(), getApplicationContext());
                    try {
                        adapter = new ContactAdapter(getApplicationContext(), contacts, activity);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
      //  myrv.setNestedScrollingEnabled(false);
    }


    public void populateFromAd(){

       // companies = new ArrayList<>();

        //myrv.setVisibility(View.VISIBLE);

        //companies = RealmManager.getCompanies();
       // GridViewAdapter myAdapter = new GridViewAdapter(HomeActivity.this,companies, activity);
        //myrv.setLayoutManager(new GridLayoutManager(HomeActivity.this,3));
        // myAdapter.setHasStableIds(true);
       // myrv.setAdapter(myAdapter);


    }

    public void populateContact(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        /*List<KeyValuePair> params = new ArrayList<>();
        params.add(new KeyValuePair("name","a"));
        params.add(new KeyValuePair("number","1000"));*/
        String url = Constant.API_URL +  "/allContacts";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: " + response.toString());
                byte[] b4 = null;
                try {
                    for(int i = 0; i < response.length(); i++){
                    JSONObject jsonObject = (JSONObject) response.get(i);
                    String name = jsonObject.getString("name");
                    contacts.add(new Contact(jsonObject));
                    }
                   // CSVManager.saveContactsInCSV(contacts);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardview_tubes:
                Intent intent = new Intent(getApplicationContext(), CityActivity.class);
                intent.putExtra("company","CHIALI TUBES");
                // mContext.startActivity(intent);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);

                break;
            case R.id.cardview_academy:
                 intent = new Intent(getApplicationContext(), CityActivity.class);
                intent.putExtra("company","CHIALI ACADEMIE");
                // mContext.startActivity(intent);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
                break;
            case R.id.cardview_groupe:
                intent = new Intent(getApplicationContext(), CityActivity.class);
                intent.putExtra("company","GROUPE CHIALI");
                // mContext.startActivity(intent);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
                break;
            case R.id.cardview_service:
                intent = new Intent(getApplicationContext(), CityActivity.class);
                intent.putExtra("company","CHIALI SERVICES");
                // mContext.startActivity(intent);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
                break;
            case R.id.cardview_profi:
                intent = new Intent(getApplicationContext(), CityActivity.class);
                intent.putExtra("company","CHIALI PROFIPLAST");
                // mContext.startActivity(intent);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
                break;

            case R.id.cardview_nawafid:
                intent = new Intent(getApplicationContext(), CityActivity.class);
                intent.putExtra("company","CHIALI NAWAFID");
                // mContext.startActivity(intent);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
                break;
            case R.id.cardview_trading:
                intent = new Intent(getApplicationContext(), CityActivity.class);
                intent.putExtra("company","CHIALI TRADING");
                // mContext.startActivity(intent);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
                break;
            case R.id.cardview_altim:
                intent = new Intent(getApplicationContext(), CityActivity.class);
                intent.putExtra("company","ALTIM");
                // mContext.startActivity(intent);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
                break;
            case R.id.cardview_huile:
                intent = new Intent(getApplicationContext(), CityActivity.class);
                intent.putExtra("company","HUILERIE");
                // mContext.startActivity(intent);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
                break;

            case R.id.cardview_r1:
                 intent = new Intent(getApplicationContext(), ContactDetailActivity.class);
                //intent.putExtra("contact", mData.get(position));
                intent.putExtra("id", id1.getText().toString() );
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //mContext.startActivity(intent);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
                break;

            case R.id.cardview_r2:
                intent = new Intent(getApplicationContext(), ContactDetailActivity.class);
                //intent.putExtra("contact", mData.get(position));
                intent.putExtra("id", id2.getText().toString() );
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //mContext.startActivity(intent);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
                break;

            case R.id.cardview_r3:
                intent = new Intent(getApplicationContext(), ContactDetailActivity.class);
                //intent.putExtra("contact", mData.get(position));
                intent.putExtra("id", id3.getText().toString() );
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //mContext.startActivity(intent);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
                break;




        }
    }



    /*public class HandlerHome extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.GET_CITY:
                    company = (String)msg.obj;
                    API_Manager.getCity((String)msg.obj, HomeActivity.this, handler);
                    break;
                case Constant.CITY:
                    ArrayList<City> cities = (ArrayList<City>)msg.obj;
                    if(cities.size() > 1){
                        Intent intent = new Intent(HomeActivity.this, CityActivity.class);
                        intent.putExtra("cities",new ListCity(cities));
                        intent.putExtra("company", company);
                        startActivity(intent);
                    }
                    else if(cities.size() == 1){
                        city = cities.get(0).getCode();
                  //    API_Manager.getDepartement(company, cities.get(0).getCode(), getApplicationContext(), handler);

                    }
                    else {
                        Toast.makeText(HomeActivity.this, "Erreur!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("CITY", "handleMessage: " + cities.get(0).getName());
                    break;
                case Constant.DEPARTMENT:
                    ArrayList<Department> departments = (ArrayList<Department>)msg.obj;

                    Intent intent = new Intent(HomeActivity.this, DepartmentActivity.class);
                    ListDepartment listDepartment = new ListDepartment(departments);
                    intent.putExtra("departments", listDepartment);
                    intent.putExtra("company", company);
                    intent.putExtra("city", city);
                    startActivity(intent);
                    break;
            }
        }
    }
*/

    public class HandlerHome extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SEARCH_EXPLAIN:
                    new MaterialTapTargetPrompt.Builder(HomeActivity.this)
                            .setTarget(R.id.search)
                            .setPrimaryText("Rechercher un collaborateur")
                            .setSecondaryText("Possibilité de rechercher par nom, numéro et poste")
                            .setPromptBackground(new RectanglePromptBackground())
                            .setPromptFocal(new RectanglePromptFocal())
                            .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                            {
                                @Override
                                public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                                {
                                    if (state == MaterialTapTargetPrompt.STATE_BACK_BUTTON_PRESSED)
                                    {
                                        Log.d(TAG, "onPromptStateChanged: OLOL");
                                    }
                                    else if(state == MaterialTapTargetPrompt.STATE_DISMISSED){
                                        new MaterialTapTargetPrompt.Builder(HomeActivity.this)
                                                .setTarget(R.id.setting_ics)
                                                .setPrimaryText("Paramètre")
                                                .setSecondaryText("Choix de l'affichage\nSynchronisation des contacts\nNotification\nDéconnexion")
                                                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                                                {
                                                    @Override
                                                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                                                    {
                                                        if (state == MaterialTapTargetPrompt.STATE_BACK_BUTTON_PRESSED)
                                                        {
                                                            Log.d(TAG, "onPromptStateChanged: OLOL");
                                                        }
                                                        else if(state == MaterialTapTargetPrompt.STATE_DISMISSED){
                                                            Log.d(TAG, "onPromptStateChanged: OLOL2");
                                                        }
                                                        else if(state == MaterialTapTargetPrompt.STATE_FINISHED){
                                                            Log.d(TAG, "onPromptStateChanged: OLOL3");
                                                        }
                                                        else if(state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED){
                                                            Log.d(TAG, "onPromptStateChanged: OLOL4");
                                                        }
                                                        else {
                                                            Log.d(TAG, "onPromptStateChanged: OLOL5");
                                                        }
                                                    }
                                                })
                                                .show();
                                        Log.d(TAG, "onPromptStateChanged: OLOL2");
                                    }
                                    else if(state == MaterialTapTargetPrompt.STATE_FINISHED){
                                        Log.d(TAG, "onPromptStateChanged: OLOL3");
                                    }
                                    else if(state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED){
                                        new MaterialTapTargetPrompt.Builder(HomeActivity.this)
                                                .setTarget(R.id.setting_ics)
                                                .setPrimaryText("Paramètre")
                                                .setSecondaryText("Choix de l'affichage\nSynchronisation des contacts\nNotification\nDéconnexion")
                                                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                                                {
                                                    @Override
                                                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                                                    {
                                                        if (state == MaterialTapTargetPrompt.STATE_BACK_BUTTON_PRESSED)
                                                        {
                                                            Log.d(TAG, "onPromptStateChanged: OLOL");
                                                        }
                                                        else if(state == MaterialTapTargetPrompt.STATE_DISMISSED){
                                                            Log.d(TAG, "onPromptStateChanged: OLOL2");
                                                        }
                                                        else if(state == MaterialTapTargetPrompt.STATE_FINISHED){
                                                            Log.d(TAG, "onPromptStateChanged: OLOL3");
                                                        }
                                                        else if(state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED){
                                                            Log.d(TAG, "onPromptStateChanged: OLOL4");
                                                        }
                                                        else {
                                                            Log.d(TAG, "onPromptStateChanged: OLOL5");
                                                        }
                                                    }
                                                })
                                                .show();
                                    }
                                    else {
                                        Log.d(TAG, "onPromptStateChanged: OLOL5");
                                    }
                                }
                            })
                            .show();
                    break;
                case Constant.SETTING_SYNC:
                    progressDialog.show();
                    handler.sendEmptyMessage(Constant.COMPANY);
                    break;

                case Constant.COMPANY:
                    API_Manager.Syncro(getApplicationContext(), handler, Constant.CONTACT);
                    break;
                case Constant.CONTACT:
                    API_Manager.getAllContacts(getApplicationContext(), handler, Constant.CITY);
                    break;

                case Constant.CITY:
                    new RealmManager().PopulateCityIntoCompany(handler, Constant.DEPARTMENT);
                    break;

                case Constant.DEPARTMENT:
                    new RealmManager().populateDepartmentIntoCity(handler, Constant.DELETE_CONTACT);
                    break;

                case Constant.DELETE_CONTACT:
                    API_Manager.deleteContact(getApplicationContext(), handler, Constant.CONTACT_FETCH);

                        search.setText("");
                        populateFromAd();

                    Log.d(TAG, "handleMessagePROG: "+ progressDialog.isShowing());
                    if(progressDialog.isShowing())
                    progressDialog.dismiss();
                    if(!MyPreferences.getMyBool(getApplicationContext(),"HOMESEEN", false))
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {

                                        handler.sendEmptyMessage(Constant.SEARCH_EXPLAIN);
                                        MyPreferences.saveMyBool(getApplicationContext(), "HOMESEEN", true);


                                    }
                                },
                                1000
                        );
                    MyPreferences.saveLong(Constant.LAST_UPDATE_TIME, System.currentTimeMillis());
                    break;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        activity = this;


            populateFromAd();
            populateRecent();

            //Glide.with(mContext).load(picId).into(holder.imageC);



    }

    @Override
    public void onBackPressed() {
        if(search.getText().length() == 0){

          super.onBackPressed();
        }
        else search.setText("");
    }

    private Toolbar createToolbar() {
        Toolbar toolbar = new Toolbar(this);
        Toolbar.LayoutParams toolBarParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.MATCH_PARENT,
                R.attr.actionBarSize
        );
        toolbar.setLayoutParams(toolBarParams);
        // toolbar.setBackgroundColor(Color.BLUE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setBackground(getDrawable(R.drawable.blue_100_600x600));
        }
        //   toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay);
        toolbar.setVisibility(View.VISIBLE);
        return toolbar;
    }









}

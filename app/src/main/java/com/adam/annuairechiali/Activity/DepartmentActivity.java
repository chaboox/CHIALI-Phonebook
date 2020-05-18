package com.adam.annuairechiali.Activity;

import android.os.Build;
import android.os.Handler;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adam.annuairechiali.Adapter.DepartmentAdapter;
import com.adam.annuairechiali.Manager.MyPreferences;
import com.adam.annuairechiali.Manager.RealmManager;
import com.adam.annuairechiali.Model.City;
import com.adam.annuairechiali.Model.Company;
import com.adam.annuairechiali.Model.Constant;
import com.adam.annuairechiali.R;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

public class DepartmentActivity extends BaseSwipeBackActivity {
    private ListView listView;
    public static String company;
    public static Company companyR;
    public static City city;
    private String idCity;
    public static Handler handler;
    private ImageView back;
    private TextView companyT, dep;
    private RelativeLayout relativeLayout;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_department;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //setContentView(R.layout.activity_department);
        initView();
        company = getIntent().getStringExtra("company");
        idCity = getIntent().getStringExtra("id");
        city = RealmManager.getCityById(idCity);
        //city = getIntent().getStringExtra("city");
        // ListDepartment departments = (ListDepartment) getIntent().getSerializableExtra("departments");

        companyR = RealmManager.getCompanyByCode(company);
        DepartmentAdapter adapter = new DepartmentAdapter(companyR.getDepartments(), getApplicationContext(), this);
        companyT.setText(companyR.getNameAD());

        listView.setAdapter(adapter);

        if(!MyPreferences.getMyBool(getApplicationContext(),"DEP", false)){
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {

                            handler.sendEmptyMessage(Constant.SEARCH_EXPLAIN);
                            MyPreferences.saveMyBool(getApplicationContext(), "DEP", true);


                        }
                    },
                    1000
            );}
        //  listView.setFastScrollEnabled(true);

    }

    private void initView(){
        companyT = findViewById(R.id.company_title);
        listView = findViewById(R.id.listview);
        handler = new HandlerDep();
        back = findViewById(R.id.back);
        dep = findViewById(R.id.search);
        relativeLayout = findViewById(R.id.re);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_out_right, R.anim.fade_right_finish);
            }
        });

       // handler = new HandlerDepartment();
    }

   /* public class HandlerDepartment extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.CONTACT_FETCH:
                    String department= (String) msg.obj;
                    API_Manager.getContactsByDepartment(company, city, department, getApplicationContext(), handler);
                    break;
                case Constant.CONTACT:
                    ArrayList<Contact> contacts= (ArrayList<Contact>)msg.obj;

                    Intent intent = new Intent(DepartmentActivity.this, ListContactActivity.class);
                    ListContact listContact= new ListContact(contacts);
                    intent.putExtra("contacts", listContact);
                    startActivity(intent);
                    break;
            }
        }}*/

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: BACKK");
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_out_right, R.anim.fade_right_finish);
    }
    public class HandlerDep extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SEARCH_EXPLAIN:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        new MaterialTapTargetPrompt.Builder(DepartmentActivity.this)
                                .setTarget(R.id.rel)
                                .setFocalColour(getColor(R.color.transparent_white))
                                .setPrimaryText("Mailing")
                                .setSecondaryText("un clic long sur un departement permet d'utiliser la fonction mailing")
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
                    break;}}}

}

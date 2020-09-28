package com.adam.annuairechiali.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import io.realm.Realm;

import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adam.annuairechiali.Manager.ContactManager;
import com.adam.annuairechiali.Manager.MyPreferences;
import com.adam.annuairechiali.Manager.RealmManager;
import com.adam.annuairechiali.Model.Constant;
import com.adam.annuairechiali.Model.Contact;
import com.adam.annuairechiali.R;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {


    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Handler handler;
    private static ActionBar actionBar;
    private static Context context;
    private static Activity activity;
    private static Activity activitySet;
    private static long count;
   // private static int  activityCount =0;
    private static ProgressDialog progressDialog;
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private static void bindPreferenceSummaryToValueBool(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), false));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("TAGOU", "onCreate: ");
        //activityCount++;
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        activity = SettingsActivity.this;
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        //setSupportActionBar(createToolbar());
         actionBar = getSupportActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar.setBackgroundDrawable(getDrawable(R.drawable.blue_100_600x600));
            actionBar.setIcon(R.drawable.ic_arrow_back_white_24dp);
            TextView textview = new TextView(SettingsActivity.this);
             RelativeLayout.LayoutParams layoutparams;
            layoutparams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);

            textview.setLayoutParams(layoutparams);
            textview.setText("Paramètres");
            textview.setTextColor(Color.parseColor("#ffffff"));
            textview.setTextSize(20);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(textview);
        }
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        activitySet = this;
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || LogOutFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || RNotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
           // addPreferencesFromResource(R.xml.pref_general);
            Intent intent = new Intent(context, AboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            getActivity().finish();
            // createActionBar("A propos");
            // setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            // bindPreferenceSummaryToValue(findPreference("example_text"));
            //bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
             //   createActionBar("Paramètres");
              //  startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_mailing);
            createActionBar("Mailing");
            setHasOptionsMenu(true);
            Preference all = findPreference("all_company");
            all.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    mailingAll();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Rédiger un mail pour tous les collaborateurs.").setPositiveButton("Continuer", dialogClickListener)
                            .setNegativeButton("Annuler", dialogClickListener).show();

                    return true;
                }
            });

            Preference tubes = findPreference("tubes");
            tubes.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    mailing("CHIALI TUBES");
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Rédiger un mail pour tous les collaborateurs de CHIALI TUBES.").setPositiveButton("Continuer", dialogClickListener)
                            .setNegativeButton("Annuler", dialogClickListener).show();

                    return true;
                }
            });

            Preference academy = findPreference("academy");
            academy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    mailing("CHIALI ACADEMIE");
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Rédiger un mail pour tous les collaborateurs de CHIALI ACADEMIE.").setPositiveButton("Continuer", dialogClickListener)
                            .setNegativeButton("Annuler", dialogClickListener).show();

                    return true;
                }
            });

            Preference groupe = findPreference("groupe");
            groupe.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    mailing("GROUPE CHIALI");
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Rédiger un mail pour tous les collaborateurs de CHIALI GROUPE.").setPositiveButton("Continuer", dialogClickListener)
                            .setNegativeButton("Annuler", dialogClickListener).show();

                    return true;
                }
            });

            Preference service = findPreference("services");
            service.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    mailing("CHIALI SERVICES");
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Rédiger un mail pour tous les collaborateurs de CHIALI SERVICES.").setPositiveButton("Continuer", dialogClickListener)
                            .setNegativeButton("Annuler", dialogClickListener).show();

                    return true;
                }
            });
            Preference profi = findPreference("profi");
            profi.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    mailing("CHIALI PROFIPLAST");
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Rédiger un mail pour tous les collaborateurs de CHIALI PROFIPLAST.").setPositiveButton("Continuer", dialogClickListener)
                            .setNegativeButton("Annuler", dialogClickListener).show();

                    return true;
                }
            });
            Preference nawafid = findPreference("nawafid");
            nawafid.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    mailing("CHIALI NAWAFID");
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Rédiger un mail pour tous les collaborateurs de CHIALI NAWAFID.").setPositiveButton("Continuer", dialogClickListener)
                            .setNegativeButton("Annuler", dialogClickListener).show();

                    return true;
                }
            });
            Preference trading = findPreference("trading");
            trading.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    mailing("CHIALI TRADING");
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Rédiger un mail pour tous les collaborateurs de CHIALI TRADING.").setPositiveButton("Continuer", dialogClickListener)
                            .setNegativeButton("Annuler", dialogClickListener).show();

                    return true;
                }
            });
            Preference altim = findPreference("altim");
            altim.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    mailing("ALTIM INVEST");
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Rédiger un mail pour tous les collaborateurs de ALTIM.").setPositiveButton("Continuer", dialogClickListener)
                            .setNegativeButton("Annuler", dialogClickListener).show();

                    return true;
                }
            });
            Preference huile = findPreference("huile");
            huile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    mailing("HUILERIE");
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Rédiger un mail pour tous les collaborateurs de la HUILERIE.").setPositiveButton("Continuer", dialogClickListener)
                            .setNegativeButton("Annuler", dialogClickListener).show();

                    return true;
                }
            });
            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //bindPreferenceSummaryToValueBool(findPreference("notifications_new_message_vibrate"));
        }
        private void mailing(String companyN){

            Intent email = new Intent("android.intent.action.SEND");
            email.setType("application/octet-stream");
            email.setData(Uri.parse("mailto:"));
            email.putExtra("android.intent.extra.EMAIL", RealmManager.getListMailsBycompany(companyN));
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Email"));
        }

        private void mailingAll(){

            Intent email = new Intent("android.intent.action.SEND");
            email.setType("application/octet-stream");
            email.setData(Uri.parse("mailto:"));
            email.putExtra("android.intent.extra.EMAIL", RealmManager.getListMails());
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Email"));
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            Log.d("OPTION", "onOptionsItemSelected: ARE YOU HERE");
            int id = item.getItemId();
            if (id == android.R.id.home) {
             //   createActionBar("Paramètres");
               // startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class RNotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_rnotification);
            createActionBar("Notification");
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //bindPreferenceSummaryToValueBool(findPreference("notifications_new_message_vibrate"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            Log.d("OPTION", "onOptionsItemSelected: ARE YOU HERE");
            int id = item.getItemId();
            if (id == android.R.id.home) {
                //   createActionBar("Paramètres");
                // startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            createActionBar("Synchronisation");
            setHasOptionsMenu(true);
            Preference myPref = findPreference("sync");
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //open browser or intent here
                    if(HomeActivity.handler != null)
                   HomeActivity.handler.sendEmptyMessage(Constant.SETTING_SYNC);
                    else Toast.makeText(context, "erreur 400 veuillez réessayer", Toast.LENGTH_LONG).show();
                  //  Log.d("SYNCOUT", "onPreferenceClick: " + activityCount);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                       activitySet.finish();
                   }
                   getActivity().finish();
                    return true;
                }
            });

            Preference Reipref = findPreference("hardsync");
            Reipref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //open browser or intent here
                    if(HomeActivity.handler != null) {
                        Realm realM;
                        realM = Realm.getDefaultInstance();
                        realM.beginTransaction();
                        realM.deleteAll();
                        realM.commitTransaction();
                        realM.close();
                        HomeActivity.handler.sendEmptyMessage(Constant.SETTING_SYNC);
                    }
                    else Toast.makeText(context, "erreur 400 veuillez réessayer", Toast.LENGTH_LONG).show();
                    //  Log.d("SYNCOUT", "onPreferenceClick: " + activityCount);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                        activitySet.finish();
                    }
                    getActivity().finish();
                    return true;
                }
            });

            Preference exportPref =  findPreference("export_contact");
            exportPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //open browser or intent here
                    askForPermissionOrExportContact(getActivity());
                    return true;
                }
            });

            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            Log.d("SYNCOU", "onOptionsItemSelected: ");
            int id = item.getItemId();
            //createActionBar("Paramètres");
            if (id == android.R.id.home) {
              //  createActionBar("Paramètres");
                //startActivity(new Intent(getActivity(), SettingsActivity.class));
               // getActivity().finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class LogOutFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
           // Log.d("LOGOUT", "onCreate: " + MyPreferences.getMyBool(context, "notifications_new_message_vibrate", false));

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            MyPreferences.deletePreference(Constant.SECRET);
                            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                           // getActivity().finish();
                            ActivityCompat.finishAffinity(activity);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            startActivity(new Intent(getActivity(), SettingsActivity.class));
                            getActivity().finish();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Vous allez être déconnecté").setPositiveButton("OK", dialogClickListener)
                    .setNegativeButton("Annuler", dialogClickListener).show();




           // addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);


            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //  bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    private static void createActionBar(String text) {
        TextView textview = new TextView(context);
        RelativeLayout.LayoutParams layoutparams;
        layoutparams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);

        textview.setLayoutParams(layoutparams);
        textview.setText(text);
        textview.setTextColor(Color.parseColor("#ffffff"));
        textview.setTextSize(20);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(textview);

        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("BACKIN", "onBackPressed: ");
        createActionBar("Paramètres");

    }
    public static void askForPermissionOrExportContact(final Activity activity) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_CONTACTS},
                        12);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_CONTACTS},
                        12);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                             handler = new HandlerSetting();
                             handler.sendEmptyMessage(Constant.CONTACT);

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:

                            break;
                    }
                }
            };

            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Vous êtes sur le point d'exporter tous les contacts du groupe sur votre telephone?").setPositiveButton("Continuer", dialogClickListener)
                    .setNegativeButton("Annuler", dialogClickListener).show();
            // exportContact(contact, context);
        }
    }

    public static class HandlerSetting extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.CONTACT:
                    count = RealmManager.getCountContacts();
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setTitle("Importation des contacts");
                    progressDialog.setMessage("Patientez un instant...");
                    progressDialog.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int cpt = 0;
                            for(Contact contact: RealmManager.getAllContacts()){
                                cpt++;
                                ContactManager.exportContact(contact, context);
                                Log.d("EXPOR", "onClick: " + contact.getName());
                                Message message = new Message();
                                message.obj = cpt;
                                message.what = Constant.SHOW_LOADING;

                                handler.sendMessage(message);
                                // progressDialog.setMessage(cpt + " sur " + 1000 + " importés");
                            }
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }).start();

                    /*if(progressDialog.isShowing())
                        progressDialog.dismiss();*/
                 //   Toast.makeText(context , "contacts enregistrés", Toast.LENGTH_SHORT).show();

                    break;
                case Constant.SHOW_LOADING:
                    progressDialog.setMessage(msg.obj + " sur " + count + " importés");
                    break;

            }
        }
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    /*    int id = item.getItemId();
        if (id == android.R.id.home) {
           // Log.d("HOMEBUT", "onOptionsItemSelected: ");
           if(activityCount == 2) finish();
            return true;
        }*/
      /* if(activityCount == 2){
           activityCount--;
           finish();
       }*/

       Log.d("SYNCOU2", "onOptionsItemSelected: ");
       if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
           finish();
        return super.onOptionsItemSelected(item);
    }
}

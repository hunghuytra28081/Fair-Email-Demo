package eu.faircode.email.ui.main;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import java.util.Date;
import java.util.List;

import eu.faircode.email.ActivityBase;
import eu.faircode.email.ActivityMain;
import eu.faircode.email.ActivitySetup;
import eu.faircode.email.ActivityView;
import eu.faircode.email.ApplicationEx;
import eu.faircode.email.BuildConfig;
import eu.faircode.email.DB;
import eu.faircode.email.EntityAccount;
import eu.faircode.email.EntityMessage;
import eu.faircode.email.FragmentEula;
import eu.faircode.email.Helper;
import eu.faircode.email.Log;
import eu.faircode.email.R;
import eu.faircode.email.RunnableEx;
import eu.faircode.email.ServiceSend;
import eu.faircode.email.ServiceSynchronize;
import eu.faircode.email.ServiceUI;
import eu.faircode.email.SimpleTask;
import eu.faircode.email.ui.slide.SlideIntroActivity;


public class HomeMainActivity extends ActivityBase implements FragmentManager.OnBackStackChangedListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private static final long SPLASH_DELAY = 1500L; // milliseconds
    private static final long RESTORE_STATE_INTERVAL = 3 * 60 * 1000L; // milliseconds
    private static final long SERVICE_START_DELAY = 5 * 1000L; // milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean accept_unsupported = prefs.getBoolean("accept_unsupported", false);

        if (!accept_unsupported &&
                !Helper.isSupportedDevice() &&
                Helper.isPlayStoreInstall()) {
            setTheme(R.style.AppThemeBlueOrangeLight);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_unsupported);

            Button btnContinue = findViewById(R.id.btnContinue);
            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prefs.edit().putBoolean("accept_unsupported", true).commit();
                    ApplicationEx.restart(v.getContext());
                }
            });

            return;
        }

        Intent intent = getIntent();
        Uri data = (intent == null ? null : intent.getData());
        if (data != null &&
                "message".equals(data.getScheme()) &&
                ("email.faircode.eu".equals(data.getHost()) ||
                        BuildConfig.APPLICATION_ID.equals(data.getHost()))) {
            super.onCreate(savedInstanceState);

            Bundle args = new Bundle();
            args.putParcelable("data", data);

            new SimpleTask<EntityMessage>() {
                @Override
                protected EntityMessage onExecute(Context context, Bundle args) {
                    Uri data = args.getParcelable("data");
                    long id;
                    if ("email.faircode.eu".equals(data.getHost()))
                        id = Long.parseLong(data.getFragment());
                    else {
                        String path = data.getPath();
                        if (path == null)
                            return null;
                        String[] parts = path.split("/");
                        if (parts.length < 1)
                            return null;
                        id = Long.parseLong(parts[1]);
                    }

                    DB db = DB.getInstance(context);
                    return db.message().getMessage(id);
                }

                @Override
                protected void onExecuted(Bundle args, EntityMessage message) {
                    finish();

                    if (message == null)
                        return;

                    Intent thread = new Intent(HomeMainActivity.this, ActivityView.class);
                    thread.setAction("thread:" + message.id);
                    thread.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    thread.putExtra("account", message.account);
                    thread.putExtra("folder", message.folder);
                    thread.putExtra("thread", message.thread);
                    thread.putExtra("filter_archive", true);
                    thread.putExtra("pinned", true);
                    thread.putExtra("msgid", message.msgid);

                    startActivity(thread);
                }

                @Override
                protected void onException(Bundle args, Throwable ex) {
                    // Ignored
                }
            }.execute(this, args, "message:linked");

            return;
        }

        boolean eula = prefs.getBoolean("eula", false);
        boolean sync_on_launch = prefs.getBoolean("sync_on_launch", false);

        prefs.registerOnSharedPreferenceChangeListener(this);

        if (eula) {
            try {
                super.onCreate(savedInstanceState);
            } catch (RuntimeException ex) {
                Log.e(ex);
                // https://issuetracker.google.com/issues/181805603
                finish();
                startActivity(getIntent());
                return;
            }

            final Runnable splash = new Runnable() {
                @Override
                public void run() {
                    getWindow().setBackgroundDrawableResource(R.drawable.splash);
                }
            };

            final SimpleTask<Boolean> boot = new SimpleTask<Boolean>() {
                @Override
                protected void onPreExecute(Bundle args) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S)
                        getMainHandler().postDelayed(splash, SPLASH_DELAY);
                }

                @Override
                protected void onPostExecute(Bundle args) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S)
                        getMainHandler().removeCallbacks(splash);
                    getWindow().setBackgroundDrawable(null);
                }

                @Override
                protected Boolean onExecute(Context context, Bundle args) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    if (prefs.getBoolean("has_accounts", false))
                        return true;

                    DB db = DB.getInstance(context);
                    List<EntityAccount> accounts = db.account().getSynchronizingAccounts(null);
                    boolean hasAccounts = (accounts != null && accounts.size() > 0);

                    prefs.edit().putBoolean("has_accounts", hasAccounts).apply();

                    return hasAccounts;
                }

                @Override
                protected void onExecuted(Bundle args, Boolean hasAccounts) {
                    Bundle options = null;
                    try {
                        if (BuildConfig.DEBUG)
                            options = ActivityOptions.makeCustomAnimation(HomeMainActivity.this,
                                    R.anim.activity_open_enter, 0).toBundle();
                    } catch (Throwable ex) {
                        Log.e(ex);
                    }

                    if (hasAccounts) {
                        Intent view = new Intent(HomeMainActivity.this, ActivityView.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        // VX-N3
                        // https://developer.android.com/docs/quality-guidelines/core-app-quality
                        long now = new Date().getTime();
                        long last = prefs.getLong("last_launched", 0L);
                        if (!BuildConfig.PLAY_STORE_RELEASE &&
                                now - last > RESTORE_STATE_INTERVAL)
                            view.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        Intent saved = args.getParcelable("intent");
                        if (saved == null) {
                            prefs.edit().putLong("last_launched", now).apply();
                            startActivity(view, options);
                            if (sync_on_launch)
                                ServiceUI.sync(HomeMainActivity.this, null);
                        } else
                            try {
                                startActivity(saved);
                            } catch (SecurityException ex) {
                                Log.w(ex);
                                startActivity(view);
                            }

                        getMainHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ServiceSynchronize.watchdog(HomeMainActivity.this);
                                ServiceSend.watchdog(HomeMainActivity.this);
                            }
                        }, SERVICE_START_DELAY);
                    } else {
                        Intent setup = new Intent(HomeMainActivity.this, ActivitySetup.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(setup, options);
                    }
                    finish();
                }

                @Override
                protected void onException(Bundle args, Throwable ex) {
                    Log.unexpectedError(getSupportFragmentManager(), ex);
                }
            };

            if (Helper.shouldAuthenticate(this, false))
                Helper.authenticate(HomeMainActivity.this, HomeMainActivity.this, null,
                        new RunnableEx("auth:succeeded") {
                            @Override
                            public void delegate() {
                                Intent intent = getIntent();
                                Bundle args = new Bundle();
                                if (intent.hasExtra("intent"))
                                    args.putParcelable("intent", intent.getParcelableExtra("intent"));
                                boot.execute(HomeMainActivity.this, args, "main:accounts");
                            }
                        },
                        new RunnableEx("auth:cancelled") {
                            @Override
                            public void delegate() {
                                try {
                                    finish();
                                } catch (Throwable ex) {
                                    Log.w(ex);
                                }
                            }
                        });
            else
                boot.execute(this, new Bundle(), "main:accounts");
        } else {
            SharedPreferences.Editor editor = prefs.edit();
            Configuration config = getResources().getConfiguration();

            // Default enable compact mode for smaller screens
            if (!config.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) {
                editor.putBoolean("compact", true);
                //editor.putBoolean("compact_folders", true);
            }

            // Default disable landscape columns for small screens
            if (!config.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_NORMAL)) {
                editor.putBoolean("landscape", false);
                editor.putBoolean("landscape3", false);
            }

            // Default send bubbles off when accessibility enabled
            if (Helper.isAccessibilityEnabled(this))
                editor.putBoolean("send_chips", false);

            editor.apply();

            if (Helper.isNight(this))
                setTheme(R.style.AppThemeBlueOrangeDark);
            else
                setTheme(R.style.AppThemeBlueOrangeLight);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportFragmentManager().addOnBackStackChangedListener(this);

            Intent intentSlide = new Intent(HomeMainActivity.this, SlideIntroActivity.class);
            startActivity(intentSlide);
        }
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onBackStackChanged() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0)
            finish();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if ("eula".equals(key))
            if (prefs.getBoolean(key, false))
                recreate();
    }
}

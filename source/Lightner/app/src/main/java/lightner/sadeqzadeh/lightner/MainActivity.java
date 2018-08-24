package lightner.sadeqzadeh.lightner;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yakivmospan.scytale.Crypto;
import com.yakivmospan.scytale.Options;
import com.yakivmospan.scytale.Store;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import lightner.sadeqzadeh.lightner.encryption.DeCryptor;
import lightner.sadeqzadeh.lightner.encryption.EnCryptor;
import lightner.sadeqzadeh.lightner.entity.DaoSession;
import lightner.sadeqzadeh.lightner.fragment.GetMobileNumberFragment;
import lightner.sadeqzadeh.lightner.fragment.HomeFragment;
import lightner.sadeqzadeh.lightner.fragment.RegistrationFragment;
import lightner.sadeqzadeh.lightner.fragment.ReviewFlashcard;
import lightner.sadeqzadeh.lightner.fragment.SettingsFragment;
import lightner.sadeqzadeh.lightner.util.IabHelper;
import lightner.sadeqzadeh.lightner.util.IabResult;
import lightner.sadeqzadeh.lightner.util.Purchase;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getName();
    private SectionsStatePagerAdapter sectionsStatePagerAdapter;
    private SmoothProgressBar progressBar;
    private EnCryptor encryptor;
    private DeCryptor decryptor;
    private DaoSession daoSession;
    public boolean backPressed = false;
    public TextToSpeech textToSpeech;
    public boolean speechStatus = false;
    public IabHelper mHelper;
    public boolean iapStatus= false;
    public DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //init drawer
        drawer = findViewById(R.id.drawer_layout);

        //init encryption
        encryptor = new EnCryptor();

        try {
            decryptor = new DeCryptor();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException |
                IOException e) {
            e.printStackTrace();
        }

        progressBar = findViewById(R.id.progressbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //check  if user doesn't registered already redirect to registration page
        if (!Util.isUserLogged()) {
            GetMobileNumberFragment getMobileNumerFragment = new GetMobileNumberFragment();
            replaceFragment(getMobileNumerFragment, GetMobileNumberFragment.TAG,false);
            return;
        } else {
            HomeFragment fragment = new HomeFragment();
            replaceFragment(fragment, HomeFragment.TAG,false);
        }

        initIAP();
        initTextToSpeech();
    }

    private void initIAP() {
        //TODO check bazar is installed
        String base64EncodedPublicKey=  "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwC3jgRLYRj+0xcjee9urNoEuRo72pWKQk2gd36hdkDLYxBicuIwGWzV9hfmmSu/36llEf4wHpZt44iS7PfZouD9tbL1i2oorVg9O+FkNR/OeJVn0nRajT+gbY2nURDdsh4pZe+qvb0+70//nbD3YrZUhlDa7HhUvokbJqu4UmGnaNF0TUU+EtmtkrpwSt6xu2Buv+nQvcUkT2+RMkpW+TXSUodEVyMISghtWo2JwbMCAwEAAQ==";
        try {
            mHelper = new IabHelper(getApplicationContext(), base64EncodedPublicKey);
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        Log.d(TAG, "Problem setting up In-app Billing: " + result);
                    }
                    iapStatus = true;
                }
            });
        }catch (Exception e){
            Log.e(MainActivity.TAG,"initIAP: " + e.toString());
        }

    }

    private void initTextToSpeech() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS ){
                    speechStatus = true;
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressed = true;
       if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void showProgressbar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressbar(){
        progressBar.setVisibility(View.GONE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.settings) {
            SettingsFragment settingsFragment = new SettingsFragment();
            replaceFragment(settingsFragment,SettingsFragment.TAG,true);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onResume() {
        super.onResume();
        // set default lang to persian
        Locale locale = new Locale("fa");
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public void replaceFragment(Fragment fragment, String TAG, boolean addToBackStack){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(addToBackStack){
            ft.replace(R.id.container, fragment)
                    .addToBackStack(TAG)
                    .commit();
        }else {
            ft.replace(R.id.container, fragment)
                    .commit();
        }
    }

    public String decryptText(byte[] encryptedData) {
        try {
            System.out.println("IVBase64EncodedFetched:" + Util.fetchFromPreferences(Const.ENCRYPTION_IV));
            byte[] iv = Base64.decode(Util.fetchFromPreferences(Const.ENCRYPTION_IV),Base64.DEFAULT);
            System.out.println("IVFromPreferences:" + iv);
            return decryptor
                    .decryptData(Const.ALIAS,encryptedData, iv);
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
                KeyStoreException | NoSuchPaddingException | NoSuchProviderException |
                IOException | InvalidKeyException e) {
            Log.e(TAG, "decryptData() called with: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encryptText(String textToEncrypt) {

        try {
            final byte[] encryptedText = encryptor
                    .encryptText(Const.ALIAS, textToEncrypt);
            return Base64.encodeToString(encryptedText, Base64.DEFAULT);
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchProviderException |
                KeyStoreException | IOException | NoSuchPaddingException | InvalidKeyException e) {
            Log.e(TAG, "called with: " + e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException | SignatureException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DaoSession  getDaoSession(){
        daoSession = ((App) getApplication()).getDaoSession();
        return  daoSession;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }
}

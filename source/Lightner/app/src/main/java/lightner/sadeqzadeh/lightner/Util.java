package lightner.sadeqzadeh.lightner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Environment;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.yakivmospan.scytale.Crypto;
import com.yakivmospan.scytale.Options;
import com.yakivmospan.scytale.Store;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class Util {
    private static final Pattern DIR_SEPORATOR;
    public static Context context;



    public static void writeToLogFile(String inputText, Object... objects) {
        String log = new Date().toString() + " : " + String.format(inputText, objects);
        File logFile = new File(getDownloadDirectoryPath() + "/lightner.log");
        if (!logFile.exists()) {
            logFile.getParentFile().mkdirs();
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (logFile.length() > 500000) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(logFile);
                fileOutputStream.write(BuildConfig.FLAVOR.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(logFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(log);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        } catch (FileNotFoundException e22) {
            e22.printStackTrace();
        } catch (IOException e32) {
            e32.printStackTrace();
        }
    }

    public static String getDownloadDirectoryPath() {
        String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File dir = new File(downloadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return downloadPath;
    }


    static {
        DIR_SEPORATOR = Pattern.compile("/");
    }

    public static String[] getStorageDirectories() {
        Set<String> rv = new HashSet();
        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
        if (!TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            String rawUserId;
            if (VERSION.SDK_INT < 17) {
                rawUserId = BuildConfig.FLAVOR;
            } else {
                String[] folders = DIR_SEPORATOR.split(Environment.getExternalStorageDirectory().getAbsolutePath());
                String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                } catch (NumberFormatException e) {
                }
                rawUserId = isDigit ? lastFolder : BuildConfig.FLAVOR;
            }
            if (TextUtils.isEmpty(rawUserId)) {
                rv.add(rawEmulatedStorageTarget);
            } else {
                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        } else if (TextUtils.isEmpty(rawExternalStorage)) {
            rv.add("/storage/sdcard0");
        } else {
            rv.add(rawExternalStorage);
        }
        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
            Collections.addAll(rv, rawSecondaryStoragesStr.split(File.pathSeparator));
        }
        return (String[]) rv.toArray(new String[rv.size()]);
    }

    public static String fetchFromPreferences(String key) {
        String str = Const.APP_CONFIG;
        return context.getSharedPreferences(str, 0).getString(key, null);
    }

    public static void saveInPreferences(String key, String value) {
        String str = Const.APP_CONFIG;
        Editor editor = context.getSharedPreferences(str, 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean fetchBooleanFromPreferences(String key, boolean defaultValue) {
        String str = Const.APP_CONFIG;
        return context.getSharedPreferences(str, 0).getBoolean(key, defaultValue);
    }

    public static void saveBooleanInPreferences(String key, boolean value) {
        String str = Const.APP_CONFIG;
        Editor editor = context.getSharedPreferences(str, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public static void login() {
/*
        if (fetchFromPreferences(Const.USERNAME) != null) {
            ApplicationController.getInstance().addToRequestQueue(new LoginRequest(1, URL.LOGIN, new LoginResponseListener(), new LoginErrorResponseListener()));
        }
*/
    }

    public static void moveFile(String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputFile);
            out = new FileOutputStream(inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputFile).delete();


        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public static boolean isUserLogged(){
        String mobile = fetchFromPreferences(Const.MSISDN);
        if(mobile  ==  null || mobile.isEmpty()){
            return false;
        }
        return true;
    }


    public static String randomToken(int len) {
        String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < len; i++){
            tempChar = ALLOWED_CHARACTERS.charAt(generator.nextInt(35 ) + 1 );
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static int randomNumber(int bound){
        Random random = new Random();
        return random.nextInt(bound);
    }

    public static void encryptAndSave(Context context,String key, String value){
        Store store = new Store(context);
        SecretKey secretKey=null;
        if (!store.hasKey(Const.ALIAS)) {
            secretKey = store.generateSymmetricKey(Const.ALIAS, null);
        }else {
            secretKey = store.getSymmetricKey(Const.ALIAS, null);
        }
        Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);

        String encryptedData = crypto.encrypt(value, secretKey);
        saveInPreferences(key, encryptedData);
    }

    public static String fetchAndDecrypt(Context context, String key){
        Store store = new Store(context);
        SecretKey secretKey=null;
        if (!store.hasKey(Const.ALIAS)) {
            secretKey = store.generateSymmetricKey(Const.ALIAS, null);
        }else {
            secretKey = store.getSymmetricKey(Const.ALIAS, null);
        }
        Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);

        return crypto.decrypt(fetchFromPreferences(key), secretKey);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();

    }

}


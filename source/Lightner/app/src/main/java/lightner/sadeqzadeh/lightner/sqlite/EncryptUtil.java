package lightner.sadeqzadeh.lightner.sqlite;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;


import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.Util;

public class EncryptUtil {

  /*  private void InitializeSQLCipher(Context context) {
        SQLiteDatabase.loadLibs(context);
        File databaseFile = context.getDatabasePath("lightner.db");
        databaseFile.mkdirs();
        databaseFile.delete();
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "test123", null);
        database.execSQL("create table t1(a, b)");
        database.execSQL("insert into t1(a, b) values(?, ?)", new Object[]{"one for the money",
                "two for the show"});
    }*/

    public static byte[] encrypt(String textToEncrypt){
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            final KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore
                    .getEntry(Const.ALIAS, null);
            SecretKey secretKey;
            if(secretKeyEntry == null){
                final KeyGenerator keyGenerator = KeyGenerator
                        .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                final KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(Const.ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build();
                keyGenerator.init(keyGenParameterSpec);
                secretKey = keyGenerator.generateKey();
            }else {
                secretKey = secretKeyEntry.getSecretKey();
            }
            System.out.println(secretKey.toString());
            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            if(Util.fetchFromPreferences(Const.ENCRYPTION_IV) == null || Util.fetchFromPreferences(Const.ENCRYPTION_IV).isEmpty() ){
                byte[] encryptionIv = cipher.getIV();
                String ivStr  = Base64.encodeToString(encryptionIv,Base64.DEFAULT);
                Util.saveInPreferences(Const.ENCRYPTION_IV,ivStr);
            }
            return cipher.doFinal(textToEncrypt.getBytes("UTF-8"));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(byte[] encrypted){
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            final KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore
                    .getEntry(Const.ALIAS, null);
            final SecretKey secretKey = secretKeyEntry.getSecretKey();
            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] iv = Base64.decode(Util.fetchFromPreferences(Const.ENCRYPTION_IV),Base64.DEFAULT);
            final GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
            final byte[] decodedData = cipher.doFinal(encrypted);
            final String unencryptedString = new String(decodedData, "UTF-8");
            return unencryptedString;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}

package lightner.sadeqzadeh.lightner.encryption;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.Util;

/**
  ______        _____                  _             
 |  ____|      / ____|                | |            
 | |__   _ __ | |     _ __ _   _ _ __ | |_ ___  _ __ 
 |  __| | '_ \| |    | '__| | | | '_ \| __/ _ \| '__|
 | |____| | | | |____| |  | |_| | |_) | || (_) | |   
 |______|_| |_|\_____|_|   \__, | .__/ \__\___/|_|   
                            __/ | |                  
                           |___/|_|                  
 */
public class EnCryptor {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private byte[] encryption;

    public EnCryptor() {
    }

    public byte[] encryptText(final String alias, final String textToEncrypt)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
            InvalidAlgorithmParameterException, SignatureException, BadPaddingException,
            IllegalBlockSizeException {

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));

        if(Util.fetchFromPreferences(Const.ENCRYPTION_IV) == null || Util.fetchFromPreferences(Const.ENCRYPTION_IV).isEmpty()){
            System.out.println("PureIV:"+ cipher.getIV());
            System.out.println("PureIVEncoded:"+ Base64.encodeToString(cipher.getIV(), Base64.DEFAULT));
            Util.saveInPreferences(Const.ENCRYPTION_IV, Base64.encodeToString(cipher.getIV(), Base64.DEFAULT));
        }

        return (encryption = cipher.doFinal(textToEncrypt.getBytes("UTF-8")));
    }

    @NonNull
    private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException {

        final KeyGenerator keyGenerator = KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);

        keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build());

        return keyGenerator.generateKey();
    }

    public byte[] getEncryption() {
        return encryption;
    }

}

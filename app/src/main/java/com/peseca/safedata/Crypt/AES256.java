package com.peseca.safedata.Crypt;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.spongycastle.crypto.params.KeyParameter;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256 {

    public static String encrypt(String strToEncrypt, String SECRET_KEY, String SALT, Context context) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            byte[] secretKey = pbkdf2(SECRET_KEY, SALT, 100000, 32);
            SecretKey secretKeyfinal = new SecretKeySpec((secretKey), "AES");


            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeyfinal, ivspec);
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)), Base64.DEFAULT);
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
            Toast.makeText(context, "Error while encrypting: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String SECRET_KEY, String SALT, Context context) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            /*
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 100000, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            */

            byte[] secretKey = pbkdf2(SECRET_KEY, SALT, 100000, 32);
            SecretKey secretKeyfinal = new SecretKeySpec((secretKey), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeyfinal , ivspec);
            return new String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
            Toast.makeText(context, "Error while decrypting: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private static byte[] pbkdf2(String secret, String salt, int iterations, int keyLength) {
        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
        byte[] secretData = secret.getBytes();
        byte[] saltData = salt.getBytes();
        gen.init(secretData, saltData, iterations);
        byte[] derivedKey = ((KeyParameter)gen.generateDerivedParameters(keyLength * 8)).getKey();
        return derivedKey;
    }
}

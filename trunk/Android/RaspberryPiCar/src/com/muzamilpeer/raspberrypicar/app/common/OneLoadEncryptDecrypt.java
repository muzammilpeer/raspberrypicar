
package com.muzamilpeer.raspberrypicar.app.common;

import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import android.content.Context;

public class OneLoadEncryptDecrypt {
    private static String PUBLIC_KEY_FILE = "/resources/public.key";

    private static String PRIVATE_KEY_FILE = "/resources/private.key";

//    private static String ALGORITHM = "RSA";
    private static String ALGORITHM = "RSA/ECB/PKCS1Padding";
//    private static String ALGORITHM = "RSA/ECB/NoPadding";

    private static PrivateKey privateKey;

    private static PublicKey publicKey = null;

    private static PublicKey getPublicKey(Context context) throws Exception {
        if (publicKey == null) {
            ObjectInputStream inputStream = null;

            inputStream = new ObjectInputStream(context.getAssets().open("public.key"));

            publicKey = (PublicKey)inputStream.readObject();

        }
        return publicKey;
    }

    private static PrivateKey getPrivateKey(Context context) throws Exception {
        if (privateKey == null) {
            ObjectInputStream inputStream = new ObjectInputStream(context.getAssets().open(
                    "private.key"));
            privateKey = (PrivateKey)inputStream.readObject();
        }
        return privateKey;
    }

    /**
     * Encrypt the plain text using public key.
     * 
     * @param text : original plain text
     * @param key :The public key
     * @return Encrypted text
     * @throws java.lang.Exception
     */
    public static byte[] encrypt(String text, Context context) throws Exception {
        byte[] cipherText = null;
        // get an RSA cipher object and print the provider
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        // encrypt the plain text using the public key
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(context));
        cipherText = cipher.doFinal(text.getBytes());
        return cipherText;
    }

    /**
     * Decrypt text using private key.
     * 
     * @param text :encrypted text
     * @param key :The private key
     * @return plain text
     * @throws java.lang.Exception
     */
    public static String decrypt(byte[] text, Context context) throws Exception {

        byte[] dectyptedText = null;
        // get an RSA cipher object and print the provider
        final Cipher cipher = Cipher.getInstance(ALGORITHM);

        // decrypt the text using the private key
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(context));
        dectyptedText = cipher.doFinal(text);

        return new String(dectyptedText);
    }
}

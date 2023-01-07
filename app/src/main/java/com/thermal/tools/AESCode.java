package com.thermal.tools;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
//import java.security.SecureRandom;

public class AESCode {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16;
    private static final int KEY_SIZE = 16;

    public static void encrypt(InputStream inputStream, OutputStream outputStream)
            throws Exception {
        encryptOrDecrypt(inputStream,outputStream, Cipher.ENCRYPT_MODE);
    }

    public static void decrypt(InputStream inputStream, OutputStream outputStream)
            throws Exception {
        encryptOrDecrypt(inputStream, outputStream, Cipher.DECRYPT_MODE);
    }

    private static void encryptOrDecrypt(InputStream inputStream, OutputStream outputStream,int mode) throws Exception {

            String key = "thermalopenssl.h";

            byte[] keyBytes = key.getBytes();
            byte[] ivBytes = keyBytes;
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(mode, secretKeySpec, ivParameterSpec);

    //        outputStream.write(ivBytes);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(cipher.update(buffer, 0, length));
            }
            outputStream.write(cipher.doFinal());
            outputStream.flush();
        }

//    public static void main(String[] args) throws Exception {
//        String key = "thermalopenssl.h";
////        System.out.println(new File(".").getAbsolutePath());
//
//        InputStream inputStream = new FileInputStream("app\\src\\main\\java\\com\\su\\admin\\out.txt");
//        OutputStream outputStream = new FileOutputStream("app\\src\\main\\java\\com\\su\\admin\\crypt.conf");
//        AESCode.encrypt(key, inputStream, outputStream);
//        inputStream.close();
//        outputStream.close();
//
////        InputStream inputStream2 = new FileInputStream("app\\src\\main\\java\\com\\su\\admin\\thermal-normal.conf");
////        OutputStream outputStream2 = new FileOutputStream("app\\src\\main\\java\\com\\su\\admin\\out.txt");
////        AESCode.decrypt(key, inputStream2, outputStream2);
////        inputStream2.close();
////        outputStream2.close();
//    }
}
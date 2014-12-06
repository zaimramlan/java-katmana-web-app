package com.katmana;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author Zaid
 */
public class Encryption {

  public static boolean verifyPassword(String user_encrypted_password, String password){
    String user_salt = user_encrypted_password.split("\\$")[1];
    user_encrypted_password = user_encrypted_password.split("\\$")[2];
    String encrypted_password = getEncryption(password, user_salt);

    return user_encrypted_password.equals(encrypted_password);
    // return encrypted_password;
  }

  public static String getEncryptedPassword(String user_password){
    String salt = null, encrypted_password = null;
    try {
      salt = generateSalt();
      encrypted_password = "$" + salt + "$" + getEncryption(user_password, salt);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return encrypted_password;
  }

  private static String getEncryption(String password, String salt) {
    String encryptedPassword = null;
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(salt.getBytes());
      byte[] bytes = md.digest(password.getBytes());
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < bytes.length; i++) {
        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
      }
      encryptedPassword = sb.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return encryptedPassword;
  }

  private static String generateSalt() throws NoSuchAlgorithmException {
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    sr.nextBytes(salt);
    return salt.toString();
  }
}

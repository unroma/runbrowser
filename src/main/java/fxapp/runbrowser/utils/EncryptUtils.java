package fxapp.runbrowser.utils;

import lombok.experimental.UtilityClass;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;



@UtilityClass
public class EncryptUtils {

    private final String SECRET_TEXT = "Veryverysecretwordtogeneratekey!";

    public String encryptText(String plainText) {
        if (plainText != null)  {
            byte[] encryptedBytes;
            try {
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, generateKey());
                encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encodeToString(encryptedBytes);
            } catch (Exception e) {
                System.out.println("Encryption has failed ");
                return null;
            }
        }
        return null;
    }

    public String decryptText(String encryptedText) {
        if (encryptedText != null) {
            byte[] decryptedBytes;
            try {
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, generateKey());
                decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
                return new String(decryptedBytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
                System.out.println("Decryption has failed");
                return null;
            }
        }
        return null;
    }

    private static SecretKey generateKey() {
        byte[] keyBytes = SECRET_TEXT.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "AES");
    }
}

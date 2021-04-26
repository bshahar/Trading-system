package Service;

import java.util.Base64;
import java.util.logging.Level;
import javax.crypto.*;

public class Encryptor {
    static Cipher cipher;
    private SecretKey key;

    public Encryptor() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128); // block size is 128bits
            SecretKey secretKey = keyGenerator.generateKey();
            this.key = secretKey;
            cipher = Cipher.getInstance("AES"); //SunJCE provider AES algorithm, mode(optional) and padding schema(optional)
        }
        catch (Exception e)
        {
            KingLogger.logError("error creatinr encryptor");
        };
    }

    public String encrypt(String plainText) {
        try {
            byte[] plainTextByte = plainText.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, this.key);
            byte[] encryptedByte = cipher.doFinal(plainTextByte);
            Base64.Encoder encoder = Base64.getEncoder();
            String encryptedText = encoder.encodeToString(encryptedByte);
            return encryptedText;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public String decrypt(String encryptedText)
            throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, this.key);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }
}
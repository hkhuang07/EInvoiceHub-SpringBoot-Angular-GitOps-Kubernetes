package vn.softz.app.einvoicehub.provider.bkav.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class BkavCryptoUtil {
    
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";

    public static String encryptData(String data, String partnerToken) {
        try {
            // Step 1: Compress data bằng GZip
            byte[] compressedData = compress(data);
            
            // Step 2: Parse partner token để lấy Key và IV
            String[] tokenParts = partnerToken.split(":");
            if (tokenParts.length != 2) {
                throw new IllegalArgumentException("Invalid partner token format. Expected: base64Key:base64IV");
            }
            
            byte[] key = Base64.getDecoder().decode(tokenParts[0]);
            byte[] iv = Base64.getDecoder().decode(tokenParts[1]);
                        
            // Step 3: Encrypt bằng AES-256
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            
            byte[] encryptedData = cipher.doFinal(compressedData);            
            
            // Step 4: Encode Base64
            String result = Base64.getEncoder().encodeToString(encryptedData);
            
            return result;
            
        } catch (Exception e) {
            log.error("Error encrypting data", e);
            throw new RuntimeException("Encryption failed: " + e.getMessage(), e);
        }
    }
    
    public static String decryptData(String encryptedData, String partnerToken) {
        try {            
            // Step 1: Decode Base64
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            
            // Step 2: Parse partner token
            String[] tokenParts = partnerToken.split(":");
            if (tokenParts.length != 2) {
                throw new IllegalArgumentException("Invalid partner token format");
            }
            
            byte[] key = Base64.getDecoder().decode(tokenParts[0]);
            byte[] iv = Base64.getDecoder().decode(tokenParts[1]);
            
            // Step 3: Decrypt bằng AES-256
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            
            byte[] decryptedData = cipher.doFinal(decodedData);
            log.debug("Decrypted data size: {} bytes", decryptedData.length);
            
            // Step 4: Decompress GZip
            String result = decompress(decryptedData);
            log.debug("Decompressed data length: {}", result.length());
            
            return result;
            
        } catch (Exception e) {
            log.error("Error decrypting data", e);
            throw new RuntimeException("Decryption failed: " + e.getMessage(), e);
        }
    }
    
    // Compress data bằng GZip
    private static byte[] compress(String data) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
            gzipStream.write(data.getBytes(StandardCharsets.UTF_8));
        }
        return byteStream.toByteArray();
    }
    
    // Decompress data từ GZip
    private static String decompress(byte[] compressedData) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPInputStream gzipStream = new GZIPInputStream(new ByteArrayInputStream(compressedData))) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipStream.read(buffer)) > 0) {
                byteStream.write(buffer, 0, len);
            }
        }
        return byteStream.toString(StandardCharsets.UTF_8.name());
    }
}

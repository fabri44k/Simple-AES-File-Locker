import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;


public class AES {
	
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";

	public static void encrypt(String key, File inputFile, File outputFile) throws CryptoException {
		doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
	}

	public static void decrypt(String key, File inputFile, File outputFile) throws CryptoException {
		doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
	}

	private static void doCrypto(int cipherMode, String key, File inputFile, File outputFile) throws CryptoException {
		try {
			Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, secretKey);
			
			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);
			
			byte[] outputBytes = cipher.doFinal(inputBytes);
			
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);
			
			inputStream.close();
			outputStream.close();
			
		} catch (NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException ex) {
			throw new CryptoException("Error encrypting/decrypting file", ex);
		}
	}
	
	public static String generateKeyFromPassword(String password) {
		String salt = "A.sf78[kL78_lop_&ky?Hf1*kLx>5h-HJcE34°gLkP3zT7wR9sYvQ8x";
		try {
	        byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
	        int iterations = 100; 
	        int keyLength = 128;
	        
	        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, keyLength);
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
	        
	        return Base64.getEncoder().encodeToString(keyBytes);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}

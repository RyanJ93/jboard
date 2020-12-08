package Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.stream.IntStream;

public class PasswordUtils {
    private static final String AVAILABLE_SYMBOLS = "ABCDEFGJKLMNPRSTUVWXYZ0123456789";

    public static String digest(String string){
        String digest = null;
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(string.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = messageDigest.digest(byteArrayOutputStream.toByteArray());
            StringBuilder stringBuilder = new StringBuilder();
            for ( byte b : bytes ){
                stringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            digest = stringBuilder.toString();
        }catch(NoSuchAlgorithmException | IOException ex){
            ex.printStackTrace();
        }
        return digest;
    }

    public static String generateToken(int length){
        String token = null;
        try{
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            IntStream intStream = secureRandom.ints(length, 0, PasswordUtils.AVAILABLE_SYMBOLS.length());
            token = intStream.mapToObj(PasswordUtils.AVAILABLE_SYMBOLS::charAt).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
        }catch(NoSuchAlgorithmException ex){
            ex.printStackTrace();
        }
        return token;
    }
}

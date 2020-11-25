package Utils;

import java.security.MessageDigest;
import java.util.concurrent.ThreadLocalRandom;

public class PasswordCocktail {
    public static final int SALT_LENGTH = 32;
    public static final int PEPPER_LENGTH = 32;
    public static final int MIN_LOOP_VALUE = 128;
    public static final int MAX_LOOP_VALUE = 256;

    protected String hash;
    protected String salt;
    protected String pepper;
    protected int loop;

    protected String getHash(String password){
        String hash = this.salt + password + this.pepper;
        for ( int i = 0 ; i < this.loop ; i++ ){
            hash = PasswordUtils.digest(hash);
        }
        return hash;
    }

    public PasswordCocktail(String password){
        this.salt = PasswordUtils.generateToken(PasswordCocktail.SALT_LENGTH);
        this.pepper = PasswordUtils.generateToken(PasswordCocktail.PEPPER_LENGTH);
        this.loop = ThreadLocalRandom.current().nextInt(PasswordCocktail.MIN_LOOP_VALUE, PasswordCocktail.MAX_LOOP_VALUE + 1);
        this.hash = this.getHash(password);
    }

    public PasswordCocktail(String hash, String salt, String pepper, int loop){
        this.hash = hash;
        this.salt = salt;
        this.pepper = pepper;
        this.loop = loop;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public String getPepper() {
        return pepper;
    }

    public int getLoop() {
        return loop;
    }

    public boolean compare(String password){
        String hash = this.getHash(password);
        return MessageDigest.isEqual(hash.getBytes(), this.hash.getBytes());
    }
}

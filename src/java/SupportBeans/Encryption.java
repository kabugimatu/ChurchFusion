/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SupportBeans;

/**
 *
 * @author kabugi/ George
 * Pearl Technologies All rights reserved
 */
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.BASE64Encoder;

/**
 *
 * @author kabugi
 */
public class Encryption {
    MessageDigest md;
    public synchronized String encryptPassword(String password){
        String hashValue= null;
        try{
            md = MessageDigest.getInstance("SHA");
            md.update(password.getBytes("UTF-8"));
            byte raw[] = md.digest();
            hashValue = (new BASE64Encoder()).encode(raw);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(NoSuchAlgorithmException ex){
            ex.printStackTrace();
            hashValue = null;
        }
        return hashValue;
    }
    public synchronized String getDecryptedPassword(String hashValue){
        String decrypted = null;
        
        return decrypted;
    }
}

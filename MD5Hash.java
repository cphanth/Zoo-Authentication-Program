/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phanthamany_final_authentication;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author christina.pha_snhu
 */
public class MD5Hash {
    
    //private internal fields
    private String userPassword;
    private String userPasswordHash;
    private MessageDigest md;
    
    //default constructor
    public MD5Hash(){
        userPassword = "default password: fix your code!"; //messages for troubleshooting
        userPasswordHash = "default hash: fix your code!";
    }
    
    //public setters/mutators
    
    //this method passes an argument from main and sets the user's password
    public void setPassword(String passwordInputFromUser) {
       userPassword = passwordInputFromUser;
       
       return;
    }
    
    //this method passes an argument from main and sets the user's password, hashed
    public void setPasswordHash(String userPassword){
        this.userPassword = userPassword;   
        MessageDigest md = null;
        
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MD5Hash.class.getName()).log(Level.SEVERE, null, ex);
        }
        md.update(userPassword.getBytes());
        byte[] digest = md.digest();
        
        StringBuffer sb = new StringBuffer();
            for (byte b: digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
        userPasswordHash = sb.toString();
        
        return;
    }
    
    //public getters/accessors
    
    //when called from a method, it will return password or hash
    public String getPassword(){
        return userPassword;
    }
    public String getPasswordHash(){
        return userPasswordHash;
    }
    
    
    
}

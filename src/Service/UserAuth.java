package Service;

import Persistence.UserAuthWrapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class UserAuth {
    private Encryptor encryptor;
    private UserAuthWrapper userPass;

    public UserAuth(){
        encryptor= new Encryptor();
        userPass=new UserAuthWrapper();
    }


    public boolean register(String userName, String pass) {
        synchronized (userPass) {
            if (userPass.containsKey(userName)) {
                return false;
            } else {
                userPass.add(userName, this.encryptor.encrypt(pass));
                return true;
            }
        }
    }


    public boolean loginAuthentication(String userName, String pass) {
        try {
            if (userPass.containsKey(userName))//write like this for the error log
            {
                if (userPass.get(userName).equals(encryptor.encrypt(pass)))
                    return true;
                else
                    KingLogger.logEvent("Domain.User failed logging in with name: " + userName);
            } else {
                KingLogger.logEvent("Domain.User tried to login with name: " + userName + " that doesn't exist");
            }
            return false;
        }
        catch (Exception e) {
            KingLogger.logError("Domain.User failed logging in with name: " + userName);
            return false;
        }
    }


    public boolean guestRegister(String userName, String password) {
        if (userPass.containsKey(userName)) {
            return false;
        } else {
            userPass.add(userName, this.encryptor.encrypt(password));
            return true;
        }
    }
}


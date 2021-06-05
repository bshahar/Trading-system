package Tests;

import Domain.Result;
import Persistence.AdminTableWrapper;
import Persistence.DataBaseHelper;
import Persistence.UserWrapper;
import Service.API;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SystemManagerTests {


    @BeforeEach
    public void setUp() {
        try {
            DataBaseHelper.cleanAllTable();
            API.initTradingSystem();
        } catch (Exception e) {
            System.out.println("ERROR!");
        }
    }

/*"GuestsCounter"
"NormalUsersCounter"
"ManagersCounter"
"OwnersCounter"*/
    @Test
    public void GuestsCounterNoGuestsInside(){

        Result result = API.getSystemManagerStats();
        int numberOfGuests = (int)((JSONObject)result.getData()).get("GuestsCounter");
        assertEquals(numberOfGuests,0);
    }

    @Test
    public void GuestsCounterWithGuests(){
        API.guestLogin();
        Result result = API.getSystemManagerStats();
        int numberOfGuests = (int)((JSONObject)result.getData()).get("GuestsCounter");
        assertEquals(numberOfGuests,1);
    }
    @Test
    public void ManyGuestLoginAndRegisterLogin(){
        API.guestLogin();
        API.guestLogin();
        API.guestLogin();
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password, 20);
        Result result = API.getSystemManagerStats();
        int numberOfGuests = (int)((JSONObject)result.getData()).get("GuestsCounter");
        assertEquals(numberOfGuests,3);
    }


}

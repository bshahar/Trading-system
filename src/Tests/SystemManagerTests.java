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
            DataBaseHelper.cleanAllTable("test");
            API.initTradingSystem("test","");
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
    @Test
    public void NormalUsersLogin(){
        String userName1="kandabior1";
        String password= "or321654";
        API.register(userName1,password, 20);
        String userName2="kandabior2";
        API.register(userName2,password, 20);
        String userName3="kandabior3";
        API.register(userName1,password, 20);
        API.register(userName2,password, 20);
        API.register(userName3,password, 20);

         API.registeredLogin(userName1,password);
        API.registeredLogin(userName2,password);
        API.registeredLogin(userName3,password);

        Result result = API.getSystemManagerStats();
        int numberOfGuests = (int)((JSONObject)result.getData()).get("NormalUsersCounter");
        assertEquals(numberOfGuests,3);
    }
    @Test
    public void NormalUsersLoginAndLogoutAndLoginAgain(){
        String userName1="kandabior1";
        String password= "or321654";
        API.register(userName1,password, 20);
        String userName2="kandabior2";
        API.register(userName2,password, 20);
        String userName3="kandabior3";
        API.register(userName1,password, 20);
        API.register(userName2,password, 20);
        int userId =(int)API.register(userName3,password, 20).getData();

        API.registeredLogin(userName1,password);
        API.registeredLogin(userName2,password);
        API.registeredLogin(userName3,password);

        Result result = API.getSystemManagerStats();
        int numberOfNormalUsers = (int)((JSONObject)result.getData()).get("NormalUsersCounter");
        assertEquals(numberOfNormalUsers,3);
        API.registeredLogout(userId);
        API.registeredLogin(userName3,password);
        result = API.getSystemManagerStats();
        numberOfNormalUsers = (int)((JSONObject)result.getData()).get("NormalUsersCounter");
        assertEquals(numberOfNormalUsers,4);

    }
    @Test
    public void OwnersCounter(){
        String userName1="kandabior1";
        String password= "or321654";
        API.register(userName1,password, 20);
        String userName2="kandabior2";
        API.register(userName2,password, 20);
        String userName3="kandabior3";
        API.register(userName1,password, 20);
        API.register(userName2,password, 20);
        int userId =(int)API.register(userName3,password, 20).getData();

        API.registeredLogin(userName1,password);
        API.registeredLogin(userName2,password);
        API.registeredLogin(userName3,password);

        API.openStore(userId,"eladStore");
        Result result = API.getSystemManagerStats();
        int numberOfOwnersUsers = (int)((JSONObject)result.getData()).get("OwnersCounter");
        assertEquals(numberOfOwnersUsers,1);

    }
    @Test
    public void OwnersCounterLogOutAndLogin(){
        String userName1="kandabior1";
        String password= "or321654";
        API.register(userName1,password, 20);
        String userName2="kandabior2";
        API.register(userName2,password, 20);
        String userName3="kandabior3";
        API.register(userName1,password, 20);
        API.register(userName2,password, 20);
        int userId =(int)API.register(userName3,password, 20).getData();

        API.registeredLogin(userName1,password);
        API.registeredLogin(userName2,password);
        API.registeredLogin(userName3,password);

        API.openStore(userId,"eladStore");
        API.registeredLogout(userId);
        API.registeredLogin(userName3,password);
        Result result = API.getSystemManagerStats();
        int numberOfOwnersUsers = (int)((JSONObject)result.getData()).get("OwnersCounter");
        assertEquals(numberOfOwnersUsers,2);

    }

    @Test
    public void ManagersCounter(){
        String userName1="kandabior1";
        String password= "or321654";
        API.register(userName1,password, 20);
        String userName2="kandabior2";
        String userName3="kandabior3";
        API.register(userName1,password, 20);
        int managerId =(int) API.register(userName2,password, 20).getData();
        int ownerId =(int)API.register(userName3,password, 20).getData();

        API.registeredLogin(userName1,password);
        API.registeredLogin(userName2,password);
        API.registeredLogin(userName3,password);

        int storeId = (int)API.openStore(ownerId,"eladStore").getData();
        API.addStoreManager(ownerId,managerId,storeId);

        Result result = API.getSystemManagerStats();
        int numberOfOwnersUsers = (int)((JSONObject)result.getData()).get("ManagersCounter");
        assertEquals(numberOfOwnersUsers,1);

    }








}

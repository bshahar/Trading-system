package Tests;

import Domain.Result;
import Persistence.DataBaseHelper;
import Service.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

public class NotficationsTest {

    private int registerId1;
    private int registerId2;
    private int registerId3;
    private int storeId1;
    private int productId1;
    private int productId2;

    private int observableId1;
    private int observableId2;

    @BeforeEach
    public void setUp() {
        DataBaseHelper.cleanAllTable();
        Properties testProps = new Properties();
        try {
            API.initTradingSystem();
            InputStream input = getClass().getClassLoader().getResourceAsStream("testsSetUp.properties");
            if(input != null)
                testProps.load(input);
            else
                throw new FileNotFoundException("Property file was not found.");
        } catch (Exception e) {
        }

        API.register(testProps.getProperty("user1name"), testProps.getProperty("user1password"), Integer.parseInt(testProps.getProperty("user1age")));
        API.register(testProps.getProperty("user2name"), testProps.getProperty("user2password"), Integer.parseInt(testProps.getProperty("user2age")));
        API.register(testProps.getProperty("user3name"), testProps.getProperty("user3password"), Integer.parseInt(testProps.getProperty("user3age")));

        registerId1 = (int) API.registeredLogin(testProps.getProperty("user1name"), testProps.getProperty("user1password")).getData();
        registerId2 = (int) API.registeredLogin(testProps.getProperty("user2name"), testProps.getProperty("user2password")).getData();
        registerId3 = (int) API.registeredLogin(testProps.getProperty("user3name"), testProps.getProperty("user3password")).getData();

        storeId1 = (int) API.openStore(registerId1, testProps.getProperty("storeNameTest")).getData();
        LinkedList<String> catList = new LinkedList<>();
        catList.add(testProps.getProperty("categoryFood"));

        productId1 = (int) API.addProduct(1, storeId1, testProps.getProperty("prodMilkName"), catList,
                Integer.parseInt(testProps.getProperty("milkPrice")),
                testProps.getProperty("descriptionFood"),
                Integer.parseInt(testProps.getProperty("prodQuantity1"))).getData();

        observableId1 = (int) API.addObservable(testProps.getProperty("observableName1")).getData();
        observableId2 = (int) API.addObservable(testProps.getProperty("observableName2")).getData();

        API.subscribeToObservable(observableId2, registerId2);
        API.subscribeToObservable(observableId2, registerId3);


    }

    @Test
    public void addObservableType(){
        Assertions.assertTrue(API.addObservable("Store").isResult());
    }

    @Test
    public void removeObservableTypeSuccess(){
        Assertions.assertTrue(API.removeObservable(observableId1).isResult());
    }
    @Test
    public void removeObservableTypeFailed(){
        Assertions.assertFalse(API.removeObservable(10).isResult());
    }

    @Test
    public void subscribeToNotExistObservable(){
        Assertions.assertFalse(API.subscribeToObservable(10,registerId1).isResult());
    }

    @Test
    public void subscribeToObservable(){
        Assertions.assertTrue(API.subscribeToObservable(observableId1,registerId1).isResult());
    }

    @Test
    public void unSubscribeToObservable(){
        Assertions.assertTrue(API.unsubscribeToObservable(observableId2,registerId2).isResult());
    }

    @Test
    public void notifyToSubscribersThatLoggedOutNumOfMessage(){

        API.registeredLogout(registerId2);
        API.registeredLogout(registerId3);
        API.notifyToSubscribers(observableId2,"msg1");
        API.notifyToSubscribers(observableId2,"msg2");
        API.notifyToSubscribers(observableId2,"msg3");
        Result result2 = API.getMessagesQueue(registerId2);
        Result result3 = API.getMessagesQueue(registerId3);
        Assertions.assertTrue(result2.isResult());
        Assertions.assertTrue(result3.isResult());

        Queue<String> queue2  =  (Queue<String>)result2.getData();
        Queue<String> queue3  =  (Queue<String>)result3.getData();

        Assertions.assertTrue(queue2.size()==3);
        Assertions.assertTrue(queue3.size()==3);

    }

    @Test
    public void notifyToSubscribersThatLoggedOutContentOfMessage(){

        API.registeredLogout(registerId2);
        API.notifyToSubscribers(observableId2,"msg1");
        API.notifyToSubscribers(observableId2,"msg2");
        API.notifyToSubscribers(observableId2,"msg3");
        Result result2 = API.getMessagesQueue(registerId2);
        Assertions.assertTrue(result2.isResult());

        Queue<String> queue2  =  (Queue<String>)result2.getData();
        Assertions.assertTrue(queue2.size()==3);

        Object[] list = queue2.toArray();
        Assertions.assertEquals(list[0],"msg1");
        Assertions.assertEquals(list[1],"msg2");
        Assertions.assertEquals(list[2],"msg3");
    }

    @Test
    public void notifyToSubscribersThatLogged(){

        API.notifyToSubscribers(observableId2,"msg1");
        API.notifyToSubscribers(observableId2,"msg2");
        API.notifyToSubscribers(observableId2,"msg3");
        Result result2 = API.getMessagesQueue(registerId2);
        Assertions.assertTrue(result2.isResult());

        Queue<String> queue2  =  (Queue<String>)result2.getData();

        Assertions.assertTrue(queue2.size()==0);

    }


    @Test
    public void notifyToMangersSubscribersToStore(){

        API.addStoreManager(registerId1,registerId2,storeId1);
        API.registeredLogout(registerId2);
        int notificationId = (int)API.getNotificationIdByStoreId(storeId1).getData();
        API.notifyToSubscribers(notificationId,"msg1");
        Result result2 = API.getMessagesQueue(registerId2);
        Assertions.assertTrue(result2.isResult());
        Queue<String> queue2  =  (Queue<String>)result2.getData();
        Assertions.assertTrue(queue2.size()==1);
    }


    @Test
    public void notifyToOwnersSubscribersToStore(){

        API.addStoreOwner(registerId1,registerId2,storeId1);
        API.registeredLogout(registerId2);
        API.registeredLogout(registerId1);
        int notificationId = (int)API.getNotificationIdByStoreId(storeId1).getData();
        API.notifyToSubscribers(notificationId,"msg1");
        Result result1 = API.getMessagesQueue(registerId1);
        Result result2 = API.getMessagesQueue(registerId2);

        Assertions.assertTrue(result1.isResult());
        Assertions.assertTrue(result2.isResult());

        Queue<String> queue1  =  (Queue<String>)result1.getData();
        Queue<String> queue2  =  (Queue<String>)result2.getData();

        Assertions.assertTrue(queue1.size()==1);
        Assertions.assertTrue(queue2.size()==1);
    }

    @Test
    public void notifyToUserThatNowNoLongerOwnerLoggedIn(){
        API.setSessionDemo(registerId2);
        API.addStoreOwner(registerId1,registerId2,storeId1);
        API.removeOwner(registerId1,registerId2,storeId1);

        Result result1 = API.getLoginMessagesQueue(registerId2);

        Assertions.assertTrue(result1.isResult());

        Queue<String> queue1  =  (Queue<String>)result1.getData();
        Assertions.assertTrue(queue1.size()==2);
    }

    @Test
    public void notifyToUserThatNowNoLongerManagerLoggedIn(){
        API.setSessionDemo(registerId2);
        API.addStoreManager(registerId1,registerId2,storeId1);
        API.removeManager(registerId1,registerId2,storeId1);
        Result result1 = API.getLoginMessagesQueue(registerId2);

        Assertions.assertTrue(result1.isResult());

        Queue<String> queue1  =  (Queue<String>)result1.getData();
        Assertions.assertTrue(queue1.size()==2);
    }

    @Test
    public void notifyToUserThatNoLonerOwnerLogout(){
        API.setSessionDemo(registerId2);
        API.addStoreOwner(registerId1,registerId2,storeId1);
        API.registeredLogout(registerId2);
        API.removeOwner(registerId1,registerId2,storeId1);
        Result result_logout = API.getMessagesQueue(registerId2);
        Result result_login = API.getLoginMessagesQueue(registerId2);
        Assertions.assertTrue(result_logout.isResult());
        Assertions.assertTrue(result_login.isResult());
        Queue<String> queue_logout  =  (Queue<String>)result_logout.getData();

        Assertions.assertTrue(queue_logout.size()==1);

        Object[] list_logout = queue_logout.toArray();
        Assertions.assertEquals(list_logout[0],"You are no longer owner in store: kandabior store");

    }

    @Test
    public void notifyToUserThatNoLonerManagerLogout(){
        API.setSessionDemo(registerId2);
        API.addStoreManager(registerId1,registerId2,storeId1);
        API.registeredLogout(registerId2);
        API.removeManager(registerId1,registerId2,storeId1);
        Result result_logout = API.getMessagesQueue(registerId2);
        Result result_login = API.getLoginMessagesQueue(registerId2);
        Assertions.assertTrue(result_logout.isResult());
        Assertions.assertTrue(result_login.isResult());
        Queue<String> queue_logout  =  (Queue<String>)result_logout.getData();

        Assertions.assertTrue(queue_logout.size()==1);

        Object[] list_logout = queue_logout.toArray();
        Assertions.assertEquals(list_logout[0],"You are no longer manager in store: kandabior store");



    }















}

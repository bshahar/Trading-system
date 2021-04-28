package Tests;

import Domain.ObservableType;
import Domain.Product;
import Domain.Result;
import Domain.User;
import Service.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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

        API.initTradingSystem("Elad");
        String userName1="kandabior";
        String password1= "or321654";
        String userName2="elad";
        String password2= "elad321654";
        String userName3="erez";
        String password3= "erez321654";
        API.register(userName1,password1);
        API.register(userName2,password2);
        API.register(userName3,password3);
        registerId1=(int) API.registeredLogin(userName1,password1).getdata();
        registerId2=(int) API.registeredLogin(userName2,password2).getdata();
        registerId3= (int)API.registeredLogin(userName3,password3).getdata();
        storeId1=(int)API.openStore(registerId1,"kandabior store").getdata();
        LinkedList<String> catList= new LinkedList<>();
        catList.add("FOOD");
        productId1= (int)API.addProduct(1, storeId1,"milk",catList ,10,"FOOD", 1 ).getdata();
        observableId1 = (int) API.addObservable("Store1").getdata();
        observableId2 = (int) API.addObservable("Store2").getdata();
        API.subscribeToObservable(observableId2,registerId2);
        API.subscribeToObservable(observableId2,registerId3);

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

        Queue<String> queue2  =  (Queue<String>)result2.getdata();
        Queue<String> queue3  =  (Queue<String>)result3.getdata();

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

        Queue<String> queue2  =  (Queue<String>)result2.getdata();
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

        Queue<String> queue2  =  (Queue<String>)result2.getdata();

        Assertions.assertTrue(queue2.size()==0);

    }


    @Test
    public void notifyToMangersSubscribersToStore(){

        API.addStoreManager(registerId1,registerId2,storeId1);
        API.registeredLogout(registerId2);
        int notificationId = (int)API.getNotificationIdByStoreId(storeId1).getdata();
        API.notifyToSubscribers(notificationId,"msg1");
        Result result2 = API.getMessagesQueue(registerId2);
        Assertions.assertTrue(result2.isResult());
        Queue<String> queue2  =  (Queue<String>)result2.getdata();
        Assertions.assertTrue(queue2.size()==1);
    }


    @Test
    public void notifyToOwnersSubscribersToStore(){

        API.addStoreOwner(registerId1,registerId2,storeId1);
        API.registeredLogout(registerId2);
        API.registeredLogout(registerId1);
        int notificationId = (int)API.getNotificationIdByStoreId(storeId1).getdata();
        API.notifyToSubscribers(notificationId,"msg1");
        Result result1 = API.getMessagesQueue(registerId1);
        Result result2 = API.getMessagesQueue(registerId2);

        Assertions.assertTrue(result1.isResult());
        Assertions.assertTrue(result2.isResult());

        Queue<String> queue1  =  (Queue<String>)result1.getdata();
        Queue<String> queue2  =  (Queue<String>)result2.getdata();

        Assertions.assertTrue(queue1.size()==1);
        Assertions.assertTrue(queue2.size()==1);

    }











}

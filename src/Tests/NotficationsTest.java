package Tests;

import Domain.ObservableType;
import Domain.Product;
import Domain.User;
import Service.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class NotficationsTest {


//    private static int registerId1;
//    private static  int registerId1;
//    private staticint registerId2;
//    private int registerId3;
//    private int storeId1;
//    private int productId1;
//    private int productId2;

    public static void main(String args[]) {
//        API.initTradingSystem("Elad");
//        String userName1="kandabior";
//        String password1= "or321654";
//        String userName2="elad";
//        String password2= "elad321654";
//        String userName3="erez";
//        String password3= "erez321654";
//        API.register(userName1,password1);
//        API.register(userName2,password2);
//        API.register(userName3,password3);
//        registerId1=(int) API.registeredLogin(userName1,password1).getdata();
//        registerId2=(int) API.registeredLogin(userName2,password2).getdata();
//        registerId3= (int)API.registeredLogin(userName3,password3).getdata();
//        storeId1=(int)API.openStore(registerId1,"kandabior store").getdata();
//        LinkedList<Product.Category> catList= new LinkedList<>();
//        catList.add(Product.Category.FOOD);
//        productId1= (int)API.addProduct(1, storeId1,"milk",catList ,10,"FOOD", 1 ).getdata();
        ObservableType observable = new ObservableType("store",1);
        User observer = new User("Elad",10,1);
        User observer2 = new User("Dorin",10,1);
        observer2.setLogged(true);
        observable.addObserver(observer);
        observable.addObserver(observer2);
        observable.sendAll("hello");
        int a = 2;


    }



}

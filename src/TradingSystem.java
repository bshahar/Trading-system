import org.json.simple.JSONObject;

import java.util.*;
import java.util.logging.Level;

public class TradingSystem {

    private static int userCounter ;

    private  PaymentAdapter paymentAdapter;
    private  SupplementAdapter supplementAdapter;
    private  List<Store> stores;
    private  Registered systemManager; //TODO change to whatever you want
    private  List<Receipt> receipts;
    private  List<User> users; //TODO every user is a thread
    private  HashMap<String,String> userPass; // TODO change that the password will be secure



    public TradingSystem (Registered systemManager) {
        this.stores = new LinkedList<>();
        this.receipts = new LinkedList<>();
        this.systemManager = systemManager;
        this.users = new LinkedList<>();
        this.userPass = new LinkedHashMap<>();
        this.userCounter = 1;
    }


    public void register(String userName, String pass) {
        if(userPass.containsKey(userName))
        {
            JSONObject obj = new JSONObject();
            obj.put("type", "FAIL");
            obj.put("msg", userName+"userName"+"already exist , try again\n");
            String msg = obj.toJSONString();
            System.out.println("User"+userName+"already exist , try again\n");
        }
        else
        {
            userPass.put(userName,pass);
            KingLogger.logEvent(Level.INFO,"User "+userName+" register to the system");
            System.out.println("Register Successfully! You can now enter the system");
            users.add(new User(userName,userCounter++));
        }
    }

    public void login(String userName,String pass) {
        if(loginAuthentication(userName,pass))
        {
            System.out.println("Login successfully!\n");
            KingLogger.logEvent(Level.INFO,"User "+userName+" enter the system\n");
            int id = -1;
            for(User user : users)
            {
                if(user.getUserName() == userName)
                {
                    id = user.getId();
                }
            }
            systemMainPageRegister(id);
        }
        else
        {
            System.out.println("Error Login with user name or password , try again!\n");
        }
    }

    private boolean loginAuthentication(String userName, String pass) {
        if(userPass.containsKey(userName))//write like this for the error log
        {
            if(userPass.get(userName).equals(pass))
                return true;
            else
                KingLogger.logEvent(Level.INFO,"User try to login with name "+userName+" and pass "+pass+" and Failed");
        }
        else
        {
            KingLogger.logEvent(Level.INFO,"User try to login with name "+userName+" that doesn't exist");
        }
        return false;
    }

    private void guestEnter() {
        users.add(new User("Guest",-1));//TODO check what to give to guest user
        systemMainPageGuest(-1);
    }

    private void systemMainPageGuest(int userId)
    {
        int ans = -1;
        do {
            printSystemMainPageGuest(userId);
            ans = getValidInput(1,5);
            switch (ans)
            {
                case 1:
                    storesInfo();
                    break;
                case 2:
                    search(userId);
                    break;
                case 3:
                    editBag(userId);
                    break;
                case 4:
                    register(null,null);
                    break;
                case 5:
                    System.out.println("Bye Bye!");
                    break;
            }
        }while (ans != 4);

    }



    //main page when login to the system
    private void systemMainPageRegister(int userId)
    {
        int ans = -1;
        do {
            printSystemMainPageRegister(userId);
            ans = getValidInput(1,10);
            switch (ans)
            {
                case 1:
                    storesInfo();
                    break;
                case 2:
                    search(userId);
                    break;
                case 3:
                    editBag(userId);
                    break;
                case 4:
                    openStore(userId);
                    break;
                case 5:
                    writeReview(userId);
                    break;
                case 6:
                    rank(userId);
                    break;
                case 7:
                    contact(userId);
                    break;
                case 8:
                    purchasesHistory(userId);
                    break;
                case 9:
                    editInfo(userId);
                    break;
                case 10:
                    System.out.println("Bye Bye!");
                    break;
            }
        }while (ans != 4);

    }

    private void editInfo(int userId) {
    }

    private void purchasesHistory(int userId) {
    }

    private void contact(int userId) {
    }

    private void rank(int userId) {
    }

    private void writeReview(int userId) {
    }

    private void openStore(int userId) {
    }

    private void editBag(int userId) {
    }

    private void search(int userId) {
    }

    private void storesInfo() {
        //TODO print the stores names and let the user choose one
        int storeIndex = getValidInput(1, stores.size());
        if(storeIndex != -1) {
            Store s = getStoreByIndex(storeIndex);
            System.out.print(s.getStoreInfo());
        }
        else
            System.out.println("invalid choice, try again");
    }

    private Store getStoreByIndex(int index) {
        Store[] storesArr = (Store[]) stores.toArray();
        return storesArr[index-1];
    }

    private int getValidInput(int min , int max)
    {
        int ans = -1;
        do {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            try {
                ans = Integer.parseInt(input);
                if (ans < min || ans > max) {
                    System.out.println("invalid choice, try again");
                }
                else
                    return ans;
            } catch (Exception e) {
                System.out.println("invalid choice, try again");
            }
        }while (ans>=min || ans<=max);
        return -1;
    }


    private void printMainMenu()
    {
        System.out.println("Enter your choose:");
        System.out.println("1.Enter as guest");
        System.out.println("2.Login");
        System.out.println("3.Register");
        System.out.println("4.Exit");
    }

    private void printSystemMainPageRegister(int userId)
    {
        System.out.println("Enter your choose:");
        System.out.println("1.Info about stores and products");
        System.out.println("2.Search & add products to bag");
        System.out.println("3.Edit my bags");
        System.out.println("4.Open Store");
        System.out.println("5.Write review");
        System.out.println("6.Rank store & products");
        System.out.println("7.Contact us!(Questions , request , complains)");
        System.out.println("8.Purchases history");
        System.out.println("9.Show and edit details");
        System.out.println("10.Logout");

    }

    private void printSystemMainPageGuest(int userId)
    {
        System.out.println("Enter your choose:");
        System.out.println("1.Info about stores and products");
        System.out.println("2.Search & add products to bag");
        System.out.println("3.Edit my bags");
        System.out.println("4.Register");
        System.out.println("5.Exit the system");

    }







}

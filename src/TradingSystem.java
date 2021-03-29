import java.util.*;
import java.util.logging.Level;

public class TradingSystem {

    private static int userCounter ;

    private static PaymentAdapter paymentAdapter;
    private static SupplementAdapter supplementAdapter;
    private static List<Store> stores;
    private static Registered systemManager; //TODO change to whatever you want
    private static List<Receipt> receipts;
    private static List<User> users; //TODO every user is a thread
    private static HashMap<String,String> userPass; // TODO change that the password will be secure



    private static void initializeSystem(Registered systemManager) {
        stores = new LinkedList<>();
        receipts = new LinkedList<>();
        systemManager = systemManager;
        users = new LinkedList<>();
        userPass = new LinkedHashMap<>();
        userCounter = 1;
    }

    public static void main(String[] args) {
        initializeSystem(null);// TODO add Manager somehow

        System.out.println("Welcome to EOE Trading System!\n");
        int ans = 0;
        do
        {
            printMainMenu();
            Scanner scanner = new Scanner(System.in);
            ans = getValidInput(1,4);
                    switch(ans) {
                        case 1:
                            guestEnter();
                            break;
                        case 2:
                            login();
                            break;
                        case 3:
                            register();
                            break;
                        case 4:
                            System.out.println("Bye Bye!\n");
                            break;
                    }
        }
        while(ans!=4);

    }

    private static void register() {
        //TODO add restriction on password
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter user name");
        String userName = scanner.nextLine();
        System.out.println("Please Enter password");
        String pass = scanner.nextLine();
        if(userPass.containsKey(userName))
        {
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

    private static void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter Your user name");
        String userName = scanner.nextLine();
        System.out.println("Please Enter Your password");
        String pass = scanner.nextLine();
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

    private static boolean loginAuthentication(String userName, String pass) {
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

    private static void guestEnter() {
        users.add(new User("Guest",-1));//TODO check what to give to guest user
        systemMainPageGuest(-1);
    }

    private static void systemMainPageGuest(int userId)
    {
        int ans = -1;
        do {
            printSystemMainPageGuest(userId);
            ans = getValidInput(1,5);
            switch (ans)
            {
                case 1:
                    storeInfo(userId);
                    break;
                case 2:
                    search(userId);
                    break;
                case 3:
                    editBag(userId);
                    break;
                case 4:
                    register();
                    break;
                case 5:
                    System.out.println("Bye Bye!");
                    break;
            }
        }while (ans != 4);

    }



    //main page when login to the system
    private static void systemMainPageRegister(int userId)
    {
        int ans = -1;
        do {
            printSystemMainPageReister(userId);
            ans = getValidInput(1,10);
            switch (ans)
            {
                case 1:
                    storeInfo(userId);
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

    private static void editInfo(int userId) {
    }

    private static void purchasesHistory(int userId) {
    }

    private static void contact(int userId) {
    }

    private static void rank(int userId) {
    }

    private static void writeReview(int userId) {
    }

    private static void openStore(int userId) {
    }

    private static void editBag(int userId) {
    }

    private static void search(int userId) {
    }

    private static void storeInfo(int userId) {
    }

    private static int getValidInput(int min , int max)
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


    private static void printMainMenu()
    {
        System.out.println("Enter your choose:");
        System.out.println("1.Enter as guest");
        System.out.println("2.Login");
        System.out.println("3.Register");
        System.out.println("4.Exit");
    }

    private static void printSystemMainPageReister(int userId)
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

    private static void printSystemMainPageGuest(int userId)
    {
        System.out.println("Enter your choose:");
        System.out.println("1.Info about stores and products");
        System.out.println("2.Search & add products to bag");
        System.out.println("3.Edit my bags");
        System.out.println("4.Register");
        System.out.println("5.Exit the system");

    }







}

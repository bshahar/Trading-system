
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import org.json.simple.JSONObject;


public class client {

    public static void main(String []args) throws IOException {
        client client = new client();
        client.startConnection("127.0.0.1", 5555);
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
    private static void register() throws IOException {
        //TODO add restriction on password
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter user name");
        String userName = scanner.nextLine();
        System.out.println("Please Enter password");
        String pass = scanner.nextLine();

        JSONObject obj = new JSONObject();
        obj.put("type", "REGISTER");
        obj.put("name", userName);
        obj.put("pass", pass);
        String msg = obj.toJSONString();

        String response = client.sendMessage(msg);
        System.out.println(response);
    }

    private static void login() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter Your user name");
        String userName = scanner.nextLine();
        System.out.println("Please Enter Your password");
        String pass = scanner.nextLine();
        if(loginAuthentication(userName,pass)) {
            System.out.println("Login successfully!\n");
        }
        else
        {
            System.out.println("Error Login with user name or password , try again!\n");
        }
    }

    private static boolean loginAuthentication(String userName, String pass) throws IOException {
        String response = client.sendMessage("LOGIN name "+userName+" pass "+pass);
        System.out.println(response);
        return (response.equals("TRUE"));
    }

    private static void guestEnter() throws IOException {
        //users.add(new User("Guest",-1));//TODO check what to give to guest user
        systemMainPageGuest(-1);
    }

    private static void systemMainPageGuest(int userId) throws IOException {
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

    private static void storesInfo() {
        //TODO print the stores names and let the user choose one
//        int storeIndex = getValidInput(1, stores.size());
//        if(storeIndex != -1) {
//            Store s = getStoreByIndex(storeIndex);
//            System.out.print(s.getStoreInfo());
//        }
//        else
//            System.out.println("invalid choice, try again");
    }

//    private static Store getStoreByIndex(int index) {
//        //Store[] storesArr = (Store[]) stores.toArray();
//        //return storesArr[index-1];
//        return null;
//    }

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

    private static void printSystemMainPageRegister(int userId)
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



        private Socket clientSocket;
        private static PrintWriter out;
        private static BufferedReader in;

        public void startConnection(String ip, int port) throws IOException {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        public static String sendMessage(String msg) throws IOException {
            out.println(msg);
            String resp = in.readLine();
            return resp;
        }

        public void stopConnection() throws IOException {
            in.close();
            out.close();
            clientSocket.close();
        }
    }

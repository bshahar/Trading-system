package Domain;

import Domain.DiscountFormat.*;
import Domain.DiscountPolicies.DiscountCondition;
import Domain.Operators.LogicOperator;
import Domain.Operators.NoneOperator;
import Domain.PurchaseFormat.ImmediatePurchase;
import Domain.PurchaseFormat.Purchase;
import Domain.PurchasePolicies.PurchaseCondition;
import Service.counter;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private int storeId;
    private int notificationId;
    private String name;
    private Inventory inventory;
    private List<User> employees;
    private List<User> owners;
    private List<User> managers;
    private List<Receipt> receipts;
    private Service.counter counter;
    private Map<Product, Discount> discountsOnProducts;
    private Map<String, Discount> discountsOnCategories;
    private Discount discountsOnStore;
    private Map<Product, ImmediatePurchase> purchasesOnProducts;
    private Map<String, ImmediatePurchase> purchasesOnCategories;
    private ImmediatePurchase purchasesOnStore;
    //private List<ImmediatePurchase> purchasePoliciesInStore;
    private Map<User,List<User>> appointments; //appointer & list of appointees
    private Map<Integer, Bag> usersBags;
    private double rate;
    private int ratesCount;


    public Store(int id, String name, User owner) { //create a store with empty inventory
        this.storeId = id;
        this.name = name;
        //this.shoppingBags = new LinkedList<>();
        this.inventory = new Inventory();
        this.rate = 0;
        this.ratesCount = 0;
        this.employees = Collections.synchronizedList(new LinkedList<>());
        this.employees.add(owner);
        this.receipts = new LinkedList<>();
        this.appointments = new HashMap<>();
        this.appointments.put(owner, new LinkedList<>());
        this.owners = Collections.synchronizedList(new LinkedList<>());
        this.managers = Collections.synchronizedList(new LinkedList<>());
        this.discountsOnProducts = new ConcurrentHashMap<>();
        this.discountsOnCategories = new ConcurrentHashMap<>();
        this.counter = new counter();
        this.usersBags = new HashMap<>();
        this.purchasesOnProducts = new ConcurrentHashMap<>();
        this.purchasesOnCategories = new ConcurrentHashMap<>();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public double getRate() {
        return rate;
    }

    public void setNotificationId(int notificationId)
    {
        this.notificationId = notificationId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public boolean addToInventory(User currUser, Product prod, int numOfProd) {
        return this.inventory.addProduct(prod , numOfProd);
    }

    public String getName() {
        return name;
    }

    private boolean validateProductId(int id){
        return this.inventory.validateProductId(id);
    }

    public int getStoreId() {
        return storeId;
    }

    public boolean addProductToStore(int productId,  String name, List<String> categories, double price, String description, int quantity) {
        Product p = new Product(productId, name, categories, price, description);
        return this.inventory.addProduct(p, quantity);
    }

    public Result removeProductFromStore( int productId) {
        synchronized (inventory){
            if(this.inventory.prodExists(productId)){
                return this.inventory.removeProduct(productId);
            }
            else{
                return new Result(false,"Product not exist");
            }

        }
    }

    public List<Integer> getProductsByName(Filter filter){
         return this.inventory.getProductsByName(filter,this.rate);
    }

    public List<Integer> getProductsByCategory(Filter filter) {
        return this.inventory.getProductsByCategory(filter,this.rate);
    }

    public List<Integer> getProductsByKeyWords(Filter filter) {

        return this.inventory.getProductsByKeyWords(filter, this.rate);
    }

    public List<Integer> getProductsByPriceRange(String[] filter) {
        return this.inventory.getProductsByPriceRange(filter);
    }


    public Result removeManager(User owner, User manager) {
        if(appointments.get(owner).remove(manager)){
            employees.remove(manager);
            manager.removeFromMyStores(this);

            if(appointments.containsKey(manager)){
                List<User> managers=appointments.get(manager);
                for(User user : managers){
                    this.managers.remove(user);
                    removeManager(manager,user);
                }

            }
            return new Result(true,true);
        }
        return new Result(false,"Remove of the manager has failed");
    }

    public List<User> getWorkersInformation(int ownerId) {
        return this.employees;
    }

    public boolean getStorePurchaseHistory(int ownerId) {
        return true;
    }//TODO

    public Product getProductById(int id) {
        return inventory.getProductById(id);
    }

    public boolean canBuyProduct(Product product, int amount) {
        return inventory.canBuyProduct(product,amount);
    }

    public void removeProductAmount(Product product, Integer amount) {
        inventory.removeProductAmount(product,amount);
    }

    public void addEmployee(User owner,User user) {
        this.employees.add(user);
        if (!this.appointments.containsKey(owner)) {
            this.appointments.put(owner, new LinkedList<>());
        }
        this.appointments.get(owner).add(user);
    }
    public Result getEmployees()
    {
        return new Result(true,this.employees);
    }

    public Result getPurchaseHistory() {
        return new Result(true,this.receipts);
    }

    public void addOwnerToAppointments( User user) {
        appointments.put(user,new LinkedList<>());
    }

    public void addReceipt(Receipt receipt)
    {
        this.receipts.add(receipt);
    }

    public boolean addOwner(User user) {
        if(this.owners.contains(user))
            return false;
        owners.add(user);
        return true;
    }
    public boolean addManager(User user)
    {
        synchronized (managers) {
            if (this.managers.contains(user))
                return false;
            managers.add(user);
            return true;
        }
    }

    public void abortPurchase(Map<Product, Integer> productsAmount) {
        for(Product product : productsAmount.keySet()){
            synchronized (product){
                this.inventory.addProductAmount(product,productsAmount.get(product));
            }
        }
    }

    public void addDiscountOnProduct(int prodId, Date begin, Date end, DiscountCondition conditions, int percentage, Discount.MathOp op) {
        this.discountsOnProducts.put(getProductById(prodId), new ConditionalDiscount(counter.inc(), begin, end, conditions, percentage, op));
    }

    public void addDiscountOnCategory(String category, Date begin, Date end, DiscountCondition conditions, int percentage, Discount.MathOp op) {
        this.discountsOnCategories.put(category, new ConditionalDiscount(counter.inc(), begin, end, conditions, percentage, op));
    }

    public void addDiscountOnStore(Date begin, Date end, DiscountCondition conditions, int percentage, Discount.MathOp op) {
        this.discountsOnStore = new ConditionalDiscount(counter.inc(), begin, end, conditions, percentage, op);
    }

    public void addPurchaseOnProduct(int prodId, PurchaseCondition conditions) {
        this.purchasesOnProducts.put(getProductById(prodId), new ImmediatePurchase(counter.inc(), conditions));
    }

    public void addPurchaseOnCategory(String category, PurchaseCondition conditions) {
        this.purchasesOnCategories.put(category, new ImmediatePurchase(counter.inc(), conditions));
    }

    public void addPurchaseOnStore(PurchaseCondition conditions) {
        this.purchasesOnStore = new ImmediatePurchase(counter.inc(), conditions);
    }

    public void removeDiscountOnProduct(int prodId){
        Product prod = this.inventory.getProductById(prodId);
        this.discountsOnProducts.remove(prod);
    }

    public void removeDiscountOnCategory(String category){
        this.discountsOnCategories.remove(category);
    }

    public void removeDiscountOnStore(){
        this.discountsOnStore = null;
    }

    public void removePurchaseOnProduct(int prodId){
        Product prod = this.inventory.getProductById(prodId);
        this.purchasesOnProducts.remove(prod);
    }

    public void removePurchaseOnCategory(String category){
        this.purchasesOnCategories.remove(category);
    }

    public void removePurchaseOnStore(){
        this.purchasesOnStore = null;
    }

   /* public double calculateDiscounts(double totalCost, User user, String mathOperator) {
        Bag bag = this.usersBags.get(user.getId());
        switch (mathOperator) {
            case "Max":
                double discount = 0;
                double temp;
                if (this.discountsOnStore.size() > 0)
                    discount = calcStoreDiscount(totalCost, user, bag);
                if (this.discountsOnCategories.size() > 0) {
                    temp = calcCategoriesDiscount(totalCost, user, bag);
                    if (discount < temp)
                        discount = temp;
                }
                if (this.discountsOnProducts.size() > 0) {
                    temp = calcProductsDiscount(totalCost, user, bag);
                    if (discount < temp)
                        discount = temp;
                }
                totalCost -= discount;
                break;
            default:
                if (this.discountsOnStore.size() > 0)
                    totalCost -= calcStoreDiscount(totalCost, user, bag);
                if (this.discountsOnCategories.size() > 0)
                    totalCost -= calcCategoriesDiscount(totalCost, user, bag);
                if (this.discountsOnProducts.size() > 0)
                    totalCost -= calcProductsDiscount(totalCost, user, bag);
                break;
        }
        return totalCost;
    }
*/
    public double calcDiscountPerProduct(Product prod, Date date, User user, Bag bag){
        List<Double> SumDiscount = new LinkedList<>();
        List<Double> MaxDiscount = new LinkedList<>();
        double discountProduct = 0;
        double discountCategory = 0;
        double discountStore = 0;
        if (this.discountsOnProducts.containsKey(prod)) {
            Discount dis = discountsOnProducts.get(prod);
            if(dis != null) {
                discountProduct = discountsOnProducts.get(prod).calculateDiscount(prod, user, date, bag);
                if (discountsOnProducts.get(prod).getMathOp().equals(Discount.MathOp.MAX))
                    MaxDiscount.add(discountProduct);
                else
                    SumDiscount.add(discountProduct);
            }
        }
        for (String cat:prod.getCategories()) {
            Discount dis = discountsOnCategories.get(cat);
            if(dis != null) {
                discountCategory = dis.calculateDiscount(prod, user, date, bag);
                if (discountsOnProducts.get(prod).getMathOp().equals(Discount.MathOp.MAX))
                    MaxDiscount.add(discountCategory);
                else
                    SumDiscount.add(discountCategory);
            }
        }
        if (this.discountsOnStore != null) {
            discountStore = discountsOnStore.calculateDiscount(prod,user,date,bag);
            if(discountsOnStore.getMathOp().equals(Discount.MathOp.MAX))
                MaxDiscount.add(discountStore);
            else
                SumDiscount.add(discountStore);
        }

        double finalDiscount = 0;
        for (double disc: SumDiscount) {
            finalDiscount += disc;
        }
        for (double disc:MaxDiscount) {
            if(disc > finalDiscount)
                finalDiscount = disc;
        }
        return Math.min(finalDiscount, bag.getBagTotalCost()); //if discount > 100% return bag total cost (100% discount)

    }
/*
    private double calcStoreDiscount(double totalCost, User user, Bag bag) {
        double discount = 0;
        for (Discount disCon: this.discountsOnStore) {
            discount += disCon.calculateDiscount(totalCost, user, new Date(), bag);
        }
        return discount;
    }

    private double calcCategoriesDiscount(double totalCost, User user, Bag bag) {
        double discount = 0;
        Bag discountProds = new Bag(this);
        for (Product prod: bag.getProducts()) {
            for (Product.Category category: prod.getCategories()) {
                if (this.discountsOnCategories.containsKey(category))
                    discountProds.addProduct(prod, bag.getProductsAmounts().get(prod));
            }
        }
        for (Discount disCon : this.discountsOnStore) {
            discount += disCon.calculateDiscount(totalCost, user, new Date(), discountProds);
        }
        return discount;
    }

    private double calcProductsDiscount(double totalCost, User user, Bag bag) {
        double discount = 0;
        Bag discountProds = new Bag(this);
        for (Product prod : bag.getProducts()) {
            if (this.discountsOnProducts.containsKey(prod)) {
                discountProds.addProduct(prod, bag.getProductsAmounts().get(prod));
            }
        }
        for (Discount disCon : this.discountsOnStore) {
            discount += disCon.calculateDiscount(totalCost, user, new Date(), bag);
        }
        return discount;
    }
*/
    public void addSimpleDiscountOnProduct(int prodId, Date begin, Date end, int percentage, Discount.MathOp op) {
        this.discountsOnProducts.put(getProductById(prodId), new SimpleDiscount(counter.inc(), begin, end, percentage, op));
    }

    public void addSimpleDiscountOnCategory(String category, Date begin, Date end, int percentage, Discount.MathOp op) {
        this.discountsOnCategories.put(category, new SimpleDiscount(counter.inc(), begin, end, percentage, op));
    }

    public void addSimpleDiscountOnStore(Date begin, Date end, int percentage, Discount.MathOp op) {
        this.discountsOnStore = new SimpleDiscount(counter.inc(), begin, end, percentage, op);
    }

    public boolean validatePurchasePerProduct(Product prod ,User user, Date time, Bag bag){
        boolean isValid = true;
        if (this.purchasesOnProducts.containsKey(prod)) {
            ImmediatePurchase ip = purchasesOnProducts.get(prod);
            if(ip != null)
                isValid = isValid && ip.validatePurchase(user, time, bag);
        }
        for (String cat:prod.getCategories()) {
            ImmediatePurchase ip = purchasesOnCategories.get(cat);
            if(ip != null)
                isValid = isValid && ip.validatePurchase(user, time, bag);
        }
        if (this.purchasesOnStore != null)
            isValid = isValid && purchasesOnStore.validatePurchase(user, time, bag);
        return isValid;

    }

    public Result viewDiscountPoliciesOnProduct(int prodId) {
        Product product = getProductById(prodId);
        if(product != null) {
            List<Object> discountPolicies = new LinkedList<>();
            Discount dis = this.discountsOnProducts.get(product);
            List<Pair<String, List<String>>> policiesParams = new LinkedList<>();
            if(dis instanceof ConditionalDiscount) {
                discountPolicies.add(((ConditionalDiscount) dis).getConditions().getOperatorStr());
                for (Policy policy: ((ConditionalDiscount) dis).getConditions().getDiscounts()) {
                    policiesParams.add(new Pair<>(policy.getPolicyName(), policy.getPolicyParams()));
                }
            }
            else
                discountPolicies.add(""); //logic operator- if simple discount then empty
            discountPolicies.add(policiesParams);
            discountPolicies.add(dis.getBegin().toString());
            discountPolicies.add(dis.getEnd().toString());
            discountPolicies.add(String.valueOf(dis.getPercentage()));
            discountPolicies.add(dis.getMathOpStr());
            return new Result(true, discountPolicies);
        }
        return new Result(false, "No discount policies on this product.");
    }

    public Result viewDiscountPoliciesOnCategory(String category) {
        if(category != null) {
            List<Object> discountPolicies = new LinkedList<>();
            Discount dis = this.discountsOnCategories.get(category);
            List<Pair<String, List<String>>> policiesParams = new LinkedList<>();
            if(dis instanceof ConditionalDiscount) {
                discountPolicies.add(((ConditionalDiscount) dis).getConditions().getOperatorStr());
                for (Policy policy: ((ConditionalDiscount) dis).getConditions().getDiscounts()) {
                    policiesParams.add(new Pair<>(policy.getPolicyName(), policy.getPolicyParams()));
                }
            }
            else
                discountPolicies.add(""); //logic operator- if simple discount then empty
            discountPolicies.add(policiesParams);
            discountPolicies.add(dis.getBegin().toString());
            discountPolicies.add(dis.getEnd().toString());
            discountPolicies.add(String.valueOf(dis.getPercentage()));
            discountPolicies.add(dis.getMathOpStr());
            return new Result(true, discountPolicies);
        }
        return new Result(false, "No discount policies on this category.");
    }

    public Result viewDiscountPoliciesOnStore() {
        if(this.discountsOnStore != null) {
            List<Object> discountPolicies = new LinkedList<>();
            Discount dis = this.discountsOnStore;
            List<Pair<String, List<String>>> policiesParams = new LinkedList<>();
            if(dis instanceof ConditionalDiscount) {
                discountPolicies.add(((ConditionalDiscount) dis).getConditions().getOperatorStr());
                for (Policy policy: ((ConditionalDiscount) dis).getConditions().getDiscounts()) {
                    policiesParams.add(new Pair<>(policy.getPolicyName(), policy.getPolicyParams()));
                }
            }
            else
                discountPolicies.add(""); //logic operator- if simple discount then empty
            discountPolicies.add(policiesParams);
            discountPolicies.add(this.dateToString(dis.getBegin()));
            discountPolicies.add(this.dateToString(dis.getEnd()));
            discountPolicies.add(String.valueOf(dis.getPercentage()));
            discountPolicies.add(dis.getMathOpStr());
            return new Result(true, discountPolicies);
        }
        return new Result(false, "No discount policies in this store.");
    }

    public Result viewPurchasePoliciesOnProduct(int prodId) {
        Product product = getProductById(prodId);
        if(product != null && this.purchasesOnProducts.containsKey(product)) {
            List<Object> purchasePolicies = new LinkedList<>();
            ImmediatePurchase ip = this.purchasesOnProducts.get(product);
            List<Pair<String, List<String>>> policiesParams = new LinkedList<>();
            for (Policy policy : ((ImmediatePurchase) ip).getConditions().getPurchases()) {
                policiesParams.add(new Pair<>(policy.getPolicyName(), policy.getPolicyParams()));
            }
            purchasePolicies.add(ip.getConditions().getOperator());
            purchasePolicies.add(policiesParams);

            return new Result(true, purchasePolicies);
        }
        return new Result(false, "No purchase policies on this product.");
    }


    private String dateToString(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE)+"/"+(calendar.get(Calendar.MONTH)+1) +"/" + calendar.get(Calendar.YEAR);
    }

    public Result viewPurchasePoliciesOnCategory(String category) {
        if(category != null && this.purchasesOnCategories.containsKey(category)) {
            List<Object> purchasePolicies = new LinkedList<>();
            ImmediatePurchase ip = this.purchasesOnProducts.get(category);
            List<Pair<String, List<String>>> policiesParams = new LinkedList<>();
            for (Policy policy : ((ImmediatePurchase) ip).getConditions().getPurchases()) {
                policiesParams.add(new Pair<>(policy.getPolicyName(), policy.getPolicyParams()));
            }
            purchasePolicies.add(ip.getConditions().getOperator());
            purchasePolicies.add(policiesParams);
            return new Result(true, purchasePolicies);
        }
        return new Result(false, "No purchase policies on this product.");
    }

    public Result viewPurchasePoliciesOnStore() {
        if(this.purchasesOnStore != null) {
            List<Object> purchasePolicies = new LinkedList<>();
            ImmediatePurchase ip = this.purchasesOnStore;
            List<Pair<String, List<String>>> policiesParams = new LinkedList<>();
            for (Policy policy : ((ImmediatePurchase) ip).getConditions().getPurchases()) {
                policiesParams.add(new Pair<>(policy.getPolicyName(), policy.getPolicyParams()));
            }
            purchasePolicies.add(ip.getConditions().getOperator());
            purchasePolicies.add(policiesParams);
            return new Result(true, purchasePolicies);
        }
        return new Result(false, "No purchase policies on this product.");
    }

    public boolean isManager(User user) {
        return this.managers.contains(user);
    }

    public boolean prodExists(int prodId){ return this.inventory.prodExists(prodId); }


    public Set<Integer> getManagersAndOwners() {
        Set<Integer> list = new HashSet<>();
        for(User user: managers)
        {
            list.add(user.getId());
        }
        for(User user: owners)
        {
            list.add(user.getId());
        }
        return list;
    }

    public Result removeOwner(User owner, User ownerToDelete) {
        if(appointments.get(owner).remove(ownerToDelete)){
            employees.remove(ownerToDelete);
            ownerToDelete.removeFromMyStores(this);
            if(appointments.containsKey(ownerToDelete)){
                List<User> ownersList=appointments.get(ownerToDelete);
                for(User user : ownersList){
                    this.owners.remove(user);
                    removeOwner(ownerToDelete,user);
                }
            }
            return new Result(true,true);
        }
        return new Result(false,"Remove of the manager has failed");
    }

    public void setProductAmount(Product product, int amount) {
        inventory.setProductAmount(product,amount);
    }

    public int getProductAmount(Integer prodId) {
        return inventory.getProductsAmounts().get(getProductById(prodId));
    }
}



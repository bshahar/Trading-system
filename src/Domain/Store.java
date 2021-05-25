package Domain;

import Domain.DiscountFormat.ConditionalDiscount;
import Domain.DiscountFormat.Discount;
import Domain.DiscountFormat.SimpleDiscount;
import Domain.DiscountPolicies.DiscountCondition;
import Domain.PurchaseFormat.ImmediatePurchase;
import Domain.PurchaseFormat.PurchaseOffer;
import Domain.PurchasePolicies.PurchaseCondition;
import Persistence.*;
import Service.counter;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private int storeId;
    private int notificationId;
    private String name;
    private Inventory inventory;
    private StoreEmployeesWrapper employees;
    private StoreOwnerWrapper owners;
    private StoreManagerWrapper managers;
    private StoreReceiptWrapper receipts;
    private Service.counter counter;
    private Service.counter offerCounter;
    private DiscountsOnProductsWrapper discountsOnProducts;
    private DiscountsOnCategoriesWrapper discountsOnCategories;
    private DiscountsOnStoresWrapper discountsOnStore;
    private Map<Product, ImmediatePurchase> purchasesOnProducts;
    private Map<String, ImmediatePurchase> purchasesOnCategories;
    private ImmediatePurchase purchasesOnStore;
    private AppointmentsWrapper appointments; //appointer & list of appointees
    //private Map<Product,LinkedList<Pair<User,Double>>> offersOnProduct;
    private Map<Product, LinkedList<PurchaseOffer>> offersOnProduct;
    private double rate;
    private int ratesCount;


    public Store(int id, String name, User owner) { //create a store with empty inventory
        this.storeId = id;
        this.name = name;
        this.inventory = new Inventory();
        this.rate = 0;
        this.ratesCount = 0;
        this.employees = new StoreEmployeesWrapper();
        //this.employees.add(owner);
        this.receipts = new StoreReceiptWrapper();
        this.appointments = new AppointmentsWrapper();
//        this.appointments.put(owner, new LinkedList<>());
        this.owners = new StoreOwnerWrapper();
        this.managers = new StoreManagerWrapper();
        this.discountsOnProducts = new DiscountsOnProductsWrapper();
        this.discountsOnCategories = new DiscountsOnCategoriesWrapper();
        this.counter = new counter();
        this.offerCounter = new counter();
        this.purchasesOnProducts = new ConcurrentHashMap<>();
        this.purchasesOnCategories = new ConcurrentHashMap<>();
        this.offersOnProduct = new ConcurrentHashMap<>();
    }

    public Store(int id, String name) { //create a store with empty inventory
        this.storeId = id;
        this.name = name;
        this.inventory = new Inventory();
        this.rate = 0;
        this.ratesCount = 0;
        this.employees = new StoreEmployeesWrapper();
//        this.employees.add(owner,storeId);
        this.receipts = new StoreReceiptWrapper();
        this.appointments = new AppointmentsWrapper();
//        this.appointments.put(owner, new LinkedList<>());
        this.owners = new StoreOwnerWrapper();
        this.managers = new StoreManagerWrapper();
        this.discountsOnProducts = new DiscountsOnProductsWrapper();
        this.discountsOnCategories = new DiscountsOnCategoriesWrapper();
        this.counter = new counter();
        this.offerCounter = new counter();
        this.purchasesOnProducts = new ConcurrentHashMap<>();
        this.purchasesOnCategories = new ConcurrentHashMap<>();
        this.offersOnProduct = new ConcurrentHashMap<>();
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
        return this.inventory.addProduct(prod , numOfProd ,storeId);
    }

    public String getName() {
        return name;
    }

    private boolean validateProductId(int id){
        return this.inventory.validateProductId(id,storeId);
    }

    public int getStoreId() {
        return storeId;
    }

    public boolean addProductToStore(int productId,  String name, List<String> categories, double price, String description, int quantity) {
        Product p = new Product(productId, name, categories, price, description,this.storeId);
        return this.inventory.addProduct(p, quantity,storeId);
    }

    public Result removeProductFromStore( int productId) {
        synchronized (inventory){
            if(this.inventory.prodExists(productId,storeId)){
                return this.inventory.removeProduct(productId,storeId);
            }
            else{
                return new Result(false,"Product not exist");
            }

        }
    }

    public List<Integer> getProductsByName(Filter filter){
         return this.inventory.getProductsByName(filter,this.rate,storeId);
    }

    public List<Integer> getProductsByCategory(Filter filter) {
        return this.inventory.getProductsByCategory(filter,this.rate,storeId);
    }

    public List<Integer> getProductsByKeyWords(Filter filter) {

        return this.inventory.getProductsByKeyWords(filter, this.rate,storeId);
    }

    public List<Integer> getProductsByPriceRange(String[] filter) {
        return this.inventory.getProductsByPriceRange(filter,storeId);
    }


    public Result removeManager(User owner, User manager) {
//        if(appointments.get(owner).remove(manager)){

        if(appointments.removeAppointment(this.storeId,owner.getId(),manager.getId())){
            employees.remove(this.storeId,manager);
            manager.removeFromMyStores(this);

            List<User> managers=appointments.get(this.storeId,manager);
            for(User user : managers){
                this.managers.remove(this.storeId,user);
                removeManager(manager,user);
            }


            return new Result(true,true);
        }
        return new Result(false,"Remove of the manager has failed");
    }

    public List<User> getWorkersInformation(int ownerId) {
        return this.employees.getAll(storeId);
    }

    public boolean getStorePurchaseHistory(int ownerId) {
        return true;
    }//TODO

    public Product getProductById(int id) {
        return inventory.getProductById(id,storeId);
    }

    public List<User> getOwners(){return owners.getAll(storeId);}

    public List<User> getManagers(){return managers.getAll(storeId);}

    public Product getProductByName(String name) {
        return inventory.getProductByName(name,storeId);
    }

    public boolean canBuyProduct(Product product, int amount) {
        return inventory.canBuyProduct(product,amount,storeId);
    }

    public void removeProductAmount(Product product, Integer amount) {
        inventory.removeProductAmount(product,amount,storeId);
    }

    public void addEmployee(User owner,User user) {
        this.employees.add(user,storeId);
//        if (!this.appointments.containsKey(owner)) {
//            this.appointments.put(owner, new LinkedList<>());
//        }
//        this.appointments.get(owner).add(user);
        this.appointments.add(owner,user,storeId);
    }
    public Result getEmployees()
    {
        return new Result(true,this.employees.getAll(storeId));
    }

    public Result getPurchaseHistory() {
        return new Result(true,this.receipts.getAll(storeId));
    }

    public void addOwnerToAppointments( User user) {
//        appointments.put(user,new LinkedList<>());
    }

    public void addReceipt(Receipt receipt)
    {

        this.receipts.add(receipt,storeId);
    }

    public boolean addOwner(User user) {
        if(this.owners.contains(user,storeId))
            return false;
        owners.add(user,storeId);
        return true;
    }
    public boolean addManager(User user)
    {
        synchronized (managers) {
            if (this.managers.contains(user,storeId))
                return false;
            managers.add(user,storeId);
            return true;
        }
    }

    public void abortPurchase(Map<Product, Integer> productsAmount) {
        for(Product product : productsAmount.keySet()){
            synchronized (product){
                this.inventory.addProductAmount(product,productsAmount.get(product),storeId);
            }
        }
    }

    public void addDiscountOnProduct(int prodId, Date begin, Date end, DiscountCondition conditions, int percentage, Discount.MathOp op) {
        this.discountsOnProducts.add(this.storeId, getProductById(prodId), new ConditionalDiscount(counter.inc(), begin, end, conditions, percentage, op));
    }

    public void addDiscountOnCategory(String category, Date begin, Date end, DiscountCondition conditions, int percentage, Discount.MathOp op) {
        this.discountsOnCategories.add(this.storeId, category, new ConditionalDiscount(counter.inc(), begin, end, conditions, percentage, op));
    }

    public void addDiscountOnStore(Date begin, Date end, DiscountCondition conditions, int percentage, Discount.MathOp op) {
        this.discountsOnStore = new DiscountsOnStoresWrapper(new ConditionalDiscount(counter.inc(), begin, end, conditions, percentage, op));
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
        Product prod = this.inventory.getProductById(prodId,storeId);
        Discount discount = this.discountsOnProducts.get(prod);
        if(discount != null)
            this.discountsOnProducts.remove(discount);
    }

    public void removeDiscountOnCategory(String category){
        Discount dis = this.discountsOnCategories.get(category, this.storeId);
        if(dis != null)
            this.discountsOnCategories.remove(dis.getId());
    }

    public void removeDiscountOnStore() {
        Discount dis = this.discountsOnStore.get(this.storeId);
        if (dis != null)
            this.discountsOnStore.remove(dis.getId());
        this.discountsOnStore = null;
    }

    public void removePurchaseOnProduct(int prodId){
        Product prod = this.inventory.getProductById(prodId,storeId);
        this.purchasesOnProducts.remove(prod);
    }

    public void removePurchaseOnCategory(String category){
        this.purchasesOnCategories.remove(category);
    }

    public void removePurchaseOnStore(){
        this.purchasesOnStore = null;
    }

    public double calcDiscountPerProduct(Product prod, Date date, User user, Bag bag) {
        List<Double> SumDiscount = new LinkedList<>();
        List<Double> MaxDiscount = new LinkedList<>();
        double discountProduct = 0;
        double discountCategory = 0;
        double discountStore = 0;
        Discount disOnProd = discountsOnProducts.get(prod);
        if (disOnProd != null) {
            discountProduct = disOnProd.calculateDiscount(prod, user, date, bag);
            if (disOnProd.getMathOp().equals(Discount.MathOp.MAX))
                MaxDiscount.add(discountProduct);
            else
                SumDiscount.add(discountProduct);
        }
        for (String cat : prod.getCategories()) {
            Discount dis = discountsOnCategories.get(cat, this.storeId);
            if (dis != null) {
                discountCategory = dis.calculateDiscount(prod, user, date, bag);
                if (discountsOnProducts.get(prod).getMathOp().equals(Discount.MathOp.MAX))
                    MaxDiscount.add(discountCategory);
                else
                    SumDiscount.add(discountCategory);
            }
        }
        if (this.discountsOnStore != null) {
            discountStore = discountsOnStore.get(this.storeId).calculateDiscount(prod, user, date, bag);
            if (discountsOnStore.get(this.storeId).getMathOp().equals(Discount.MathOp.MAX))
                MaxDiscount.add(discountStore);
            else
                SumDiscount.add(discountStore);
        }

        double finalDiscount = 0;
        for (double disc : SumDiscount) {
            finalDiscount += disc;
        }
        for (double disc : MaxDiscount) {
            if (disc > finalDiscount)
                finalDiscount = disc;
        }
        return Math.min(finalDiscount, bag.getBagTotalCost(user.getId(), storeId)); //if discount > 100% return bag total cost (100% discount)
    }

    public void addSimpleDiscountOnProduct(int prodId, Date begin, Date end, int percentage, Discount.MathOp op) {
        this.discountsOnProducts.add(this.storeId, getProductById(prodId), new SimpleDiscount(counter.inc(), begin, end, percentage, op));
    }

    public void addSimpleDiscountOnCategory(String category, Date begin, Date end, int percentage, Discount.MathOp op) {
        this.discountsOnCategories.add(this.storeId, category, new SimpleDiscount(counter.inc(), begin, end, percentage, op));
    }

    public void addSimpleDiscountOnStore(Date begin, Date end, int percentage, Discount.MathOp op) {
        this.discountsOnStore = new DiscountsOnStoresWrapper(new SimpleDiscount(counter.inc(), begin, end, percentage, op));
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
            Discount dis = discountsOnProducts.get(product);
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
        return new Result(false, "No discount policies on this product.");
    }

    public Result viewDiscountPoliciesOnCategory(String category) {
        if(category != null && !category.equals("")) {
            List<Object> discountPolicies = new LinkedList<>();
            Discount dis = discountsOnCategories.get(category, this.storeId);
            if(dis == null)
                return new Result(false, "No discount policies on this category.");
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
        return new Result(false, "No discount policies on this category.");
    }

    public Result viewDiscountPoliciesOnStore() {
        if(this.discountsOnStore != null) {
            List<Object> discountPolicies = new LinkedList<>();
            Discount dis = this.discountsOnStore.get(this.storeId);
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
        return this.managers.contains(user,storeId);
    }

    public boolean prodExists(int prodId){ return this.inventory.prodExists(prodId,storeId); }


    public Set<Integer> getManagersAndOwners() {
        Set<Integer> list = new HashSet<>();
        for(User user: managers.getAll(storeId))
        {
            list.add(user.getId());
        }
        for(User user: owners.getAll(storeId))
        {
            list.add(user.getId());
        }
        return list;
    }

    public Result removeOwner(User owner, User ownerToDelete) {
//        if(appointments.get(owner).remove(ownerToDelete)){
        if(appointments.removeAppointment(storeId,owner.getId(),ownerToDelete.getId())){
            employees.remove(storeId,ownerToDelete);
            ownerToDelete.removeFromMyStores(this);

            List<User> ownersList=appointments.get(storeId,ownerToDelete);
            for(User user : ownersList){
                this.owners.remove(user,storeId);
                removeOwner(ownerToDelete,user);
            }

            return new Result(true,true);
        }
        return new Result(false,"Remove of the manager has failed");
    }

    public void setProductAmount(Product product, int amount) {
        inventory.setProductAmount(product,amount,storeId);
    }

    public int getProductAmount(Integer prodId) {
        return inventory.getProductsAmounts(storeId).get(getProductById(prodId));
    }

    public boolean removeReceipt(Receipt receipt) {
        return this.receipts.remove(receipt,storeId);
    }

    public int addPurchaseOffer(int prodId, User user, double offer, int numOfProd) {
        int offerId =  offerCounter.inc();
        if(user.isRegistered()) {
            if (this.offersOnProduct.containsKey(getProductById(prodId))) {
                List<PurchaseOffer> offers = this.offersOnProduct.get(getProductById(prodId));
                Iterator<PurchaseOffer> iterator = offers.iterator();
                while (iterator.hasNext()) {
                    PurchaseOffer p = iterator.next();
                    if (p.getUser().equals(user)) {
                        offers.remove(p);
                    }
                }
                offers.add(new PurchaseOffer(offerId,offer,numOfProd,user));
            } else {
                LinkedList<PurchaseOffer> offers = new LinkedList();
                offers.add(new PurchaseOffer(offerId,offer,numOfProd,user));
                this.offersOnProduct.put(getProductById(prodId), offers);
            }
            return offerId;

        }
        return -1;
    }
    public int getUserMadeTheOffer(int prodId ,int offerId){
        List<PurchaseOffer> offers = this.offersOnProduct.get(getProductById(prodId));
        for (PurchaseOffer p: offers) {
            if (p.getId() == offerId)
                return p.getUser().getId();
        }
        return -1;
    }

    public void removeOffer(int prodId ,int offerId){
        List<PurchaseOffer> offers = this.offersOnProduct.get(getProductById(prodId));
        PurchaseOffer po = null;
        for (PurchaseOffer p: offers) {
            if (p.getId() == offerId)
                po = p;
        }
        if(po != null)
            offers.remove(po);
        if(offers.size() == 0);
        this.offersOnProduct.remove(getProductById(prodId));
    }

    public Result responedToOffer(int prodId, int offerId, String responed, double counterOffer, String option) {
        if(option.equals("ACT")) {
            switch (responed) {
                case "APPROVED":
                    double offer = 0;
                    int amount = 0;
                    User user = null;
                    List<PurchaseOffer> offers = this.offersOnProduct.get(getProductById(prodId));
                    for (PurchaseOffer p : offers) {
                        if (p.getId() == offerId) {
                            offer = p.getPriceOfOffer();
                            user = p.getUser();
                            amount = p.getNumOfProd();
                        }
                    }

                    if (user != null) {
                        Bag bag = user.getBagByStoreId(this.storeId);
                        if (bag == null) {
                            bag = new Bag(this);
                            user.getBags().add(bag);
                        }
                        bag.productsAmounts.add(getProductById(prodId), amount,storeId,user.getId());
                        bag.productsApproved.put(getProductById(prodId), offer);
                        removeOffer(prodId, offerId);
                        return new Result(true, "the offer approved");
                    }
                    return new Result(false, "the offer wasn't found");
                case "DISAPPROVED":
                    removeOffer(prodId, offerId);
                    return new Result(true, "the offer disapproved");
                case "COUNTEROFFER":
                    user = null;
                    offers = this.offersOnProduct.get(getProductById(prodId));
                    PurchaseOffer po = null;
                    for (PurchaseOffer p : offers) {
                        if (p.getId() == offerId) {
                            p.setPriceOfOffer(counterOffer);
                            user = p.getUser();
                            po = p;
                        }
                    }
                    if (user != null) {
                        Bag bag = user.getBagByStoreId(this.storeId);
                        if (bag == null) {
                            bag = new Bag(this);
                            user.getBags().add(bag);
                        }
                        bag.counterOffers.put(getProductById(prodId), po);
                        removeOffer(prodId, offerId);
                        return new Result(true, "counter offer has been sent");
                    }
                    break;
                default:
                    return new Result(false, "offer did not get response yet");

        }
        }

        else
        {
           Map<PurchaseOffer, Product> output= new HashMap<>();
            for (Map.Entry<Product, LinkedList<PurchaseOffer>> entry : offersOnProduct.entrySet()) {
                List<PurchaseOffer> offers = entry.getValue();
                for (PurchaseOffer p: offers) {
                    output.put(p,entry.getKey());
                }
            }
            return new Result(true, output);
        }
        return new Result(false, "offer did not get response yet");
    }

    public int getRatesCount() {

        return ratesCount;
    }

    public List<Receipt> getReceipts() {
        return this.receipts.getAll(storeId);
    }

    public Map<User,List<User>> getAppointments() {
        return this.appointments.getAll(storeId);

    }

    public void setRate(double rate) {
        this.rate=rate;
    }

    public void setRateCount(int ratesCount) {
        this.ratesCount=ratesCount;
    }


}



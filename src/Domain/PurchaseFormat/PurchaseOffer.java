package Domain.PurchaseFormat;

import Domain.Bag;
import Domain.PurchasePolicies.PurchasePolicy;
import Domain.User;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PurchaseOffer extends Purchase {
    private int id;
    //private PurchaseCondition conditions;
    private double priceOfOffer;
    private boolean isApproved;
    private int numOfProd;
    private User user;
    private LinkedList<Integer> ownersAndMangersLeft;
    private int counterOffer = 0;

    public PurchaseOffer(int id, double priceOfOffer , int numOfProd, User user){
        this.id = id;
       // this.conditions = conditions;
        this.priceOfOffer = priceOfOffer;
        this.numOfProd = numOfProd;
        this.user  = user;
        this.isApproved = false;
        this.ownersAndMangersLeft = new LinkedList<>();
        this.counterOffer = 0;
    }

    public void addOwnersAndMangersLeft(LinkedList<Integer> ownersAndMangers){
        this.ownersAndMangersLeft.addAll(ownersAndMangers);
    }

    public void removeOwnerAfterApproved(int ownerId){
        Iterator<Integer> it = this.ownersAndMangersLeft.iterator();
        while (it.hasNext()){
            int i = it.next();
            if(i==ownerId){
               it.remove();
            }
        }
    }

    public boolean allOwnersApproved(){
        return this.ownersAndMangersLeft.isEmpty();
    }

    public LinkedList<Integer> getManager(){
        return this.ownersAndMangersLeft;
    }

    public void setCounterOffer(){
        this.counterOffer = 1;
    }

    public void setCounterOfferDb(int counter){
        this.counterOffer = counter;
    }

    public boolean HasCounterOffer(){
        return this.counterOffer == 1;
    }
    public int getCounterOffer(){
        return this.counterOffer;
    }
    public boolean validatePurchase( User user, Date time, Bag bag) {
        //return conditions.validateCondition( user, time, bag);
        return true;
    }

    @Override
    public boolean validatePurchase(List<PurchasePolicy> policies) {
        return false;
    }

    //public PurchaseCondition getConditions() { return this.conditions; }

    public double getPriceOfOffer() { return this.priceOfOffer; }

    public User getUser() { return this.user; }

    public int getId() {return this.id;}

    public void setPriceOfOffer(double priceOfOffer){this.priceOfOffer = priceOfOffer;}

    public boolean getIsApproved(){return isApproved;}

    public void setIsApproved(boolean isApproved){this.isApproved = isApproved;}

    public int getNumOfProd(){return this.numOfProd;}
}
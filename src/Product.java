import java.util.LinkedList;
import java.util.List;

public class Product {


    enum Category {
        FOOD,
        DRINKS,
        ENTERTAINMENT,
        OTHER
    }

    private int id;
    private String name;
    private List<Category> categories;
    private double price;
    private double rate;
    private int ratesCount;
    private String description;
    private List<String> reviews;

    public Product(int id, String name, List<Category> categories, double price , String description) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.price = price;
        this.ratesCount = 0;
        this.description = description;
        this.rate = 0;
    }

    public int getId() {return id; }

    public String getName() {
        return name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public double getPrice() {
        return price;
    }

    public double getRate() {
        return rate;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void rateProduct(double newRate) {
        this.ratesCount ++;
        this.rate = (this.rate + newRate) / this.ratesCount;
    }

    public void addReview(String review) {
        this.reviews.add(review);
    }

    public String toString() {
        String output =  "Name - " + this.name + " Categories -  ";
        for (Category c: categories) {
            output += c + ", ";
        }
        if (output.endsWith(", "))
            output.substring(0,output.length()-2);
        output = output + "Price - " + this.price + " Rate - " + this.rate + " Description - " + this.description + " Reviews - ";
        for (String r: reviews) {
            output+=r;
        }
        return output;
    }

    public boolean containsCategory(String category){
        Category c = Category.valueOf(category);
        for (Category cat:categories) {
            if(cat.equals(c))
                return true;
        }
        return false;
    }

    public boolean containsKeyWords(String keyWordsStr) {
        String []keyWords = keyWordsStr.split(" ");
        boolean contains = false;
        for (int i = 0 ; i < keyWords.length ; i++){
            if(description.contains(keyWords[i]))
                contains = true;
        }
        return contains;
    }

    public boolean inPriceRange(String[] prices) {
        if(prices.length > 2) {
            int lower = Integer.parseInt(prices[0]);
            int upper = Integer.parseInt(prices[1]);
            if (this.price >= lower && this.price <= upper)
               return true;
        }
        return false;
    }

}

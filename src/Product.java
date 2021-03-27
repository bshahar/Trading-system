import java.util.LinkedList;
import java.util.List;

public class Product {
    enum Category {
        Food,
        Entertainment,
        Other
    } //TODO add more categories


    private String name;
    private List<Category> categories;
    private double price;
    private double rate;
    private int ratesCount;
    private String description;
    private List<String> reviews;

    public Product(String name, List<Category> categories, double price ,double rate, String description) {
        this.name = name;
        this.categories = categories;
        this.price = price;
        this.ratesCount = 0;
        this.description = description;
    }

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
}

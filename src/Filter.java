public class Filter {

    String searchType;
    String param;
    int minPrice;
    int maxPrice;
    int prodRank;
    String category;
    int storeRank;

    public Filter(String searchType,String param, int minPrice, int maxPrice, int prodRank,String category, int storeRank) {
        this.searchType=searchType;
        this.param=param;
        this.minPrice=minPrice;
        this.maxPrice=maxPrice;
        this.prodRank=prodRank;
        this.category=category;
        this.storeRank=storeRank;
    }
}

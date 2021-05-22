package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BagProductAmount")
public class BagProductAmountDAO {


        @DatabaseField(id = true)
        private int userId;
        @DatabaseField(id = true)
        private int storeId;
        @DatabaseField(id = true)
        private int productId;
        @DatabaseField
        private int amount;


        public BagProductAmountDAO() {
            // ORMLite needs a no-arg constructor
        }

        public BagProductAmountDAO(int userId, int storeId, int productId, int amount) {
                this.userId = userId;
                this.storeId = storeId;
                this.productId = productId;
                this.amount = amount;
        }

        public int getUserId() {
                return userId;
        }

        public int getStoreId() {
                return storeId;
        }

        public int getProductId() {
                return productId;
        }

        public int getAmount() {
                return amount;
        }

        public void setUserId(int userId) {
                this.userId = userId;
        }

        public void setStoreId(int storeId) {
                this.storeId = storeId;
        }

        public void setProductId(int productId) {
                this.productId = productId;
        }

        public void setAmount(int amount) {
                this.amount = amount;
        }
}

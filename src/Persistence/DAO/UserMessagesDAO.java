package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "UserMessages")
public class UserMessagesDAO {


        @DatabaseField(id = true)
        private int id;
        @DatabaseField
        private String message;
        @DatabaseField
        private int userId;

        public UserMessagesDAO() {
            // ORMLite needs a no-arg constructor
        }

    public UserMessagesDAO(int id, String message, int userId) {
        this.id = id;
        this.message = message;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

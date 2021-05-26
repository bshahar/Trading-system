package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "UserNamePasswords")
public class UserAuthDAO {

    @DatabaseField(id=true)
    private String userName;
    @DatabaseField
    private String password;


    public UserAuthDAO() {
        // ORMLite needs a no-arg constructor
    }

    public UserAuthDAO(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


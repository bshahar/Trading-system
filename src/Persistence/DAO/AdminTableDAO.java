package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.Date;

@DatabaseTable(tableName = "AdminTable")
public class AdminTableDAO {


    @DatabaseField(id = true)
    private String Date;
    @DatabaseField
    private int GuestsCounter;
    @DatabaseField
    private int NormalUsersCounter;
    @DatabaseField
    private int ManagersCounter;
    @DatabaseField
    private int OwnersCounter;

    public AdminTableDAO() {
    }

    public AdminTableDAO(String date, int guestsCounter, int normalUsers, int managers, int owners) {
        Date = date;
        GuestsCounter = guestsCounter;
        NormalUsersCounter = normalUsers;
        ManagersCounter = managers;
        OwnersCounter = owners;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getGuestsCounter() {
        return GuestsCounter;
    }

    public void setGuestsCounter(int guestsCounter) {
        GuestsCounter = guestsCounter;
    }

    public int getNormalUsers() {
        return NormalUsersCounter;
    }

    public void setNormalUsers(int normalUsers) {
        NormalUsersCounter = normalUsers;
    }

    public int getManagers() {
        return ManagersCounter;
    }

    public void setManagers(int managers) {
        ManagersCounter = managers;
    }

    public int getOwners() {
        return OwnersCounter;
    }

    public void setOwners(int owners) {
        OwnersCounter = owners;
    }
}


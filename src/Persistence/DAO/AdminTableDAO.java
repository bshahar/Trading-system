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
    private int NormalUsers;
    @DatabaseField
    private int Managers;
    @DatabaseField
    private int Owners;

    public AdminTableDAO() {
    }

    public AdminTableDAO(String date, int guestsCounter, int normalUsers, int managers, int owners) {
        Date = date;
        GuestsCounter = guestsCounter;
        NormalUsers = normalUsers;
        Managers = managers;
        Owners = owners;
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
        return NormalUsers;
    }

    public void setNormalUsers(int normalUsers) {
        NormalUsers = normalUsers;
    }

    public int getManagers() {
        return Managers;
    }

    public void setManagers(int managers) {
        Managers = managers;
    }

    public int getOwners() {
        return Owners;
    }

    public void setOwners(int owners) {
        Owners = owners;
    }
}


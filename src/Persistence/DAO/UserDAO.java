package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

    @DatabaseTable(tableName = "Receipts")
    public class UserDAO {

        @DatabaseField(id = true)
        private int id;
        @DatabaseField
        private String userName;
        @DatabaseField
        private boolean registered;
        @DatabaseField
        private int age;
        @DatabaseField
        private boolean logged;
        @DatabaseField
        private boolean isSystemManger;

        public UserDAO() {
            // ORMLite needs a no-arg constructor
        }
        public UserDAO(int id, String userName ,boolean registered , int age ,boolean logged,boolean isSystemManger ) {
            this.id = id;
            this.userName = userName;
            this.registered = registered;
            this.age = age;
            this.logged = logged;
            this.isSystemManger = isSystemManger;

        }
        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setSystemManager(boolean systemManager) {
            isSystemManger = systemManager;
        }

        public void setLogged(boolean logged) {
            this.logged = logged;
        }

        public boolean isSystemManger() {
            return isSystemManger;
        }

        public boolean isLogged() {
            return logged;
        }

        public void setRegistered(boolean registered) {
            this.registered = registered;
        }
        public boolean getRegistered() {
            return registered;
        }

    }


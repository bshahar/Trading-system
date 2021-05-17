package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Users", schema = "zw9P3SlfWt", catalog = "")
public class UsersEntity {
    private int id;
    private String userName;
    private Byte registered;
    private Integer age;
    private Byte logged;
    private Byte isSystemManger;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "userName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "registered")
    public Byte getRegistered() {
        return registered;
    }

    public void setRegistered(Byte registered) {
        this.registered = registered;
    }

    @Basic
    @Column(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Basic
    @Column(name = "logged")
    public Byte getLogged() {
        return logged;
    }

    public void setLogged(Byte logged) {
        this.logged = logged;
    }

    @Basic
    @Column(name = "isSystemManger")
    public Byte getIsSystemManger() {
        return isSystemManger;
    }

    public void setIsSystemManger(Byte isSystemManger) {
        this.isSystemManger = isSystemManger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return id == that.id && Objects.equals(userName, that.userName) && Objects.equals(registered, that.registered) && Objects.equals(age, that.age) && Objects.equals(logged, that.logged) && Objects.equals(isSystemManger, that.isSystemManger);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, registered, age, logged, isSystemManger);
    }
}

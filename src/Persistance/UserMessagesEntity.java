package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "UserMessages", schema = "zw9P3SlfWt", catalog = "")
public class UserMessagesEntity {
    private int id;
    private String message;
    private Integer userId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Column(name = "userId")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMessagesEntity that = (UserMessagesEntity) o;
        return id == that.id && Objects.equals(message, that.message) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, userId);
    }
}

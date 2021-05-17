package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class MemberStorePermissionsEntityPK implements Serializable {
    private int userId;
    private int storeId;

    @Column(name = "userId")
    @Id
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "storeId")
    @Id
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberStorePermissionsEntityPK that = (MemberStorePermissionsEntityPK) o;
        return userId == that.userId && storeId == that.storeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, storeId);
    }
}

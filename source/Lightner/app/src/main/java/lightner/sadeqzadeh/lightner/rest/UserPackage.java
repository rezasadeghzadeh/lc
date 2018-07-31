package lightner.sadeqzadeh.lightner.rest;

public class UserPackage {
    private long id;
    private long packageId;
    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserPackage{" +
                "id=" + id +
                ", packageId=" + packageId +
                ", userId=" + userId +
                '}';
    }
}

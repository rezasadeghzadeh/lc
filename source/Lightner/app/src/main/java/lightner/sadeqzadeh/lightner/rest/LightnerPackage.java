package lightner.sadeqzadeh.lightner.rest;

import com.google.gson.annotations.SerializedName;

public class LightnerPackage {
    @SerializedName("ID")
    private long id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Price")
    private long  price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}

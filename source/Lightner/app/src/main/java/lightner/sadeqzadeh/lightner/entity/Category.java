package lightner.sadeqzadeh.lightner.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Category {
    @Id
    private Long id;
    @NotNull
    private String name;
    private String codeColor;
    @Generated(hash = 489304180)
    public Category(Long id, @NotNull String name, String codeColor) {
        this.id = id;
        this.name = name;
        this.codeColor = codeColor;
    }
    @Generated(hash = 1150634039)
    public Category() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCodeColor() {
        return this.codeColor;
    }
    public void setCodeColor(String codeColor) {
        this.codeColor = codeColor;
    }
}

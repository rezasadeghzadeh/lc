package lightner.sadeqzadeh.lightner.entity;

public class BoxStat {
    public long total;
    public long reviewableCount;

    public BoxStat(long total, long  reviewableCount) {
        this.total = total;
        this.reviewableCount = reviewableCount;
    }
}

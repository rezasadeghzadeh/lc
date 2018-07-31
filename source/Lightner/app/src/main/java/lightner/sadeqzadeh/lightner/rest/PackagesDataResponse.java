package lightner.sadeqzadeh.lightner.rest;

import java.util.List;

public class PackagesDataResponse {
    private List<LightnerPackage> packages;
    private List<UserPackage> userPackages;

    public List<LightnerPackage> getPackages() {
        return packages;
    }

    public void setPackages(List<LightnerPackage> packages) {
        this.packages = packages;
    }

    public List<UserPackage> getUserPackages() {
        return userPackages;
    }

    public void setUserPackages(List<UserPackage> userPackages) {
        this.userPackages = userPackages;
    }
}

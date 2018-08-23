package lightner.sadeqzadeh.lightner.rest;

import com.google.gson.annotations.SerializedName;

public class    SaveUserDataResponse {

    @SerializedName("result")
    private boolean result;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}

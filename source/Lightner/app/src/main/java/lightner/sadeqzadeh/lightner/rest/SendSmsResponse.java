package lightner.sadeqzadeh.lightner.rest;

import com.google.gson.annotations.SerializedName;

public class SendSmsResponse {
    @SerializedName("status")
    private boolean status;

    public SendSmsResponse(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}


package core.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("statuscode")
    @Expose
    private String statuscode;
    @SerializedName("statusmessage")
    @Expose
    private String statusmessage;

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public String getStatusmessage() {
        return statusmessage;
    }

    public void setStatusmessage(String statusmessage) {
        this.statusmessage = statusmessage;
    }

}

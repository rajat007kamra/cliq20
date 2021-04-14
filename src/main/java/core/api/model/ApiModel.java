
package core.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiModel {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("context")
    @Expose
    private Context context;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}

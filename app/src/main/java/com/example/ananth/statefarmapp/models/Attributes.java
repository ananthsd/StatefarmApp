
package com.example.ananth.statefarmapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("XCOORD")
    @Expose
    private Integer xCOORD;
    @SerializedName("YCOORD")
    @Expose
    private Integer yCOORD;

    public Integer getXCOORD() {
        return xCOORD;
    }

    public void setXCOORD(Integer xCOORD) {
        this.xCOORD = xCOORD;
    }

    public Integer getYCOORD() {
        return yCOORD;
    }

    public void setYCOORD(Integer yCOORD) {
        this.yCOORD = yCOORD;
    }

}

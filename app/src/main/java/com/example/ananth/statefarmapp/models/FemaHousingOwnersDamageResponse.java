
package com.example.ananth.statefarmapp.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FemaHousingOwnersDamageResponse {

    @SerializedName("metadata")
    @Expose
    private Metadata metadata;
    @SerializedName("HousingAssistanceOwners")
    @Expose
    private List<HousingAssistanceOwner> housingAssistanceOwners = null;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<HousingAssistanceOwner> getHousingAssistanceOwners() {
        return housingAssistanceOwners;
    }

    public void setHousingAssistanceOwners(List<HousingAssistanceOwner> housingAssistanceOwners) {
        this.housingAssistanceOwners = housingAssistanceOwners;
    }

}

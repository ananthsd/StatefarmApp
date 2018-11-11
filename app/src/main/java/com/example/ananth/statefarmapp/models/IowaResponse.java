
package com.example.ananth.statefarmapp.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IowaResponse {

    @SerializedName("objectIdFieldName")
    @Expose
    private String objectIdFieldName;
    @SerializedName("uniqueIdField")
    @Expose
    private UniqueIdField uniqueIdField;
    @SerializedName("globalIdFieldName")
    @Expose
    private String globalIdFieldName;
    @SerializedName("geometryType")
    @Expose
    private String geometryType;
    @SerializedName("spatialReference")
    @Expose
    private SpatialReference spatialReference;
    @SerializedName("fields")
    @Expose
    private List<Field> fields = null;
    @SerializedName("features")
    @Expose
    private List<Feature> features = null;
    @SerializedName("exceededTransferLimit")
    @Expose
    private Boolean exceededTransferLimit;

    public String getObjectIdFieldName() {
        return objectIdFieldName;
    }

    public void setObjectIdFieldName(String objectIdFieldName) {
        this.objectIdFieldName = objectIdFieldName;
    }

    public UniqueIdField getUniqueIdField() {
        return uniqueIdField;
    }

    public void setUniqueIdField(UniqueIdField uniqueIdField) {
        this.uniqueIdField = uniqueIdField;
    }

    public String getGlobalIdFieldName() {
        return globalIdFieldName;
    }

    public void setGlobalIdFieldName(String globalIdFieldName) {
        this.globalIdFieldName = globalIdFieldName;
    }

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    public SpatialReference getSpatialReference() {
        return spatialReference;
    }

    public void setSpatialReference(SpatialReference spatialReference) {
        this.spatialReference = spatialReference;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public Boolean getExceededTransferLimit() {
        return exceededTransferLimit;
    }

    public void setExceededTransferLimit(Boolean exceededTransferLimit) {
        this.exceededTransferLimit = exceededTransferLimit;
    }

}

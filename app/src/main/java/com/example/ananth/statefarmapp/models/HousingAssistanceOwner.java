
package com.example.ananth.statefarmapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HousingAssistanceOwner {

    @SerializedName("disasterNumber")
    @Expose
    private int disasterNumber;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("county")
    @Expose
    private String county;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("zipCode")
    @Expose
    private String zipCode;
    @SerializedName("validRegistrations")
    @Expose
    private int validRegistrations;
    @SerializedName("averageFemaInspectedDamage")
    @Expose
    private int averageFemaInspectedDamage;
    @SerializedName("totalInspected")
    @Expose
    private int totalInspected;
    @SerializedName("totalDamage")
    @Expose
    private int totalDamage;
    @SerializedName("noFemaInspectedDamage")
    @Expose
    private int noFemaInspectedDamage;
    @SerializedName("femaInspectedDamageBetween1And10000")
    @Expose
    private int femaInspectedDamageBetween1And10000;
    @SerializedName("femaInspectedDamageBetween10001And20000")
    @Expose
    private int femaInspectedDamageBetween10001And20000;
    @SerializedName("femaInspectedDamageBetween20001And30000")
    @Expose
    private int femaInspectedDamageBetween20001And30000;
    @SerializedName("femaInspectedDamageGreaterThan30000")
    @Expose
    private int femaInspectedDamageGreaterThan30000;
    @SerializedName("approvedForFemaAssistance")
    @Expose
    private int approvedForFemaAssistance;
    @SerializedName("totalApprovedIhpAmount")
    @Expose
    private int totalApprovedIhpAmount;
    @SerializedName("repairReplaceAmount")
    @Expose
    private int repairReplaceAmount;
    @SerializedName("rentalAmount")
    @Expose
    private int rentalAmount;
    @SerializedName("otherNeedsAmount")
    @Expose
    private int otherNeedsAmount;
    @SerializedName("approvedBetween1And10000")
    @Expose
    private int approvedBetween1And10000;
    @SerializedName("approvedBetween10001And25000")
    @Expose
    private int approvedBetween10001And25000;
    @SerializedName("approvedBetween25001AndMax")
    @Expose
    private int approvedBetween25001AndMax;
    @SerializedName("totalMaxGrants")
    @Expose
    private int totalMaxGrants;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("lastRefresh")
    @Expose
    private String lastRefresh;
    @SerializedName("id")
    @Expose
    private String id;

    public int getDisasterNumber() {
        return disasterNumber;
    }

    public void setDisasterNumber(int disasterNumber) {
        this.disasterNumber = disasterNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getValidRegistrations() {
        return validRegistrations;
    }

    public void setValidRegistrations(int validRegistrations) {
        this.validRegistrations = validRegistrations;
    }

    public int getAverageFemaInspectedDamage() {
        return averageFemaInspectedDamage;
    }

    public void setAverageFemaInspectedDamage(int averageFemaInspectedDamage) {
        this.averageFemaInspectedDamage = averageFemaInspectedDamage;
    }

    public int getTotalInspected() {
        return totalInspected;
    }

    public void setTotalInspected(int totalInspected) {
        this.totalInspected = totalInspected;
    }

    public int getTotalDamage() {
        return totalDamage;
    }

    public void setTotalDamage(int totalDamage) {
        this.totalDamage = totalDamage;
    }

    public int getNoFemaInspectedDamage() {
        return noFemaInspectedDamage;
    }

    public void setNoFemaInspectedDamage(int noFemaInspectedDamage) {
        this.noFemaInspectedDamage = noFemaInspectedDamage;
    }

    public int getFemaInspectedDamageBetween1And10000() {
        return femaInspectedDamageBetween1And10000;
    }

    public void setFemaInspectedDamageBetween1And10000(int femaInspectedDamageBetween1And10000) {
        this.femaInspectedDamageBetween1And10000 = femaInspectedDamageBetween1And10000;
    }

    public int getFemaInspectedDamageBetween10001And20000() {
        return femaInspectedDamageBetween10001And20000;
    }

    public void setFemaInspectedDamageBetween10001And20000(int femaInspectedDamageBetween10001And20000) {
        this.femaInspectedDamageBetween10001And20000 = femaInspectedDamageBetween10001And20000;
    }

    public int getFemaInspectedDamageBetween20001And30000() {
        return femaInspectedDamageBetween20001And30000;
    }

    public void setFemaInspectedDamageBetween20001And30000(int femaInspectedDamageBetween20001And30000) {
        this.femaInspectedDamageBetween20001And30000 = femaInspectedDamageBetween20001And30000;
    }

    public int getFemaInspectedDamageGreaterThan30000() {
        return femaInspectedDamageGreaterThan30000;
    }

    public void setFemaInspectedDamageGreaterThan30000(int femaInspectedDamageGreaterThan30000) {
        this.femaInspectedDamageGreaterThan30000 = femaInspectedDamageGreaterThan30000;
    }

    public int getApprovedForFemaAssistance() {
        return approvedForFemaAssistance;
    }

    public void setApprovedForFemaAssistance(int approvedForFemaAssistance) {
        this.approvedForFemaAssistance = approvedForFemaAssistance;
    }

    public int getTotalApprovedIhpAmount() {
        return totalApprovedIhpAmount;
    }

    public void setTotalApprovedIhpAmount(int totalApprovedIhpAmount) {
        this.totalApprovedIhpAmount = totalApprovedIhpAmount;
    }

    public int getRepairReplaceAmount() {
        return repairReplaceAmount;
    }

    public void setRepairReplaceAmount(int repairReplaceAmount) {
        this.repairReplaceAmount = repairReplaceAmount;
    }

    public int getRentalAmount() {
        return rentalAmount;
    }

    public void setRentalAmount(int rentalAmount) {
        this.rentalAmount = rentalAmount;
    }

    public int getOtherNeedsAmount() {
        return otherNeedsAmount;
    }

    public void setOtherNeedsAmount(int otherNeedsAmount) {
        this.otherNeedsAmount = otherNeedsAmount;
    }

    public int getApprovedBetween1And10000() {
        return approvedBetween1And10000;
    }

    public void setApprovedBetween1And10000(int approvedBetween1And10000) {
        this.approvedBetween1And10000 = approvedBetween1And10000;
    }

    public int getApprovedBetween10001And25000() {
        return approvedBetween10001And25000;
    }

    public void setApprovedBetween10001And25000(int approvedBetween10001And25000) {
        this.approvedBetween10001And25000 = approvedBetween10001And25000;
    }

    public int getApprovedBetween25001AndMax() {
        return approvedBetween25001AndMax;
    }

    public void setApprovedBetween25001AndMax(int approvedBetween25001AndMax) {
        this.approvedBetween25001AndMax = approvedBetween25001AndMax;
    }

    public int getTotalMaxGrants() {
        return totalMaxGrants;
    }

    public void setTotalMaxGrants(int totalMaxGrants) {
        this.totalMaxGrants = totalMaxGrants;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(String lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

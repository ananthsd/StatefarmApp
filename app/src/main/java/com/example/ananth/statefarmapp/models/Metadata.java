
package com.example.ananth.statefarmapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("skip")
    @Expose
    private int skip;
    @SerializedName("top")
    @Expose
    private int top;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("filter")
    @Expose
    private String filter;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("orderby")
    @Expose
    private Orderby orderby;
    @SerializedName("select")
    @Expose
    private Object select;
    @SerializedName("entityname")
    @Expose
    private String entityname;
    @SerializedName("url")
    @Expose
    private String url;

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Orderby getOrderby() {
        return orderby;
    }

    public void setOrderby(Orderby orderby) {
        this.orderby = orderby;
    }

    public Object getSelect() {
        return select;
    }

    public void setSelect(Object select) {
        this.select = select;
    }

    public String getEntityname() {
        return entityname;
    }

    public void setEntityname(String entityname) {
        this.entityname = entityname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

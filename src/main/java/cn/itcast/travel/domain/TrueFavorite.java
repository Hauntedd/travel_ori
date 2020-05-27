package cn.itcast.travel.domain;

import java.io.Serializable;

public class TrueFavorite implements Serializable {
    private Integer rid;
    private String date;
    private Integer uid;

    public TrueFavorite(Integer rid, String date, Integer uid) {
        this.rid = rid;
        this.date = date;
        this.uid = uid;
    }

    public TrueFavorite() {
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "TrueFavorite{" +
                "rid=" + rid +
                ", date='" + date + '\'' +
                ", uid=" + uid +
                '}';
    }
}

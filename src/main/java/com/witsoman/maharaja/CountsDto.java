package com.witsoman.maharaja;

public class CountsDto {
    private String date;
    private String type;
    private String month;
    private String monthNo;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public String getMonthNo() {
        return monthNo;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public void setMonthNo(String monthNo) {
        this.monthNo = monthNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type= type;
    }
}

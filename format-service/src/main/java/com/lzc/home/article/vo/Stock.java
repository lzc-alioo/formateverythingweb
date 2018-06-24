package com.lzc.home.article.vo;

public class Stock {
    private String symbol;
    private String code;
    private String name;
    private String current;
    private String percent;
    private String change;
    private String high;
    private String low;
    private String high52w;
    private String low52w;
    private String marketcapital;
    private String amount;
    private String type;
    private String pettm;
    private String volume;
    private String hasexist;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh52w() {
        return high52w;
    }

    public void setHigh52w(String high52w) {
        this.high52w = high52w;
    }

    public String getLow52w() {
        return low52w;
    }

    public void setLow52w(String low52w) {
        this.low52w = low52w;
    }

    public String getMarketcapital() {
        return marketcapital;
    }

    public void setMarketcapital(String marketcapital) {
        this.marketcapital = marketcapital;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPettm() {
        return pettm;
    }

    public void setPettm(String pettm) {
        this.pettm = pettm;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getHasexist() {
        return hasexist;
    }

    public void setHasexist(String hasexist) {
        this.hasexist = hasexist;
    }

    @Override
    public String toString() {
        return symbol + "," +
                code + "," +
                name + "," +
                current + "," +
                percent + "," +
                change + "," +
                high + "," +
                low + "," +
                high52w + "," +
                low52w + "," +
                marketcapital + "," +
                amount + "," +
                type + "," +
                pettm + "," +
                volume + "," +
                hasexist;

    }
}

package com.pageprovider.model;

public class Page {
    private int     id;
    private int     aggregatorsId;
    private int     paymentPageTypeId;
    private int     contentId;
    private String  htmlFile;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getAggregatorsId() {
        return aggregatorsId;
    }

    public void setAggregatorsId(int aggregatorsId) {
        this.aggregatorsId = aggregatorsId;
    }

    public int getPaymentPageTypeId() {
        return paymentPageTypeId;
    }

    public void setPaymentPageTypeId(int paymentPageTypeId) {
        this.paymentPageTypeId = paymentPageTypeId;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getHtmlFile() {
        return htmlFile;
    }

    public void setHtmlFile(String htmlFile) {
        this.htmlFile = htmlFile;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page that = (Page) o;

        if (aggregatorsId != that.aggregatorsId) return false;
        if (contentId != that.contentId) return false;
        if (paymentPageTypeId != that.paymentPageTypeId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = aggregatorsId;
        result = 31 * result + paymentPageTypeId;
        result = 31 * result + contentId;
        return result;
    }

    @Override
    public String toString() {
        return "PaymentPages{" +
                "id=" + id +
                ", aggregatorsId=" + aggregatorsId +
                ", paymentPageTypeId=" + paymentPageTypeId +
                ", contentId=" + contentId +
                ", htmlFile='" + htmlFile + '\'' +
                '}';
    }
}

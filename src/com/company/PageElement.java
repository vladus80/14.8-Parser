package com.company;

import java.util.Objects;

public class PageElement {

    private String keyWord;
    private String value;
    private String vacancy;
    private String date;

    public PageElement(String value) {
        //this.keyWord = vacancy;
        this.value = value;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageElement)) return false;
        PageElement that = (PageElement) o;
        return keyWord.equals(that.keyWord) &&
                value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyWord, value);
    }

    @Override
    public String toString() {
        return "PageElement{" +
                "tag='" + keyWord + '\'' +
                ", value='" + value + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getTag() {
        return keyWord;
    }

    public void setTag(String tag) {
        this.keyWord = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getVacancy() {
        return vacancy;
    }

    public void setVacancy(String vacancy) {
        this.vacancy = vacancy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
}
}
package com.company;

import org.jsoup.nodes.Element;

import java.util.Objects;

public class PageElement{

    private String keyWord;
    private String value;
    private Element element;
   // private String date;

//    public PageElement(String value) {
//
//        this.value = value;
//
//    }

    public PageElement(Element element){
        this.element = element;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    //public String getDate() {return date;}

    //public void setDate(String date) {this.date = date; }

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
        return value ;
    }
}
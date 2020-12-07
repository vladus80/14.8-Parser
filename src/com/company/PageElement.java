package com.company;

/*
* Класс реализует структура данных в которой будут хранится объекты класса Element, а так же текст тэга
* */

import org.jsoup.nodes.Element;

public class PageElement{

    private String value; // поле куда будет складываться текст тэга
    private Element element; // поле куда будем ложить объект класса Element


    // Конструктор класса получает ссылку класса Element
    public PageElement(Element element){
        this.element = element;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Element getElement() {
        return element;
    }

    @Override
    public String toString() {
        return value ;
    }
}
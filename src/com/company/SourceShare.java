package com.company;
/*
* Класс позволяет запросить страницу и вернуть список элементов.
* Класс имплиментирует два метода: sourceConnect и getPageElements интерфейса IParser
* */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class SourceShare implements IParser {
    private String url;
    private String tag;
    PageElement element; // ссылка на объект класса  Element
    private ArrayList<PageElement> pageElements = new ArrayList<>(); // ссылка на класс, куда складываем элементы

    // Конструктор класса
    public SourceShare(String url, String tag) {
        this.url = url; //url - ссылка на ресурс откуда необходмио получить данные
        this.tag = tag; //tag - тэг который хотим получить
    }

    // Метод соединяется с ресурсом и заполняет объект класса PageElement
    // имплиментирует метод интерфейса IParser
    @Override
    public void sourceConnect() throws IOException {
        Document document = Jsoup.connect(url).get(); //запрашиваем документ
        Elements elements = document.select(tag);     // получаем коллекцию элементов  по тэгу

        //перебираем коллекцию
        for (Element el : elements) {
            element = new PageElement(el);
            element.setValue(el.text()); // Записываем в поле value объекта pageElements текст тэга
            pageElements.add(element);   // Добавляем в список объект Element (Класс Element библиотеки Jsoup)
        }
    }

    // Метод возвращает список объектов класса pageElements, в которых содержатся заполненные данные
    // имплиментирует метод интерфейса IParser
    @Override
    public ArrayList<PageElement> getPageElements() {
        return pageElements;
    }

    @Override
    public String toString() {
        return element.getElement().toString();
    }

}

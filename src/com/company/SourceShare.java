package com.company;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class SourceShare  implements IParser{
    private String url;
    private String tag;
    private ArrayList<PageElement> pageElements = new ArrayList<>();

    public SourceShare(String url, String tag) {
        this.url = url;
        this.tag = tag;
    }


    @Override
    public void sourceConnect() throws IOException {

        Document document = Jsoup.connect( url).get();
        Elements elements = document.select(tag);

        for (Element el : elements ) {
            //System.out.println(el.child(0).text()+" " + el);
            PageElement element = new PageElement(el);
            element.setValue(el.text());
            pageElements.add(element);

        }
        //System.out.println(elements.size());


    }

    @Override
    public ArrayList<PageElement> getPageElements() {
        return pageElements;
    }

}

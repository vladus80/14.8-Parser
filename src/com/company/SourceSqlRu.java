package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.*;


public class SourceSqlRu implements IParser {

    String cssQuery; // Тег или другой элемент HTML разметки
    int periodMonths;  // Период в месяцах
    String keyWord;    // Ключевое слово
    String link;      // ссылка на сайт

    DateHelp dateHelp = new DateHelp(); // вспомогательный класс для форматирования даты со страницы сайта
    Document document;
    long dataFormatLong; // сюда помещаем дату из вакансии
    Calendar calendar = new GregorianCalendar(); // текущая дата
    long dataMarker = calendar.getTime().getTime();
    long dateDist; // конечная дата
    ArrayList<PageElement> pageElements = new ArrayList<>(); // Список куда будем ложить отфильтрованные элементы

    // конструктор по умолчанию
    public SourceSqlRu() {

        this("https://www.sql.ru/forum/actualsearch.aspx?search=java&sin=1&bid=66&a=&ma=0&dt=-1&s=4&so=1&pg=", ".postslisttopic", 1, "java");
    }

    public SourceSqlRu( int periodMonths, String keyWord) throws UnsupportedEncodingException {

        this("https://www.sql.ru/forum/actualsearch.aspx?search="+ URLEncoder.encode( keyWord, "cp1251")+"&sin=1&bid=66&a=&ma=0&dt=-1&s=4&so=1&pg=",
        ".postslisttopic", periodMonths, keyWord );

    }

    // конструктор с параметрами
    public SourceSqlRu(String link, String cssQuery, int periodMonths, String keyWord) {

        this.cssQuery = cssQuery;
        this.periodMonths = periodMonths;
        this.keyWord = keyWord;
        this.link = link;
        calendar.add(Calendar.MONTH, this.periodMonths - this.periodMonths*2); // отнимаем кол-во месяцев (делаем число отрицательным)

    }

    @Override
    public void sourceConnect() {

        dateDist = calendar.getTime().getTime(); // Конечная дата
        int i = 1; // счетчик для листания страниц форума
        // Запускаем цикл до тех пор время из поста больше конечного времени
        while (dataMarker > dateDist){

            try {

                try {
                    //запрашиваем страницу
                    document = Jsoup.connect( this.link+ i).get();
                }catch (UnknownHostException e){

                    System.out.println("Невозможно установить соединение. Проверьте подключение...");
                    break;
                }

                Elements elements = document.select(this.cssQuery); //фильтруем по элементу
                if(elements.size() != 0){
                    listElements(elements); // перебираем коллекцию запрошенных элементов

                }else{
                    System.out.println("Поиск не дал результатов");
                    break;
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            i ++; // страница (листаем форум)
        }

    }

    @Override
    public ArrayList<PageElement> getPageElements() {
        return pageElements;
    }


    // Метод добавляет объекты в структуру данных ArrayList класса PageElement
    private void listElements(Elements elements) throws  ParseException {

        for (Element element : elements) {
            String vacancy = element.child(0).text(); // строка с название вакансии
            String linkJobVacancy = element.child(0).attr("href"); // ссылка для захода внутрь страницы вакансии и поиска даты публикации
            String dataFormatStr  = dateHelp.getDateFormat(dateHelp.getDatePublication(linkJobVacancy)); // отформатированная дата вакансии в текстовом формате
            dataFormatLong = dateHelp.dateLong(dateHelp.getDatePublication(linkJobVacancy));  // отформатированная дата в long
            dataMarker = dataFormatLong;

            if(dataFormatLong > dateDist){
                PageElement element1 = new PageElement(element);
                //element1.setDate(dataFormatStr);
                element1.setKeyWord(keyWord);
                element1.setValue(vacancy);
                pageElements.add(element1);

                //pageElements.add(new PageElement(vacancy));
            }else{
                break; //Если дата публикации меньше конечной даты, выходим
            }
        }
    }
}


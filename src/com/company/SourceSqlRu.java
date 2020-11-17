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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class SourceSqlRu implements IParser {

    String cssQuery; // Тег или другой элемент HTML разметки
    int periodMonths;  // Период в месяцах
    String keyWord;    // Ключевое слово
    String link;      // ссылка на сайт


    // конструктор по умолчанию search="+keyWord+"
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

    }


    @Override
    public void sourceRun() {

        Document document;
        long dataFormatLong; // сюда помещаем дату из вакансии
        Calendar calendar = new GregorianCalendar(); // текущая дата
        long dataMarker = calendar.getTime().getTime();
        calendar.add(Calendar.MONTH, this.periodMonths - this.periodMonths*2); // отнимаем кол-во месяцев (делаем число отрицательным)
        long dateDist = calendar.getTime().getTime(); // Конечная дата
        ArrayList<String> stringList = new ArrayList<>(); // список строк куда помещает отфильтрованнные данные
        DateHelp dateHelp = new DateHelp(); // вспомогательный класс для форматирования даты со страницы сайта

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
                if(elements.size() == 0){
                    System.out.println("Поиск не дал результатов");
                    break;
                }

                // перебираем коллекцию запрошенных элементов
                for (Element element : elements) {
                        String vacancy = element.child(0).text(); // строка с название вакансии
                        String linkJobVacancy = element.child(0).attr("href"); // ссылка для захода внутрь страницы вакансии и поиска даты публикации
                        String dataFormatStr  = dateHelp.getDateFormat(dateHelp.getDatePublication(linkJobVacancy)); // отформатированная дата вакансии в текстовом формате
                        dataFormatLong = dateHelp.dateLong(dateHelp.getDatePublication(linkJobVacancy));  // отформатированная дата в long

                        dataMarker = dataFormatLong;
                        if(dataFormatLong > dateDist){

                            stringList.add("Вакансия: " + vacancy + "- Опубликовано: " + dataFormatStr);
                            System.out.println("Вакансия: " + vacancy + "- Опубликовано: " + dataFormatStr );

                        }else{
                            String finalLine = "==================\r\n"
                                    +"Найдено " + stringList.size() + " вакансий (" + keyWord  + ") за период  "+  periodMonths + " месяца(ев) с "
                                    + new SimpleDateFormat("d MMM yyyy").format(dateDist) + " по "
                                    + new SimpleDateFormat("d MMM yyyy").format(new Date())
                                    +"\r\n=================";
                            stringList.add(finalLine);
                            DateHelp.saveDataInFile(stringList, "data.txt");
                            System.out.println(finalLine);
                            break; //Если дата публикации меньше конечной даты, выходим
                        }
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            System.out.println("Страница " +i);
            i ++; // страница (листаем форум)
        }
    }
}


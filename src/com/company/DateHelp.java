package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateHelp {

    // Возвращает дату в миллисекундах (тип long)
    // параметр strDateFormat - Отформатированная строка даты методом getDateFormat;
    public long dateLong(String strDateFormat) throws ParseException {

        Locale loc_ru = new Locale("ru", "RU");
        Date date = new SimpleDateFormat("d MMM yy", loc_ru).parse(getDateFormat(strDateFormat));
        return date.getTime();
    }

    // Метод вычитает из текущей даты время в месяцах и возвращает разницу в миллисекундах
    // параметр raw1 сырая строка времени из даты публикации вакансии
    // параметр month принимает число месяцев для вычитания
    public long getDiffDate(String raw1, int month) {

        Date date1 = null;
        Date date2 = null;
        Locale loc_ru = new Locale("ru", "RU");

        Calendar calendar = new GregorianCalendar();
        System.out.println(calendar.getTime().getTime()); // Текущая дата

        calendar.add(Calendar.MONTH, month);              // Дата с разницей во времени по месяцам
        System.out.println(calendar.getTime().getTime());

        try {
            date1 = new SimpleDateFormat("d MMM yy", loc_ru).parse(getDateFormat(raw1));
            date2 = calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        long diff = date1.getTime() - date2.getTime();

        return diff;
    }

    // Метод возвращает дату в виде строки в формате 'd MMM yyyy'
    public String getDateJavaFormat(String rawDate) {

        Date date = null;
        Locale loc_ru = new Locale("ru", "RU");

        try {
            date = new SimpleDateFormat("d MMM yy", loc_ru).parse(rawDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //System.out.println(new SimpleDateFormat("d MMM yyyy", loc_ru).format(date));

        return new SimpleDateFormat("d MMM yyyy", loc_ru).format(date);
    }

    // Метод обрабытывает сырую строку времени полученную методом getDatePublication
    // rawDate строка полученная методом getDatePublication (приводит строку '1 ноя 20' к '1 нояб. 2020')
    public String getDateFormat(String rawDate) {

        String ret = null;
        Locale loc_ru = new Locale("ru", "RU");
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        if (rawDate.equals("сегодня")) {
            Date date = new Date();

            ret = new SimpleDateFormat("d MMM yyyy", loc_ru).format(date);


        } else if(rawDate.equals("вчера")){

            ret = new SimpleDateFormat("d MMM yyyy", loc_ru).format(calendar.getTime());

        } else {

            String[] arStr = rawDate.split(" ");
            String mon = arStr[1];
            String newMon = "";

            switch (mon) {

                case "янв":
                    newMon = "января";
                    break;

                case "фев":
                    newMon = "февраля";
                    break;
                case "мар":
                    newMon = "марта";
                    break;
                case "апр":
                    newMon = "апреля";
                    break;
                case "май":
                    newMon = "мая";
                    break;
                case "июн":
                    newMon = "июня";
                    break;
                case "июл":
                    newMon = "июля";
                    break;
                case "авг":
                    newMon = "августа";
                    break;
                case "сен":
                    newMon = "сентября";
                    break;
                case "окт":
                    newMon = "октября";
                    break;
                case "ноя":
                    newMon = "ноября";
                    break;
                case "дек":
                    newMon = "декабря";
                    break;


            }

            ret = arStr[0] + " " + newMon + " " + arStr[2];

        }


        return getDateJavaFormat(ret);
    }


    // Метод возвращает дату публикации в виде сырой строки
    // Параметр link - ссылка на объявление
    public String getDatePublication(String link) {

        String ret = null;
        String retRes = null;
        Document document = null;
        try {
            document = Jsoup.connect(link).get();
            Elements elements = document.select(".msgFooter");
            // Выбирает первое сообщение в посте и возвращает время как на сайте
            retRes = elements.get(0).text().split(",")[0];

        } catch (IOException e) {
            e.printStackTrace();
        }
        ret = retRes;
        return ret;

    }
}

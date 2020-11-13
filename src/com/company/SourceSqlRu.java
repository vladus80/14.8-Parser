package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class SourceSqlRu implements IParser {

    String cssQuery; // Тег или другой элемент HTML разметки
    int periodMonths;  // Период в месяцах
    String keyWord;    // Ключевое слово
    String link;      // ссылка на сайт


    // конструктор по умолчанию
    public SourceSqlRu() {
        this("https://www.sql.ru/forum/job-offers/",".postslisttopic",1,"java");
    }

    // конструктор с параметрами
    public SourceSqlRu(String link, String cssQuery, int periodMonths, String keyWord) {
        this.cssQuery = cssQuery;
        this.periodMonths = periodMonths;
        this.keyWord = keyWord;
        this.link = link;
        //System.out.println(link);
    }


    @Override
    public void sourceRun() {
        //System.out.println(this.periodMonths - this.periodMonths*2);
        Document document = null;
        long dataFormatLong = 0; // сюда помещаем дату из вакансии
        Calendar calendar = new GregorianCalendar(); // текущая дата
        long dataMarker = calendar.getTime().getTime();
        calendar.add(Calendar.MONTH, this.periodMonths - this.periodMonths*2); // отнимаем кол-во месяцев (делаем число отрицательным)
        long dateDist = calendar.getTime().getTime(); // Конечная дата
        Charset charset = Charset.defaultCharset();
        ArrayList<String> stringList = new ArrayList<String>();


        int i = 1; // счетчик для листания страниц форума
        int marker =0; // маркер для выхода из основного цикла do

        // Запускаем цикл до тех пор время из поста больше конечного времени
        while (dataMarker > dateDist){
            //System.out.println(i);
            try {
                // запрашиваем страницу
                document = Jsoup.connect(this.link+ i).get();
                Elements elements = document.select(this.cssQuery); //фильтруем по элементу
                // создаем шаблон по которому будем фильтровать
                Pattern pattern = Pattern.compile(this.keyWord, Pattern.CASE_INSENSITIVE);
                // перебираем коллекцию запрошенных элементов
                for (Element element : elements) {
                    Matcher matcher = pattern.matcher(element.child(0).text());

                    // Если найдено сопадение
                    if (matcher.find()) {
                        String vacancy = element.child(0).text(); // строка с название вакансии
                        String linkJobVacancy = element.child(0).attr("href"); // ссылка для захода внутрь страницы вакансии и поиска даты публикации
                        String dataFormatStr  = getDateFormat(getDatePublication(linkJobVacancy)); // отформатированная дата вакансии в текстовом формате
                        dataFormatLong = dateLong(getDatePublication(linkJobVacancy));  // отформатированная дата в long
                        dataMarker = dataFormatLong;
                        if(dataFormatLong < dateDist){
                            //System.out.println("!!!! " +new Date(dateDist));
                            String finalLine = "==================\r\n"
                                    +"Найдено " + stringList.size() + " вакансий (" + keyWord  + ") за период  "+  periodMonths + " месяца(ев) с "
                                    + new SimpleDateFormat("d MMM yyyy").format(dateDist) + " по "
                                    + new SimpleDateFormat("d MMM yyyy").format(new Date())
                                    +"\r\n=================";
                            stringList.add(finalLine);
                            saveDataInFile(stringList, "data.txt");
                            System.out.println(finalLine);
                            break; //Если дата публикации меньше конечной даты, выходим
                        }else {
                            stringList.add("Вакансия: " + vacancy + "- Опубликовано: " + dataFormatStr);
                            System.out.println("Вакансия: " + vacancy + "- Опубликовано: " + dataFormatStr);
                        }

                    }
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            i ++; // страница (листаем форум)
        };

    }

    // Возвращает дату в миллисекундах (тип long)
    // параметр strDateFormat - Отформатированная строка даты методом getDateFormat;
    private long dateLong(String strDateFormat) throws ParseException {

        Locale loc_ru = new Locale("ru", "RU");
        Date date = new SimpleDateFormat("d MMM yy", loc_ru).parse(getDateFormat(strDateFormat));
        return date.getTime();
    }

    // Метод вычитает из текущей даты время в месяцах и возвращает разницу в миллисекундах
    // параметр raw1 сырая строка времени из даты публикации вакансии
    // параметр month принимает число месяцев для вычитания
    private long getDiffDate(String raw1, int month) {

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
    private String getDateJavaFormat(String rawDate) {

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
    private String getDateFormat(String rawDate) {

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
    private String getDatePublication(String link) {

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

    public ArrayList parseElement(ArrayList elements) {


        return null;
    }


    // Метод сохраняет в файл список найденных вхождений
    public void saveDataInFile(ArrayList<String> data, String fileSave) throws IOException {

        File file = new File(fileSave);
        if (!file.exists() && !file.isDirectory()) {
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String value : data) {
                writer.write(value + "\n");
            }
            writer.close();
            // Если файл пустой и не является директорией
        } else {

            if (file.length() == 0) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (String value : data) {
                    writer.write(value + "\n");
                }
                writer.close();
            }

        }
    }


}


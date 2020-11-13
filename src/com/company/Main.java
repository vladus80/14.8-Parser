package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {


       //new SourceSqlRu("https://www.sql.ru/forum/job-offers/", ".postslisttopic", Integer.valueOf("1"), "sql").sourceRun();
       // new SourceSqlRu().sourceRun();

//        int i = 1;
//        while (i<5){
//
//
//            String link = "https://www.sql.ru/forum/job-offers/"+i;
//            System.out.println("!!!Страница "+link);
//            Document doc = Jsoup.connect(link).get(); // получение html-страницы от сервера
//            Charset charset = Charset.defaultCharset();
//            doc.charset(charset); // конвертация в дефолтный charset
//            Elements elements = doc.select(".postslisttopic"); // получение элементов с тэгом h1
//
//            for (Element element: elements) {
//                System.out.println(element.child(0));
//            }
//            i++;
//        }


            if(args.length>0){
                String link = null;
                int period = Integer.valueOf(args[3]);
                String keyWord = args[5];
                if (args[1].equals("sql")) {
                    link = "https://www.sql.ru/forum/job-offers/";
                }

                if (!args[0].equals("-link") || !args[2].equals("-period") || !args[4].equals("-kw")) {

                    System.out.println("Неверный параметр...");
                    System.out.println("Введите в формате -link [ссылка] -period [число] -kw [ключевое_слово_поиска]");
                    System.exit(0);
                }else{
                    new SourceSqlRu(link, ".postslisttopic", period, keyWord).sourceRun();
                }

            }else {

                new SourceSqlRu().sourceRun();
            }

    }
}

package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

       //new SourceSqlRu("https://www.sql.ru/forum/job-offers/", ".postslisttopic", Integer.valueOf("1"), "sql").sourceRun(); URLEncoder.encode(yourString, HTTP.UTF-8);
       // new SourceSqlRu(2, "аналитик").sourceRun();
       // new SourceSqlRu().sourceRun();


             if(args.length==4) {
                int period = Integer.valueOf(args[1]);
                String keyWord = args[3];

                if(args[0].equals("-period") && args[2].equals("-kw")){

                    new SourceSqlRu(period, keyWord).sourceRun();

                }else {
                    System.out.println("Неверно заданы параметры. Введите в формате: -period 1 -kw 'java' где \n\r" +
                                        "-period период в месяцах \n\r"+
                                        "-kw ключевое слово");
                }

            }else {
                new SourceSqlRu().sourceRun();

            }

    }
}

package com.company;

import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class  Main {


    public static void main(String[] args) throws IOException {

        startIde();
        //startConsole(args);

    }

    public static void startIde() throws IOException {

//        Parser parser = new Parser(new SourceShare("https://lenta.ru", "a"));
//        parser.runParser();
//        parser.saveDataInFile("data3.txt",true);
//
//        Parser parser = new Parser(new SourceSqlRu(3, "sql"));
//        parser.runParser();
//        parser.saveDataInFile("data3.txt",true);

        Parser parser1 = new Parser(new SourceShare("http://ursa-tm.ru/forum/index.php?", "a"));
        parser1.saveDataInFile("data5.txt", true);

        //Parser parser2 = new Parser(new SourceSqlRu(2, "java"));
        //parser2.saveDataInFile("data6.txt", true);


    }


    public static void  startConsole(String[] args) throws IOException {
        if(args.length==4) {
            int period = Integer.valueOf(args[1]);
            String keyWord = args[3];

            if(args[0].equals("-period") && args[2].equals("-kw")){

                SourceSqlRu sourceSqlRu = new SourceSqlRu(period, keyWord);
                Parser parser = new Parser (sourceSqlRu);
                parser.runParser();
                parser.saveDataInFile("data.txt", true);


            }else {
                System.out.println("Неверно заданы параметры. Введите в формате: -period 1 -kw 'java' где \n\r" +
                        "-period период в месяцах \n\r"+
                        "-kw ключевое слово");
            }

        }else {
            Parser parser = new Parser (new SourceSqlRu());
            parser.runParser();
            parser.saveDataInFile("data.txt", true);

        }

    }


}

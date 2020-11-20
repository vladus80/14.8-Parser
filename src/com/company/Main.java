package com.company;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class  Main {


    public static void main(String[] args) throws IOException {

        //startIde();
        startConsole(args);

    }

    public static void startIde() throws UnsupportedEncodingException {

        SourceSqlRu sourceSqlRu = new SourceSqlRu();

        sourceSqlRu.sourceConnect();

        //System.out.println(sourceSqlRu.getPageElements());

        Parser parser = new Parser (new SourceSqlRu(3, "аналитик"));
        parser.setSource(sourceSqlRu);

        parser.setSource(new SourceSqlRu(5,"java"));
        //parser.saveObjectInFile("data2.txt");
        for (PageElement element: parser.getElements()) {
            System.out.println(element.getValue() + " " + element.getDate());
        }
        System.out.println("========================\n\r Всего найдено "
                + parser.getElements().size() + " вакансий с "
                + parser.getElements().get(0).getDate() +
                " по " + parser.getElements().get(parser.getElements().size()-1).getDate());

        //parser.runParser();


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

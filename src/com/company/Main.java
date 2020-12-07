package com.company;

import java.io.IOException;

public class  Main {


    public static void main(String[] args) throws IOException {

        //startIde();  // если запускаем через Idea
        startConsole(args); // если запускаем через jar-файл в терминале

    }

    public static void startIde() throws IOException {

        // Тестовый запрос к сайту https://lenta.ru в котором получаем все ссылки документа
        Parser parserLentaRu = new Parser(new SourceShare("https://lenta.ru", "a"));
        parserLentaRu.runParser();
        parserLentaRu.saveDataInFile("dataLenta.txt",true);

        // Тестовый запрос к сайту sql.ru в котором получаем вакансии с ключевым словом 'sql' за 3 последних месяца
        Parser parserSqlRu = new Parser(new SourceSqlRu(3, "sql"));
        parserSqlRu.runParser();
        parserSqlRu.saveDataInFile("dataSqlRu.txt",true);

    }


    public static void  startConsole(String[] args) throws IOException {
        if(args.length==6) {
            String url= args[1];
            int period = Integer.valueOf(args[3]);
            String keyWord = args[5];

            if(args[0].equals("-url") &&  args[2].equals("-period") && args[4].equals("-kw") ){

                if (url.equals("sql")){
                    //args[0] = "https://www.sql.ru/forum/actualsearch.aspx?search=java&sin=1&bid=66&a=&ma=0&dt=-1&s=4&so=1&pg=";
                    SourceSqlRu sourceSqlRu = new SourceSqlRu(period, keyWord);
                    Parser parser = new Parser (sourceSqlRu);
                    parser.runParser();
                    parser.saveDataInFile("dataSqlRu.txt", true);

                }else{
                    System.out.println("Неверно заданы параметры. Введите в формате: -url 'sql' -period 1 -kw 'java' где \n\r" +
                            "-period период в месяцах \n\r"+
                            "-kw ключевое слово");
                }


            }else {

                SourceShare sourceShare = new SourceShare(url, keyWord);
                Parser parser = new Parser(sourceShare);
                parser.runParser();
                parser.saveDataInFile("data.txt", true);

            }

        }else if (args.length==4){

            String url = args[1];
            String kw  = args[3];
            Parser parser = new Parser (new SourceShare(url, kw));
            parser.saveDataInFile("data.txt", true);


        }


        else {
            Parser parserSqlRu = new Parser (new SourceSqlRu());
            parserSqlRu.runParser();
            parserSqlRu.saveDataInFile("dataSqlRu.txt", true);

        }

    }

}

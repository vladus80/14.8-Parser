package com.company;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
* Основной класс, через который осуществляется работа с классами имплиментирующими интерфейс IParser,
* которые в свою очередь реализуют конкретные классы для работы с конкретными сайтами
* */

public class   Parser {

    private  IParser iParser;

    // Конструктор класса  принимает объекты реализующее интерфейс IParser
    public  Parser(IParser iParser) {

        this.iParser = iParser;
    }

    // Запускает работу парсера вызывая метод sourceConnect() интерфейса iParser
    public  void runParser() throws IOException {
        iParser.sourceConnect();
    }

    // Устанавливает объект класса реализующий интерфейс IParser
    public  void setSource(IParser iParser) {
        this.iParser = iParser;
    }

    public IParser getIParser() {
        return iParser;
    }

    // Метод возвращает список объектов PageElement
    public ArrayList<PageElement> getElements() throws IOException {
        iParser.sourceConnect();
        //iParser.toString();
        return iParser.getPageElements();

    }


   /*
   * Метод создает файл с данным и выводит в консоль полученные данные
   * Метод принимает два параметра:
   * fileSave - имя файла
   * outConsole - осуществлять вывод в консоль или нет
   * */

    public  void saveDataInFile( String fileSave, boolean outConsole) throws IOException {

        ArrayList<PageElement> data =  this.getElements();
        File file = new File(fileSave);
        if (!file.exists() && !file.isDirectory()) {
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (PageElement value : data) {
                writer.write(value.toString()+"\n");
                if (outConsole){
                    System.out.print(value.toString()+"\n");
                }
            }
            writer.close();
            // Если файл пустой и не является директорией
        } else {

            file.delete();
            file.createNewFile();

            if (file.length() == 0) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (PageElement value : data) {
                    writer.write(value.toString()+"\n");
                    if (outConsole){
                        System.out.print(value.toString()+"\n");
                    }
                }
                writer.close();
            }
        }
    }
}

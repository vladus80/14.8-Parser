package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class   Parser {


    private  IParser iParser;

    // Конструктор класса  принимает объекты реализующее интерфейс IParser
    public  Parser(IParser iParser) {
        this.iParser = iParser;
    }

    public  void runParser() {
        iParser.sourceConnect();
    }

    public  void setSource(IParser iParser) {
        this.iParser = iParser;
    }


    // Метод возвращает список объектов
    public ArrayList<PageElement> getElements(){
        iParser.sourceConnect();
        //iParser.toString();
        return iParser.getPageElements();

    }

    // Метод создает файл с данными
    public  void saveDataInFile( String fileSave, boolean outConsole) throws IOException {

        ArrayList<PageElement> data = this.getElements();
        File file = new File(fileSave);
        if (!file.exists() && !file.isDirectory()) {
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (PageElement value : data) {
                writer.write(value.getValue() + "\n");
                if (outConsole){
                    System.out.println(value.getValue());
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
                    writer.write(value.getValue() + "\n");
                    if (outConsole){
                        System.out.println(value.getValue());
                    }
                }
                writer.close();
            }
        }
    }
}

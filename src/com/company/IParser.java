package com.company;

/*
* Интерфейс определяет два метода:
* sourceConnect() - отвечает за соединение с ресурсом
* getPageElements()  - возвращает элеиенты документа
* */

import java.io.IOException;
import java.util.ArrayList;

public  interface  IParser {
    void sourceConnect() throws IOException;
    ArrayList<PageElement> getPageElements();
}

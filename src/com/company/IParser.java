package com.company;

import java.io.IOException;
import java.util.ArrayList;

public  interface  IParser {
    void sourceConnect() throws IOException;
    ArrayList<PageElement> getPageElements();
}

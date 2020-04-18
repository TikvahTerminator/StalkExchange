package com.example.stalkexchange;

import java.io.IOException;
import java.io.Reader;

public class readall {

    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int charplace;
        while ((charplace = rd.read()) != -1) {
            sb.append((char) charplace);
        }
        return sb.toString();
    }
}

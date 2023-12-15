package ui;

import java.math.BigDecimal;

public interface UserIO {

    void print(String message);

    String readString(String prompt);

    Double readDouble(String prompt);
    BigDecimal readBigDecimal(String prompt);

    char readChar(String prompt);

    int readInt(String prompt);
}

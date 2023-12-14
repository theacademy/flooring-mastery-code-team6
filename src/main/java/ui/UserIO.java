package ui;

public interface UserIO {

    void print(String message);

    String readString(String prompt);

    Double readDouble(String prompt);

    char readChar(String prompt);

    int readInt(String prompt);
}

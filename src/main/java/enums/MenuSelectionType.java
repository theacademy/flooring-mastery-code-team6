package enums;

public enum MenuSelectionType {
    DISPLAY_ORDERS (1),
    ADD_ORDERS (2),
    EDIT_ORDERS (3),
    REMOVE_ORDERS (4),
    EXPORT_ALL_DATA (5),
    QUIT (6);

    public final int selection;

    MenuSelectionType(int selection) {
        this.selection = selection;
    }

    public int getSelection() {
        return selection;
    }
}
package chapter12;

public interface Walk {
    default String getSpeed() {
        return "1";
    }

    static String ifNoAccessThenPublic() {
        return "this one is public";
    }

    private String privateAllowed() {
        return "private allowed";
    }

    static String protectedNotAllowed() {
        return "protected not allowed";
    }
}

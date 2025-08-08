package model;

public class Util {

    public static String generateCode() {
        return String.format("%6d", (int) (Math.random() * 1000000));
    }

    public static boolean isEmailValid(String email) {
        return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }

    public static boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*+=]).{8,}$");
    }

    public static boolean isCodeValid(String code) {
        return code.matches("^\\d{4,5}$");
    }

    public static boolean isInteger(String value) {
        return value.matches("^\\d+$");
    }

    public static boolean isDouble(String value) {
        return value.matches("^\\d+(\\.\\d{2})?$");
    }

    public static boolean isMobileValid(String value) {
        return value.matches("^07[0,1,2,4,5,6,7,8][0-9]{7}$");
    }
}

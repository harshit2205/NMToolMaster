package com.rajorpay.hex.nmtoolmaster.Utils;

public class ValidationUtil {
    public static String VALID = "VALID";

    public static boolean emailValidator(String email){

        return true;
    }

    public static String stbNrValidator(String stbNr){
        if(stbNr.length()!= 12 && stbNr.length()!= 16 && stbNr.length()!= 11){
            return "Enter valid set top box number";
        }
        return VALID;
    }

    public static String phoneNrValidator(String phoneNr){
        if(phoneNr.length()!=10 && !phoneNr.matches("[0-9]+"))return "Enter a valid Phone number";
        return VALID;
    }

    public static String nameValidator(String name){
        if(name.length()<3)return "Check Name Size";
        if(!name.matches("^[A-Za-z ]*$"))return "Name can only be characters";
        return VALID;
    }

    public static String passwordValidator(String password){
        if(password.length()<4)return "enter valid password";

        return VALID;
    }
}

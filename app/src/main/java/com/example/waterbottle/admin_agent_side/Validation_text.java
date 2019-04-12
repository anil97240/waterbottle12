package com.example.waterbottle.admin_agent_side;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation_text{

//validation for data add
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }

    public boolean isValidName(String name) {
        if (name != null && name.length() > 3) {
            return true;
        }
        return false;
    }
    public boolean isValidPrice(String name) {
        if (name != null && name.length() > 1) {
            return true;
        }
        return false;
    }
    public boolean isValidMobile(String name) {
        if (name != null && name.length() == 10) {
            return true;
        }
        return false;
    }
    public boolean isvaliddetails(String name) {
        if (name != null && name.length() > 2) {
            return true;
        }
        return false;
    }

  public void dataadd() {
      final List<String> list = new ArrayList<String>();
      list.add("Agent");
      list.add("Admin");

  }
}

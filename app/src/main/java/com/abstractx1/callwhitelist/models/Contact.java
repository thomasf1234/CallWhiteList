package com.abstractx1.callwhitelist.models;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tfisher on 25/02/2018.
 */

public class Contact {
    private String name;
    private List<String> phoneNumbers;


    public Contact(String name, List<String> phoneNumbers) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    public String getName() {
        return name;
    }

    public boolean hasPhoneNumber() {
        return !phoneNumbers.isEmpty();
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public boolean isBlacklistEntry() {
        Pattern pattern = Pattern.compile("^ *([zZ][Aa][Pp])|([bB][Ll][Oo][Cc][Kk])");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find( )) {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Pattern blackListPattern() {
        if (isBlacklistEntry()) {
            String blacklistEntry = name.substring(name.lastIndexOf(" ")+1);
            String blacklistRegex = blacklistEntry.replace("*",".*").replace("d","\\d");
            Pattern pattern = Pattern.compile(blacklistRegex);
            return pattern;
        } else {
            return null;
        }
    }
}

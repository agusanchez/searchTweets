package com.tweets.search.application.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String getDateDifference(Date thenDate){
        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();
        now.setTime(new Date());
        then.setTime(thenDate);

        long nowMs = now.getTimeInMillis();
        long thenMs = then.getTimeInMillis();
        long diff = nowMs - thenMs;

        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffMinutes<60){
            if (diffMinutes==1)
                return diffMinutes + " minuto";
            else
                return diffMinutes + " minutos";
        } else if (diffHours<24){
            if (diffHours==1)
                return diffHours + " hora";
            else
                return diffHours + " horas";
        }else if (diffDays<30){
            if (diffDays==1)
                return diffDays + " día";
            else
                return diffDays + " días";
        }else {
            return "";
        }
    }

    public static void changeTextinView(TextView tv, List<String> strings, int colour) {
        String vString = tv.getText().toString();
        Spannable spanRange = new SpannableString(vString);

        for (String target : strings) {
            int startSpan = 0, endSpan = 0;
            while (true) {
                startSpan = vString.indexOf(target, endSpan);
                ForegroundColorSpan foreColour = new ForegroundColorSpan(colour);

                if (startSpan < 0)
                    break;
                endSpan = startSpan + target.length();
                spanRange.setSpan(foreColour, startSpan, endSpan,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }
        tv.setText(spanRange);
    }

    public static List<String> findListOfCoincidences(String origin, List<String> patterns){
        List<String> strings= new ArrayList<String>();

        for (String pattern : patterns) {
            Pattern titleFinder = Pattern.compile(pattern);
            String string = origin;
            Matcher regexMatcher = titleFinder.matcher(string);
            while (regexMatcher.find()) {
                strings.add(regexMatcher.group());
            }
        }
        return strings;
    }
}

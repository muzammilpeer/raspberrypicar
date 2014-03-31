
package com.muzamilpeer.raspberrypicar.app.common;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class CommonObjects {

    public static final String APP_NAME_TAG = "mPay LOG";

    public static final String DATA = "data";

    public static final String MESSAGE = "message";

    public static final String ERROR = "error";

    public static final String STATUS = "status";

    public static final String DEVICE_TYPE = "1";

    public static boolean testEmpty(String str) {
        if ((str == null) || str.matches("^\\s*$")) {
            return true;
        } else {
            if (str.equalsIgnoreCase("null")) {
                return true;
            } else if (str.contains("null")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }

    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public String getRandomNumber(int digCount) {
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(digCount);
        for (int i = 0; i < digCount; i++)
            sb.append((char)('0' + rnd.nextInt(10)));
        return sb.toString();

    }

    public static boolean isNumeric(String str) {
        try {
            @SuppressWarnings("unused")
            int d = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isNumericFloat(String str) {
        try {
            @SuppressWarnings("unused")
            float f = Float.parseFloat(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String getGMTDate(String dob) {

        String dateNewFormat;// "2-26-1990";

        String oldFormat = "MM-dd-yyyy";
        String newFormat = "yyyy-MM-dd";

        SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat, Locale.ENGLISH);

        try {

            dateNewFormat = sdf2.format(sdf1.parse(dob));

            MyLog.e("dob_new_format", dateNewFormat);

            SimpleDateFormat date_formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale.ENGLISH);
            date_formater.setTimeZone(TimeZone.getTimeZone("GMT"));

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
            Date start_date = cal.getTime();

            String time_starts = start_date.getTime() + "";
            time_starts = time_starts.substring(0, 10);

            String date_start_GMT = date_formater.format(start_date);

            MyLog.e("GMT_date_start", date_start_GMT);

            date_start_GMT = dateNewFormat
                    + date_start_GMT
                            .substring(date_start_GMT.indexOf("T"), date_start_GMT.length());

            MyLog.e("Final dob", date_start_GMT);

            return date_start_GMT;

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return e.getMessage();
        }

    }

    public static String isValidDate(String checkDate) {

        Calendar c = Calendar.getInstance();
        int current_year = c.get(Calendar.YEAR);
        int current_month = c.get(Calendar.MONTH);
        int current_day = c.get(Calendar.DAY_OF_MONTH);

        String currentDate = new StringBuilder().append(current_year).append("-")
                .append(current_month + 1).append("-").append(current_day).toString();

        MyLog.e("Current Sys Date", currentDate);
        MyLog.e("Check Date", checkDate);

        String dateNewFormat;// "2-26-1990";

        String oldFormat = "yyyy-MM-dd";
        String newFormat = "yyyy-MM-dd";

        String result = "";

        try {

            SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
            SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat, Locale.ENGLISH);

            dateNewFormat = sdf2.format(sdf1.parse(checkDate));

            MyLog.e("dob_new_format", dateNewFormat);

            // Date date1 = sdf.parse("2010-01-31");
            // Date date2 = sdf.parse("2010-01-31");

            Date check_date = sdf2.parse(dateNewFormat);
            Date current_date = sdf2.parse(currentDate);

            System.out.println(sdf2.format(check_date));
            System.out.println(sdf2.format(current_date));

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(check_date);
            cal2.setTime(current_date);

            if (cal1.after(cal2)) {

                System.out.println("check_date is after current_date");

                result = "after_today";
            }

            if (cal1.before(cal2)) {

                System.out.println("check_date is before current_date");

                result = "before_today";
            }

            if (cal1.equals(cal2)) {

                System.out.println("check_date is equal current_date");

                result = "today";
            }

            return result;

        } catch (ParseException ex) {
            ex.printStackTrace();

            return result;
        }
    }

    public static String checkEndDate(String start_date, String end_date) {

        String start_dateNewFormat;// "2-26-1990";
        String end_dateNewFormat;

        String oldFormat = "MM-dd-yyyy";
        String newFormat = "yyyy-MM-dd";

        String result = "";

        try {

            SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
            SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat, Locale.ENGLISH);

            start_dateNewFormat = sdf2.format(sdf1.parse(start_date));
            end_dateNewFormat = sdf2.format(sdf1.parse(end_date));

            MyLog.e("start_date_new_format", start_dateNewFormat);
            MyLog.e("end_date_new_format", end_dateNewFormat);

            Date end_date1 = sdf2.parse(end_dateNewFormat);
            Date start_date2 = sdf2.parse(start_dateNewFormat);

            System.out.println(sdf2.format(end_date1));
            System.out.println(sdf2.format(start_date2));

            if (end_date1.after(start_date2)) {

                System.out.println("end_date1 is after start_date2");

                result = "after";
            }

            if (end_date1.before(start_date2)) {

                System.out.println("end_date1 is before start_date2");

                result = "before";
            }

            if (end_date1.equals(start_date2)) {

                System.out.println("end_date1 is equal start_date2");

                result = "today";
            }

            return result;

        } catch (ParseException ex) {
            ex.printStackTrace();

            return result;
        }
    }

    public static int getDaysDifferece(String start_date, String end_date) {

        String start_dateNewFormat;// "2-26-1990";
        String end_dateNewFormat;

        String oldFormat = "MM-dd-yyyy";
        String newFormat = "yyyy-MM-dd";

        int days = 0;

        try {

            SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
            SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat, Locale.ENGLISH);

            start_dateNewFormat = sdf2.format(sdf1.parse(start_date));
            end_dateNewFormat = sdf2.format(sdf1.parse(end_date));

            MyLog.e("start_date_new_format", start_dateNewFormat);
            MyLog.e("end_date_new_format", end_dateNewFormat);

            Date end_date1 = sdf2.parse(end_dateNewFormat);
            Date start_date2 = sdf2.parse(start_dateNewFormat);

            days = (int)((end_date1.getTime() - start_date2.getTime()) / (1000 * 60 * 60 * 24));

            MyLog.e("Days", ""
                    + (int)((end_date1.getTime() - start_date2.getTime()) / (1000 * 60 * 60 * 24)));

            return days;

        } catch (ParseException ex) {
            ex.printStackTrace();

            return days;
        }
    }

    public static String getGMTParsedDate(String serverDate) {

        String birthdate = serverDate;

        birthdate = birthdate.substring(0, birthdate.indexOf("T"));

        String oldFormat = "yyyy-MM-dd";
        String newFormat = "MM-dd-yyyy";

        SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat, Locale.ENGLISH);

        try {

            birthdate = sdf2.format(sdf1.parse(birthdate));

            System.out.println(sdf2.format(sdf1.parse(birthdate)));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        MyLog.e("dob", birthdate);

        return birthdate;

    }

    public static String getStartDate() {
        SimpleDateFormat date_formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.ENGLISH);

        date_formater.setTimeZone(TimeZone.getTimeZone("GMT"));

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);

        Date start_date = cal.getTime();

        String time_starts = start_date.getTime() + "";
        time_starts = time_starts.substring(0, 10);

        String date_start_GMT = date_formater.format(start_date);

        return date_start_GMT;
    }

    public static String getNonce() {
        String md5Pass = null;
        String nonce = null;
        try {
//            md5Pass = MD5Encryption.MD5(SoapUtil.USERNAME);
            md5Pass = MD5Encryption.MD5("username");

            String md5Pass_lower = md5Pass.toLowerCase();

            CommonObjects cm = new CommonObjects();
            String random_string = cm.getRandomNumber(8);

            SimpleDateFormat date_formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale.ENGLISH);

            date_formater.setTimeZone(TimeZone.getTimeZone("GMT"));

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);

            Date start_date = cal.getTime();

            String time_starts = start_date.getTime() + "";
            time_starts = time_starts.substring(0, 10);

            String date_start_GMT = date_formater.format(start_date);

            start_date.setMinutes(start_date.getMinutes() + 3);

            String date_end_GMT = date_formater.format(start_date);

            String digest = md5Pass_lower + ":" + random_string + ":" + date_start_GMT;

            String encodedDigest;

            encodedDigest = MD5Encryption.MD5(digest);

            nonce = random_string + ":" + time_starts + ":" + encodedDigest;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return nonce;
    }
}

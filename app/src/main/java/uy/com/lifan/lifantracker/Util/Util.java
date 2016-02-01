package uy.com.lifan.lifantracker.Util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Util extends Activity {
    /**
     * Check if a VIN Number is Valid
     * Based on VIN.java class at:
     * http://www.cs.princeton.edu/introcs/31datatype/VIN.java.html
     **/

    private static final String LOG_TAG = "VIN_Algorithm";

    public static boolean isValid(String vinNumber) {

        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 0, 1, 2, 3, 4, 5, 0, 7, 0, 9,
                2, 3, 4, 5, 6, 7, 8, 9};
        int[] weights = {8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2};


        String s = vinNumber;
        s = s.replaceAll("-", "");
        s = s.toUpperCase();

        if (s.length() != 17) {
            Log.d(LOG_TAG, "VIN number must be 17 characters");
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char c = s.charAt(i);
            int value;
            int weight = weights[i];

            // letter
            if (c >= 'A' && c <= 'Z') {
                value = values[c - 'A'];
                if (value == 0) {
                    Log.d(LOG_TAG, "Illegal character: " + c);
                    return false;
                }
            }

            // number
            else if (c >= '0' && c <= '9')
                value = c - '0';

                // illegal character
            else {
                Log.d(LOG_TAG, "Illegal character: " + c);
                return false;
            }

            sum = sum + weight * value;

        }

        // check digit
        sum = sum % 11;
        char check = s.charAt(8);
        if (check != 'X' && (check < '0' || check > '9')) {
            Log.d(LOG_TAG, "Illegal check digit: " + check);
            return false;
        }
        if (sum == 10 && check == 'X') {
            Log.d(LOG_TAG, "Valid");
            return true;
        } else if (sum == check - '0') {
            Log.d(LOG_TAG, "Valid");
            return true;
        } else {
            Log.d(LOG_TAG, "Invalid");
            return false;
        }
    }

    public static String getAnioVIN(String vinNumber) {

        String s = vinNumber;

        s = s.replaceAll("-", "");
        s = s.toUpperCase();

        if (s.length() != 17) {
            Log.d(LOG_TAG, "VIN number must be 17 characters");
            return "Error";
        }

        String letraAnio;
        letraAnio = s.substring(9, 10);

        if (letraAnio.compareTo("A") == 0)
            return "2010";
        else if (letraAnio.compareTo("B") == 0)
            return "2011";
        else if (letraAnio.compareTo("C") == 0)
            return "2012";
        else if (letraAnio.compareTo("D") == 0)
            return "2013";
        else if (letraAnio.compareTo("E") == 0)
            return "2014";
        else if (letraAnio.compareTo("F") == 0)
            return "2015";
        else if (letraAnio.compareTo("G") == 0)
            return "2016";
        else if (letraAnio.compareTo("H") == 0)
            return "2017";
        else if (letraAnio.compareTo("J") == 0)
            return "2018";
        else if (letraAnio.compareTo("K") == 0)
            return "2019";
        else
            return "Error";

    }

    public int is_conected() {

        int conected = 0;

        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {

            URL url = null;
            try {
                url = new URL("http://192.168.2.2:8080/admin");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                conected = 1;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection)
                        url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                conected = 1;
            }
            connection.setRequestProperty("User-Agent", "yourAgent");
            connection.setRequestProperty("Connection", "close");
            connection.setConnectTimeout(1000);

            try {
                connection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (connection.getResponseCode() == 200) {
                    conected = 0;
                } else {
                    conected = 1;

                }
            } catch (IOException e) {
                e.printStackTrace();
                conected = 1;
            }


        } else {

            conected = 2;

        }
        return conected;

    }
}

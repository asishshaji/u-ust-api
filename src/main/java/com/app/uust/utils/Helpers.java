package com.app.uust.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helpers {

  public static Long getCurrentTimestamp() {
    return new java.sql.Timestamp(System.currentTimeMillis()).getTime();
  }
}

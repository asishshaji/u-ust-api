package com.app.uust.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helpers {

  public static String getCurrentTimestamp() {
    return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.US)
    .format(new Date());
  }
}

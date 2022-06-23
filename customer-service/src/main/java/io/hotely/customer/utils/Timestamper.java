package io.hotely.customer.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Component;

@Component
public class Timestamper {

  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:s");
  Calendar calendar = Calendar.getInstance();

  /** Returns the current Date with Timestamp in dd-mm-yyyy hh:mm:s format */
  public String timestamp() {
    return simpleDateFormat.format(calendar.getTime());
  }
}

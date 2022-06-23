package io.hotely.hotel.config;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class LocalDateToDate implements Converter<LocalDate, Date> {

  @Override
  public Date convert(LocalDate localDate) {
   return java.util.Date.from(localDate
          .atStartOfDay()
          .atZone(ZoneId.systemDefault())
          .toInstant());
  }
}

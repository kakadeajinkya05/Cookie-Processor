package com.qc.cli.utils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateTimeUtil {

  public static OffsetDateTime getParsedOffsetDateTime(String timestamp) {
    try {
      return OffsetDateTime.parse(timestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
          .withOffsetSameInstant(ZoneOffset.UTC);
    } catch (DateTimeParseException e) {
      log.error("Date Time Parsing Exception", e);
      return null;
    }
  }

  public static LocalDate getParsedLocalDate(String date) {
    try {
      return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (DateTimeParseException e) {
      log.error("Date Time Parsing Exception", e.getMessage());
      return null;
    }
  }

}

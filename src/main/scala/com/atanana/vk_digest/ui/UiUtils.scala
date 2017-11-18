package com.atanana.vk_digest.ui

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate, ZoneId}
import java.util.Locale

object UiUtils {
  val SUBJECT_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("ru"))
  val MESSAGE_DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm d/MM")

  def messageDateFormat(date: Int): String = {
    Instant.ofEpochSecond(date).atZone(ZoneId.of("GMT+3")).toLocalDateTime.format(MESSAGE_DATETIME_FORMATTER)
  }

  def subjectDate: String = {
    LocalDate.now().format(SUBJECT_DATE_FORMATTER)
  }
}

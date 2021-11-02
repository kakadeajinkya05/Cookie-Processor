package com.qc.cli.commands;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.qc.cli.domain.Cookie;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CookieProcessorApplicationTests {

  @InjectMocks
  private CookieReader cookieReader;

  private Optional<File> file;
  private List<String> collect;

  @DisplayName("Should log an error message if file not found in the path")
  @Test
  void shouldLogAnErrorIfFileIsAbsent() {
    file = Optional.ofNullable(new File("test"));
    cookieReader.setCookieFile(file);

    List<Cookie> cookies = cookieReader.call();

    assertThat(cookies.size(), is(0));

  }

  @DisplayName("Should log an error message if file is not passed with option -f")
  @Test
  void shouldLogAnErrorIfFileIsNotPassed() {
    file = Optional.ofNullable(null);
    cookieReader.setCookieFile(file);

    List<Cookie> cookies = cookieReader.call();

    assertThat(cookies.size(), is(0));

  }

  @DisplayName("Should log an error message if Date cannot be parsed")
  @Test
  void shouldLogAnErrorIfDateCannotBeParsed() {
    file = Optional.ofNullable(new File("src\\test\\resources\\cookie"));
    cookieReader.setDate("09-12-2018");
    cookieReader.setCookieFile(file);

    List<Cookie> cookies = cookieReader.call();

    assertThat(cookies.size(), is(0));

  }

  @DisplayName("Should log an error message if Date is not passed with option -d")
  @Test
  void shouldLogAnErrorIfDateIsNotPassed() {
    file = Optional.ofNullable(new File("src\\test\\resources\\cookie"));
    cookieReader.setCookieFile(file);

    List<Cookie> cookies = cookieReader.call();

    assertThat(cookies.size(), is(0));

  }

  @DisplayName("Should return the cookies for valid date")
  @Test
  public void shouldReturnCookiesForValidDate() {
    file = Optional.ofNullable(new File("src\\test\\resources\\cookie"));
    cookieReader.setDate("2018-12-09");
    cookieReader.setCookieFile(file);

    List<Cookie> cookies = cookieReader.call();
    collect = cookies.stream().map(Cookie::getCookie).collect(Collectors.toList());

    assertThat(cookies.size(), is(4));
    assertTrue(collect.contains("AtY0laUfhglK3lC7"));
    assertTrue(collect.contains("5UAVanZf6UtGyKVS"));
    assertTrue(collect.contains("SAZuXPGUrfbcn5UA"));
    assertTrue(collect.contains("AtY0laUfhglK3lC7"));

  }


  @DisplayName("Should return empty cookies for absent cookie timestamp")
  @Test
  public void shouldReturnEmptyCookiesForAbsentTimestamp() {
    file = Optional.ofNullable(new File("src\\test\\resources\\cookie"));
    cookieReader.setDate("2018-12-10");
    cookieReader.setCookieFile(file);

    List<Cookie> cookies = cookieReader.call();

    assertNull(cookies);

  }

  @DisplayName("Should not store data if cookies is absent in the file")
  @Test
  public void shouldNotStoreDataIfCookieIsAbsent() {
    file = Optional.ofNullable(new File("src\\test\\resources\\without-cookie"));
    cookieReader.setDate("2018-12-9");
    cookieReader.setCookieFile(file);

    List<Cookie> cookies = cookieReader.call();

    assertThat(cookies.size(), is(0));

  }

  @DisplayName("Should not store cookie if date is absent")
  @Test
  public void shouldNotStoreCookieIfTimestampIsAbsent() {
    file = Optional.ofNullable(new File("src\\test\\resources\\cookie-without-timestamp"));
    cookieReader.setCookieFile(file);

    List<Cookie> cookies = cookieReader.call();

    assertThat(cookies.size(), is(0));

  }


}

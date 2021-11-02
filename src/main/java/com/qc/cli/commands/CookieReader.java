package com.qc.cli.commands;

import com.qc.cli.domain.Cookie;
import com.qc.cli.utils.DateTimeUtil;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Option;

@Slf4j
@Setter
@NoArgsConstructor
public class CookieReader implements Callable<List<Cookie>> {

  private final int SPLIT_SIZE = 2;
  @Option(names = {"-f"}, description = "specify cookie file")
  private Optional<File> CookieFile;

  @Option(names = {"-d", "--date"}, description = "specify date in UTC format")
  private String date;


  public List<Cookie> call() {

    List<Cookie> cookies = new ArrayList<>();
    if (isValid(CookieFile) && isValid(date)) {
      Map<LocalDate, List<Cookie>> map = mapFileContentToCookie(CookieFile.get());
      LocalDate parsedDate = DateTimeUtil.getParsedLocalDate(date);

      cookies = map.get(parsedDate);

      if (cookies != null) {
        cookies.stream().sorted(Comparator.comparing(Cookie::getOffsetDateTime)).forEach(c -> log.info(c.getCookie()));
      }
    } else {
      log.error("Invalid Date or File ");
    }
    return cookies;
  }

  private static boolean isValid(String date) {
    return date != null && DateTimeUtil.getParsedLocalDate(date) != null;
  }

  private static boolean isValid(Optional<File> file) {
    return file.isPresent() && file.get().isFile();
  }

  private Map<LocalDate, List<Cookie>> mapFileContentToCookie(File file) {

    Map<LocalDate, List<Cookie>> map = new HashMap<>();

    try {
      List<String> lines = Files.readAllLines(file.toPath());
      List<Cookie> cookies = new ArrayList<>();
      for (String line : lines) {
        validateAndSplitString(cookies, line);
      }
      addCookiesToMap(map, cookies);
    } catch (Exception e) {
      log.error("Exception occurred", e);
    }

    return map;
  }

  private void validateAndSplitString(List<Cookie> cookies, String line) {
    if (!line.startsWith("cookie")) {
      String[] split = line.split(",");
      if (split.length >= SPLIT_SIZE) {
        cookies.add(new Cookie(split[0], DateTimeUtil.getParsedOffsetDateTime(split[1])));
      }
    }
  }

  private static void addCookiesToMap(Map<LocalDate, List<Cookie>> map, List<Cookie> cookies) {
    for (Cookie cookie : cookies) {
      LocalDate localDate = cookie.getOffsetDateTime().toLocalDate();
      if (map.get(localDate) != null) {
        map.get(localDate).add(cookie);
      } else {
        List<Cookie> list = map.computeIfAbsent(localDate, h -> new ArrayList<>());
        list.add(cookie);
        map.put(localDate, list);
      }
    }
  }
}

package com.qc.cli.domain;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cookie {

  private String cookie;
  private OffsetDateTime offsetDateTime;
}

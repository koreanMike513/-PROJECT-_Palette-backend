package com.palette.back_end.util.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {
  JPG(".jpg"),
  JPEG(".jpeg"),
  PNG(".png"),
  SVG(".svg")
  ;

  private final String extension;
}

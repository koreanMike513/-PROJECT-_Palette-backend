package com.palette.back_end.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypes {
  ARTIST("화가"),
  CLIENT("의뢰인"),
  BOTH("중복")
  ;

  private final String description;
}

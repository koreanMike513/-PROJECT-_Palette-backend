package com.palette.back_end.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Conditions {
  FULLY_PAID("모든 비용 부담 O (교통, 식대, 등등 포함)"),
  PARTLY_PAID("부분 부담 (상의 필요)"),
  NOT_PAID("모든 비용 부담 X"),
  OTHERS("기타")
  ;

  private final String description;
}

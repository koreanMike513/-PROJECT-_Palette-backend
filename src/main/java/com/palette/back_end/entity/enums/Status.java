package com.palette.back_end.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
  READY("대기 중"),
  IN_PROGRESS("진행 중"),
  COMPLETED("완료")
  ;

  private final String description;
}

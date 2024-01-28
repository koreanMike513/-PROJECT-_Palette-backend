package com.palette.back_end.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArtTypes {
  PAINTING("그림"),
  SCULPTURE("조긱"),
  DECORATING("장식"),
  INSTALLATION("설치"),
  PRINT_MAKING("판화"),
  CALLIGRAPHY("캘리그라피"),
  DRAWING("드로잉"),
  PHOTOGRAPHY("사진"),
  GRAFFITI("그래피티"),
  OTHERS("기타")
  ;

  private final String name;
}

package com.palette.back_end.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Districts {
  GANGNAM("강남구"),
  GANGBUK("강북구"),
  GANGDONG("강북구"),
  GANGSEO("강서구"),
  GWANAK("관악구"),
  GWANGJIN("광진구"),
  GURO("구로구"),
  GEUMCHEON("금천구"),
  NOWON("노원구"),
  DOBONG("도봉구"),
  DONGDAEMUN("동대문구"),
  DONGJAK("동작구"),
  MAPO("마포구"),
  SEOCHO("서초구"),
  SEODAEMUN("서대문구"),
  SEONGDONG("성동구"),
  SEONGBUK("성북구"),
  SONGBUK("송북구"),
  YANGCHEON("양천구"),
  YEONGDEUNGPO("영등포구"),
  YONGSAN("용산구"),
  EUNPYEONG("은평구"),
  JONGNO("종로구"),
  JUNG("중구"),
  JUNGNANG("중랑구")
  ;

  private final String name;
}




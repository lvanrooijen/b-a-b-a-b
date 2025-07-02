package com.lvr.babab.babab.util;

public class TestUtilities {
  public static String getRandomString(int length) {
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < length; i++) {
      str.append((char) (Math.random() * 26 + 97));
    }
    return str.toString();
  }
}

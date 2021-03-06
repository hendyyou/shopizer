package com.salesmanager.web.admin.entity.userpassword;

import java.util.Random;


public class UserReset
{
  final static String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

  final int RANDOM_STRING_LENGTH = 10;

  public static String generateRandomString()
  {
    StringBuilder randStr = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      int number = getRandomNumber();
      char ch = CHAR_LIST.charAt(number);
      randStr.append(ch);
    }
    return randStr.toString();
  }

  private static int getRandomNumber()
  {
    int randomInt = 0;
    Random randomGenerator = new Random();
    randomInt = randomGenerator.nextInt(CHAR_LIST.length());
    if (randomInt - 1 == -1) {
      return randomInt;
    }
    return randomInt - 1;
  }

  
}
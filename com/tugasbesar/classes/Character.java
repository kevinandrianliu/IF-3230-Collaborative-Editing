package com.tugasbesar.classes;

import java.io.Serializable;

public class Character implements Serializable {
  private String computerId;
  private char value;
  private int[] position;

  public Character (String computerId, char value, int[] position){
    this.computerId = computerId;
    this.value = value;
    this.position = position.clone();
  }

  @Override
  public String toString(){
    return computerId + ", " + value + ", " + position[0] + position[1];
  }
}
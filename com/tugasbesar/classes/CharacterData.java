package com.tugasbesar.classes;

import java.io.Serializable;

public class CharacterData implements Serializable {
  private String computerId;
  private char value;
  private int position;
  private String positionId;
  private String order;

  public CharacterData (String computerId, char value, int position, String positionId, String order){
    this.computerId = computerId;
    this.value = value;
    this.position = position;
    this.positionId = positionId;
    this.order = order;
  }

  @Override
  public String toString(){
    return computerId + ", " + value + ", " + position + ", " + positionId;
  }

  /**
   * @return the computerId
   */
  public String getComputerId() {
    return computerId;
  }

  /**
   * @param computerId the computerId to set
   */
  public void setComputerId(String computerId) {
    this.computerId = computerId;
  }

  /**
   * @return the value
   */
  public char getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(char value) {
    this.value = value;
  }

  /**
   * @return the position
   */
  public int getPosition() {
    return position;
  }

  /**
   * @param position the position to set
   */
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * @return the positionId
   */
  public String getPositionId() {
    return positionId;
  }

  /**
   * @param positionId the positionId to set
   */
  public void setPositionId(String positionId) {
    this.positionId = positionId;
  }

  /**
   * @return the order
   */
  public String getOrder() {
    return order;
  }

  /**
   * @param order the order to set
   */
  public void setOrder(String order) {
    this.order = order;
  }
}
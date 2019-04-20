public class Character {
  private String computerId;
  private char value;
  private int[] position;

  public Character (String computerId, char value, int[] position){
    this.computerId = computerId;
    this.value = value;
    this.position = position.clone();
  }
}
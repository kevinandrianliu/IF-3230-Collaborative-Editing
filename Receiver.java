import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receiver {
  private final short BUFFER_SIZE = 1024 * 6;
  private MulticastSocket socket;
  private InetAddress ipAddressGroup;
  private int port;

  public Receiver(String ip, int port) {
    this.port = port;

    // Connecting to a multicast address
    try {
      socket = new MulticastSocket(port);
      ipAddressGroup = InetAddress.getByName(ip);
      socket.joinGroup(ipAddressGroup);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }

  public void receiveMessage() throws IOException {
    while (true){
      // Receive object
      System.out.println("[Receiving Object...]");
      byte[] buffer = new byte[BUFFER_SIZE];
      socket.receive(new DatagramPacket(buffer, BUFFER_SIZE, ipAddressGroup, port));
      
      // Deserialize
      ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
      ObjectInputStream ois = new ObjectInputStream(bais);
      try {
        Character character = (Character) ois.readObject();
        System.out.println(character.toString());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args){
    Receiver receiver = new Receiver(args[0], Integer.parseInt(args[1]));
    try {
      receiver.receiveMessage();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

class Character implements Serializable {
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
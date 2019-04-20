import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Sender {
  private MulticastSocket socket;
  private InetAddress ipAdressGroup;
  private int port;

  public Sender(String ip, int port){
    this.port = port;

    try {
      socket = new MulticastSocket(port);
      ipAdressGroup = InetAddress.getByName(ip);
      socket.joinGroup(ipAdressGroup);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void SendMessage(Character character) throws IOException{
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(character);
    byte[] data = baos.toByteArray();

    System.out.println("[Sending object...]");
    socket.send(new DatagramPacket(data, data.length, ipAdressGroup, port));
  }

  public static void main(String[] args){
    Sender sender = new Sender(args[0], Integer.parseInt(args[1]));

    int[] pos = {1,2};
    try {
      sender.SendMessage(new Character("A123",'A',pos));
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
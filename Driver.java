import com.tugasbesar.classes.Character;
import com.tugasbesar.networking.Sender;
import com.tugasbesar.networking.Receiver;

public class Driver {
  public static void main(String[] args){
    if (args.length != 3){
      System.out.println("Usage: java Driver <ip-addr> <port> <1/2>");
      System.exit(0);
    }
    String ip = args[0];
    int port = Integer.parseInt(args[1]);

    int[] position = {1, 2};
    SenderThread senderThread = new SenderThread(ip, port);
    senderThread.CreateCharacter("A123", '%', position);
    ReceiverThread receiverThread = new ReceiverThread(ip, port);

    if (args[2].equals("1")){
      senderThread.CreateCharacter("A1", '%', position);
    } else {
      senderThread.CreateCharacter("X3", '$', position);
    }

    senderThread.run();
    receiverThread.run();
  }
}

class SenderThread implements Runnable {
  private String ip;
  private int port;
  private Sender sender;
  private Character character;

  public SenderThread(String ip, int port){
    this.ip = ip;
    this.port = port;

    sender = new Sender(ip,port);
  }

  public void CreateCharacter(String computerId, char value, int[] position){
    character = new Character(computerId, value, position);
  }

  public void run() {
    sender.StartConnection();

    if (character != null){
      int i = 10;
      while (i > 0){
        sender.SendMessage(character);
        i--;

        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          System.out.println("Thread interrupted");
        }
      }
    }

    sender.CloseConnection();
  }
}

class ReceiverThread implements Runnable {
  private String ip;
  private int port;
  private Receiver receiver;

  public ReceiverThread(String ip, int port){
    this.ip = ip;
    this.port = port;

    receiver = new Receiver(ip,port);
  }

  public void run() {
    receiver.StartConnection();

    while (true){
      receiver.receiveMessage();
    }

    //receiver.CloseConnection();
  }
}
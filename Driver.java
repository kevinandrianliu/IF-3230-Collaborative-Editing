import java.util.Scanner;

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

    SenderThread senderThread = new SenderThread(ip, port);
    ReceiverThread receiverThread = new ReceiverThread(ip, port);

    senderThread.start();
    receiverThread.start();

    Scanner scanner = new Scanner(System.in);

    while(true){
      synchronized(senderThread){
        System.out.print("Enter text: ");
        String line = scanner.nextLine();
        String[] words = line.split(" ");

        if (words.length != 3){
          System.out.println("Not enough param for character!");
        } else {
          String computerId = words[0];
          char value = words[1].charAt(0);
          int[] position = new int[2];
          position[0] = Integer.parseInt((words[2].split(","))[0]);
          position[1] = Integer.parseInt((words[2].split(","))[1]);
          
          senderThread.CreateCharacter(computerId, value, position);
          senderThread.notify();

          try {
            senderThread.wait();
          } catch (InterruptedException e) {
            System.out.println("Sender thread interrupted on maint thread");
          }
        }
      }
    }
  }
}

class SenderThread extends Thread {
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

    while(true){
      synchronized(this){
        try {
          wait();
          sender.SendMessage(character);
          notify();
        } catch (InterruptedException e) {
          System.out.println("Thread sender interrupted on sender thread");
        }
      }
    }
    //sender.CloseConnection();
  }
}

class ReceiverThread extends Thread {
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
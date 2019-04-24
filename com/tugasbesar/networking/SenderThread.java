package com.tugasbesar.networking;

import com.tugasbesar.classes.CharacterData;
import java.util.concurrent.ThreadLocalRandom;

public class SenderThread extends Thread {
  private String ip;
  private int port;
  private Sender sender;
  private CharacterData character;
  private String computerId;
  public boolean isConnecting;

  public SenderThread(String ip, int port){
    this.ip = ip;
    this.port = port;
    this.isConnecting = true;

    sender = new Sender(ip,port);
  }

  public void CreateCharacter(String computerId, char value, int position, String positionId, String order){
    character = new CharacterData(computerId, value, position, positionId, order);
  }
  public void SetComputerId(String computerId) {
    this.computerId = computerId;
  }

  public void run() {
    sender.StartConnection();
    while(true){
      sender.SendComputerId(computerId);

      if (!isConnecting){
        break;
      } else {
        try {
          Thread.sleep((ThreadLocalRandom.current().nextInt(1, 6)) * 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

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
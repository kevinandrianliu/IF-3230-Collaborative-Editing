package com.tugasbesar.networking;

import com.tugasbesar.classes.CharacterData;

public class SenderThread extends Thread {
  private String ip;
  private int port;
  private Sender sender;
  private CharacterData character;

  public SenderThread(String ip, int port){
    this.ip = ip;
    this.port = port;

    sender = new Sender(ip,port);
  }

  public void CreateCharacter(String computerId, char value, int position, String positionId, String order){
    character = new CharacterData(computerId, value, position, positionId, order);
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
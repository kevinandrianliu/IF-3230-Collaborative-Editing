package com.tugasbesar.networking;

import com.tugasbesar.classes.CRDT;
import com.tugasbesar.classes.CharacterData;
import com.tugasbesar.classes.VersionVector;
import java.util.LinkedList;

public class ReceiverThread extends Thread {
  private String ip;
  private int port;
  private Receiver receiver;
  private CRDT crdt;
  private LinkedList<VersionVector> versionVectorList;
  private LinkedList<CharacterData> deletionBuffer;
  private LinkedList<CharacterData> insertionBuffer;

  public boolean isConnecting;

  public ReceiverThread(String ip, int port){
    this.ip = ip;
    this.port = port;
    this.crdt = new CRDT();
    this.isConnecting = true;
    this.versionVectorList = new LinkedList<VersionVector>();

    receiver = new Receiver(ip,port);
  }

  public void run() {
    receiver.StartConnection();
    while(true){
      String computerId = receiver.receiveComputerId();;
      boolean isAlreadyAvailable = false;
      for (VersionVector vVector : versionVectorList){
        if (vVector.getId().equals(computerId)){
          isAlreadyAvailable = true;
          break;
        }
      }

      if (!(isAlreadyAvailable)){
        versionVectorList.add(new VersionVector(computerId, 0));
        System.out.println(computerId);
      }

      if (!isConnecting) {
        break;
      }
    }

    while (true){
      CharacterData character = receiver.receiveMessage();
      System.out.println(character.getOrder());
      if (character.getOrder().equals("INSERT")){
        crdt.insert(character);
      } else if (character.getOrder().equals("DELETE")){
        System.out.println("POS ID: " + character.getPositionId());
        crdt.remove(character.getPositionId());
      }
    }

    //receiver.CloseConnection();
  }

  /**
   * @return the ip
   */
  public String getIp() {
    return ip;
  }

  /**
   * @param ip the ip to set
   */
  public void setIp(String ip) {
    this.ip = ip;
  }

  /**
   * @return the port
   */
  public int getPort() {
    return port;
  }

  /**
   * @param port the port to set
   */
  public void setPort(int port) {
    this.port = port;
  }

  /**
   * @return the receiver
   */
  public Receiver getReceiver() {
    return receiver;
  }

  /**
   * @param receiver the receiver to set
   */
  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  /**
   * @return the crdt
   */
  public CRDT getCrdt() {
    return crdt;
  }

  /**
   * @param crdt the crdt to set
   */
  public void setCrdt(CRDT crdt) {
    this.crdt = crdt;
  }
}
package com.tugasbesar.networking;

import com.tugasbesar.classes.CRDT;
import com.tugasbesar.classes.CharacterData;
import com.tugasbesar.classes.VersionVector;
import java.util.LinkedList;

import javax.swing.JTextArea;

public class ReceiverThread extends Thread {
  private String ip;
  private int port;
  private Receiver receiver;
  private String computerId;
  private CRDT crdt;
  private LinkedList<VersionVector> versionVectorList;
  private LinkedList<CharacterData> deletionBuffer;
  private LinkedList<CharacterData> insertionBuffer;
  private JTextArea textArea;
  private JTextArea tArea;

  public boolean isConnecting;

  public ReceiverThread(String ip, int port, JTextArea textArea, JTextArea tArea, String computerId){
    this.ip = ip;
    this.port = port;
    this.crdt = new CRDT();
    this.isConnecting = true;
    this.versionVectorList = new LinkedList<VersionVector>();
    this.textArea = textArea;
    this.tArea = tArea;
    this.computerId = computerId;

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
        tArea.insert(computerId + "\n", tArea.getText().length());
        versionVectorList.add(new VersionVector(computerId, 0));
      }

      if (!isConnecting) {
        break;
      }
    }

    while (true){
      Object object = receiver.receiveMessage();
      if ((object) instanceof CharacterData){
        CharacterData character = (CharacterData) object;
        
          if (character.getOrder().equals("INSERT")){
            crdt.insert(character);

            if (!(character.getComputerId().equals(computerId))){
              textArea.insert("" + character.getValue(), character.getPosition());
            }
          } else if (character.getOrder().equals("DELETE")){
            crdt.remove(character.getPositionId());

            if (!(character.getComputerId().equals(computerId))){
              textArea.setText("");
              int i = 0;
              for (CharacterData characterData : crdt.getCharacterDataCRDT()){
                textArea.insert("" + characterData.getValue(), i);
                i++;
              }
            }
          }
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
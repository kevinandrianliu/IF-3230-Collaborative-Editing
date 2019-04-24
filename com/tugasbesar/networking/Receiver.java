package com.tugasbesar.networking;

import com.tugasbesar.classes.CharacterData;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Receiver {
  private final short BUFFER_SIZE = 1024 * 6;
  private MulticastSocket socket;
  private InetAddress ipAddressGroup;
  private int port;
  private String ip;

  public Receiver(String ip, int port) {
    this.port = port;
    this.ip = ip;
  }

  public void StartConnection() {
    if (socket != null){
      System.out.println("ERROR Socket is already bounded. Unbound first.");
    } else {
      try {
        socket = new MulticastSocket(port);
        ipAddressGroup = InetAddress.getByName(ip);
        socket.joinGroup(ipAddressGroup);
      } catch (UnknownHostException e) {
        System.out.println("ERROR IP address is unknown or invalid: " + e.getLocalizedMessage());
      } catch (IOException e) {
        System.out.println("ERROR Cannot create and/or bind socket: " + e.getLocalizedMessage());
      }
    }
  }

  public void CloseConnection() {
    socket.close();
    socket = null;
  }

  public String receiveComputerId(){
    // Receive object
    byte[] buffer = new byte[BUFFER_SIZE];
    try {
      socket.receive(new DatagramPacket(buffer, BUFFER_SIZE, ipAddressGroup, port));
    } catch (Exception e) {
      System.out.println("ERROR Cannot receive data: " + e.getLocalizedMessage());
    }
    
    // Deserialize
    ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
    String computerId = null;
    try {
      ObjectInputStream ois = new ObjectInputStream(bais);
    
      computerId = (String) ois.readObject();
    } catch (IOException e) {
      System.out.println("ERROR Cannot create ObjectInputStream or object is corrupted: " + e.getLocalizedMessage());
    } catch (ClassNotFoundException e){
      System.out.println("ERROR Declared class does not exist.");
    }

    return computerId;
  }

  public CharacterData receiveMessage() {
    // Receive object
    byte[] buffer = new byte[BUFFER_SIZE];
    try {
      socket.receive(new DatagramPacket(buffer, BUFFER_SIZE, ipAddressGroup, port));
    } catch (Exception e) {
      System.out.println("ERROR Cannot receive data: " + e.getLocalizedMessage());
    }
    
    // Deserialize
    ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
    CharacterData character = null;
    try {
      ObjectInputStream ois = new ObjectInputStream(bais);
      
      character = (CharacterData) ois.readObject();
    } catch (IOException e) {
      System.out.println("ERROR Cannot create ObjectInputStream or object is corrupted: " + e.getLocalizedMessage());
    } catch (ClassNotFoundException e){
      System.out.println("ERROR Declared class does not exist.");
    } catch (ClassCastException e){
      
    }

    return character;
  }
}
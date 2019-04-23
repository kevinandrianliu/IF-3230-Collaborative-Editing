package com.tugasbesar.networking;

import com.tugasbesar.classes.Character;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Sender {
  private MulticastSocket socket;
  private InetAddress ipAddressGroup;
  private String ip;
  private int port;

  public Sender(String ip, int port){
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

  public void SendMessage(Character character) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(character);
    } catch (IOException e) {
      System.out.println("ERROR Handling error in ObjectOutputStream: " + e.getLocalizedMessage());
    }
    byte[] data = baos.toByteArray();

    System.out.println("[Sending object...]");
    try {
      socket.send(new DatagramPacket(data, data.length, ipAddressGroup, port));
    } catch (IOException e){
      System.out.println("ERROR Cannot send data through socket: " + e.getLocalizedMessage());
    }
  }
}
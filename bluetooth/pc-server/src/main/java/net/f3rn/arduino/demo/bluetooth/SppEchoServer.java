package net.f3rn.arduino.demo.bluetooth;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;

/**
 * Class that implements an SPP Echo Server which accepts a line of
 * message from an SPP client then sends it back to the client.
 *
 * You can exit with `q`
 */
public class SppEchoServer {

  private void startServer() throws IOException{

    UUID uuid = new UUID("1101", true);

    //Create the servicve url
    String connectionString = "btspp://localhost:" + uuid +";name=SPP Echo Server";

    //open server url
    StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier)Connector.open( connectionString );

    //server loop
    while(true) {
      //Wait for client connection
      System.out.println("\nServer Started. Waiting for clients to connect…");
      StreamConnection connection = streamConnNotifier.acceptAndOpen();

      //Init connection
      RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
      System.out.println("Remote device address: " + dev.getBluetoothAddress());
      System.out.println("Remote device name: " + dev.getFriendlyName(true));

      InputStream inStream = connection.openInputStream();
      BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

      OutputStream outStream = connection.openOutputStream();
      PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));


      //send welcome message
      pWriter.write("Bluetooth Serial Echo Server\r\n");
      pWriter.write("For disconnect please send 'q'\r\n");
      pWriter.flush();

      //echo loop
      while (true) {
        String lineRead = bReader.readLine();
        if ("q".equals(lineRead)) {
          pWriter.write("bye!\r\n");
          break;
        }
        System.out.println(lineRead);
        pWriter.write("echo:" + lineRead + "\r\n");
        pWriter.flush();
      }

      bReader.close();
      pWriter.close();
    }
    //streamConnNotifier.close();

  }

  public static void main(String[] args) throws IOException {

    //display local device address and name
    LocalDevice localDevice = LocalDevice.getLocalDevice();
    System.out.println("Address: "+localDevice.getBluetoothAddress());
    System.out.println("Name: "+localDevice.getFriendlyName());

    SppEchoServer sampleSPPServer=new SppEchoServer();
    sampleSPPServer.startServer();

  }
}

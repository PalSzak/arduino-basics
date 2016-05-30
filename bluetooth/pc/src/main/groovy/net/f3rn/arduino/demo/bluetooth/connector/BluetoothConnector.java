package net.f3rn.arduino.demo.bluetooth.connector;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;


//TODO: close stuffs... :P
public class BluetoothConnector {


  private static final List<RemoteDevice> devicesDiscovered = Collections.synchronizedList(new ArrayList<>());
  private static List<ServiceRecord> servicesDiscovered = Collections.synchronizedList(new ArrayList<>());

  private static final Object waitForDevice = new Object();

  private final DeviceCollector collector = new DeviceCollector();
  private final DiscoveryAgent discoveryAgent = LocalDevice.getLocalDevice().getDiscoveryAgent();

  private static final UUID[] SERIAL_PROTOCOL = new UUID[]{new UUID(0x1101)};

  private static final int[] attrIDs =  new int[] {
      0x0100 // Service name
  };
  private PrintWriter output;

  public BluetoothConnector() throws BluetoothStateException {
  }


  public List<RemoteDevice> getDeviceList() throws InterruptedException, BluetoothStateException {
    if (devicesDiscovered.size() > 0)
        return devicesDiscovered;

    synchronized(waitForDevice) {
      boolean started = discoveryAgent.startInquiry(DiscoveryAgent.GIAC, collector);
      if (started) {
        waitForDevice.wait();
      }
    }
    return devicesDiscovered;
  }

  public List<ServiceRecord> getSPPServicesList(RemoteDevice device) throws InterruptedException, BluetoothStateException {
    if (servicesDiscovered.size() > 0)
      return servicesDiscovered;

    synchronized(waitForDevice) {
      discoveryAgent.searchServices(null, SERIAL_PROTOCOL, device, collector);
      waitForDevice.wait();
    }
    return servicesDiscovered;
  }

  public void connectToService(ServiceRecord service, Consumer<String> callback) throws IOException {
    String connectionURL = service.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
    StreamConnection connection = (StreamConnection) Connector.open(connectionURL);

    output = new PrintWriter(new OutputStreamWriter(connection.openOutputStream()));

    BufferedReader bReader2=new BufferedReader(new InputStreamReader(connection.openInputStream()));
    Executors.newSingleThreadExecutor().execute(() -> {
      try {
        while (true) {
          callback.accept(bReader2.readLine());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  public void sendMessage(String message) throws Exception {
    if(output == null)
      throw new Exception("Connect first");
    output.write(String.format("%s\r\n", message.trim()));
    output.flush();
  }


  private static final class DeviceCollector implements DiscoveryListener {

    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
      System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
      devicesDiscovered.add(btDevice);
      try {
        System.out.println("     name " + btDevice.getFriendlyName(false));
      } catch (IOException cantGetDeviceName) {
      }
    }

    public void inquiryCompleted(int discType) {
      System.out.println("Device Inquiry completed!");
      synchronized(waitForDevice){
        waitForDevice.notifyAll();
      }
    }

    @Override
    public void serviceSearchCompleted(int arg0, int arg1) {
      System.out.println("Service Search completed!");
      synchronized (waitForDevice) {
        waitForDevice.notify();
      }
    }

    @Override
    public void servicesDiscovered(int arg0, ServiceRecord[] services) {
      servicesDiscovered.addAll(Arrays.asList(services));
    }

  }
}

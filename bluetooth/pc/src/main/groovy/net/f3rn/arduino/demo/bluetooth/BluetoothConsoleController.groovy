package net.f3rn.arduino.demo.bluetooth;

import javafx.fxml.FXML
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent
import net.f3rn.arduino.demo.bluetooth.connector.BluetoothConnector

import java.util.function.Consumer;

//TODO: refactor connector to apply callbacks & use thread executors -> don't block ui
public class BluetoothConsoleController implements Initializable {

  @FXML TextField input;
  @FXML TextArea output;

  BluetoothConnector connector;

  @Override
  void initialize(URL location, ResourceBundle resources) {
    connector = new BluetoothConnector();
  }

  @FXML
  protected void type(KeyEvent e) {
    if (e.getCode() == KeyCode.ENTER)
      switch (input.getText().trim()) {
        case "/connect"  :
          connect();
          input.clear();
          break;
        case ~/^\/connect \d$/:
          System.out.println("AAAAA " + Integer.parseInt("${input.getText().trim().charAt(input.getText().trim().size()-1)}"));
          searchServices Integer.parseInt("${input.getText().trim().charAt(input.getText().trim().size()-1)}")
          input.clear();
          break;
        default:
          connector.sendMessage input.getText()
          printSent((collectInput()))
      }
  }

  def searchServices(int choice) {
    print("${connector.getSPPServicesList(connector.deviceList[choice]).size()} services(s) found\n", );
    connector.connectToService(connector.getSPPServicesList(connector.deviceList[choice])[0],
        new Consumer<String>() {
          @Override
          void accept(String s) {
            printRecived(s);
          }
        }
    );
    print("Please select one!\n");
  }

  private connect(){
    print("${connector.deviceList.size()} device(s) found\n", );
    connector.deviceList.eachWithIndex {device, idx->
      print("[$idx] ${device.getFriendlyName false}\n", );
    }
    print("Please select one!\n");
  }


  private collectInput() {
    def typed = input.getText()
    input.clear()
    typed;
  }

  private printRecived(String recived){
    print(String.format("<<< %s\n", recived))
  }

  private printSent(String sent){
    print(String.format(">>> %s\n", sent))
  }

  private print(String text) {
    output.appendText(text)
  }
}

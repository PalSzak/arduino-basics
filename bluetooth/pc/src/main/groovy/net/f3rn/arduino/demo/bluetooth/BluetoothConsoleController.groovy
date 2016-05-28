package net.f3rn.arduino.demo.bluetooth;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BluetoothConsoleController {

  @FXML TextField input;
  @FXML TextArea output;

  @FXML
  protected void type(KeyEvent e) {
    if (e.getCode() == KeyCode.ENTER)
      printSent((collectInput()))
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

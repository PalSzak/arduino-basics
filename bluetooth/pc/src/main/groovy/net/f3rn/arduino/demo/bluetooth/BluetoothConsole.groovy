package net.f3rn.arduino.demo.bluetooth

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

public class BluetoothConsole extends Application {

  @Override
  void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("bluetooth-console.fxml"));

    Scene scene = new Scene(root);
    primaryStage.setScene(scene);

    primaryStage.setTitle("BluetootConsole");

    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(BluetoothConsole.class, args);
  }
}

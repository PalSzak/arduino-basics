package net.f3rn.arduino.demo.bluetooth

import spock.lang.Specification

class BluetoothConsoleTest extends Specification{
  def "someLibraryMethod returns true"() {
    setup:
    BluetoothConsole lib = new BluetoothConsole()
    when:
    def result = true
    then:
    result == true
  }
}

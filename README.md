# Sensor Example

This application is an example of how to enumerate and poll data from the raw sensors on WearOS.
This example requires at least WearOS 2.0 and a Fossil Gen 5 smartwatch. Emulators may not be
fully supported. When run, the debug console will be populated with the readings.

## Debugging on a Physical Watch

### Enable developer settings and connect to ADB

To enable developer settings, go to Settings->System->About and tap the Build Number 5 times.
A new menu will be available in the main settings page.

Enable WiFi and connect the watch to the same access point as the debugging computer. Go to
Developer Settings->Enable ADB then tap Debug Over WiFi. After a few moments, the IP address of
the watch will be displayed under Debug Over WiFi.

On the debugging computer, open a command console and type the below command to connect the watch
to the debugger:

```bash
adb connect <ip_of_the_watch>:<port_of_the_watch>
```

The watch will ask for confirmation. Click OK. You may see a message on the command prompt that
authorization failed. As long as the watch asked for confirmation you can ignore this.

### Debug on the Watch

In android studio in the top left next to the run button, you should see a drop down. Click this
and select the Fossil watch.

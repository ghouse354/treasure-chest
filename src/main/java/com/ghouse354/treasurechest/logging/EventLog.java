package com.ghouse354.treasurechest.logging;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotBase;

public class EventLog {
    private static EventLog sInstance = new EventLog();
    public static EventLog getInstance() {
        return sInstance;
    }

    private EventLog() {}

    private static Path sLogFile;
    private static String sLogFolder = "/home/lvuser/logs";

    private static final Queue<String> sEvents = new LinkedList<>();

    private static final Notifier sLogPeriodic = new Notifier(new LogWriter());

    public void startLogging() {
        if (RobotBase.isSimulation()) {
            String sysTempDir = System.getProperty("java.io.tmpdir");
            sLogFolder = Paths.get(sysTempDir, "ghouse354-logs").toString();
            System.out.println("Running in simulation mode. Using local temp directory: " + sLogFolder);
        }
        else {
            // Detect USB drive. If we find one, we can record to it. Otherwise,
            // Save logs on the roboRIO
            File usbLocation = new File("/media/sda1/");
            if (usbLocation.exists()) {
                sLogFolder = "/media/sda1/logs/";
            }
        }
        createLogFile();

        // Set up the notifier to run every second (so we don't trash IO)
        sLogPeriodic.startPeriodic(1.0);
    }

    public void addEvent(String category, String event) {
        String log = new StringBuilder()
                        .append(Instant.now().toString()).append("\t")
                        .append(DriverStation.getInstance().getMatchTime()).append("\t")
                        .append("[").append(category).append("]").append("\t")
                        .append(event).append("\n")
                        .toString();
        sEvents.add(log);
        System.out.print(log);
    }

    private static void createLogFolder() throws IOException {
        File logDir = new File(sLogFolder);
        if (!logDir.exists()) {
            Files.createDirectory(Paths.get(sLogFolder));
        }
    }

    private static void createLogFile() {
        try {
            createLogFolder();
            if (DriverStation.getInstance().isFMSAttached()) {
                sLogFile = Paths.get(sLogFolder,
                                    DriverStation.getInstance().getEventName() + "_" +
                                    DriverStation.getInstance().getMatchType() +
                                    DriverStation.getInstance().getMatchNumber() + "Events.txt");
            }
            else {
                sLogFile = Paths.get(sLogFolder, "testEvents.txt");
            }

            System.out.println("Creating Event Log at " + sLogFile.toString());

            if (Files.exists(sLogFile)) {
                Files.delete(sLogFile);
            }
            Files.createFile(sLogFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class LogWriter implements Runnable {
        @Override
        public void run() {
            while (!sEvents.isEmpty()) {
                try {
                    String event = sEvents.remove();
                    Files.write(sLogFile, event.getBytes(), StandardOpenOption.APPEND);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

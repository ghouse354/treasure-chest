package com.ghouse354.logging;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import edu.wpi.first.wpilibj.DriverStation;

public class CSVLogger {
    private static CSVLogger sInstance = new CSVLogger();
    public static CSVLogger getInstance() {
        return sInstance;
    }

    private final List<LogSource> mSources = new ArrayList<>();
    private Path mLogFile;
    private String mLogFolder = "/home/lvuser/logs/";

    private CSVLogger() {
        File usbLocation = new File("/media/sda1/");
        if (usbLocation.exists()) {
            mLogFolder = "/media/sda1/logs/";
        }
    }

    private void createLogFolder() throws IOException {
        File logDir = new File(mLogFolder);
        if (!logDir.exists()) {
            Files.createDirectory(Paths.get(mLogFolder));
        }
    }

    private void createLogFile() {
        try {
            createLogFolder();
            if (DriverStation.getInstance().isFMSAttached()) {
                mLogFile = Paths.get(mLogFolder +
                                    DriverStation.getInstance().getEventName() + "_" +
                                    DriverStation.getInstance().getMatchType() +
                                    DriverStation.getInstance().getMatchNumber() + ".csv");
            }
            else {
                mLogFile = Paths.get(mLogFolder + "test.csv");
            }

            if (Files.exists(mLogFile)) {
                Files.delete(mLogFile);
            }
            Files.createFile(mLogFile);
            writeHeader();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSource(String name, Supplier<Object> supplier) {
        mSources.add(new LogSource(name, supplier.get()::toString));
    }

    public void addStringSource(String name, Supplier<String> supplier) {
        mSources.add(new LogSource(name, supplier.get()::toString));
    }

    public void addDoubleSource(String name, Supplier<Double> supplier) {
        mSources.add(new LogSource(name, supplier.get()::toString));
    }

    public void addIntegerSource(String name, Supplier<Integer> supplier) {
        mSources.add(new LogSource(name, supplier.get()::toString));
    }

    public void addLongSource(String name, Supplier<Long> supplier) {
        mSources.add(new LogSource(name, supplier.get()::toString));
    }

    public void addBooleanSource(String name, Supplier<Boolean> supplier) {
        mSources.add(new LogSource(name, supplier.get()::toString));
    }

    public void addCharacterSource(String name, Supplier<Character> supplier) {
        mSources.add(new LogSource(name, supplier.get()::toString));
    }

    public void writeLogs() {
        try {
            if (mLogFile == null) {
                createLogFile();
            }

            StringBuilder data = new StringBuilder();
            data.append(Instant.now().toString()).append(",");
            data.append(DriverStation.getInstance().getMatchTime()).append(",");
            data.append(getValues());
            Files.write(mLogFile, Collections.singletonList(data.toString()), StandardOpenOption.APPEND);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeHeader() throws IOException {
        StringBuilder header = new StringBuilder();
        header.append("Timestamp,");
        header.append("match_time,");
        header.append(mSources.stream().map(t -> t.mName).collect(Collectors.joining(","))).append(",");
        Files.write(mLogFile, Collections.singletonList(header.toString()), StandardOpenOption.APPEND);
    }

    private String getValues() {
        return mSources.stream().map(s -> s.mSupplier.get()).collect(Collectors.joining(","));
    }

    private class LogSource {
        private final String mName;
        private final Supplier<String> mSupplier;

        public LogSource(String name, Supplier<String> supplier) {
            mName = name;
            mSupplier = supplier;
        }
    }
}
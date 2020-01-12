package com.ghouse354.treasurechest.input;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class XboxController extends Joystick {
    // Physical Buttons
    public Button buttonA, buttonB, buttonX, buttonY, buttonSelect, buttonStart;

    // Stick
    public Button leftStickPress, rightStickPress;

    // Triggers as buttons
    public Button leftTrigger, rightTrigger;

    // Bumpers
    public Button leftBumper, rightBumper;

    // POV as buttons
    public Button povN, povNE, povE, povSE, povS, povSW, povW, povNW;

    private double triggerThreshold;

    public XboxController(int port) {
        this(port, 0.75);
    }

    public XboxController(int port, double triggerPressThreshold) {
        super(port);

        this.triggerThreshold = triggerPressThreshold;

        // Set up the buttons
        buttonA = new JoystickButton(this, 1);
        buttonB = new JoystickButton(this, 2);
        buttonX = new JoystickButton(this, 3);
        buttonY = new JoystickButton(this, 4);

        leftBumper = new JoystickButton(this, 5);
        rightBumper = new JoystickButton(this, 6);

        buttonSelect = new JoystickButton(this, 7);
        buttonStart = new JoystickButton(this, 8);

        leftStickPress = new JoystickButton(this, 9);
        rightStickPress = new JoystickButton(this, 10);

        leftTrigger = new Button() {
            @Override
            public boolean get() {
                return getLeftTrigger() > triggerThreshold;
            }
        };

        rightTrigger = new Button() {
            @Override
            public boolean get() {
                return getRightTrigger() > triggerThreshold;
            }
        };

        povN = new Button() {
            @Override
            public boolean get() {
                return getPOV() == 0;
            }
        };

        povNE = new Button() {
            @Override
            public boolean get() {
                return getPOV() == 45;
            }
        };

        povE = new Button() {
            @Override
            public boolean get() {
                return getPOV() == 90;
            }
        };

        povSE = new Button() {
            @Override
            public boolean get() {
                return getPOV() == 135;
            }
        };

        povS = new Button() {
            @Override
            public boolean get() {
                return getPOV() == 180;
            }
        };

        povSW = new Button() {
            @Override
            public boolean get() {
                return getPOV() == 225;
            }
        };

        povW = new Button() {
            @Override
            public boolean get() {
                return getPOV() == 270;
            }
        };

        povNW = new Button() {
            @Override
            public boolean get() {
                return getPOV() == 315;
            }
        };
    }

    public double getLeftTrigger() {
        return this.getRawAxis(2);
    }

    public double getRightTrigger() {
        return this.getRawAxis(3);
    }

    public double getLeftStickX() {
        return this.getRawAxis(0);
    }

    public double getLeftStickY() {
        return this.getRawAxis(1);
    }

    public double getRightStickX() {
        return this.getRawAxis(4);
    }

    public double getRightStickY() {
        return this.getRawAxis(5);
    }

    public double getDpadX() {
        if (this.getPOV() > 0 && this.getPOV() < 180) {
            return 1;
        }
        else if (this.getPOV() > 180 && this.getPOV() < 360) {
            return -1;
        }
        return 0;
    }

    public double getDpadY() {
        if ((this.getPOV() > 0 && this.getPOV() < 90) ||
            (this.getPOV() > 270 && this.getPOV() < 360)) {
            return 1;
        }
        else if (this.getPOV() > 90 && this.getPOV() < 270) {
            return -1;
        }
        return 0;
    }

    public boolean getButtonA() {
        return buttonA.get();
    }

    public boolean getButtonB() {
        return buttonB.get();
    }

    public boolean getButtonX() {
        return buttonX.get();
    }

    public boolean getButtonY() {
        return buttonY.get();
    }

    public boolean getButtonSelect() {
        return buttonSelect.get();
    }

    public boolean getButtonStart() {
        return buttonStart.get();
    }

    public boolean getButtonLeftStick() {
        return leftStickPress.get();
    }

    public boolean getButtonRightStick() {
        return rightStickPress.get();
    }

    public boolean getLeftBumper() {
        return leftBumper.get();
    }

    public boolean getRightBumper() {
        return rightBumper.get();
    }

    public boolean getLeftTriggerPress() {
        return leftTrigger.get();
    }

    public boolean getRightTriggerPress() {
        return rightTrigger.get();
    }

    public boolean getPovN() {
        return povN.get();
    }

    public boolean getPovNE() {
        return povNE.get();
    }

    public boolean getPovE() {
        return povE.get();
    }

    public boolean getPovSE() {
        return povSE.get();
    }

    public boolean getPovS() {
        return povS.get();
    }

    public boolean getPovSW() {
        return povSW.get();
    }

    public boolean getPovW() {
        return povW.get();
    }

    public boolean getPovNW() {
        return povNW.get();
    }
}

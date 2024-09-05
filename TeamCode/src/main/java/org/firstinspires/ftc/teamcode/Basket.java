package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Basket {

    private final Servo rotate;
    private final Servo leftPivot;
    private final Servo rightPivot;
    private final Servo pixelA;
    private final Servo pixelB;

    public Basket(HardwareMap hardwareMap) {
        rotate = hardwareMap.get(Servo.class, "rotate");
        leftPivot = hardwareMap.get(Servo.class, "leftPivot");
        rightPivot = hardwareMap.get(Servo.class, "rightPivot");
        pixelA = hardwareMap.get(Servo.class, "pixelA");
        pixelB = hardwareMap.get(Servo.class, "pixelB");
    }

    public Action readyIntake() {
        return new Action() {
            private boolean initialized = false;
            private final ElapsedTime timer = new ElapsedTime();
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    timer.reset();
                    initialized = true;
                }
                leftPivot.setPosition(0);
                rightPivot.setPosition(1);
                if (timer.seconds() > 0.3) {
                    pixelA.setPosition(1);
                    pixelB.setPosition(1);
                    rotate.setPosition(0);
                }
                return (timer.seconds() < 0.8);
            }
        };
    }

    public Action readyScore() {
        return new Action() {
            private boolean initialized = false;
            private final ElapsedTime timer = new ElapsedTime();
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    timer.reset();
                    initialized = true;
                }
                leftPivot.setPosition(1);
                rightPivot.setPosition(0);
                pixelA.setPosition(0);
                pixelB.setPosition(0);
                rotate.setPosition(0);
                return (timer.seconds() < 0.5);
            }
        };
    }

    public Action releaseA() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                pixelA.setPosition(1);
                return false;
            }
        };
    }

    public Action releaseB() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                pixelB.setPosition(1);
                return false;
            }
        };
    }

}

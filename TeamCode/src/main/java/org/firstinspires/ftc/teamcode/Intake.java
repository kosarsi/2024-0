package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake {

    private final DcMotorEx intake;
    private final Servo leftPivot;
    private final Servo rightPivot;

    public Intake(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        leftPivot = hardwareMap.get(Servo.class, "leftPivot");
        rightPivot = hardwareMap.get(Servo.class, "rightPivot");
    }

    public Action intakeOn() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                intake.setPower(1);
                leftPivot.setPosition(0);
                rightPivot.setPosition(1);
                return false;
            }
        };
    }

    public Action intakeOff() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                intake.setPower(0);
                leftPivot.setPosition(1);
                rightPivot.setPosition(0);
                return false;
            }
        };
    }

    public Action intakeReverse() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                intake.setPower(-1);
                leftPivot.setPosition(1);
                rightPivot.setPosition(0);
                return false;
            }
        };
    }

}

package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Lift {

    private final DcMotorEx left;
    private final DcMotorEx right;
    private final DigitalChannel liftSwitch;

    private final double kP = 0;
    private final double kD = 0;
    private final double kF = 0;

    public Lift(HardwareMap hardwareMap) {
        left = hardwareMap.get(DcMotorEx.class, "leftLift");
        right = hardwareMap.get(DcMotorEx.class, "rightLift");
        liftSwitch = hardwareMap.get(DigitalChannel.class, "liftSwitch");

        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public Action liftTo(double target) {
        return new Action() {
            double lastError = 0;
            final ElapsedTime timer = new ElapsedTime();
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    timer.reset();
                    initialized = true;
                }
                double currentPosition = ((double) (left.getCurrentPosition() + right.getCurrentPosition())) / 2;
                double error = target - currentPosition;
                telemetryPacket.put("Position", currentPosition);

                double derivative = ((error - lastError) / timer.seconds()) * kD;
                double proportional = error * kP;
                lastError = error;
                timer.reset();

                double power = proportional + derivative + kF;

                left.setPower(power);
                right.setPower(power);

                if (error < 50) {
                    left.setPower(kF);
                    right.setPower(kF);
                    return false;
                } else {
                    return true;
                }
            }
        };
    }

    public Action liftDown() {
        return new Action() {
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    left.setPower(-0.5);
                    right.setPower(-0.5);
                    initialized = true;
                }

                if (!liftSwitch.getState()) {
                    left.setPower(0);
                    right.setPower(0);
                    left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    return false;
                } else {
                    return true;
                }
            }
        };
    }

}

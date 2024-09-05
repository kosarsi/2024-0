package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp(name="Lift Tuner")
public class LiftTuner extends OpMode {

    public static double kP = 0, kD = 0, kF = 0;

    public static int target = 0;

    private DcMotorEx left;
    private DcMotorEx right;

    private double lastError = 0;
    private final ElapsedTime timer = new ElapsedTime();

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        left = hardwareMap.get(DcMotorEx.class, "leftLift");
        right = hardwareMap.get(DcMotorEx.class, "rightLift");
    }

    @Override
    public void start() {
        super.start();
        timer.reset();
    }

    @Override
    public void loop() {
        double position = ((double) (left.getCurrentPosition() + right.getCurrentPosition())) / 2;
        double error = target - position;

        double derivative = ((error - lastError) / timer.seconds()) * kD;
        double proportional = error * kP;
        lastError = error;
        timer.reset();

        double power = proportional + derivative + kF;

        left.setPower(power);
        right.setPower(power);

        telemetry.addData("pos ", position);
        telemetry.addData("target", target);
        telemetry.update();
    }
}

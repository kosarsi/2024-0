package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Teleop Mac")
public class MacTesting extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            if (gamepad1.cross) {
                telemetry.addData("Button", "Pressed");
            } else {
                telemetry.addData("Button", "Not Pressed");
            }
            telemetry.update();
        }
    }
}

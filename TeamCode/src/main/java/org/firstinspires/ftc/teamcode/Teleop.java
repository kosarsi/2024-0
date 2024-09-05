package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="Teleop")
public class Teleop extends OpMode {

    Basket basket;
    Intake intake;
    Lift lift;
    Drivetrain drivetrain;

    List<Action> runningActions = new ArrayList<>();
    FtcDashboard dash = FtcDashboard.getInstance();

    public enum RobotState {
        READY_INTAKE,
        READY_SCORE,
    }

    RobotState robotState;

    boolean aScored = false;
    boolean bScored = false;

    Gamepad currentGamepad1 = new Gamepad();
    Gamepad currentGamepad2 = new Gamepad();

    Gamepad previousGamepad1 = new Gamepad();
    Gamepad previousGamepad2 = new Gamepad();

    double liftTarget = 0;

    ElapsedTime transitionTimer;

    ElapsedTime deltaTime = new ElapsedTime();

    @Override
    public void init() {
        basket = new Basket(hardwareMap);
        intake = new Intake(hardwareMap);
        lift = new Lift(hardwareMap);
        drivetrain = new Drivetrain(hardwareMap);
        robotState = RobotState.READY_INTAKE;
        transitionTimer = new ElapsedTime();
    }

    @Override
    public void start() {
        super.start();
        deltaTime.reset();
        transitionTimer.reset();
    }

    @Override
    public void loop() {

        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);

        TelemetryPacket packet = new TelemetryPacket();

        if (gamepad1.triangle) {
            drivetrain.resetHeading();
        }

        if (gamepad1.right_trigger > 0.5) {
            drivetrain.driveCentric((gamepad1.right_trigger - 0.5) * 2, 0, 0);
        } else if (gamepad1.left_trigger > 0.5) {
            drivetrain.driveCentric(-(gamepad1.left_trigger - 0.5) * 2, 0, 0);
        } else {
            drivetrain.drive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        }

        switch (robotState) {
            case READY_INTAKE:
                if (transitionTimer.seconds() > 0.6) {
                    if (gamepad1.right_bumper && gamepad1.left_bumper) {
                        runningActions.add(intake.intakeReverse());
                    } else if (gamepad1.right_bumper) {
                        runningActions.add(intake.intakeOn());
                    } else if (gamepad1.left_bumper) {
                        runningActions.add(intake.intakeOff());
                    }
                    if (gamepad1.cross) {
                        runningActions.add(intake.intakeOff());
                        runningActions.add(basket.readyScore());
                        transitionTimer.reset();
                        robotState = RobotState.READY_SCORE;
                    }
                }

            case READY_SCORE:

                if (transitionTimer.seconds() > 0.6) {
                    if (gamepad1.cross && !previousGamepad1.cross) {
                        if (!bScored) {
                            runningActions.add(basket.releaseB());
                            bScored = true;
                        } else if (!aScored) {
                            runningActions.add(basket.releaseA());
                            aScored = true;
                        } else {
                            runningActions.add(basket.readyIntake());
                            runningActions.add(lift.liftDown());
                            liftTarget = 0;
                            transitionTimer.reset();
                            robotState = RobotState.READY_INTAKE;
                        }
                    }

                    if (gamepad1.right_trigger > 0.5) {
                        liftTarget += deltaTime.seconds() * 50;
                    }

                    if (gamepad1.left_trigger > 0.5) {
                        liftTarget += deltaTime.seconds() * 50;
                    }

                    runningActions.add(lift.liftTo(liftTarget));
                }

            default:

        }

        List<Action> newActions = new ArrayList<>();
        for (Action action : runningActions) {
            action.preview(packet.fieldOverlay());
            if (action.run(packet)) {
                newActions.add(action);
            }
        }
        runningActions = newActions;

        dash.sendTelemetryPacket(packet);

        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);

        deltaTime.reset();

    }
}

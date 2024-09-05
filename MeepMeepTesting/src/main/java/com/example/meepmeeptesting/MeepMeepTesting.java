package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(12, 60, Math.toRadians(270)))
                .strafeTo(new Vector2d(12, 35))
                .waitSeconds(1)
                .strafeTo(new Vector2d(30, 35))
                .splineToSplineHeading(new Pose2d(48, 35, Math.toRadians(180)), Math.toRadians(0))
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(36, 24))
                        .splineToConstantHeading(new Vector2d(-56, 11), Math.toRadians(190))
                        .waitSeconds(1)
                        .strafeToConstantHeading(new Vector2d(36, 11))
                        .splineToConstantHeading(new Vector2d(48, 35), Math.toRadians(45))

                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
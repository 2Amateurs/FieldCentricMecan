// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

public final class Constants {
    public static final double maxVoltage = 12.0;
    public static class OperatorConstants {
        public static final int DRIVER_CONTROLLER_PORT = 0;
    }
    public static class Drive {
        public static final double forwardStrafeRatio = 1.4; //meaning that the robot will cover 1.4 times the distance for 1 second driving forward versus 1 second strafing
        public static final double forwardAccelerationLimit = 0.1;
        public static final double strafeAccelerationLimit = 0.1;
        public static final double rotationalAccelerationLimit = 0.1;
    }
    public static final class CAN_IDs {
        public static final int leftFront = 1;
        public static final int rightFront = 2;
        public static final int leftBack = 3;
        public static final int rightBack = 4;
    }
}

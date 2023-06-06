package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.MecanumBase;

import static frc.robot.Constants.CAN_IDs;

public class DriveSubsystem extends SubsystemBase {
    CANSparkMax leftFront;
    CANSparkMax rightFront;
    CANSparkMax leftBack;
    CANSparkMax rightBack;
    MecanumBase<CANSparkMax> base;

    public DriveSubsystem() {
        leftFront = new CANSparkMax(CAN_IDs.leftFront, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightFront = new CANSparkMax(CAN_IDs.leftFront, CANSparkMaxLowLevel.MotorType.kBrushless);
        leftBack = new CANSparkMax(CAN_IDs.leftFront, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightBack = new CANSparkMax(CAN_IDs.leftFront, CANSparkMaxLowLevel.MotorType.kBrushless);
        base = new MecanumBase<>(leftFront, rightFront, leftBack, rightBack);
        base.setForwardLimit(MecanumBase.Limits.NONE);
        base.setStrafeLimit(MecanumBase.Limits.NONE);
        base.setRotationLimit(MecanumBase.Limits.NONE);
    }

    public void drive(double drive, double strafe, double turn) {
        base.mecanDrive(drive, strafe, turn, true);
    }
}

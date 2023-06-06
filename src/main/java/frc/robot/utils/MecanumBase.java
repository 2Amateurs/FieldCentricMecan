package frc.robot.utils;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.Constants;

import static frc.robot.GlobalVariables.pose;

public class MecanumBase<T extends MotorController> {
    public final T leftFront;
    public final T rightFront;
    public final T leftBack;
    public final T rightBack;
    private DriveLimit forwardLimit;
    private DriveLimit strafeLimit;
    private DriveLimit rotationLimit;
    private final boolean isCANSparkMax;
    private DriveInput lastInput;
    public MecanumBase(T leftFront, T rightFront, T leftBack, T rightBack) {
        this.leftFront = leftFront;
        this.rightFront = rightFront;
        this.leftBack = leftBack;
        this.rightBack = rightBack;
        isCANSparkMax = leftFront instanceof CANSparkMax;
    }
    private void feed(DriveVoltageGroup voltageGroup) {
        leftFront.setVoltage(voltageGroup.voltages[0]);
        rightFront.setVoltage(voltageGroup.voltages[1]);
        leftBack.setVoltage(voltageGroup.voltages[2]);
        rightBack.setVoltage(voltageGroup.voltages[3]);
    }
    private void simpleMecanDrive(double drive, double strafe, double rot) { //This method will take raw input and drive the robot without doing any processing
        lastInput = new DriveInput(new Vector2d(new Vector2d.CartesianPoint(strafe, drive)), rot);
        double leftFrontVoltage = drive - strafe - rot;
        double rightFrontVoltage = drive + strafe + rot;
        double leftBackVoltage = drive + strafe - rot;
        double rightBackVoltage = drive - strafe + rot;
        DriveVoltageGroup driveVoltageGroup = new DriveVoltageGroup(new double[] {leftFrontVoltage, rightFrontVoltage, leftBackVoltage, rightBackVoltage});
        feed(driveVoltageGroup);
    }
    public void mecanDrive(double drive, double strafe, double rot, boolean isFieldCentric) { //This method will limit acceleration and speed, and calculate the math for field centric drive
        DriveInput processedInput;
        if (isFieldCentric) {
            processedInput = (new DriveInput(drive, strafe, rot)).toFieldCentric().scaleInput().applyLimits();
        } else {
            processedInput = (new DriveInput(drive, strafe, rot)).scaleInput().applyLimits();
        }
        simpleMecanDrive(processedInput.vector.getY(), processedInput.vector.getX(), processedInput.rot);
    }
    public void mecanDrive(double drive, double strafe, double rot) {
        mecanDrive(drive, strafe, rot, false);
    }
    public class DriveInput {
        private final Vector2d vector;
        private final double rot;
        public DriveInput(Vector2d vector, double rot) {
            this.vector = vector;
            this.rot = rot;
        }
        public DriveInput(double drive, double strafe, double rot) {
            this(new Vector2d(new Vector2d.CartesianPoint(strafe, drive)), rot);
        }
        private DriveInput toFieldCentric() { //converts input to field centric
            Vector2d outputVector = this.vector.rotate(-pose.getRotation().getRadians()); //To find the input vector relative to the robot's angle, subtract the robot's angle from that of the input vector.  The equivalent, which is done here, is to rotate the input vector by the negative of the robot's angle.
            return new DriveInput(outputVector, this.rot);
        }
        private DriveInput scaleInput() {
            Vector2d outputVector = this.vector.scale(Constants.Drive.forwardStrafeRatio, 1.0); //Since a robot with a mecanum wheel drivebase will always strafe more slowly than it drives forward, a scaling factor is applied to the x variable to ensure that the robot will drive at the desired angle.
            return new DriveInput(outputVector, this.rot);
        }
        private DriveInput applyLimits() {
            try {
                double drive = forwardLimit.getLimitedValue(this.vector.getY(), lastInput.vector.getY());
                double strafe = strafeLimit.getLimitedValue(this.vector.getX(), lastInput.vector.getX());
                double rot = rotationLimit.getLimitedValue(this.rot, lastInput.rot);
                return new DriveInput(new Vector2d(new Vector2d.CartesianPoint(strafe, drive)), rot);
            } catch (NullPointerException e) {
                e.printStackTrace();
                return this;
            }
        }
    }
    public static final class Limits {
        public static final DriveLimit NONE = (double currentValue, double lastValue) -> currentValue;
        public static final DriveLimit FULL = (double currentValue, double lastValue) -> 0;
    }
    public void setForwardLimit(DriveLimit forwardLimit) {
        this.forwardLimit = forwardLimit;
    }
    public void setStrafeLimit(DriveLimit strafeLimit) {
        this.strafeLimit = strafeLimit;
    }
    public void setRotationLimit(DriveLimit rotationLimit) {
        this.rotationLimit = rotationLimit;
    }
    public interface DriveLimit {
        double getLimitedValue(double currentValue, double lastValue);
    }
    private static class DriveVoltageGroup {
        double[] voltages;
        private DriveVoltageGroup(double[] voltages) {
            this.voltages = voltages;
            if (voltages.length != 4) {
                throw new ArrayStoreException("Expected an array of 4 doubles and got " + voltages.length);
            }
            double divisor = 1.0;
            for (double voltage : voltages) {
                if (Math.abs(voltage) > Constants.maxVoltage) {
                    divisor = Math.abs(voltage) / Constants.maxVoltage;
                }
            }
            if (divisor > 1.0) {
                for (int i = 1; i < voltages.length; i++) { //can't be done with the shorthand for method used above, as this actually makes a "ghost variable"
                    voltages[i] /= divisor;
                }
            }
        }
    }
}

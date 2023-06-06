package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

import java.util.function.DoubleSupplier;

public class DriveDefaultCommand extends CommandBase {
    DoubleSupplier drive;
    DoubleSupplier turn;
    DoubleSupplier strafe;
    private final DriveSubsystem subsystem;

    public DriveDefaultCommand(DoubleSupplier drive, DoubleSupplier turn, DoubleSupplier strafe) {
        this.drive = drive;
        this.strafe = strafe;
        this.turn = turn;
        this.subsystem = new DriveSubsystem();
        addRequirements(subsystem);
    }

    @Override
    public void execute() {
        subsystem.drive(drive.getAsDouble(), strafe.getAsDouble(), turn.getAsDouble());
    }
}

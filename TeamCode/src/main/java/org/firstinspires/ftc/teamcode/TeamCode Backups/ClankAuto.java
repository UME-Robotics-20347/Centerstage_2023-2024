package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="ClankAuto", group="Autonomous Bread")
public class ClankAuto extends LinearOpMode {
    Hardware robot = new Hardware();

    static final double countsPerMotorRev = 140;
    static final double wheelCircumferenceInches = 4 * Math.PI;
    static final double countsPerInch = countsPerMotorRev / wheelCircumferenceInches;
    static final double inchesPerDegree = (10 * Math.PI) / 360;
    static final double countsPerDegree = countsPerInch * inchesPerDegree;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        robot.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        resetMotors();

        waitForStart();

        encoderDrive(0.5, 72, 0);
    }

    public void encoderDrive(double speed, double inches, int msToWaitAfter) {
        resetMotors();

        robot.backLeft.setTargetPosition((int)Math.round(inches * countsPerInch));
        robot.backRight.setTargetPosition((int)Math.round(inches * countsPerInch));
        robot.frontLeft.setTargetPosition((int)Math.round(inches * countsPerInch));
        robot.frontRight.setTargetPosition((int)Math.round(inches * countsPerInch));

        robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Start motion.
        robot.backLeft.setPower(speed);
        robot.backRight.setPower(speed);
        robot.frontLeft.setPower(speed);
        robot.frontRight.setPower(speed);

        while (busy()) {
            telemetry.update();
        }

        resetMotors();
        sleep(msToWaitAfter);
    }

    public void encoderTurn(double speed, double turnDegrees, int msToWaitAfter) {
        resetMotors();
        robot.backLeft.setTargetPosition((int) (turnDegrees * countsPerDegree));
        robot.backRight.setTargetPosition((int) -(turnDegrees * countsPerDegree));
        robot.frontLeft.setTargetPosition((int) (turnDegrees * countsPerDegree));
        robot.frontRight.setTargetPosition((int) -(turnDegrees * countsPerDegree));

        //Turn On RUN_TO_POSITION
        robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //reset the timeout time and start motion.
        resetRuntime();
        robot.backLeft.setPower(speed);
        robot.backRight.setPower(-speed);
        robot.frontLeft.setPower(speed);
        robot.frontRight.setPower(-speed);

        while (busy()) {
            telemetry.addData("", "Running");
        }

        resetMotors();
    }

    public void resetMotors() {
        //Reset power
        robot.backLeft.setPower(0);
        robot.backRight.setPower(0);
        robot.frontLeft.setPower(0);
        robot.frontRight.setPower(0);

        //Reset target position
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition());
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition());
        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition());
        robot.frontRight.setTargetPosition(robot.frontLeft.getCurrentPosition());

        //Reset encoders
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private boolean busy() {
        return opModeIsActive() && (robot.backLeft.isBusy() && robot.backRight.isBusy() && robot.frontLeft.isBusy() && robot.frontRight.isBusy());
    }
}
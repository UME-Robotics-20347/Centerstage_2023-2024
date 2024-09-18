package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeout)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Toaster", group="Autonomous Bread")
public class Toaster extends LinearOpMode {
    // Declare OpMode members.
    Hardware_Test robot = new Hardware_Test();   //Hardware_Test is our
    private ElapsedTime runtime = new ElapsedTime();
    static final double countsPerMotorRev = 140;
    static final double wheelCircumferenceInches = 4 * Math.PI;
    static final double countsPerInch = countsPerMotorRev / wheelCircumferenceInches;
    static final double inchesPerDegree = (10 * Math.PI) / 360;
    static final double countsPerDegree = countsPerInch * inchesPerDegree;
    public char parkingSpot;

    @Override
    public void runOpMode() {

        //Initialize the drive system variables.
        //The init() method of the hardware class does all the work here

        if (true) {
            robot.init(hardwareMap);

            resetMotors();

            robot.wrist.setPower(.43);
            sleep(1000);
            robot.wrist.setPower(1);

            waitForStart();
        } //initialization
        if (true) {
            robot.fulcrum.setPower(.5);
            sleep(3000);
            robot.fulcrum.setPower(0);
            robot.fulcrum.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            encoderDrive(0.25, 24, 1000);
            if (!(parkingSpot == 'g')) {encoderDrive(0.25, 4.5, 1000);}

            if (parkingSpot == 'r')
            {
                encoderTurn(0.3, -73, 100);
                encoderDrive(0.25, 24, 0);
            }
                //The robot is already in the parking spot that 'g' would indicate.
            if (parkingSpot == 'b')
            {
                encoderTurn(0.3, 78, 100);
                encoderDrive(0.25, 22, 0);
            }
            robot.fulcrum.setPower(-.5);
            sleep(1500);
            robot.fulcrum.setPower(0);
            robot.wrist.setPower(.43);
        } //main code
        if (true) {
            telemetry.addData("Path", "Complete");
            while (opModeIsActive())
            {
                //telemetry.addData("countsPerDegree", countsPerDegree)
                telemetry.addData("Cone reading", parkingSpot);
                telemetry.addData("Current assumed color", parkingSpot());
                telemetry.addData("Alex", "is driving this super cool robot");
                telemetry.update();

            }
        } //telemetry
    }

    /*
     *  These methods perform relative movement, based on encoder counts.
     *  Move will stop if any of three conditions occur:
     *  1) Movement reaches the desired position
     *  2) Movement runs out of time
     *  3) Driver stops the opmode running.
    */
    public void encoderTurn (double speed, double turnDegrees, int msToWaitAfter) {
        if (opModeIsActive()) {
            if (true){
                resetMotors();
                robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() - (int) (turnDegrees * countsPerDegree));
                robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() + (int) (turnDegrees * countsPerDegree));
                robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() - (int) (turnDegrees * countsPerDegree));
                robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() + (int) (turnDegrees * countsPerDegree));

            }//find and set motors' target position
            if (true){
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
            }//begin driving to target position
            if (true){
                while ((opModeIsActive() && (robot.backLeft.isBusy() && robot.backRight.isBusy() && robot.frontLeft.isBusy() && robot.frontRight.isBusy())) || runtime.seconds() <= 1)
                {
                    telemetry.addData("", "Running");
                }
            }//wait until done
            if (true){
                //Stop all motion, wait .1 second to lower G force
                robot.backLeft.setPower(0);
                robot.backRight.setPower(0);
                robot.frontLeft.setPower(0);
                robot.frontRight.setPower(0);
                robot.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                resetMotors();
                sleep(msToWaitAfter);
            }//stop
        }
    }
    public void encoderDrive(double speed, double inches, int msToWaitAfter) {
        if (opModeIsActive()) {
            if (true) {
                resetMotors();
                    robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() - (int) Math.round(inches * countsPerInch));
                    robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() - (int) Math.round(inches * countsPerInch));
                    robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() - (int) Math.round(inches * countsPerInch));
                    robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() - (int) Math.round(inches * countsPerInch));
            }//find and set motors' target position
            if (true) {
                robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                //Start motion.
                speed *= -1;
                //if drifting right, go down, if drifting left, go up, if not drifting, set to 1
                double drift = .9;
                robot.backLeft.setPower(speed * drift);
                robot.backRight.setPower(speed / drift);
                robot.frontLeft.setPower(speed * drift);
                robot.frontRight.setPower(speed / drift);
            }//begin driving to target position
            if (true) {
                while (opModeIsActive() && (robot.backLeft.isBusy() && robot.backRight.isBusy() && robot.frontLeft.isBusy() && robot.frontRight.isBusy()))
                {
                    telemetry.addData("countsPerDegree", countsPerDegree);
                    telemetry.update();
                    if(!(parkingSpot() == 'n'))
                    {
                        parkingSpot = parkingSpot();
                    }
                }
            }//wait until done
            if (true) {
                //Stop all motion, wait .1 second to lower G force
                robot.backLeft.setPower(0);
                robot.backRight.setPower(0);
                robot.frontLeft.setPower(0);
                robot.frontRight.setPower(0);
                robot.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                resetMotors();
                sleep(msToWaitAfter);
            }//stop
        }
    }
    public void resetMotors() {
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
    public char parkingSpot() {
        if (robot.colorSensor.red() >= 100 | robot.colorSensor.green() >= 100 | robot.colorSensor.blue() >= 100) {
            if (robot.colorSensor.red() > robot.colorSensor.blue()) {
                if (robot.colorSensor.red() > robot.colorSensor.green()) {
                    return 'r';
                } else {
                    return 'g';
                }
            } else {
                if (robot.colorSensor.blue() > robot.colorSensor.green()) {
                    return 'b';
                } else {
                    return 'g';
                }
            }
        } else {
            return 'n';
        }
    } //parkingSpot() returns a char value of r if the color detected is red, g if the color is green, b if the color detected is blue, or n if no color is detected.
}
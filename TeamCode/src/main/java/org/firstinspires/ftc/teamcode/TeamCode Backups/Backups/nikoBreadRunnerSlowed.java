package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="nikoBreadRunnerSlowed", group="TeleOp Bread")
public class nikoBreadRunnerSlowed extends LinearOpMode
{
    Hardware_Test robot = new Hardware_Test();
    //declare & set motor power variables to 0 in case a bug causes them to not get set later.
    double frontLeft = 0;
    double frontRight = 0;
    double backLeft = 0;
    double backRight = 0;

    //declare & set drive, strafe, and turn variables to 0 in case a bug causes them to not get set later.
    double drive = 0;
    double strafe = 0;
    double turn = 0;

    //1 moves at full speed, 0 doesn't move.
    double driveSpeed = 0.3;
    double strafeSpeed = 0.4;
    double turnSpeed = 0.3;

    //use 1 when no drift
    double driveDrift = 1.125; //if drift is left, change this up, if drift is right, change this down
    double strafeDrift = 1.1; //if strafe orbits behind the bot, change this down, if it orbits in front, change this up

    /*
    "if(true){...}" makes code collapsible, which helps us stay organized.
    "if(false){...}" allows for disabling code without having to make many changes.
    */

    @Override
    public void runOpMode()
    {
        robot.init(this.hardwareMap);
        waitForStart();

        while (opModeIsActive()) {
            if(true){
                if(true){
                    if(true){
                        if(true){
                            if (Math.abs(gamepad1.left_stick_x + gamepad1.right_stick_x) / 2 >= .25) {
                                strafe = (gamepad1.left_stick_x + gamepad1.right_stick_x) / 2;
                            } else {
                                strafe = 0;
                            }
                            if (Math.abs(gamepad1.left_stick_y - gamepad1.right_stick_y) >= .25) {
                                turn = gamepad1.left_stick_y - gamepad1.right_stick_y;
                            } else {
                                turn = 0;
                            }
                            if (Math.abs((gamepad1.left_stick_y + gamepad1.right_stick_y) / 2) >= .25) {
                                drive = (gamepad1.left_stick_y + gamepad1.right_stick_y) / 2;
                            } else {
                                drive = 0;
                            }
                    }//stick movement
                        if(true){
                            if (!(gamepad1.right_bumper & gamepad1.left_bumper))
                            {
                                if (gamepad1.left_bumper) {turn = 2;}
                                if (gamepad1.right_bumper) {turn = -2;}
                            }
                        }//bumper turning
                    }//sets drive, strafe, and turn variables
                    if(true){
                        drive *= driveSpeed;
                        strafe *= strafeSpeed;
                        turn *= turnSpeed;

                        frontLeft = (drive * driveDrift) + (turn) + (strafe / strafeDrift);
                        frontRight = (drive / driveDrift) - (turn) - (strafe / strafeDrift);
                        backLeft = (drive * driveDrift) + (turn) - (strafe * strafeDrift);
                        backRight = (drive / driveDrift) - (turn) + (strafe * strafeDrift);
                    }//applies drive, strafe, turn, their speeds, and drift directions to motor power variables
                }//sets motor power variables
                if(true){
                    if (gamepad1.x)
                    {
                        driveSpeed = 0.3;
                        strafeSpeed = 0.4;
                    }
                    if (gamepad1.b)
                    {
                        driveSpeed = 0.4;
                        strafeSpeed = 0.5;
                    }
                }//recieves button inputs for speed values
                if(true){
                    double max = Math.max(
                            Math.max(Math.abs(frontLeft), Math.abs(frontRight)),
                            Math.max(Math.abs(backLeft), Math.abs(backRight)));
                    if (max > 1)
                    {
                        backLeft /= max;
                        backRight /= max;
                        frontLeft /= max;
                        frontRight /= max;
                    }
                }//makes sure no motor has a power greater than 1
                if(true){
                    robot.frontRight.setPower(frontRight);
                    robot.backRight.setPower(backRight);
                    robot.frontLeft.setPower(frontLeft);
                    robot.backLeft.setPower(backLeft);
                }//sets actual wheel powers
            }//operates chassis motors
            if(true){
                if(true){
                    if (Math.abs(gamepad2.left_stick_y) > .1)
                    {
                            robot.fulcrum.setPower(-gamepad2.left_stick_y);
                    }
                    else
                    {
                        robot.fulcrum.setPower(0);
                        robot.fulcrum.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    }
                }//receives & acts on linear slide related inputs
                if(true){
                    if(gamepad2.x)
                    {
                        robot.wrist.setPower(.97);
                    }
                    if(gamepad2.b)
                    {
                        robot.wrist.setPower(.43);
                    }
                }//receives & acts on clamp related inputs
            }//operates linear slide
            if(true){
                //Tells the Control Hub the current power of the motors
                telemetry.addData("Drive", drive);
                telemetry.addData("Strafe", strafe);
                telemetry.addData("Turn", turn);
                telemetry.update();
            }//telemetry updates
        }
    }
}
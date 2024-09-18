package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="BreadRunner", group="TeleOp Bread")
@Disabled
public class BreadRunner extends LinearOpMode
{
    Hardware_Test robot = new Hardware_Test();
    double multiplier = 0.3;
    double driveSpeed = 0.35;
    double strafeSpeed = 0.4;
    double turnSpeed = 0.55;
    double frontLeft = 0;
    double backLeft = 0;
    double frontRight = 0;
    double backRight = 0;
    double strafe = 0;
    double strafeDrift = 1.15;
    double driveDrift = 1.2;
    //"if (true) {}" makes code collapsible, which helps us stay organized.
    //"if (false) {}" allows for disabling code without having to find where the comment lines are.

    @Override
    public void runOpMode()
    {
        robot.init(this.hardwareMap);
        waitForStart();

        while (opModeIsActive()) {
            if(true){
                if(true){
                    //Sets strafe to the horizontal movement of the driver sticks
                    if (Math.abs(gamepad1.left_stick_x + gamepad1.right_stick_x) / 2 >= .1)
                    {
                        strafe = (gamepad1.left_stick_x + gamepad1.right_stick_x) / 2;
                    }
                    else
                    {
                        strafe = 0;
                    }

                    //Sets the variables to the initial power.
                    if (!(Math.abs(strafe) >= .9))
                    {
                        if (Math.abs(gamepad1.left_stick_y) >= .1) {
                            backLeft = gamepad1.left_stick_y - strafe;
                            frontLeft = gamepad1.left_stick_y + strafe;
                        }
                        else
                        {
                            backLeft = -strafe;
                            frontLeft = strafe;
                        }
                        if (Math.abs(gamepad1.right_stick_y) >= .1) {
                            backRight = gamepad1.right_stick_y + strafe;
                            frontRight = gamepad1.right_stick_y - strafe;
                        }
                        else
                        {
                            backRight = strafe;
                            frontRight = -strafe;
                        }
                    }
                    else
                    {
                        backLeft = -strafe;
                        frontLeft = strafe;
                        backRight = strafe;
                        frontRight = -strafe;
                    }
                }//gets controller stick data
                if(true){
                    if(Math.abs(frontLeft - frontRight) <= .25)
                    {
                        if (Math.abs(strafe) >= .25)
                        {
                            backLeft *= strafeDrift;
                            backRight *= strafeDrift;
                            frontLeft /= strafeDrift;
                            frontRight /= strafeDrift;
                        }
                        else
                        {
                            backLeft *= driveDrift;
                            backRight /= driveDrift;
                            frontLeft *= driveDrift;
                            frontRight /= driveDrift;
                        }
                    }
                }//accounts for drift
                if(true){
                    if (strafe <= .25 & Math.abs(frontLeft - frontRight) <= 0.25)
                    {
                        double temp = (frontLeft + frontRight) / 2;
                        frontLeft = temp;
                        frontRight = temp;
                        backLeft = temp;
                        backRight = temp;
                    }
                }//makes it easier for the driver to go straight
                if(true){
                    if (gamepad1.b)
                    {
                        driveSpeed = 0.45;
                        strafeSpeed = 0.5;
                    }
                    if (gamepad1.x)
                    {
                        driveSpeed = 0.35;
                        strafeSpeed = 0.4;
                    }

                    if (Math.abs(strafe) >= .25)
                    {
                        multiplier = strafeSpeed;
                    }
                    else
                    {
                        if (Math.abs(frontLeft - frontRight) >= .25)
                        {
                            multiplier = turnSpeed;
                        }
                        else
                        {
                            multiplier = driveSpeed;
                        }
                    }
                }//sets multiplier
                if(true){
                    double max = Math.max(
                            Math.max(Math.abs(frontLeft), Math.abs(frontRight)),
                            Math.max(Math.abs(backLeft), Math.abs(backRight)));

                    {
                        backLeft /= max;
                        backRight /= max;
                        frontLeft /= max;
                        frontRight /= max;
                    }
                    if (max * multiplier > 1);
                }//makes sure no motor has a power greater than 1
                if(true){
                    robot.frontRight.setPower(frontRight * multiplier);
                    robot.backRight.setPower(backRight * multiplier);
                    robot.frontLeft.setPower(frontLeft * multiplier);
                    robot.backLeft.setPower(backLeft * multiplier);
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
                }//operates linear slide body
                if(true){
                    if(gamepad2.x)
                    {
                        robot.wrist.setPower(.97);
                    }
                    if(gamepad2.b)
                    {
                        robot.wrist.setPower(.43);
                    }
                }//operates wrist
            }//operates linear slide
            if(true){
                //Tells the Control Hub the current power of the motors
                telemetry.addData("Drive", (gamepad1.left_stick_y + gamepad1.right_stick_y) / 2);
                telemetry.addData("Strafe", strafe);
                telemetry.addData("Turn", frontLeft - frontRight);
                telemetry.addData("Speed Level", multiplier);
                telemetry.update();
            }//telemetry updates
        }
    }
}
package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="ClankTeleOp", group="TeleOp Bread")
public class ClankTeleOp extends LinearOpMode {
    //declare and initialize ClankHardware handler
    Hardware robot = new Hardware();
    //declare and initialize constants
    double DEADZONE = .2;
    double DRIVE_SPEED = 0.35;
    double STRAFE_SPEED = 0.45;
    double TURN_SPEED = 0.45;
    double drive, strafe, turn, frontLeft, frontRight, backLeft, backRight;

    @Override
    public void runOpMode() {
        robot.init(this.hardwareMap);
        waitForStart();

        while (opModeIsActive()) {
            findChassisPower();
            robot.frontRight.setPower(frontRight);
            robot.backRight.setPower(backRight);
            robot.frontLeft.setPower(frontLeft);
            robot.backLeft.setPower(backLeft);

            telemetry.addData("Drive", drive);
            telemetry.addData("Strafe", strafe);
            telemetry.addData("Turn", turn);
            telemetry.update();
        }
    }
    public void findChassisPower() {
        if (gamepad1.x) {
            DRIVE_SPEED = 0.35;
            STRAFE_SPEED = 0.45;
        } else if (gamepad1.b) {
            DRIVE_SPEED = 0.45;
            STRAFE_SPEED = 0.55;
        }

        //stick input
        double tempInput = (gamepad1.left_stick_y + gamepad1.right_stick_y) / 2;
        drive = (Math.abs(tempInput) >= DEADZONE)? tempInput * DRIVE_SPEED : 0;
        tempInput = (gamepad1.left_stick_x + gamepad1.right_stick_x) / 2;
        strafe = (Math.abs(tempInput) >= DEADZONE)? tempInput * STRAFE_SPEED : 0;
        tempInput = gamepad1.left_stick_y - gamepad1.right_stick_y;
        turn = (Math.abs(tempInput) >= DEADZONE)? tempInput * TURN_SPEED: 0;

        //conversion to wheel powers
        frontLeft = drive + turn + strafe;
        frontRight = drive - turn - strafe;
        backLeft = drive + turn - strafe;
        backRight = drive - turn + strafe;

        //make sure motors dont power > 1, maintain ratios
        double max = Math.max(
                Math.max(Math.abs(frontLeft), Math.abs(frontRight)),
                Math.max(Math.abs(backLeft), Math.abs(backRight))
        );
        if (max > 1) {
            backLeft /= max;
            backRight /= max;
            frontLeft /= max;
            frontRight /= max;
        }
    }
}
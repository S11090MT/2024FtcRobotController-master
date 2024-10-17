package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "NewBasicDrive (Blocks to Java)")
public class NewBasicDrive extends LinearOpMode {

    private DcMotor leftFront_drive;
    private DcMotor rightFront_drive;
    private DcMotor rightBack_drive;
    private DcMotor leftBack_drive;
    double motorSpeed = 0.6;

    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        leftFront_drive = hardwareMap.get(DcMotor.class, "leftFront_drive");
        rightFront_drive = hardwareMap.get(DcMotor.class, "rightFront_drive");
        rightBack_drive = hardwareMap.get(DcMotor.class, "rightBack_drive");
        leftBack_drive = hardwareMap.get(DcMotor.class, "leftBack_drive");

        // Put initialization blocks here.
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (gamepad1.left_stick_y < 0) {
                    leftFront_drive.setDirection(DcMotor.Direction.REVERSE);
                    rightFront_drive.setDirection(DcMotor.Direction.FORWARD);
                    rightBack_drive.setDirection(DcMotor.Direction.FORWARD);
                    leftBack_drive.setDirection(DcMotor.Direction.REVERSE);
                    rightFront_drive.setPower(motorSpeed * gamepad1.left_stick_y);
                    leftFront_drive.setPower(motorSpeed * gamepad1.left_stick_y);
                    rightBack_drive.setPower(motorSpeed * gamepad1.left_stick_y);
                    leftBack_drive.setPower(motorSpeed * gamepad1.left_stick_y);
                } else if (gamepad1.left_stick_y > 0) {
                    leftFront_drive.setDirection(DcMotor.Direction.REVERSE);
                    rightFront_drive.setDirection(DcMotor.Direction.FORWARD);
                    rightBack_drive.setDirection(DcMotor.Direction.FORWARD);
                    leftBack_drive.setDirection(DcMotor.Direction.REVERSE);
                    leftFront_drive.setPower(motorSpeed * gamepad1.left_stick_y);
                    rightBack_drive.setPower(motorSpeed * gamepad1.left_stick_y);
                    rightFront_drive.setPower(motorSpeed * gamepad1.left_stick_y);
                    leftBack_drive.setPower(motorSpeed * gamepad1.left_stick_y);
                } else {
                    leftFront_drive.setDirection(DcMotor.Direction.REVERSE);
                    leftBack_drive.setDirection(DcMotor.Direction.REVERSE);
                    rightBack_drive.setDirection(DcMotor.Direction.FORWARD);
                    rightFront_drive.setDirection(DcMotor.Direction.FORWARD);
                    rightBack_drive.setPower(((0 + motorSpeed * gamepad1.right_stick_y) - motorSpeed * gamepad1.left_stick_x) + motorSpeed * gamepad1.right_stick_x);
                    leftBack_drive.setPower((0 + motorSpeed * gamepad1.right_stick_y + motorSpeed * gamepad1.left_stick_x) - motorSpeed * gamepad1.right_stick_x);
                    leftFront_drive.setPower(((0 + motorSpeed * gamepad1.right_stick_y) - motorSpeed * gamepad1.left_stick_x) - motorSpeed * gamepad1.right_stick_x);
                    rightFront_drive.setPower(0 + motorSpeed * gamepad1.right_stick_y + motorSpeed * gamepad1.left_stick_x + motorSpeed * gamepad1.right_stick_x);
                }
                if (gamepad1.left_bumper) {
                    rightFront_drive.setDirection(DcMotor.Direction.FORWARD);
                    leftFront_drive.setDirection(DcMotor.Direction.REVERSE);
                    rightBack_drive.setDirection(DcMotor.Direction.FORWARD);
                    leftBack_drive.setDirection(DcMotor.Direction.REVERSE);
                    rightFront_drive.setPower(motorSpeed);
                    leftFront_drive.setPower(motorSpeed);
                    rightBack_drive.setPower(motorSpeed);
                    leftBack_drive.setPower(motorSpeed);
                } else if (gamepad1.right_bumper) {
                    rightFront_drive.setDirection(DcMotor.Direction.REVERSE);
                    leftFront_drive.setDirection(DcMotor.Direction.FORWARD);
                    rightBack_drive.setDirection(DcMotor.Direction.REVERSE);
                    leftBack_drive.setDirection(DcMotor.Direction.FORWARD);
                    rightFront_drive.setPower(motorSpeed);
                    leftFront_drive.setPower(motorSpeed);
                    rightBack_drive.setPower(motorSpeed);
                    leftBack_drive.setPower(motorSpeed);
                }
                telemetry.update();
            }
        }
    }
}

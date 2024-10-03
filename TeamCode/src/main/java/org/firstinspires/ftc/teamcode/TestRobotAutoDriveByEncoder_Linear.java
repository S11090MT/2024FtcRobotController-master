
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
Hi Mike have a nice day!
 * This OpMode illustrates the concept of driving a path based on encoder counts.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: RobotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forward, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 CM
 *   - Spin right for 12 CM
 *   - Drive Backward for 24 CM
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftbackCM, rightbackCM, timeoutS)
 *  that performs the actual movement.
 *  This method assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */
@Autonomous(name="Robot: Auto Drive By Encoder", group="Robot")
public class TestRobotAutoDriveByEncoder_Linear extends LinearOpMode {

    /* Declare OpMode members. */
    private DcMotor         leftfrontDrive   = null;
    private DcMotor         leftbackDrive   = null;
    private DcMotor         rightfrontDrive   = null;
    private DcMotor         rightbackDrive  = null;

    private ElapsedTime     runtime = new ElapsedTime();

    // Calculate the COUNTS_PER_CM for your specific drive train.
    // Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
    // For external drive gearing, set DRIVE_GEAR_REDUCTION as needed.
    // For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
    // This is gearing DOWN for less speed and more torque.
    // For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_CM   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_CM         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_CM * 14); //Change to cm
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    @Override
    public void runOpMode() {

        // Initialize the drive system variables.
        leftfrontDrive  = hardwareMap.get(DcMotor.class, "leftfront_drive");
        leftbackDrive  = hardwareMap.get(DcMotor.class, "leftback_drive");
        rightfrontDrive = hardwareMap.get(DcMotor.class, "rightback_drive");
        rightbackDrive = hardwareMap.get(DcMotor.class, "leftback_drive");


        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        leftfrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftbackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightfrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightbackDrive.setDirection(DcMotor.Direction.FORWARD);

        leftfrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftbackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightfrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightbackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftfrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftbackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightfrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightbackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Starting at",  "%7d :%7d",
                          rightfrontDrive.getCurrentPosition(),
                          leftbackDrive.getCurrentPosition(),
                          rightfrontDrive.getCurrentPosition(),
                          rightbackDrive.getCurrentPosition());

        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        encoderDrive(DRIVE_SPEED,  48,  48, 48,48, 5.0);  // S1: Forward 47 CM with 5 Sec timeout
        encoderDrive(TURN_SPEED,   12, -12, 12,12, 2.0);  // S2: Turn Right 12 CM with 4 Sec timeout
        encoderDrive(DRIVE_SPEED, -24, -24, -24,-24, 5.0);  // S3: Reverse 24 CM with 4 Sec timeout

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);  // pause to display final telemetry message.
    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the OpMode running.
     */
    public void encoderDrive(double speed,
                             double leftbackCM, double leftfrontCM, double rightbackCM, double rightfrontCM,
                             double timeoutS) {
        int newLeftbackTarget;
        int newLeftfrontTarget;
        int newRightfrontTarget;
        int newRightbackTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftbackTarget = leftbackDrive.getCurrentPosition() + (int)(leftbackCM * COUNTS_PER_CM);
            newLeftfrontTarget = leftfrontDrive.getCurrentPosition() + (int)(leftfrontCM * COUNTS_PER_CM);
            newRightbackTarget = rightbackDrive.getCurrentPosition() + (int)(rightbackCM * COUNTS_PER_CM);
            newRightfrontTarget = rightfrontDrive.getCurrentPosition() + (int)(rightfrontCM * COUNTS_PER_CM);
            leftfrontDrive.setTargetPosition(newLeftfrontTarget);
            leftbackDrive.setTargetPosition(newLeftbackTarget);
            rightbackDrive.setTargetPosition(newRightbackTarget);
            rightfrontDrive.setTargetPosition(newRightfrontTarget);

            // Turn On RUN_TO_POSITION
            leftfrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftbackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightbackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightbackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            leftfrontDrive.setPower(Math.abs(speed));
            leftbackDrive.setPower(Math.abs(speed));
            rightbackDrive.setPower(Math.abs(speed));
            rightbackDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (leftbackDrive.isBusy() && rightbackDrive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to",  " %7d :%7d", newRightfrontTarget, newRightbackTarget,  newLeftfrontTarget, newLeftbackTarget);
                telemetry.addData("Currently at",  " at %7d :%7d",
                                            leftbackDrive.getCurrentPosition(), rightbackDrive.getCurrentPosition(),leftfrontDrive.getCurrentPosition(), rightfrontDrive.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftfrontDrive.setPower(0);
            leftbackDrive.setPower(0);
            rightbackDrive.setPower(0);
            rightbackDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            leftfrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftbackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightfrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightbackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move.
        }
    }
}

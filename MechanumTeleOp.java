package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class MecanumTeleOp extends LinearOpMode {
    private boolean halfSpeed = false;
    private boolean lastButtonState = false;

    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontRight = hardwareMap.dcMotor.get("frontRight");
        DcMotor rearRight = hardwareMap.dcMotor.get("rearRight");
        DcMotor rearLeft = hardwareMap.dcMotor.get("rearLeft");
        DcMotor frontLeft = hardwareMap.dcMotor.get("frontLeft");

        // Reverse the right side motors
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // Check for half-speed toggle
            boolean currentButtonState = gamepad1.left_stick_button;
            if (currentButtonState && !lastButtonState) {
                halfSpeed = !halfSpeed;
            }
            lastButtonState = currentButtonState;

            double y = gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;

            // Apply half-speed if toggled
            if (halfSpeed) {
                y *= 0.3;
                x *= 0.3;
                rx *= 0.3;
            }

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontRightPower = (y + x + rx) / denominator;
            double rearRightPower = (y - x + rx) / denominator;
            double rearLeftPower = (y + x - rx) / denominator;
            double frontLeftPower = (y - x - rx) / denominator;

            frontRight.setPower(frontRightPower);
            rearRight.setPower(rearRightPower);
            rearLeft.setPower(rearLeftPower);
            frontLeft.setPower(frontLeftPower);

            // Display half-speed status on telemetry
            telemetry.addData("Half-Speed Mode", halfSpeed ? "ON" : "OFF");
            telemetry.update();
        }
    }
}

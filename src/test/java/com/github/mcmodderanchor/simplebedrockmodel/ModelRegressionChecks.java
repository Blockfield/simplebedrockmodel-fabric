package com.github.mcmodderanchor.simplebedrockmodel;

import com.github.mcmodderanchor.simplebedrockmodel.v1.common.animation.AnimationRateLimiter;
import com.github.mcmodderanchor.simplebedrockmodel.v1.common.time.AnimationClock;
import com.github.mcmodderanchor.simplebedrockmodel.v1.util.math.MathUtil;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ModelRegressionChecks {
    public static void main(String[] args) {
        animationCaching();
        rotations();
        for (double fov : new double[] {30, 70, 110}) {
            near(MathUtil.magnificationToFov(1, fov), fov);
            for (double magnification : new double[] {1.5, 4, 10}) {
                double zoomed = MathUtil.magnificationToFov(magnification, fov);
                near(MathUtil.fovToMagnification(zoomed, fov), magnification);
                check(zoomed < fov, "magnification must narrow FOV");
            }
        }
        System.out.println("Model animation cache, rotation interpolation and FOV checks passed");
    }

    private static void animationCaching() {
        long[] now = {0};
        long[] interval = {10};
        int[] evaluations = {0};
        AnimationClock clock = () -> now[0];
        var limiter = new AnimationRateLimiter<Integer>(clock, () -> interval[0]);
        check(limiter.getCachedValue() == null, "cache starts empty");
        check(limiter.update(() -> ++evaluations[0]) == 1, "first frame evaluates");
        now[0] = 9;
        check(limiter.update(() -> ++evaluations[0]) == 1, "subinterval frame reuses pose");
        now[0] = 10;
        check(limiter.update(() -> ++evaluations[0]) == 2, "boundary frame evaluates");
        check(limiter.update(() -> ++evaluations[0]) == 2, "same frame reuses pose");
        interval[0] = 0;
        check(limiter.update(() -> ++evaluations[0]) == 3, "unlimited mode evaluates");
        check(limiter.update(() -> ++evaluations[0]) == 4, "unlimited mode never throttles");
        interval[0] = 10;
        limiter.setCachedValue(42);
        check(limiter.update(() -> ++evaluations[0]) == 42, "prewarmed pose is reused");
        check(evaluations[0] == 4, "cached updates must not call the evaluator");
        limiter.reset();
        check(limiter.update(() -> ++evaluations[0]) == 5, "reset forces evaluation");
        now[0] = 1_234_567;
        check(clock.nowMillis() == 1, "clock converts nanoseconds to milliseconds");
    }

    private static void rotations() {
        float[] identity = MathUtil.QUATERNION_ONE;
        float[] turn = MathUtil.toQuaternion(0, (float) (Math.PI / 2), 0);
        quaternionNear(MathUtil.slerp(identity, turn, 0), identity);
        quaternionNear(MathUtil.slerp(identity, turn, 1), turn);
        float[] halfway = MathUtil.slerp(identity, turn, 0.5f);
        near(MathUtil.toEulerAngles(halfway)[1], Math.PI / 4);
        quaternionNear(MathUtil.slerp(identity, new float[] {0, 0, 0, -1}, 0.5f), identity);
        var objectHalfway = MathUtil.slerp(new Quaternionf(), MathUtil.toQuaternion(turn), 0.5f);
        quaternionNear(
                new float[] {objectHalfway.x, objectHalfway.y, objectHalfway.z, objectHalfway.w},
                halfway);
        var from = new Vector3f(1, 0, 0);
        var to = new Vector3f(-1, 0, 0);
        Quaternionf rotation = MathUtil.setFromUnitVectors(from, to, new Quaternionf());
        Vector3f rotated = new Vector3f(from).rotate(rotation);
        near(rotated.x, to.x);
        near(rotated.y, to.y);
        near(rotated.z, to.z);
    }

    private static void quaternionNear(float[] actual, float[] expected) {
        check(actual.length == expected.length, "quaternion size");
        for (int i = 0; i < expected.length; i++) near(actual[i], expected[i]);
    }

    private static void near(double actual, double expected) {
        if (!Double.isFinite(actual) || Math.abs(actual - expected) > 1e-5)
            throw new AssertionError("expected " + expected + ", got " + actual);
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}

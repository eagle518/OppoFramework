package android.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.CanvasProperty;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.RippleComponent.RenderNodeAnimatorSet;
import android.hardware.camera2.params.TonemapCurve;
import android.util.FloatProperty;
import android.util.Property;
import android.view.DisplayListCanvas;
import android.view.RenderNodeAnimator;

/*  JADX ERROR: NullPointerException in pass: ReSugarCode
    java.lang.NullPointerException
    	at jadx.core.dex.visitors.ReSugarCode.initClsEnumMap(ReSugarCode.java:159)
    	at jadx.core.dex.visitors.ReSugarCode.visit(ReSugarCode.java:44)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:12)
    	at jadx.core.ProcessClass.process(ProcessClass.java:32)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
    */
/*  JADX ERROR: NullPointerException in pass: ExtractFieldInit
    java.lang.NullPointerException
    	at jadx.core.dex.visitors.ExtractFieldInit.checkStaticFieldsInit(ExtractFieldInit.java:58)
    	at jadx.core.dex.visitors.ExtractFieldInit.visit(ExtractFieldInit.java:44)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:12)
    	at jadx.core.ProcessClass.process(ProcessClass.java:32)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
    */
class RippleBackground extends RippleComponent {
    private static final TimeInterpolator LINEAR_INTERPOLATOR = null;
    private static final BackgroundProperty OPACITY = null;
    private static final int OPACITY_ENTER_DURATION = 600;
    private static final int OPACITY_ENTER_DURATION_FAST = 120;
    private static final int OPACITY_EXIT_DURATION = 480;
    private boolean mIsBounded;
    private float mOpacity;
    private CanvasProperty<Paint> mPropPaint;
    private CanvasProperty<Float> mPropRadius;
    private CanvasProperty<Float> mPropX;
    private CanvasProperty<Float> mPropY;

    private static abstract class BackgroundProperty extends FloatProperty<RippleBackground> {
        public BackgroundProperty(String name) {
            super(name);
        }
    }

    /* renamed from: android.graphics.drawable.RippleBackground$1 */
    static class AnonymousClass1 extends BackgroundProperty {
        AnonymousClass1(String $anonymous0) {
            super($anonymous0);
        }

        public void setValue(RippleBackground object, float value) {
            object.mOpacity = value;
            object.invalidateSelf();
        }

        public Float get(RippleBackground object) {
            return Float.valueOf(object.mOpacity);
        }
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 0073 in method: android.graphics.drawable.RippleBackground.<clinit>():void, dex: 
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
        	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1227)
        	at com.android.dx.io.OpcodeInfo.getName(OpcodeInfo.java:1234)
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:581)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:74)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:104)
        	... 5 more
        */
    static {
        /*
        // Can't load method instructions: Load method exception: bogus opcode: 0073 in method: android.graphics.drawable.RippleBackground.<clinit>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.RippleBackground.<clinit>():void");
    }

    public RippleBackground(RippleDrawable owner, Rect bounds, boolean isBounded, boolean forceSoftware) {
        super(owner, bounds, forceSoftware);
        this.mOpacity = TonemapCurve.LEVEL_BLACK;
        this.mIsBounded = isBounded;
    }

    public boolean isVisible() {
        return this.mOpacity <= TonemapCurve.LEVEL_BLACK ? isHardwareAnimating() : true;
    }

    protected boolean drawSoftware(Canvas c, Paint p) {
        int origAlpha = p.getAlpha();
        int alpha = (int) ((((float) origAlpha) * this.mOpacity) + 0.5f);
        if (alpha <= 0) {
            return false;
        }
        p.setAlpha(alpha);
        c.drawCircle(TonemapCurve.LEVEL_BLACK, TonemapCurve.LEVEL_BLACK, this.mTargetRadius, p);
        p.setAlpha(origAlpha);
        return true;
    }

    protected boolean drawHardware(DisplayListCanvas c) {
        c.drawCircle(this.mPropX, this.mPropY, this.mPropRadius, this.mPropPaint);
        return true;
    }

    protected Animator createSoftwareEnter(boolean fast) {
        int duration = (int) ((1.0f - this.mOpacity) * ((float) (fast ? 120 : 600)));
        Property property = OPACITY;
        float[] fArr = new float[1];
        fArr[0] = 1.0f;
        ObjectAnimator opacity = ObjectAnimator.ofFloat((Object) this, property, fArr);
        opacity.setAutoCancel(true);
        opacity.setDuration((long) duration);
        opacity.setInterpolator(LINEAR_INTERPOLATOR);
        return opacity;
    }

    protected Animator createSoftwareExit() {
        int fastEnterDuration;
        AnimatorSet set = new AnimatorSet();
        Property property = OPACITY;
        float[] fArr = new float[1];
        fArr[0] = TonemapCurve.LEVEL_BLACK;
        ObjectAnimator exit = ObjectAnimator.ofFloat((Object) this, property, fArr);
        exit.setInterpolator(LINEAR_INTERPOLATOR);
        exit.setDuration(480);
        exit.setAutoCancel(true);
        Builder builder = set.play(exit);
        if (this.mIsBounded) {
            fastEnterDuration = (int) ((1.0f - this.mOpacity) * 120.0f);
        } else {
            fastEnterDuration = 0;
        }
        if (fastEnterDuration > 0) {
            property = OPACITY;
            fArr = new float[1];
            fArr[0] = 1.0f;
            Animator enter = ObjectAnimator.ofFloat((Object) this, property, fArr);
            enter.setInterpolator(LINEAR_INTERPOLATOR);
            enter.setDuration((long) fastEnterDuration);
            enter.setAutoCancel(true);
            builder.after(enter);
        }
        return set;
    }

    protected RenderNodeAnimatorSet createHardwareExit(Paint p) {
        RenderNodeAnimatorSet set = new RenderNodeAnimatorSet();
        int targetAlpha = p.getAlpha();
        p.setAlpha((int) ((this.mOpacity * ((float) targetAlpha)) + 0.5f));
        this.mPropPaint = CanvasProperty.createPaint(p);
        this.mPropRadius = CanvasProperty.createFloat(this.mTargetRadius);
        this.mPropX = CanvasProperty.createFloat(TonemapCurve.LEVEL_BLACK);
        this.mPropY = CanvasProperty.createFloat(TonemapCurve.LEVEL_BLACK);
        int fastEnterDuration = this.mIsBounded ? (int) ((1.0f - this.mOpacity) * 120.0f) : 0;
        RenderNodeAnimator exit = new RenderNodeAnimator(this.mPropPaint, 1, TonemapCurve.LEVEL_BLACK);
        exit.setInterpolator(LINEAR_INTERPOLATOR);
        exit.setDuration(480);
        if (fastEnterDuration > 0) {
            exit.setStartDelay((long) fastEnterDuration);
            exit.setStartValue((float) targetAlpha);
        }
        set.add(exit);
        if (fastEnterDuration > 0) {
            RenderNodeAnimator enter = new RenderNodeAnimator(this.mPropPaint, 1, (float) targetAlpha);
            enter.setInterpolator(LINEAR_INTERPOLATOR);
            enter.setDuration((long) fastEnterDuration);
            set.add(enter);
        }
        return set;
    }

    protected void jumpValuesToExit() {
        this.mOpacity = TonemapCurve.LEVEL_BLACK;
    }
}

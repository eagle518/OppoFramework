package android.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.CanvasProperty;
import android.graphics.Paint;
import android.util.SparseIntArray;
import com.android.internal.util.VirtualRefBasePtr;
import com.android.internal.view.animation.FallbackLUTInterpolator;
import com.android.internal.view.animation.HasNativeInterpolator;
import com.android.internal.view.animation.NativeInterpolatorFactory;
import java.util.ArrayList;

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
public class RenderNodeAnimator extends Animator {
    public static final int ALPHA = 11;
    public static final int LAST_VALUE = 11;
    public static final int PAINT_ALPHA = 1;
    public static final int PAINT_STROKE_WIDTH = 0;
    public static final int ROTATION = 5;
    public static final int ROTATION_X = 6;
    public static final int ROTATION_Y = 7;
    public static final int SCALE_X = 3;
    public static final int SCALE_Y = 4;
    private static final int STATE_DELAYED = 1;
    private static final int STATE_FINISHED = 3;
    private static final int STATE_PREPARE = 0;
    private static final int STATE_RUNNING = 2;
    public static final int TRANSLATION_X = 0;
    public static final int TRANSLATION_Y = 1;
    public static final int TRANSLATION_Z = 2;
    public static final int X = 8;
    public static final int Y = 9;
    public static final int Z = 10;
    private static ThreadLocal<DelayedAnimationHelper> sAnimationHelper;
    private static final SparseIntArray sViewPropertyAnimatorMap = null;
    private float mFinalValue;
    private TimeInterpolator mInterpolator;
    private VirtualRefBasePtr mNativePtr;
    private int mRenderProperty;
    private long mStartDelay;
    private long mStartTime;
    private int mState;
    private RenderNode mTarget;
    private final boolean mUiThreadHandlesDelay;
    private long mUnscaledDuration;
    private long mUnscaledStartDelay;
    private View mViewTarget;

    /* renamed from: android.view.RenderNodeAnimator$1 */
    static class AnonymousClass1 extends SparseIntArray {
        AnonymousClass1(int $anonymous0) {
            super($anonymous0);
            put(1, 0);
            put(2, 1);
            put(4, 2);
            put(8, 3);
            put(16, 4);
            put(32, 5);
            put(64, 6);
            put(128, 7);
            put(256, 8);
            put(512, 9);
            put(1024, 10);
            put(2048, 11);
        }
    }

    private static class DelayedAnimationHelper implements Runnable {
        private boolean mCallbackScheduled;
        private final Choreographer mChoreographer = Choreographer.getInstance();
        private ArrayList<RenderNodeAnimator> mDelayedAnims = new ArrayList();

        public void addDelayedAnimation(RenderNodeAnimator animator) {
            this.mDelayedAnims.add(animator);
            scheduleCallback();
        }

        public void removeDelayedAnimation(RenderNodeAnimator animator) {
            this.mDelayedAnims.remove(animator);
        }

        private void scheduleCallback() {
            if (!this.mCallbackScheduled) {
                this.mCallbackScheduled = true;
                this.mChoreographer.postCallback(1, this, null);
            }
        }

        public void run() {
            long frameTimeMs = this.mChoreographer.getFrameTime();
            this.mCallbackScheduled = false;
            int end = 0;
            for (int i = 0; i < this.mDelayedAnims.size(); i++) {
                RenderNodeAnimator animator = (RenderNodeAnimator) this.mDelayedAnims.get(i);
                if (!animator.processDelayed(frameTimeMs)) {
                    if (end != i) {
                        this.mDelayedAnims.set(end, animator);
                    }
                    end++;
                }
            }
            while (this.mDelayedAnims.size() > end) {
                this.mDelayedAnims.remove(this.mDelayedAnims.size() - 1);
            }
            if (this.mDelayedAnims.size() > 0) {
                scheduleCallback();
            }
        }
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 0073 in method: android.view.RenderNodeAnimator.<clinit>():void, dex: 
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
        // Can't load method instructions: Load method exception: bogus opcode: 0073 in method: android.view.RenderNodeAnimator.<clinit>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.RenderNodeAnimator.<clinit>():void");
    }

    private static native long nCreateAnimator(int i, float f);

    private static native long nCreateCanvasPropertyFloatAnimator(long j, float f);

    private static native long nCreateCanvasPropertyPaintAnimator(long j, int i, float f);

    private static native long nCreateRevealAnimator(int i, int i2, float f, float f2);

    private static native void nEnd(long j);

    private static native long nGetDuration(long j);

    private static native void nSetAllowRunningAsync(long j, boolean z);

    private static native void nSetDuration(long j, long j2);

    private static native void nSetInterpolator(long j, long j2);

    private static native void nSetListener(long j, RenderNodeAnimator renderNodeAnimator);

    private static native void nSetStartDelay(long j, long j2);

    private static native void nSetStartValue(long j, float f);

    private static native void nStart(long j);

    public static int mapViewPropertyToRenderProperty(int viewProperty) {
        return sViewPropertyAnimatorMap.get(viewProperty);
    }

    public RenderNodeAnimator(int property, float finalValue) {
        this.mRenderProperty = -1;
        this.mState = 0;
        this.mUnscaledDuration = 300;
        this.mUnscaledStartDelay = 0;
        this.mStartDelay = 0;
        this.mRenderProperty = property;
        this.mFinalValue = finalValue;
        this.mUiThreadHandlesDelay = true;
        init(nCreateAnimator(property, finalValue));
    }

    public RenderNodeAnimator(CanvasProperty<Float> property, float finalValue) {
        this.mRenderProperty = -1;
        this.mState = 0;
        this.mUnscaledDuration = 300;
        this.mUnscaledStartDelay = 0;
        this.mStartDelay = 0;
        init(nCreateCanvasPropertyFloatAnimator(property.getNativeContainer(), finalValue));
        this.mUiThreadHandlesDelay = false;
    }

    public RenderNodeAnimator(CanvasProperty<Paint> property, int paintField, float finalValue) {
        this.mRenderProperty = -1;
        this.mState = 0;
        this.mUnscaledDuration = 300;
        this.mUnscaledStartDelay = 0;
        this.mStartDelay = 0;
        init(nCreateCanvasPropertyPaintAnimator(property.getNativeContainer(), paintField, finalValue));
        this.mUiThreadHandlesDelay = false;
    }

    public RenderNodeAnimator(int x, int y, float startRadius, float endRadius) {
        this.mRenderProperty = -1;
        this.mState = 0;
        this.mUnscaledDuration = 300;
        this.mUnscaledStartDelay = 0;
        this.mStartDelay = 0;
        init(nCreateRevealAnimator(x, y, startRadius, endRadius));
        this.mUiThreadHandlesDelay = true;
    }

    private void init(long ptr) {
        this.mNativePtr = new VirtualRefBasePtr(ptr);
    }

    private void checkMutable() {
        if (this.mState != 0) {
            throw new IllegalStateException("Animator has already started, cannot change it now!");
        } else if (this.mNativePtr == null) {
            throw new IllegalStateException("Animator's target has been destroyed (trying to modify an animation after activity destroy?)");
        }
    }

    static boolean isNativeInterpolator(TimeInterpolator interpolator) {
        return interpolator.getClass().isAnnotationPresent(HasNativeInterpolator.class);
    }

    private void applyInterpolator() {
        if (this.mInterpolator != null) {
            long ni;
            if (isNativeInterpolator(this.mInterpolator)) {
                ni = ((NativeInterpolatorFactory) this.mInterpolator).createNativeInterpolator();
            } else {
                ni = FallbackLUTInterpolator.createNativeInterpolator(this.mInterpolator, nGetDuration(this.mNativePtr.get()));
            }
            nSetInterpolator(this.mNativePtr.get(), ni);
        }
    }

    public void start() {
        if (this.mTarget == null) {
            throw new IllegalStateException("Missing target!");
        } else if (this.mState != 0) {
            throw new IllegalStateException("Already started!");
        } else {
            this.mState = 1;
            applyInterpolator();
            if (this.mNativePtr == null) {
                cancel();
            } else if (this.mStartDelay <= 0 || !this.mUiThreadHandlesDelay) {
                nSetStartDelay(this.mNativePtr.get(), this.mStartDelay);
                doStart();
            } else {
                getHelper().addDelayedAnimation(this);
            }
        }
    }

    private void doStart() {
        if (this.mRenderProperty == 11) {
            this.mViewTarget.mTransformationInfo.mAlpha = this.mFinalValue;
        }
        moveToRunningState();
        if (this.mViewTarget != null) {
            this.mViewTarget.invalidateViewProperty(true, false);
        }
    }

    private void moveToRunningState() {
        this.mState = 2;
        if (this.mNativePtr != null) {
            nStart(this.mNativePtr.get());
        }
        notifyStartListeners();
    }

    private void notifyStartListeners() {
        ArrayList<AnimatorListener> listeners = cloneListeners();
        int numListeners = listeners == null ? 0 : listeners.size();
        for (int i = 0; i < numListeners; i++) {
            ((AnimatorListener) listeners.get(i)).onAnimationStart(this);
        }
    }

    public void cancel() {
        if (this.mState != 0 && this.mState != 3) {
            if (this.mState == 1) {
                getHelper().removeDelayedAnimation(this);
                moveToRunningState();
            }
            ArrayList<AnimatorListener> listeners = cloneListeners();
            int numListeners = listeners == null ? 0 : listeners.size();
            for (int i = 0; i < numListeners; i++) {
                ((AnimatorListener) listeners.get(i)).onAnimationCancel(this);
            }
            end();
        }
    }

    public void end() {
        if (this.mState != 3) {
            if (this.mState < 2) {
                getHelper().removeDelayedAnimation(this);
                doStart();
            }
            if (this.mNativePtr != null) {
                nEnd(this.mNativePtr.get());
                if (this.mViewTarget != null) {
                    this.mViewTarget.invalidateViewProperty(true, false);
                    return;
                }
                return;
            }
            onFinished();
        }
    }

    public void pause() {
        throw new UnsupportedOperationException();
    }

    public void resume() {
        throw new UnsupportedOperationException();
    }

    public void setTarget(View view) {
        this.mViewTarget = view;
        setTarget(this.mViewTarget.mRenderNode);
    }

    public void setTarget(Canvas canvas) {
        if (canvas instanceof DisplayListCanvas) {
            setTarget(((DisplayListCanvas) canvas).mNode);
            return;
        }
        throw new IllegalArgumentException("Not a GLES20RecordingCanvas");
    }

    private void setTarget(RenderNode node) {
        checkMutable();
        if (this.mTarget != null) {
            throw new IllegalStateException("Target already set!");
        }
        nSetListener(this.mNativePtr.get(), this);
        this.mTarget = node;
        this.mTarget.addAnimator(this);
    }

    public void setStartValue(float startValue) {
        checkMutable();
        nSetStartValue(this.mNativePtr.get(), startValue);
    }

    public void setStartDelay(long startDelay) {
        checkMutable();
        if (startDelay < 0) {
            throw new IllegalArgumentException("startDelay must be positive; " + startDelay);
        }
        this.mUnscaledStartDelay = startDelay;
        this.mStartDelay = (long) (ValueAnimator.getDurationScale() * ((float) startDelay));
    }

    public long getStartDelay() {
        return this.mUnscaledStartDelay;
    }

    public RenderNodeAnimator setDuration(long duration) {
        checkMutable();
        if (duration < 0) {
            throw new IllegalArgumentException("duration must be positive; " + duration);
        }
        this.mUnscaledDuration = duration;
        nSetDuration(this.mNativePtr.get(), (long) (((float) duration) * ValueAnimator.getDurationScale()));
        return this;
    }

    public long getDuration() {
        return this.mUnscaledDuration;
    }

    public long getTotalDuration() {
        return this.mUnscaledDuration + this.mUnscaledStartDelay;
    }

    public boolean isRunning() {
        return this.mState == 1 || this.mState == 2;
    }

    public boolean isStarted() {
        return this.mState != 0;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        checkMutable();
        this.mInterpolator = interpolator;
    }

    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    protected void onFinished() {
        if (this.mState == 0) {
            releaseNativePtr();
            return;
        }
        if (this.mState == 1) {
            getHelper().removeDelayedAnimation(this);
            notifyStartListeners();
        }
        this.mState = 3;
        ArrayList<AnimatorListener> listeners = cloneListeners();
        int numListeners = listeners == null ? 0 : listeners.size();
        for (int i = 0; i < numListeners; i++) {
            ((AnimatorListener) listeners.get(i)).onAnimationEnd(this);
        }
        releaseNativePtr();
    }

    private void releaseNativePtr() {
        if (this.mNativePtr != null) {
            this.mNativePtr.release();
            this.mNativePtr = null;
        }
    }

    private ArrayList<AnimatorListener> cloneListeners() {
        ArrayList<AnimatorListener> listeners = getListeners();
        if (listeners != null) {
            return (ArrayList) listeners.clone();
        }
        return listeners;
    }

    long getNativeAnimator() {
        return this.mNativePtr.get();
    }

    private boolean processDelayed(long frameTimeMs) {
        if (this.mStartTime == 0) {
            this.mStartTime = frameTimeMs;
        } else if (frameTimeMs - this.mStartTime >= this.mStartDelay) {
            doStart();
            return true;
        }
        return false;
    }

    private static DelayedAnimationHelper getHelper() {
        DelayedAnimationHelper helper = (DelayedAnimationHelper) sAnimationHelper.get();
        if (helper != null) {
            return helper;
        }
        helper = new DelayedAnimationHelper();
        sAnimationHelper.set(helper);
        return helper;
    }

    private static void callOnFinished(RenderNodeAnimator animator) {
        animator.onFinished();
    }

    public Animator clone() {
        throw new IllegalStateException("Cannot clone this animator");
    }

    public void setAllowRunningAsynchronously(boolean mayRunAsync) {
        checkMutable();
        nSetAllowRunningAsync(this.mNativePtr.get(), mayRunAsync);
    }
}

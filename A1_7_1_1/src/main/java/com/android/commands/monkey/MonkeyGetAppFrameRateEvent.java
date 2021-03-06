package com.android.commands.monkey;

import android.app.IActivityManager;
import android.util.Log;
import android.view.IWindowManager;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class MonkeyGetAppFrameRateEvent extends MonkeyEvent {
    private static final String LOG_FILE = null;
    private static final Pattern NO_OF_FRAMES_PATTERN = null;
    private static final String TAG = "MonkeyGetAppFrameRateEvent";
    private static String sActivityName;
    private static float sDuration;
    private static int sEndFrameNo;
    private static long sEndTime;
    private static int sStartFrameNo;
    private static long sStartTime;
    private static String sTestCaseName;
    private String GET_APP_FRAMERATE_TMPL;
    private String mStatus;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 00e9 in method: com.android.commands.monkey.MonkeyGetAppFrameRateEvent.<clinit>():void, dex: 
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        Caused by: java.lang.IllegalArgumentException: bogus opcode: 00e9
        	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1227)
        	at com.android.dx.io.OpcodeInfo.getName(OpcodeInfo.java:1234)
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:581)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:74)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:104)
        	... 5 more
        */
    static {
        /*
        // Can't load method instructions: Load method exception: bogus opcode: 00e9 in method: com.android.commands.monkey.MonkeyGetAppFrameRateEvent.<clinit>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.commands.monkey.MonkeyGetAppFrameRateEvent.<clinit>():void");
    }

    public MonkeyGetAppFrameRateEvent(String status, String activityName, String testCaseName) {
        super(4);
        this.GET_APP_FRAMERATE_TMPL = "dumpsys gfxinfo %s";
        this.mStatus = status;
        sActivityName = activityName;
        sTestCaseName = testCaseName;
    }

    public MonkeyGetAppFrameRateEvent(String status, String activityName) {
        super(4);
        this.GET_APP_FRAMERATE_TMPL = "dumpsys gfxinfo %s";
        this.mStatus = status;
        sActivityName = activityName;
    }

    public MonkeyGetAppFrameRateEvent(String status) {
        super(4);
        this.GET_APP_FRAMERATE_TMPL = "dumpsys gfxinfo %s";
        this.mStatus = status;
    }

    private float getAverageFrameRate(int totalNumberOfFrame, float duration) {
        if (duration > 0.0f) {
            return ((float) totalNumberOfFrame) / duration;
        }
        return 0.0f;
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x007e A:{SYNTHETIC, Splitter: B:15:0x007e} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00a5 A:{SYNTHETIC, Splitter: B:21:0x00a5} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeAverageFrameRate() {
        IOException e;
        Throwable th;
        FileWriter writer = null;
        try {
            Log.w(TAG, "file: " + LOG_FILE);
            FileWriter writer2 = new FileWriter(LOG_FILE, true);
            try {
                float avgFrameRate = getAverageFrameRate(sEndFrameNo - sStartFrameNo, sDuration);
                Object[] objArr = new Object[2];
                objArr[0] = sTestCaseName;
                objArr[1] = Float.valueOf(avgFrameRate);
                writer2.write(String.format("%s:%.2f\n", objArr));
                if (writer2 != null) {
                    try {
                        writer2.close();
                    } catch (IOException e2) {
                        Log.e(TAG, "IOException " + e2.toString());
                    }
                }
                writer = writer2;
            } catch (IOException e3) {
                e2 = e3;
                writer = writer2;
                try {
                    Log.w(TAG, "Can't write sdcard log file", e2);
                    if (writer == null) {
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e22) {
                            Log.e(TAG, "IOException " + e22.toString());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                writer = writer2;
                if (writer != null) {
                }
                throw th;
            }
        } catch (IOException e4) {
            e22 = e4;
            Log.w(TAG, "Can't write sdcard log file", e22);
            if (writer == null) {
                try {
                    writer.close();
                } catch (IOException e222) {
                    Log.e(TAG, "IOException " + e222.toString());
                }
            }
        }
    }

    private String getNumberOfFrames(BufferedReader reader) throws IOException {
        Matcher m;
        do {
            String line = reader.readLine();
            if (line == null) {
                return null;
            }
            m = NO_OF_FRAMES_PATTERN.matcher(line);
        } while (!m.matches());
        return m.group(1);
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x00c7 A:{SYNTHETIC, Splitter: B:29:0x00c7} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00cc A:{Catch:{ IOException -> 0x00d0 }} */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00e9 A:{SYNTHETIC, Splitter: B:39:0x00e9} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00ee A:{Catch:{ IOException -> 0x00f2 }} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int injectEvent(IWindowManager iwm, IActivityManager iam, int verbose) {
        Exception e;
        Throwable th;
        Process process = null;
        BufferedReader result = null;
        String str = this.GET_APP_FRAMERATE_TMPL;
        Object[] objArr = new Object[1];
        objArr[0] = sActivityName;
        String cmd = String.format(str, objArr);
        try {
            process = Runtime.getRuntime().exec(cmd);
            int status = process.waitFor();
            if (status != 0) {
                PrintStream printStream = System.err;
                Object[] objArr2 = new Object[2];
                objArr2[0] = cmd;
                objArr2[1] = Integer.valueOf(status);
                printStream.println(String.format("// Shell command %s status was %s", objArr2));
            }
            BufferedReader result2 = new BufferedReader(new InputStreamReader(process.getInputStream()));
            try {
                String output = getNumberOfFrames(result2);
                if (output != null) {
                    if ("start".equals(this.mStatus)) {
                        sStartFrameNo = Integer.parseInt(output);
                        sStartTime = System.currentTimeMillis();
                    } else if ("end".equals(this.mStatus)) {
                        sEndFrameNo = Integer.parseInt(output);
                        sEndTime = System.currentTimeMillis();
                        sDuration = (float) (((double) (sEndTime - sStartTime)) / 1000.0d);
                        writeAverageFrameRate();
                    }
                }
                if (result2 != null) {
                    try {
                        result2.close();
                    } catch (IOException e2) {
                        System.err.println(e2.toString());
                    }
                }
                if (process != null) {
                    process.destroy();
                }
                result = result2;
            } catch (Exception e3) {
                e = e3;
                result = result2;
                try {
                    System.err.println("// Exception from " + cmd + ":");
                    System.err.println(e.toString());
                    if (result != null) {
                    }
                    if (process != null) {
                    }
                    return 1;
                } catch (Throwable th2) {
                    th = th2;
                    if (result != null) {
                        try {
                            result.close();
                        } catch (IOException e22) {
                            System.err.println(e22.toString());
                            throw th;
                        }
                    }
                    if (process != null) {
                        process.destroy();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                result = result2;
                if (result != null) {
                }
                if (process != null) {
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            System.err.println("// Exception from " + cmd + ":");
            System.err.println(e.toString());
            if (result != null) {
                try {
                    result.close();
                } catch (IOException e222) {
                    System.err.println(e222.toString());
                }
            }
            if (process != null) {
                process.destroy();
            }
            return 1;
        }
        return 1;
    }
}

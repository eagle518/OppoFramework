package com.android.server.media;

import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManagerNative;
import android.media.session.MediaSession;
import android.os.RemoteException;
import android.util.Log;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
class MediaSessionStack {
    private static final int[] ALWAYS_PRIORITY_STATES = null;
    private static final int[] TRANSITION_PRIORITY_STATES = null;
    private ArrayList<MediaSessionRecord> mCachedActiveList;
    private MediaSessionRecord mCachedButtonReceiver;
    private MediaSessionRecord mCachedDefault;
    private ArrayList<MediaSessionRecord> mCachedTransportControlList;
    private MediaSessionRecord mCachedVolumeDefault;
    private MediaSessionRecord mGlobalPrioritySession;
    private MediaSessionRecord mLastInterestingRecord;
    private final ArrayList<MediaSessionRecord> mSessions;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 0073 in method: com.android.server.media.MediaSessionStack.<clinit>():void, dex: 
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
        // Can't load method instructions: Load method exception: bogus opcode: 0073 in method: com.android.server.media.MediaSessionStack.<clinit>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.MediaSessionStack.<clinit>():void");
    }

    MediaSessionStack() {
        this.mSessions = new ArrayList();
    }

    private static boolean isFromMostRecentApp(MediaSessionRecord record) {
        try {
            List<RecentTaskInfo> tasks = ActivityManagerNative.getDefault().getRecentTasks(1, 15, record.getUserId()).getList();
            if (!(tasks == null || tasks.isEmpty())) {
                RecentTaskInfo recentTask = (RecentTaskInfo) tasks.get(0);
                if (recentTask.userId == record.getUserId() && recentTask.baseIntent != null) {
                    return recentTask.baseIntent.getComponent().getPackageName().equals(record.getPackageName());
                }
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public void addSession(MediaSessionRecord record, boolean fromForegroundUser) {
        this.mSessions.add(record);
        clearCache();
        if (fromForegroundUser && isFromMostRecentApp(record)) {
            this.mLastInterestingRecord = record;
        }
    }

    public void removeSession(MediaSessionRecord record) {
        this.mSessions.remove(record);
        if (record == this.mGlobalPrioritySession) {
            this.mGlobalPrioritySession = null;
        }
        clearCache();
    }

    public boolean onPlaystateChange(MediaSessionRecord record, int oldState, int newState) {
        if (!shouldUpdatePriority(oldState, newState) || this.mSessions.get(0) == record) {
            if (!MediaSession.isActiveState(newState)) {
                this.mCachedVolumeDefault = null;
            }
            return false;
        }
        this.mSessions.remove(record);
        this.mSessions.add(0, record);
        clearCache();
        this.mLastInterestingRecord = record;
        return true;
    }

    public void onSessionStateChange(MediaSessionRecord record) {
        if ((record.getFlags() & 65536) != 0) {
            this.mGlobalPrioritySession = record;
        }
        clearCache();
    }

    public ArrayList<MediaSessionRecord> getActiveSessions(int userId) {
        if (this.mCachedActiveList == null) {
            this.mCachedActiveList = getPriorityListLocked(true, 0, userId);
        }
        return this.mCachedActiveList;
    }

    public ArrayList<MediaSessionRecord> getTransportControlSessions(int userId) {
        if (this.mCachedTransportControlList == null) {
            this.mCachedTransportControlList = getPriorityListLocked(true, 2, userId);
        }
        return this.mCachedTransportControlList;
    }

    public MediaSessionRecord getDefaultSession(int userId) {
        if (this.mCachedDefault != null) {
            return this.mCachedDefault;
        }
        ArrayList<MediaSessionRecord> records = getPriorityListLocked(true, 0, userId);
        if (records.size() > 0) {
            return (MediaSessionRecord) records.get(0);
        }
        return null;
    }

    public MediaSessionRecord getDefaultMediaButtonSession(List<Integer> userIdList, boolean includeNotPlaying) {
        if (this.mGlobalPrioritySession != null && this.mGlobalPrioritySession.isActive()) {
            return this.mGlobalPrioritySession;
        }
        if (this.mCachedButtonReceiver != null) {
            return this.mCachedButtonReceiver;
        }
        ArrayList<MediaSessionRecord> records = getPriorityListLocked(true, 1, (List) userIdList);
        if (records.size() > 0) {
            MediaSessionRecord record = (MediaSessionRecord) records.get(0);
            if (record.isPlaybackActive(false)) {
                this.mLastInterestingRecord = record;
                this.mCachedButtonReceiver = record;
            } else if (this.mLastInterestingRecord != null) {
                if (!records.contains(this.mLastInterestingRecord)) {
                    for (int i = records.size() - 1; i >= 0; i--) {
                        MediaSessionRecord findRecord = (MediaSessionRecord) records.get(i);
                        if (findRecord != null && findRecord.getPackageName().equals(this.mLastInterestingRecord.getPackageName())) {
                            this.mLastInterestingRecord = findRecord;
                            Log.d("MediaSessionStack", "find actual mLastInterestingRecord in array records");
                            break;
                        }
                    }
                }
                if (records.contains(this.mLastInterestingRecord)) {
                    this.mCachedButtonReceiver = this.mLastInterestingRecord;
                } else {
                    this.mLastInterestingRecord = null;
                }
            }
            if (includeNotPlaying && this.mCachedButtonReceiver == null) {
                this.mCachedButtonReceiver = record;
            }
        }
        return this.mCachedButtonReceiver;
    }

    public MediaSessionRecord getDefaultVolumeSession(List<Integer> userIdList) {
        if (this.mGlobalPrioritySession != null && this.mGlobalPrioritySession.isActive()) {
            return this.mGlobalPrioritySession;
        }
        if (this.mCachedVolumeDefault != null) {
            return this.mCachedVolumeDefault;
        }
        ArrayList<MediaSessionRecord> records = getPriorityListLocked(true, 0, (List) userIdList);
        int size = records.size();
        for (int i = 0; i < size; i++) {
            MediaSessionRecord record = (MediaSessionRecord) records.get(i);
            if (record.isPlaybackActive(false)) {
                this.mCachedVolumeDefault = record;
                return record;
            }
        }
        return null;
    }

    public MediaSessionRecord getDefaultRemoteSession(int userId) {
        ArrayList<MediaSessionRecord> records = getPriorityListLocked(true, 0, userId);
        int size = records.size();
        for (int i = 0; i < size; i++) {
            MediaSessionRecord record = (MediaSessionRecord) records.get(i);
            if (record.getPlaybackType() == 2) {
                return record;
            }
        }
        return null;
    }

    public boolean isGlobalPriorityActive() {
        return this.mGlobalPrioritySession == null ? false : this.mGlobalPrioritySession.isActive();
    }

    public void dump(PrintWriter pw, String prefix) {
        ArrayList<MediaSessionRecord> sortedSessions = getPriorityListLocked(false, 0, -1);
        int count = sortedSessions.size();
        pw.println(prefix + "Global priority session is " + this.mGlobalPrioritySession);
        pw.println(prefix + "Sessions Stack - have " + count + " sessions:");
        String indent = prefix + "  ";
        for (int i = 0; i < count; i++) {
            ((MediaSessionRecord) sortedSessions.get(i)).dump(pw, indent);
            pw.println();
        }
    }

    private ArrayList<MediaSessionRecord> getPriorityListLocked(boolean activeOnly, int withFlags, int userId) {
        List userIdList = new ArrayList();
        userIdList.add(Integer.valueOf(userId));
        return getPriorityListLocked(activeOnly, withFlags, userIdList);
    }

    private ArrayList<MediaSessionRecord> getPriorityListLocked(boolean activeOnly, int withFlags, List<Integer> userIdList) {
        ArrayList<MediaSessionRecord> result = new ArrayList();
        int lastLocalIndex = 0;
        int lastActiveIndex = 0;
        int lastPublishedIndex = 0;
        boolean filterUser = !userIdList.contains(Integer.valueOf(-1));
        int size = this.mSessions.size();
        for (int i = 0; i < size; i++) {
            MediaSessionRecord session = (MediaSessionRecord) this.mSessions.get(i);
            if ((!filterUser || userIdList.contains(Integer.valueOf(session.getUserId()))) && (session.getFlags() & ((long) withFlags)) == ((long) withFlags)) {
                if (session.isActive()) {
                    if (session.isSystemPriority()) {
                        result.add(0, session);
                        lastLocalIndex++;
                        lastActiveIndex++;
                        lastPublishedIndex++;
                    } else if (session.isPlaybackActive(true)) {
                        result.add(lastLocalIndex, session);
                        lastLocalIndex++;
                        lastActiveIndex++;
                        lastPublishedIndex++;
                    } else {
                        result.add(lastPublishedIndex, session);
                        lastPublishedIndex++;
                    }
                } else if (!activeOnly) {
                    result.add(session);
                }
            }
        }
        return result;
    }

    private boolean shouldUpdatePriority(int oldState, int newState) {
        if (containsState(newState, ALWAYS_PRIORITY_STATES)) {
            return true;
        }
        if (containsState(oldState, TRANSITION_PRIORITY_STATES) || !containsState(newState, TRANSITION_PRIORITY_STATES)) {
            return false;
        }
        return true;
    }

    private boolean containsState(int state, int[] states) {
        for (int i : states) {
            if (i == state) {
                return true;
            }
        }
        return false;
    }

    private void clearCache() {
        this.mCachedDefault = null;
        this.mCachedVolumeDefault = null;
        this.mCachedButtonReceiver = null;
        this.mCachedActiveList = null;
        this.mCachedTransportControlList = null;
    }
}

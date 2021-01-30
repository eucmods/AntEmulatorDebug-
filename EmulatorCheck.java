package com.coringa.antemulator;

import android.content.Context;
import android.telephony.TelephonyManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Ant Emulator For You Apk
 *Telegram ⤵
 * @CoRingaModzYT
 */
public class EmulatorChecker {
    private static String[] known_device_ids = {"000000000000000",
            "e21833235b6eef10",
            "012345678912345"};
    private static String[] known_imsi_ids = {"310260000000000"
    };
    private static String[] known_pipes = {"/dev/socket/qemud", "/dev/qemu_pipe"};
    private static String[] known_files = {"/system/lib/libc_malloc_debug_qemu.so", "/sys/qemu_trace",
            "/system/bin/qemu-props"};
    private static String[] known_geny_files = {"/dev/socket/genyd", "/dev/socket/baseband_genyd"};
    private static String[] known_qemu_drivers = {"goldfish"};
    private static Property[] known_props = {new Property("init.svc.qemud", null),
            new Property("init.svc.qemu-props", null), new Property("qemu.hw.mainkeys", null),
            new Property("qemu.sf.fake_camera", null), new Property("qemu.sf.lcd_density", null),
            new Property("ro.bootloader", "unknown"), new Property("ro.bootmode", "unknown"),
            new Property("ro.hardware", "goldfish"), new Property("ro.kernel.android.qemud", null),
            new Property("ro.kernel.qemu.gles", null), new Property("ro.kernel.qemu", "1"),
            new Property("ro.product.device", "generic"), new Property("ro.product.model", "sdk"),
            new Property("ro.product.name", "sdk"),
            // Need to double check that an "empty" string ("") returns null
            new Property("ro.serialno", null)};
    
    private static int MIN_PROPERTIES_THRESHOLD = 0x5;
    public static boolean hasPipes() {
        for (String pipe : known_pipes) {
            File qemu_socket = new File(pipe);
            if (qemu_socket.exists()) {
                return true;
            }
        }

        return false;
    }


/*
public void inject(FridaAgent fridaAgent, final String packageName, boolean spawn) {
        if (mInjector == null) {
            throw new RuntimeException("did you forget to call init()?");
        }

        if (!RootManager.getInstance().isProcessRunning(packageName)) {
            spawn = true;
        }

        StringBuilder agent = new StringBuilder(fridaAgent.getWrappedAgent());

        if (!fridaAgent.getInterfaces().isEmpty()) {
            try {
                ApplicationInfo ownAi = fridaAgent.getPackageManager().getApplicationInfo(
                        fridaAgent.getPackageName(), 0);
                String ownApk = ownAi.publicSourceDir;
                ApplicationInfo targetAi = fridaAgent.getPackageManager().getApplicationInfo(packageName, 0);
                String targetPath = new File(targetAi.publicSourceDir).getPath().substring(0,
                        targetAi.publicSourceDir.lastIndexOf("/"));
                if (targetPath.startsWith("/system/")) {
                    RootManager.getInstance().remount("/system", "rw");
                }
                RootManager.getInstance().runCommand("cp " + ownApk + " " + targetPath + "/CMODs.apk");
                RootManager.getInstance().runCommand("chmod 644 " + targetPath + "/CMODs.apk");
                if (targetPath.startsWith("/system/")) {
                    RootManager.getInstance().runCommand("chown root:root " + targetPath + "/CMODs.apk");
                    RootManager.getInstance().remount("/system", "ro");
                } else {
                    RootManager.getInstance().runCommand("chown system:system " + targetPath + "/CMODs.apk");
                }

                agent.append(FridaAgent.sRegisterClassLoaderAgent);

                for (LinkedHashMap.Entry<String, Class<? extends FridaInterface>> entry :
                        fridaAgent.getInterfaces().entrySet()) {
                    agent.append("Java['")
                            .append(entry.getKey())
                            .append("'] = function() {")
                            .append("var defaultClassLoader = Java.classFactory.loader;")
                            .append("Java.classFactory.loader = Java.classFactory['xd_loader'];")
                            .append("var clazz = Java.use('")
                            .append(entry.getValue().getName())
                            .append("').$new();")
                            .append("var args = [];")
                            .append("for (var i=0;i<arguments.length;i++) {")
                            .append("args[i] = arguments[i]")
                            .append("}")
                            .append("clazz.call(Java.array('java.lang.Object', args));")
                            .append("Java.classFactory.loader = defaultClassLoader;")
                            .append("};")
                            .append("\n");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        final File fridaAgentFile = new File(fridaAgent.getFilesDir(), "wrapped_agent.js");
        Utils.writeToFile(fridaAgentFile, agent.toString());
        RootManager.getInstance().runCommand("chmod 777 " + fridaAgentFile.getPath());

        if (spawn) {
            Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
            RootManager.getInstance().killProcessByName(packageName);
            new Thread(() -> {
                long start = System.currentTimeMillis();
                while (!RootManager.getInstance().isProcessRunning(packageName)) {
                    try {
                        Thread.sleep(250);

                        if (System.currentTimeMillis() - start >
                                TimeUnit.SECONDS.toMillis(5)) {
                            throw new RuntimeException("wait timeout for process spawn");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                inject(packageName, fridaAgentFile.getPath());
            }).start();

            if (launchIntent != null) {
                mContext.startActivity(launchIntent);
            } else {
                // are we targeting a system app?
                // systemui does auto-respawn. Let's see further cases
                // todo: handle cases here
            }
        } else {
            inject(packageName, fridaAgentFile.getPath());
        }
    }

    private void inject(String packageName, String agentPath) {
        RootManager.getInstance().runCommand(mInjector.getPath() + " -f " + packageName +
                " -s " + agentPath + " --runtime=v8 -e");
    }
*/
    public static boolean hasQEmuFiles() {
        for (String pipe : known_files) {
            File qemu_file = new File(pipe);
            if (qemu_file.exists()) {
                return true;
            }
        }

        return false;
    }
    public static boolean hasGenyFiles() {
        for (String file : known_geny_files) {
            File geny_file = new File(file);
            if (geny_file.exists()) {
                return true;
            }
        }

        return false;
    }
    public static boolean hasQEmuDrivers() {
        for (File drivers_file : new File[]{new File("/proc/tty/drivers"), new File("/proc/cpuinfo")}) {
            if (drivers_file.exists() && drivers_file.canRead()) {
                byte[] data = new byte[1024];
                try {
                    InputStream is = new FileInputStream(drivers_file);
                    is.read(data);
                    is.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                String driver_data = new String(data);
                for (String known_qemu_driver : EmulatorChecker.known_qemu_drivers) {
                    if (driver_data.indexOf(known_qemu_driver) != -1) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean hasKnownDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId;
        try {
            deviceId = telephonyManager.getDeviceId();
        } catch (Exception e){
            return false;
        }
        for (String known_deviceId : known_device_ids) {
            if (known_deviceId.equalsIgnoreCase(deviceId)) {
                return true;
            }

        }
        return false;
    }

    public static boolean hasKnownImsi(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi;
        try{
            imsi = telephonyManager.getSubscriberId();
        } catch (Exception e){
            return false;
        }

        for (String known_imsi : known_imsi_ids) {
            if (known_imsi.equalsIgnoreCase(imsi)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasEmulatorBuild(Context context) {
        String BOARD = android.os.Build.BOARD;
    String BRAND = android.os.Build.BRAND; 
        String DEVICE = android.os.Build.DEVICE;
        String HARDWARE = android.os.Build.HARDWARE;
        String MODEL = android.os.Build.MODEL;
        String PRODUCT = android.os.Build.PRODUCT;
        if ((BOARD.compareTo("unknown") == 0) /* || (BOOTLOADER.compareTo("unknown") == 0) */
                || (BRAND.compareTo("generic") == 0) || (DEVICE.compareTo("generic") == 0)
                || (MODEL.compareTo("sdk") == 0) || (PRODUCT.compareTo("sdk") == 0)
                || (HARDWARE.compareTo("goldfish") == 0)) {
            return true;
        }
        return false;
    }

    public static boolean isOperatorNameAndroid(Context paramContext) {
        try {
            String szOperatorName = ((TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
            boolean isAndroid = szOperatorName.equalsIgnoreCase("android");
            return isAndroid;
        } catch (Exception e){
            return false;
        }
    }


    public static boolean hasEmulatorAdb() {
        try {
            return DebugChecker.hasAdbInEmulator();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isQEmuEnvDetected(Context context) {
        int count = 0;

        if (EmulatorChecker.hasKnownDeviceId(context)) {
            count++;
        }
        if (EmulatorChecker.isOperatorNameAndroid(context)) {
            count++;
        }
        if (EmulatorChecker.hasKnownImsi(context)) {
            count++;
        }
        if (EmulatorChecker.hasEmulatorBuild(context)) {
            count++;
        }
        if (EmulatorChecker.hasPipes()) {
            count++;
        }
        if (EmulatorChecker.hasQEmuDrivers()) {
            count++;
        }
        if (EmulatorChecker.hasQEmuFiles()) {
            count++;
        }
        if (EmulatorChecker.hasGenyFiles()) {
            count++;
        }
        if (EmulatorChecker.hasEmulatorAdb()) {
            count++;
        }

        return (count >= 2);
    }
}


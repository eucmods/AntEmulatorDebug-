import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkSecurity(); // Verifica todas as proteções ao iniciar o app
    }

    public void checkSecurity() {
        if (isBeingDebugged() || detectFrida() || isRunningInEmulator()) {
            showSecurityAlert(this);
        }
    }

    public boolean isBeingDebugged() {
        return Debug.isDebuggerConnected();
    }

    public boolean detectFrida() {
        String[] suspiciousLibs = { "frida", "xposed", "substrate", "magisk" };
        int i;
        for (i = 0; i < suspiciousLibs.length; i++) {
            try {
                System.loadLibrary(suspiciousLibs[i]);
                return true;
            } catch (UnsatisfiedLinkError ignored) {}
        }
        return false;
    }

    public boolean isRunningInEmulator() {
        String[] knownEmulators = { "Genymotion", "Nox", "BlueStacks", "Emulator" };
        int i;
        String product = Build.PRODUCT.toLowerCase();
        for (i = 0; i < knownEmulators.length; i++) {
            if (product.contains(knownEmulators[i].toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void showSecurityAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("LIAPP ALERT");
        builder.setMessage("A threat has been detected. The application will be terminated soon.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity(); // Fecha o app
            }
        });
        builder.show();
    }
}

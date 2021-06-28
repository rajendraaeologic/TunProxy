package tun.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.os.Build;
import android.text.TextUtils;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Util {
    private static native String jni_getprop(String name);

    public static List<String> getDefaultDNS(Context context) {
        String dns1 = null;
        String dns2 = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network an = cm.getActiveNetwork();
            if (an != null) {
                LinkProperties lp = cm.getLinkProperties(an);
                if (lp != null) {
                    List<InetAddress> dns = lp.getDnsServers();
                    if (dns != null) {
                        if (dns.size() > 0)
                            dns1 = dns.get(0).getHostAddress();
                        if (dns.size() > 1)
                            dns2 = dns.get(1).getHostAddress();
                    }
                }
            }
        } else {
            dns1 = jni_getprop("net.dns1");
            dns2 = jni_getprop("net.dns2");
        }

        List<String> listDns = new ArrayList<>();
        listDns.add(TextUtils.isEmpty(dns1) ? "8.8.8.8" : dns1);
        listDns.add(TextUtils.isEmpty(dns2) ? "8.8.4.4" : dns2);
        return listDns;
    }
}

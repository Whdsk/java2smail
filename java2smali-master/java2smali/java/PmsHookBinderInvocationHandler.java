package cn.wjdiankong.hookpms;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

/**
 * Created by jiangwei1-g on 2016/9/7.
 */
public class PmsHookBinderInvocationHandler implements InvocationHandler{

    private Object base;
    
    //Ӧ����ȷ��ǩ����Ϣ
    private String SIGN;
    private String appPkgName = "";

    public PmsHookBinderInvocationHandler(Object base, String sign, String appPkgName, int hashCode) {
        try {
            this.base = base;
            this.SIGN = sign;
            this.appPkgName = appPkgName;
        } catch (Exception e) {
            Log.d("jw", "error:"+Log.getStackTraceString(e));
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if("getPackageInfo".equals(method.getName())){
        	String pkgName = (String)args[0];
            Integer flag = (Integer)args[1];
            if(flag == PackageManager.GET_SIGNATURES && appPkgName.equals(pkgName)){
            	Signature sign = new Signature(SIGN);
            	Log.i("jw", "sign hashcode:"+sign.hashCode());
            	PackageInfo info = (PackageInfo) method.invoke(base, args);
            	info.signatures[0] = sign;
            	return info;
            }
        }else if("getInstallerPackageName".equals(method.getName())){
        	String pkgName = (String)args[0];
        	if("com.dans.apps.webd".equals(pkgName)){
        		return "com.android.vending";
        	}
        }
        return method.invoke(base, args);
    }

}


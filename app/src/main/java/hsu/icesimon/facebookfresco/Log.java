package hsu.icesimon.facebookfresco;

/**
 * Created by Simon Hsu on 15/4/6.
 */

public class Log {

    private static Mode sMode = Mode.AppNameWithMethod;
    private static boolean sEnabled = true;
    private static String sAppName = "Fresco GridView";

    /**
     * Defines 3 kinds of tag mode
     * AppName -- The tag would be only APP's name
     * AppNameWithClass -- The tag would &lt;appname&gt;-&lt;classname&gt;
     * AppNameWithMethod -- The tag would be &lt;appname&gt;-&lt;classname&gt;:&lt;methodname&gt;
     *
     */
    public static enum Mode
    {
        AppName, AppNameWithClass, AppNameWithMethod
    };

    /**
     * Enable the logger or not.
     * @param enabled
     */
    public static void setEnable(boolean enabled)
    {
        sEnabled = enabled;
    }

    /**
     * Setup logger tag mode
     * @param mode @see Mode
     */
    public static void setMode(Mode mode)
    {
        sMode = mode;
    }

    /**
     * Setup application name
     * @param name
     */
    public static void setAppName(String name)
    {
        sAppName = name;
    }

    public static void i(String message, String s)
    {
        if (sEnabled)
            android.util.Log.i(getTag(), message);
    }

    public static void i(String message, Throwable e)
    {
        if (sEnabled)
            android.util.Log.i(getTag(), message, e);
    }

    public static void e(String message)
    {
        if (sEnabled)
            android.util.Log.e(getTag(), message);
    }

    public static void e(String message, Throwable e)
    {
        if (sEnabled)
            android.util.Log.e(getTag(), message, e);
    }

    public static void d(String message)
    {
        if (sEnabled)
            android.util.Log.d(getTag(), message);
    }

    public static void d(String message, Throwable e)
    {
        if (sEnabled)
            android.util.Log.d(getTag(), message, e);
    }

    public static void v(String message)
    {
        if (sEnabled)
            android.util.Log.v(getTag(), message);
    }

    public static void v(String message, Throwable e)
    {
        if (sEnabled)
            android.util.Log.v(getTag(), message, e);
    }

    public static void w(String message)
    {
        if (sEnabled)
            android.util.Log.w(getTag(), message);
    }

    public static void w(String message, Throwable e)
    {
        if (sEnabled)
            android.util.Log.w(getTag(), message, e);
    }

    @SuppressWarnings("incomplete-switch")
    private static String getTag()
    {
        StackTraceElement[] st = null;
        String[] caller = null;
        switch (sMode)
        {
            case AppNameWithClass:
                st = Thread.currentThread().getStackTrace();
                if (st != null && st.length >= 4)
                {
                    caller = getCallerInfo(st);
                    return sAppName + "-" + caller[0];
                }
                break;
            case AppNameWithMethod:
                st = Thread.currentThread().getStackTrace();
                if (st != null && st.length >= 4)
                {
                    caller = getCallerInfo(st);
                    return sAppName + "-" + caller[0] + ":" + caller[1];
                }
                break;
        }
        return sAppName;
    }

    private static String[] getCallerInfo(StackTraceElement[] st)
    {
        boolean findSelf = false;
        String[] info = new String[]
                { "Unknown", "Unknown" };

        for (StackTraceElement e : st)
        {
            String name = e.getClassName();
            if (!findSelf)
            {
                if (name.equals(Log.class.getName()))
                {
                    findSelf = true;
                }
            } else
            {
                if (!name.equals(Log.class.getName()))
                {
                    String[] s = name.split("\\.");
                    info[0] = s[s.length - 1];
                    info[1] = e.getMethodName();
                    break;
                }
            }
        }

        return info;
    }

}

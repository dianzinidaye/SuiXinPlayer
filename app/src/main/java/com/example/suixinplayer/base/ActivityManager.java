package com.example.suixinplayer.base;

import android.app.Activity;

import java.util.Stack;

public class ActivityManager {

    private ActivityManager(){

    }

    private static class Manager{
        static public ActivityManager activityManager = new ActivityManager();
    }

    public static ActivityManager getInstence(){
        return Manager.activityManager;
    }


    public Stack<Activity> activityStack = new Stack<>();//增删操作多,所以用Stack

    /**
     * 添加统一管理
     * @param activity
     */
    public void attach(Activity activity){
        activityStack.add(activity);
    }


    /**
     * 移除解绑 - 防止内存泄漏
     * @param detachActivity
     */
    public void detach(Activity detachActivity){
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            Activity activity = activityStack.get(i);
            if (activity == detachActivity) {
                activityStack.remove(i);
                i--;
                size--;
            }
        }
    }

    /**
     * 关闭当前的 Activity(该方法针对能获取到Activity对象)
     * @param finishActivity
     */
    public void finish(Activity finishActivity){
        // 这样去移除有没有问题
        /*for (Activity activity : mActivities) {
            if(activity == finishActivity){
                mActivities.remove(activity);
                activity.finish();
            }
        }
*/
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            Activity activity = activityStack.get(i);
            if (activity == finishActivity) {
                activityStack.remove(i);
                activity.finish();
                i--;
                size--;
            }
        }
    }

    /**
     * 根据Activity的类名关闭 Activity(该方法针对不能获取到Activity对象)
     */
    public void finish(Class<? extends Activity> activityClass){
        // 这样 去移除有没有问题
        /*for (Activity activity : mActivities) {
            if(activity.getClass().getCanonicalName().equals(activityClass.getCanonicalName())){
                mActivities.remove(activity);
                activity.finish();
            }
        }*/

        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            Activity activity = activityStack.get(i);
            if (activity.getClass().getCanonicalName().equals(activityClass.getCanonicalName())) {
                activityStack.remove(i);
                activity.finish();
                i--;
                size--;
            }
        }
    }


    /**
     * 获取当前的Activity（最前面）
     * @return
     */
    public Activity currentActivity(){
        return activityStack.lastElement();
    }

    /**
     * 退出整个应用
     */
    public void exitApplication(){
        for (Activity activity:activityStack){
            activity.finish();
        }
    }
}

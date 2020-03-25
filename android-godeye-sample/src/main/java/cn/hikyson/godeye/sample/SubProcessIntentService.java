package cn.hikyson.godeye.sample;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;


public class SubProcessIntentService extends IntentService {

    private static final String ACTION_FOO = "cn.hikyson.godeye.sample.action.FOO";

    public SubProcessIntentService() {
        super("SubProcessIntentService");
    }

    public static void startActionFoo(Context context) {
        Intent intent = new Intent(context, SubProcessIntentService.class);
        intent.setAction(ACTION_FOO);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                handleActionFoo();
            }
        }
    }

    private void handleActionFoo() {
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

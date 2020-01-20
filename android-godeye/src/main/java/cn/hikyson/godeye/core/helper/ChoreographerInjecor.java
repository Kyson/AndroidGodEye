package cn.hikyson.godeye.core.helper;

import android.view.Choreographer;

public class ChoreographerInjecor {
    public interface ChoreographerProvider {
        public Choreographer getChoreographer();
    }

    private static ChoreographerProvider sChoreographerProvider = new ChoreographerProvider() {
        @Override
        public Choreographer getChoreographer() {
            return Choreographer.getInstance();
        }
    };

    public static void setChoreographerProvider(ChoreographerProvider choreographerProvider) {
        sChoreographerProvider = choreographerProvider;
    }

    public static ChoreographerProvider getChoreographerProvider() {
        return sChoreographerProvider;
    }
}

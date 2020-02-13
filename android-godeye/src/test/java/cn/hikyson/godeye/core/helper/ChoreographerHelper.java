package cn.hikyson.godeye.core.helper;

import android.view.Choreographer;

import org.mockito.Mockito;

public class ChoreographerHelper {

    public static void setup() {
        Choreographer choreographer = Mockito.spy(Choreographer.getInstance());
        ChoreographerInjecor.setChoreographerProvider(() -> choreographer);
        Mockito.doAnswer(invocation -> null).when(choreographer).postFrameCallback(Mockito.any());
    }

    public static void teardown() {
        ChoreographerInjecor.setChoreographerProvider(null);
    }
}

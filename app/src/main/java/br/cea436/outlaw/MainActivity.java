package br.cea436.outlaw;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TouchScreenView view = new TouchScreenView(this);
        setContentView(view);
    }
}

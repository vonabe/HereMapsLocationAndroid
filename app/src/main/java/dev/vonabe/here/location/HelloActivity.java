package dev.vonabe.here.location;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class HelloActivity extends AppCompatActivity {

    private TextView txtHello = null;
    private Animation animation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        View view = findViewById(R.id.constraint_layout);
        view.setOnTouchListener(new OnSwipeTouchListener(HelloActivity.this){
            public void onSwipeLeft() {
                changeActivity();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }

        final String[] message = {"Hi", "I work in the FSB.", "I have a present for you ...", "This beautiful bottle of champagne.", "Drag peka so we can give you a present."};

        txtHello = findViewById(R.id.textView);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        txtHello.setText(message[0]);
        txtHello.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            int counter = 1, repeat = 0;
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation anim) {
                repeat++;
                if(repeat%2!=0) {
                    if (counter >= message.length) {counter = 0;}
                    txtHello.setText(message[counter]);
                    animation.setDuration(500 + 100 * ((counter == message.length) ? message[counter].length()*2 : message[counter].length()));
                    counter++;
                    txtHello.startAnimation(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning back to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT); // Use START if using right - to - left locale
        slideTransition.setDuration(1000);

        getWindow().setReenterTransition(slideTransition);  // When MainActivity Re-enter the Screen
        getWindow().setExitTransition(slideTransition);     // When MainActivity Exits the Screen

        // For overlap of Re Entering Activity - MainActivity.java and Exiting TransitionActivity.java
        getWindow().setAllowReturnTransitionOverlap(false);
    }

    public void changeActivity(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            final Intent intent = new Intent(HelloActivity.this, MainActivity.class);
            startActivity(intent, options.toBundle());
        }
    }

}

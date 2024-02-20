package edu.fvtc.galleryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    public static final String TAG = "MainActivity";

    // Set Arrays
    Location[] locations = {
            new Location("La Fortuna", "Text"),
            new Location("Monteverde", "Text"),
            new Location("Manuel Antonio National Park", "Text")
    };

    int[][] imgs = {
            {R.drawable.fortuna1, R.drawable.fortuna2},
            {R.drawable.monteverde1, R.drawable.monteverde2},
            {R.drawable.manuelantonio1, R.drawable.manuelantonio2}
    };

    int[] textfiles = {R.raw.fortuna,
            R.raw.monteverde,
            R.raw.manuelantonio
    };


    // Initialize Variables
    int cardNo = 0;
    boolean isFront = true;
    ImageView imgCard;
    TextView tvCard;
    View cvCard;

    GestureDetector gestureDetector;


    // End of initialization code ----------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imgCard = findViewById(R.id.header_image);
        tvCard = findViewById(R.id.tvBody);
        cvCard = findViewById(R.id.cvCard);


        updateToNextCard();

        gestureDetector = new GestureDetector(this, this);

        Log.d(TAG, "onCreate: Complete");

    }

    // Important Methods
    private void updateToNextCard() {
        locations[cardNo].setDescription(readFile(textfiles[cardNo]));

        if (isFront) {
            // Front side of the card
            imgCard.setVisibility(View.VISIBLE);
            imgCard.setImageResource(imgs[cardNo][0]);
            tvCard.setText(locations[cardNo].getName());
        } else {
            // Back side of the card
            imgCard.setVisibility(View.VISIBLE);
            imgCard.setImageResource(imgs[cardNo][1]);
            tvCard.setText(locations[cardNo].getDescription());
        }
    }

    private String readFile(int fileId) {
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        StringBuffer stringBuffer;

        try{
            inputStream = getResources().openRawResource(fileId);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            stringBuffer = new StringBuffer();

            String data;

            while((data = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(data).append("\n");
            }

            // Clean up objects
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();

            Log.d(TAG, "readFile: " + stringBuffer.toString());
            return stringBuffer.toString();

        }
        catch(Exception e)
        {
            Log.d(TAG, "readFile: " + e.getMessage());
            e.printStackTrace();
            return e.getMessage();
        }
    }

    // One of the those that I have to remember!!!!!!
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        Log.d(TAG, "onTouchEvent: Touch received");
        return gestureDetector.onTouchEvent(motionEvent);
    }


    // Gesture Detection Methods
    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        Log.d(TAG, "onDown: ");
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {
        Log.d(TAG, "onShowPress: ");
    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        Log.d(TAG, "onSingleTapUp: ");

        String message;

        try {
            if(isFront){
                // Go to the back
                cvCard.animate()
                        .setDuration(500)  // Duration of the animation
                        .rotationYBy(180)  // Rotate the card
                        .withEndAction(new Runnable(){
                            @Override
                            public void run() {
                                cvCard.setRotationY(0);  // Reset rotation
                                updateToNextCard(); // update the card content
                            }
                        });
                message = "Go to back";
            }
            else{
                // got to front
                cvCard.animate()
                        .setDuration(500)  // Duration of the animation
                        .rotationYBy(-180)  // Rotate the card
                        .withEndAction(new Runnable(){
                            @Override
                            public void run() {
                                cvCard.setRotationY(0);  // Reset rotation
                                updateToNextCard(); // update the card content
                            }
                        });
                message = "Go to front";
            }

            isFront = !isFront;
            Log.d(TAG, "onSingleTapUp: " + message);
        }
        catch(Exception e)
        {
            Log.e(TAG, "onSingleTapUp: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        //Log.d(TAG, "onScroll: ");
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {
        Log.d(TAG, "onLongPress: ");
    }

    @Override
    public boolean onFling(@Nullable MotionEvent motionEvent1,
                           @NonNull MotionEvent motionEvent2,
                           float velocityX,
                           float velocityY) {
        Log.d(TAG, "onFling: ");

        int numCards = locations.length;

        try{
            // Decide which direction I am flinging.
            int x1 = (int) (motionEvent1 != null ? motionEvent1.getX() : 0);
            int x2 = (int)motionEvent2.getX();

            if(x1 < x2)
            {
                Animation move = AnimationUtils.loadAnimation(this, R.anim.moveright);
                move.setAnimationListener(new AnimationListener());
                cvCard.startAnimation(move); // Move the card instead of the items individually
                // Swipe right
                Log.d(TAG, "onFling: Right");
                cardNo = (cardNo - 1 + numCards) % numCards;
            }
            else {
                Animation move = AnimationUtils.loadAnimation(this, R.anim.moveleft);
                move.setAnimationListener(new AnimationListener());
                cvCard.startAnimation(move);
                // Swipe left
                Log.d(TAG, "onFling: Left");
                cardNo = (cardNo + 1) % numCards;
            }
            //updateToNextCard();

        }
        catch(Exception ex)
        {
            Log.e(TAG, "onFling: " + ex.getMessage());
            ex.printStackTrace();
        }
        return true;
    }

    private class AnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.d(TAG, "onAnimationEnd: ");
            updateToNextCard();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
    // End of Gesture detection methods -------------------------------------------------------
}
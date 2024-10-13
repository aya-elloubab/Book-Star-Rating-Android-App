package com.example.starratingsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);  // Associer le fichier XML à l'activité
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bar_color));
        }
        logo = findViewById(R.id.logo);

        // Animation 1 : Rotation (360 degrés)
        ObjectAnimator rotation = ObjectAnimator.ofFloat(logo, "rotation", 0f, 360f);
        rotation.setDuration(2000);
        // Animation 2 : Réduire l'échelle (scaleX et scaleY à 50%)
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(logo, "scaleX", 1f, 0.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(logo, "scaleY", 1f, 0.5f);
        scaleX.setDuration(3000);
        scaleY.setDuration(3000);
        // Animation 3 : Translation (descendre de 1000px)
        ObjectAnimator translationY = ObjectAnimator.ofFloat(logo, "translationY", 0f, 1000f);
        translationY.setDuration(2000);
        // Animation 4 : Fading (diminuer l'opacité de 1 à 0)
        ObjectAnimator alpha = ObjectAnimator.ofFloat(logo, "alpha", 1f, 0f);
        alpha.setDuration(6000);
        // Utilisation d'AnimatorSet pour combiner et enchaîner les animations
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotation)
                .with(scaleX).with(scaleY)
                .before(translationY)
                .before(alpha);
        // Démarrer la séquence d'animations
        animatorSet.start();

        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    // Pause de 5 secondes
                    sleep(5000);
                    Intent intent = new Intent(SplashActivity.this, ListActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}

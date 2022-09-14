package com.example.bora_adesso

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var imageview: ImageView

    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        imageview = findViewById(R.id.imageview11)

        val animasyon1 = AnimationUtils.loadAnimation(this, R.anim.animation1)

        imageview.animation = animasyon1

        handler = Handler()
        handler.postDelayed({

            val intent = Intent(this, MainActivity4::class.java)
            startActivity(intent)
            finish()
        }, 2500)


    }
}
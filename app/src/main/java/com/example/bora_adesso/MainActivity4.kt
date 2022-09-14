package com.example.bora_adesso

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bora_adesso.databinding.ActivityMain4Binding

class MainActivity4 : AppCompatActivity() {

    private lateinit var binding: ActivityMain4Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button41.setOnClickListener {
            click()
        }

    }


    private fun click() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
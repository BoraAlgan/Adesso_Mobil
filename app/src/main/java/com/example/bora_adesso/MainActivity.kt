package com.example.bora_adesso

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.bora_adesso.databinding.ActivityMainBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textview11.setTypeface(null, Typeface.ITALIC)

        binding.button11.setOnClickListener {
            run {
                if (binding.tiet11.text!!.isEmpty() || binding.tiet12.text!!.isEmpty() || binding.tiet13.text!!.isEmpty() || binding.tiet14.text!!.isEmpty() || !binding.cbox1.isChecked ) {

                    binding.button11.isEnabled = false

                    val dialogBuilder = AlertDialog.Builder(this)
                    dialogBuilder.setMessage("Bazı Kısımları Eksik Bıraktınız")
                        .setCancelable(false)
                        .setNegativeButton("Tamam", DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                        })
                    val alert = dialogBuilder.create()
                    alert.setTitle("Uyarı!")
                    alert.show()

                    binding.button11.isEnabled = true
                } else {
                    click()
                }
            }
        }



        binding.tiet11.doOnTextChanged() { text, start, before, count ->
            if (binding.tiet11.text!!.isNotEmpty())
                binding.button11.isEnabled = true
            else {
                binding.button11.isEnabled = false
                binding.tiet11.error = "Burası Boş Bırakılamaz"
            }
        }
        binding.tiet12.doOnTextChanged() { text, start, before, count ->
            if (binding.tiet12.text!!.isNotEmpty())
                binding.button11.isEnabled = true
            else {
                binding.button11.isEnabled = false
                binding.tiet12.error = "Burası Boş Bırakılamaz"
            }
        }
        binding.tiet13.doOnTextChanged() { text, start, before, count ->
            if (binding.tiet13.text!!.isNotEmpty())
                binding.button11.isEnabled = true
            else {
                binding.button11.isEnabled = false
                binding.tiet13.error = "Burası Boş Bırakılamaz"
            }
        }
        binding.tiet14.doOnTextChanged() { text, start, before, count ->
            if (binding.tiet14.text!!.isNotEmpty())
                binding.button11.isEnabled = true
            else {
                binding.button11.isEnabled = false
                binding.tiet14.error = "Burası Boş Bırakılamaz"
            }
        }

        binding.tiet11.doOnTextChanged() { text, start, before, count ->
            binding.button11.isEnabled = NAMEValidate(binding.tiet11.text.toString())
        }
        binding.tiet12.doOnTextChanged() { text, start, before, count ->
            if (TCKNValidate(binding.tiet12.text.toString()))
                binding.button11.isEnabled = true
            else {
                binding.button11.isEnabled = false
                binding.tiet12.error = "Hatalı veya Eksik"
            }
        }
        binding.tiet13.doOnTextChanged() { text, start, before, count ->
            if (DGValidate(binding.tiet13.text.toString()))
                binding.button11.isEnabled = true
            else {
                binding.button11.isEnabled = false
                binding.tiet13.error = "Hatalı veya Eksik"
            }
        }
        binding.tiet14.doOnTextChanged() { text, start, before, count ->
            if (PHONEValidate(binding.tiet14.text.toString()))
                binding.button11.isEnabled = true
            else {
                binding.button11.isEnabled = false
                binding.tiet14.error = "Hatalı veya Eksik"
            }
        }
        binding.cbox1.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.button11.isEnabled = isChecked
        }

    }

    private fun click() {
        val intent = Intent(this, MainActivity2::class.java)
        intent.putExtra("name", binding.tiet11.text.toString())
        startActivity(intent)
    }

    private fun NAMEValidate(text: String?): Boolean {
        var p: Pattern =
            Pattern.compile("^(?=.{2,20}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$")
        var m: Matcher = p.matcher(text)
        return m.matches()
    }

    private fun TCKNValidate(text: String?): Boolean {
        var p: Pattern = Pattern.compile("^[1-9][0-9]{9}[02468]\$")
        var m: Matcher = p.matcher(text)
        return m.matches()
    }

    private fun DGValidate(text: String?): Boolean {
        var p: Pattern =
            Pattern.compile("^([1-9]|[12][0-9]|3[01])(|/|\\.|-|\\s)?(0[1-9]|1[12])\\2(19[0-9]{2}|200[0-9]|201[0-8])\$")
        var m: Matcher = p.matcher(text)
        return m.matches()
    }

    private fun PHONEValidate(text: String?): Boolean {
        var p: Pattern =
            Pattern.compile("^(5)([0-9]{2})\\s?([0-9]{3})\\s?([0-9]{2})\\s?([0-9]{2})\$")
        var m: Matcher = p.matcher(text)
        return m.matches()
    }

}



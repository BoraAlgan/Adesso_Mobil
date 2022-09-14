package com.example.bora_adesso

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Typeface
import android.media.ExifInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.bora_adesso.databinding.ActivityMain2Binding
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector

    private var imageCapture: ImageCapture? = null
    private lateinit var imgCaptureExecutor: ExecutorService

    private var controlcam1 = false
    private var controlcam2 = false
    private var controlcams = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val name = intent.getStringExtra("name")
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor()

        binding.textview21.setTypeface(null, Typeface.ITALIC)


        if (isPermissionGranted()) {  //granted perrmission olursa kamerayı başlat
            startCamera()
        } else {
            requestPermissions(
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.switchCameraBtn.setOnClickListener {

            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA

            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }

            startCamera()
        }

        binding.takePhotoBtn.setOnClickListener {
            takePhoto()
        }
        binding.button21.setOnClickListener {
            if (controlcams && controlcam2) {
                click()
            } else {
                val snack = Snackbar.make(
                    it,
                    "Lütfen kimlik fotoğraflarınızı tamamlayınız.",
                    Snackbar.LENGTH_SHORT
                )
                snack.show()
            }

        }
        binding.textview21.text =
            "Hoş Geldiniz $name.\nLütfen kimliğinizin arkalı önlü \nfotoğrafını çekiniz."
    }

    private fun click() {
        val intent = Intent(this, MainActivity3::class.java)
        startActivity(intent)
    }

    private fun setRotateImage(filePath: String) {
        var exifInterface: ExifInterface? = null

        try {
            exifInterface = ExifInterface(filePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val orientation = exifInterface?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val matrix = Matrix()

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 ->
                matrix.setRotate(90f)

            ExifInterface.ORIENTATION_ROTATE_180 ->
                matrix.setRotate(180f)

            ExifInterface.ORIENTATION_ROTATE_270 ->
                matrix.setRotate(270f)

        }

        val myBitmap = BitmapFactory.decodeFile(filePath)

        val rotatedBitmap =
            Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.width, myBitmap.height, matrix, true)
        if (!controlcam1) {
            binding.capturedImageView.setImageBitmap(rotatedBitmap)
            controlcams = true
            controlcam1 = true
        } else {
            binding.capturedImageView2.setImageBitmap(rotatedBitmap)
            controlcams = true
            controlcam2 = true
            controlcam1 = false
        }
        binding.imageview21.setOnClickListener {
            binding.capturedImageView.setImageBitmap(null)
            binding.capturedImageView2.setImageBitmap(null)
            controlcam1 = false
            controlcam2 = false
            controlcams = false
        }

    }


    private fun startCamera() {
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
        }
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.d(TAG, "Use case binding failed")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        imageCapture?.let {

            val fileName = "JPEG_${System.currentTimeMillis()}"
            val file = File(externalMediaDirs[0], fileName)

            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                        Log.d(TAG, "onImageSaved: The image has been saved in ${file.toUri()}")


                        runOnUiThread {

                            setRotateImage(file.absolutePath)

                        }


                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(
                            binding.root.context,
                            "Error occured in taking photo",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(TAG, "Error taking photo:$exception")
                    }

                })
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (isPermissionGranted()) {
                startCamera()
            } else {
                finish()
            }
        }
    } //izin verilip verilmediği kontrol

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 101
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA) //manifeste koyulan permission çekiliyor. array içine yazılıyor permissonlar
        private const val TAG = "MainActivity"
    }  //companion object değişmeyen şeyler veirliyor
}
package com.example.owner.imagecapture


import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.hardware.Camera.ShutterCallback
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class CameraDemo : Activity(), SurfaceHolder.Callback {

    private var camera: Camera? = null
    private var cameraSurfaceView: SurfaceView? = null
    private var cameraSurfaceHolder: SurfaceHolder? = null
    private var previewing = false

    private var display: Display? = null


    private var cameraViewControl: View? = null
    private var layoutParamsControl: LinearLayout.LayoutParams? = null

    private var btnCapture: ImageButton? = null

    internal var cameraShutterCallback: ShutterCallback = ShutterCallback {
        // TODO Auto-generated method stub
    }

    internal var cameraPictureCallbackRaw: PictureCallback = PictureCallback { data, camera ->
        // TODO Auto-generated method stub
    }

    internal var cameraPictureCallbackJpeg: PictureCallback = PictureCallback { data, camera ->
        // TODO Auto-generated method stub
        var cameraBitmap: Bitmap? = BitmapFactory.decodeByteArray(data, 0, data.size)
       /* val i = Intent(this, MainActivity::class.java)
        i.putExtras("bitmapImage",cameraBitmap)

        startActivity(i)*/
        wid = cameraBitmap!!.width
        hgt = cameraBitmap.height

        var newImage: Bitmap? = Bitmap.createBitmap(wid, hgt, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(newImage!!)

        canvas.drawBitmap(cameraBitmap, 0f, 0f, null)

        var drawable: Drawable? = resources.getDrawable(R.drawable.corners)
        drawable!!.setBounds(0, 0, wid, hgt)
        drawable.draw(canvas)

        val storagePath = File(Environment.getExternalStorageDirectory().toString() + "/MyCameraApp/")
        storagePath.mkdirs()

        val myImage = File(
            storagePath,
            java.lang.Long.toString(System.currentTimeMillis()) + ".jpg"
        )

        try {
            val out = FileOutputStream(myImage)
             newImage.compress(Bitmap.CompressFormat.JPEG, 80, out)


            out.flush()
            out.close()

            val i = Intent(this, MainActivity::class.java)
        i.putExtra("filePath", myImage.absolutePath)

        setResult(-1, i)
            finish()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.d("In Saving File", e.toString() + "")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("In Saving File", e.toString() + "")
        }

        camera.startPreview()

        drawable = null

        newImage.recycle()
        newImage = null

        cameraBitmap.recycle()
        cameraBitmap = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        display = windowManager.defaultDisplay
        wid = display!!.width
        hgt = display!!.height

        window.setFormat(PixelFormat.TRANSLUCENT)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.cameraoverlay1)

        cameraSurfaceView = findViewById<View>(R.id.cameraSurfaceView) as SurfaceView
        cameraSurfaceHolder = cameraSurfaceView!!.holder
        cameraSurfaceHolder!!.addCallback(this)
        cameraSurfaceHolder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        val layoutInflater = LayoutInflater.from(baseContext)
        layoutParamsControl =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT)

        cameraViewControl = layoutInflater!!.inflate(R.layout.camlayout, null)
        this.addContentView(cameraViewControl, layoutParamsControl)
        btnCapture = findViewById<View>(R.id.btnCapture) as ImageButton
        btnCapture!!.setOnClickListener {
            // TODO Auto-generated method stub
            camera!!.takePicture(
                cameraShutterCallback,
                cameraPictureCallbackRaw,
                cameraPictureCallbackJpeg
            )
        }

    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int, width: Int, height: Int
    ) {
        // TODO Auto-generated method stub

        if (previewing) {
            camera!!.stopPreview()
            previewing = false
        }
        if (camera != null) {
            try {
                camera!!.setPreviewDisplay(cameraSurfaceHolder)
                camera!!.startPreview()
                camera!!.setDisplayOrientation(90);
                previewing = true
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // TODO Auto-generated method stub
        try {
            camera = Camera.open()
        } catch (e: RuntimeException) {
            Toast.makeText(
                applicationContext,
                "Device camera is not working properly, please try after sometime.",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // TODO Auto-generated method stub
        camera!!.stopPreview()
        camera!!.release()
        camera = null
        previewing = false
    }

    companion object {


        private var wid = 0
        private var hgt = 0
    }
}
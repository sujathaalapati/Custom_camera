package com.example.owner.imagecapture

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.*


class MainActivity : AppCompatActivity()
{
    lateinit var mimageView: ImageView
     var rl: LinearLayout?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mimageView = this.findViewById(R.id.image_from_camera) as ImageView
        rl = findViewById(R.id.rr2) as LinearLayout
        val button = this.findViewById(R.id.take_image_from_camera) as Button

        val inflater:LayoutInflater=LayoutInflater.from(baseContext)
        inflater.inflate(R.layout.camlayout, null, true)
    }

    fun takeImageFromCamera(view: View) {
        val i = Intent(this, CameraDemo::class.java)
        startActivityForResult(i, CAMERA_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            /*  val b = BitmapFactory.decodeFile(data!!.extras!!.getString("filePath"))
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    val REQUIRED_SIZE = 512
                    var scale = 1
                    var wd = b.width
                    while (wd >= REQUIRED_SIZE) {
                        wd = wd / 2
                        scale *= 2
                    }
                    options.inSampleSize = scale
                    options.inJustDecodeBounds = false*/
                    var bitmap: Bitmap? = BitmapFactory.decodeFile(data!!.extras!!.getString("filePath"))
                    val matrix = Matrix()
                    matrix.postRotate(90F)
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap!!.width, bitmap!!.height, matrix, false)
              /*      val bh = bitmap!!.height
                    val bw = bitmap.width
                     val width = rl!!.getWidth()
                   val height = rl!!.getHeight()
                    val l = imageL * bw / width
                    val t = imageT * bh / height
                    val w = mimageView!!.getWidth() * bw / width
                    val h = mimageView!!.getHeight() * bh / height*/
                   val resizedBitmap = Bitmap.createBitmap(bitmap, 180, 180, 1500, 1200)
                    if (resizedBitmap != null) {
                        mimageView.setImageBitmap(resizedBitmap)
                    }
                }
            }
        companion object {

        private val CAMERA_REQUEST = 1888
    }

}


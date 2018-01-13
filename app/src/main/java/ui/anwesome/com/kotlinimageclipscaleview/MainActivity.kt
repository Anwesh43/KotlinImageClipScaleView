package ui.anwesome.com.kotlinimageclipscaleview

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.imageclipscaleview.ImageClipScaleView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ImageClipScaleView.create(this,BitmapFactory.decodeResource(resources,R.drawable.nature))
    }
}

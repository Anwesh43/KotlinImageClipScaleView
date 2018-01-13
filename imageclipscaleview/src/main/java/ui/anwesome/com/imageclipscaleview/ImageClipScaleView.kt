package ui.anwesome.com.imageclipscaleview

/**
 * Created by anweshmishra on 14/01/18.
 */
import android.content.*
import android.graphics.*
import android.view.*
import java.util.*

class ImageClipScaleView(ctx:Context,var bitmap:Bitmap):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class ImageClipScale(var x:Float,var y:Float,var size:Float) {
        var pixels:LinkedList<Pixel> = LinkedList()
        fun draw(canvas:Canvas,paint:Paint,bitmap:Bitmap,scale:Float) {
            if(pixels.size == 0) {
                for(i in 0..size.toInt()-1) {
                    for(j in 0..size.toInt()-1) {
                        val pixel = Pixel(x+i,y+i,bitmap.getPixel(x.toInt()+i,y.toInt()+j))
                        pixels.add(pixel)
                    }
                }
            }
            val path = Path()
            path.addRect(RectF(x,y,x+size*scale,y+size*scale),Path.Direction.CW)
            canvas.clipPath(path)
            pixels.forEach {
                it.draw(canvas,paint)
            }
        }
    }
    data class Pixel(var x:Float,var y:Float,var pixel:Int) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.argb(255,Color.red(pixel),Color.blue(pixel),Color.green(pixel))
            canvas.drawRect(RectF(x,y,x+1,y+1),paint)
        }
    }
}
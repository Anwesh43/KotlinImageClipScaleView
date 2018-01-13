package ui.anwesome.com.imageclipscaleview

/**
 * Created by anweshmishra on 14/01/18.
 */
import android.app.Activity
import android.content.*
import android.graphics.*
import android.view.*
import java.util.*

class ImageClipScaleView(ctx:Context,var bitmap:Bitmap):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
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
    data class State(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            if(dir == 0f) {
                dir = 1-2*scale
                startcb()
            }
        }
    }
    data class ImageClipContainer(var w:Float,var bitmap:Bitmap) {
        val state = State()
        val images:LinkedList<ImageClipScale> = LinkedList()
        init {
            bitmap = Bitmap.createScaledBitmap(bitmap,w.toInt(),w.toInt(),true)
            val gap = w/10
            for(i in 0..9) {
                for(j in 0..9) {
                    val x_factor = i%10
                    val y_factor = i/10
                    images.add(ImageClipScale(gap*x_factor,gap*y_factor,gap))
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            images.forEach {
                it.draw(canvas,paint,bitmap,state.scale)
            }
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class Animator(var view:ImageClipScaleView,var animated:Boolean = false) {
        fun animate(updatecb:()->Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
    }
    data class Renderer(var view:ImageClipScaleView,var time:Int = 0) {
        var container:ImageClipContainer?=null
        val animator = Animator(view)
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                container = ImageClipContainer(w,view.bitmap)
            }
            canvas.drawColor(Color.parseColor("#212121"))
            container?.draw(canvas,paint)
            time++
            animator.animate {
                container?.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            container?.startUpdating {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity:Activity,bitmap: Bitmap):ImageClipScaleView {
            val view = ImageClipScaleView(activity,bitmap)
            activity.setContentView(view)
            return view
        }
    }
}
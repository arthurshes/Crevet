package workwork.test.andropediagits.presenter.lesson.victorine.customview


import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class AnimatedCircleView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var radius = 0f
    private var maxRadius = 0f
     var isAnimating = true
    private var isSelected = true
    private val paint = Paint()

    init {
        paint.isAntiAlias = true
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }

    fun startAnimation() {
        isSelected = true
        isAnimating = false
            maxRadius = (width / 2f).coerceAtMost(height / 2f)
            animateCircleColor()
    }

    private fun animateCircleColor() {
        val startColor = Color.TRANSPARENT
        val endColor = Color.GREEN

        val anim = ValueAnimator.ofFloat(0f, maxRadius)
        anim.duration = 400 // Продолжительность анимации в миллисекундах
        anim.interpolator = AccelerateDecelerateInterpolator()

        anim.addUpdateListener { valueAnimator ->
            radius = valueAnimator.animatedValue as Float
            val fraction = radius / maxRadius
                 val blendedColor = blendColors(startColor, endColor, fraction)
          //  paint.color = blendedColor
            invalidate()
        }

        anim.start()
    }

    private fun blendColors(startColor: Int, endColor: Int, ratio: Float): Int {
        val inverseRatio = 1f - ratio
        val a = (Color.alpha(startColor) * inverseRatio + Color.alpha(endColor) * ratio).toInt()
        val r = (Color.red(startColor) * inverseRatio + Color.red(endColor) * ratio).toInt()
        val g = (Color.green(startColor) * inverseRatio + Color.green(endColor) * ratio).toInt()
        val b = (Color.blue(startColor) * inverseRatio + Color.blue(endColor) * ratio).toInt()
        return Color.argb(a, r, g, b)
    }
    fun startReverseAnimation() {
        isAnimating = true
        isSelected = false
        animateCircleDisappearance()
    }
    private fun animateCircleDisappearance() {
        val startColor = Color.GREEN
        val endColor = Color.TRANSPARENT

        val anim = ValueAnimator.ofFloat(maxRadius, 0f)
        anim.duration = 400 // Продолжительность анимации в миллисекундах
        anim.interpolator = AccelerateDecelerateInterpolator()

        anim.addUpdateListener { valueAnimator ->
            radius = valueAnimator.animatedValue as Float
            val fraction = radius / maxRadius
            val blendedColor = blendColors(startColor, endColor, fraction)
           // paint.color = blendedColor
            invalidate()
        }

        anim.start()
    }
}
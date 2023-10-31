package workwork.test.andropediagits.core.utils

import android.graphics.Shader
import android.text.TextPaint
import android.text.style.ForegroundColorSpan


class ShaderSpan(private val shader: Shader) : ForegroundColorSpan(0) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.shader = shader
    }
}
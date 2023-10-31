package workwork.test.andropediagits.data.remote.interceptor

import android.annotation.SuppressLint
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val apiKey:String):Interceptor {

    @SuppressLint("SuspiciousIndentation")
    override fun intercept(chain: Interceptor.Chain): Response {
      val request = chain.request().newBuilder()
          .addHeader("api_key",apiKey)
          .build()
        return chain.proceed(request)
    }

}
/*
package com.example.imfine

import android.content.Context
import android.os.Handler
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast


class CustomBridgePreview(context: Context?, webview:WebView) {
    private var mContext: Context? = null
    private var mWebView: WebView? = null

    private var mHandler: Handler? = null
    private var next : Button? =null

    init {
        this.mContext=context
        this.mWebView=webview

        mHandler = Handler()
    }

    @JavascriptInterface
    fun callMessage() {
        //var cameraSwitching = cameraSwitching
        Toast.makeText(mContext, "Web에서 호출된 메시지입니다.", Toast.LENGTH_SHORT).show()

//        HomeFragment.btn_Convert.setOnClickListener {
//            if(cameraSwitching){
//                cameraSwitching = false
//                // Web으로 호출을 반환시, 동일 Activity로는 수행이 불가능하기때문에 스레드를 하나 생성해 호출해주어야합니다.
//                cameraSwitchingHandler(cameraSwitching)
//            }else{
//                cameraSwitching = true
//                cameraSwitchingHandler(cameraSwitching)
//            }
//        }

        mHandler!!.post {
            try {

                mWebView!!.loadUrl("javascript:callback('asdasdadaasd');")
                Log.d("adad", "확인 ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cameraSwitchingHandler(cameraSwitching: Boolean){
        mHandler!!.post {
            try {

                mWebView!!.loadUrl("javascript:callback(${cameraSwitching});")
                Log.d("adad", "확인 ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
*/

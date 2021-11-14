package com.example.imfine

import android.content.Context
import android.os.Handler
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast


class CustomBridge(context: VideoCall, webview:WebView) {
    private var mContext: Context? = null
    private var mWebView: WebView? = null

    private var mHandler: Handler? = null
    private var next : Button? =null

    companion object{
        lateinit var bridge_ROOM_ID:String
        //var bridge_ROOM_ID = "123123123"
    }

    init {
        this.mContext=context
        this.mWebView=webview

        mHandler = Handler()
    }

    @JavascriptInterface
    fun callMessage(roomid: String) {
        Toast.makeText(mContext, "Web에서 호출된 메시지입니다.", Toast.LENGTH_SHORT).show()

        // Web으로 호출을 반환시, 동일 Activity로는 수행이 불가능하기때문에 스레드를 하나 생성해 호출해주어야합니다.
        mHandler!!.post {
            try {

                mWebView!!.loadUrl("javascript:callback('콜백메시지입니다.');")
                Log.d("adad", "확인 ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ")

                bridge_ROOM_ID = roomid
                Log.d("ddddd","$bridge_ROOM_ID")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

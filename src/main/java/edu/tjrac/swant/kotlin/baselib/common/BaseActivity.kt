package edu.tjrac.swant.kotlin.baselib.common

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.TypedArray
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import com.jaeger.library.StatusBarUtil
import edu.tjrac.swant.kotlin.baselib.R
import edu.tjrac.swant.kotlin.baselib.util.StringUtils
import edu.tjrac.swant.kotlin.baselib.util.T


/**
 * Created by wpc on 2018-08-02.
 */

 abstract class BaseActivity : AppCompatActivity(), BaseContextView {

    override fun getContext(): Context {
        return mContext
    }

    lateinit var mContext: Context
    var TAG = javaClass.simpleName

    var progress: Dialog? = null
//    var progress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = this
        super.onCreate(savedInstanceState)
        initStatusBar()
        setOrientation()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            findViewById<View>(android.R.id.content).systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        }
        BaseApplication.instance?.addActivity(this)
        Log.e(this.javaClass.simpleName, "onCreate")
    }

    protected fun setOrientation() {
        if(isTranslucentOrFloating()){//android 8.0 透明activity 不能设置方向
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED          //跟随父activity
        }else{
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT          //竖屏
        }
    }
    private fun isTranslucentOrFloating(): Boolean {
        var isTranslucentOrFloating = false
        try {
            val styleableRes = Class.forName("com.android.internal.R\$styleable").getField("Window").get(null) as IntArray
            val ta = obtainStyledAttributes(styleableRes)
            val m = ActivityInfo::class.java!!.getMethod("isTranslucentOrFloating", TypedArray::class.java)
            m.setAccessible(true)
            isTranslucentOrFloating = m.invoke(null, ta) as Boolean
            m.setAccessible(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return isTranslucentOrFloating
    }
    open fun initStatusBar() {
//        UiUtil.setStatusTextColor(true, this)
        StatusBarUtil.setLightMode(this)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
    }

    override fun showToast(msg: String) {
        T.show(mContext, msg)
    }

    @SuppressLint("WrongConstant")
    override fun showToast(msg: String, resId: Int) {
        T.showToast(mContext, msg, resId)
    }

    override fun showInfoDialog() {

    }

    override fun dismissInfoDialog() {
    }

    override fun dismissProgressDialog() {
        if (progress != null && progress!!.isShowing) {
            progress!!.dismiss()
        }
    }

    override fun showProgressDialog() {
        showProgressDialog("")
    }

    override fun showProgressDialog(text: String) {
        if (progress == null) {
            var view = layoutInflater.inflate(R.layout.progress, null)
            progress = Dialog(mContext, R.style.default_dialog_style)
            progress!!.setContentView(view)
//            progress = ProgressDialog(mContext)
        }
        var tv_progress = progress!!.findViewById<TextView>(R.id.tv_progress)
        if (!StringUtils.isEmpty(text)) {
            tv_progress.visibility = View.VISIBLE
            tv_progress.setText(text)
        } else {
            tv_progress.visibility = View.GONE
        }
        progress!!.show()
    }

    override fun onDestroy() {
        BaseApplication.instance?.removeActivity(this)
        super.onDestroy()
        Log.e(this.javaClass.simpleName, "onDeatroy")
    }

    override fun onResume() {
        super.onResume()
        try {
            val aClass = Class.forName("com.umeng.analytics.MobclickAgent")
            var getter = aClass.getDeclaredMethod("onResume", Context::class.java)
            getter.invoke(aClass.newInstance(), this)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        Log.e(this.javaClass.simpleName, "onResume")
    }

    override fun onPause() {
        super.onPause()
        try {
            val aClass = Class.forName("com.umeng.analytics.MobclickAgent")
            var getter = aClass.getDeclaredMethod("onPause", Context::class.java)
            getter.invoke(aClass.newInstance(), this)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        Log.e(this.javaClass.simpleName, "onPause")
    }

}

package edu.tjrac.swant.baselib.common.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import edu.tjrac.swant.baselib.R
import edu.tjrac.swant.baselib.util.StringUtils
import edu.tjrac.swant.baselib.util.T


/**
 * Created by wpc on 2018-08-02.
 */

open class BaseFragment : Fragment(), BaseContextView {

    protected var TAG = javaClass.simpleName
    open fun onBack() {

    }
    open fun backable():Boolean {
return false
    }
    open fun getTitle():String{
        return ""
    }

    override fun getContext(): Context {
        return activity!!
    }

    override fun showToast(msg: String) {
        T.show(msg)
    }

    @SuppressLint("WrongConstant")
    override fun showToast(msg: String, resId: Int) {
        if (null == activity) return
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.toast_view, null)
        val imageView = view.findViewById<ImageView>(R.id.toast_image)
        imageView.setBackgroundResource(resId)
        val t = view.findViewById<TextView>(R.id.toast_text)
        t.text = msg

        var toast = Toast(activity)
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.view = view
        toast.show()
    }

    override fun dismissInfoDialog() {
    }


    override fun showInfoDialog() {

    }

    var progress: Dialog? = null
    override fun showProgressDialog() {
        showProgressDialog("")
    }

    override fun showProgressDialog(text: String) {
        if (null != activity) {
            if (progress == null) {
                var view = LayoutInflater.from(activity).inflate(R.layout.progress, null)
                progress = Dialog(activity, R.style.default_dialog_style)
                progress!!.setContentView(view)
//            prog = ProgressDialog(mContext)
            }
            if (!StringUtils.isEmpty(text)) {
                progress!!.findViewById<TextView>(R.id.tv_progress).setText(text)
            } else {
                progress!!.findViewById<TextView>(R.id.tv_progress).setText("")
            }
            progress!!.show()
        }
    }

    override fun dismissProgressDialog() {
        if (progress != null && progress!!.isShowing) {
            progress!!.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e(this.javaClass.simpleName, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e(this.javaClass.simpleName, "onPause")
    }
}

package edu.tjrac.swant.baselib.common.base

import android.content.Context

/**
 * Created by wpc on 2018-11-29.
 */

interface BaseContextView : BaseView {
     fun getContext(): Context
}
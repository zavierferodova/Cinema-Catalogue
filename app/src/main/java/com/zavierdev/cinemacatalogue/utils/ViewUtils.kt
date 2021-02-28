package com.zavierdev.cinemacatalogue.utils

import android.view.View

class ViewUtils {
    companion object {
        @Deprecated("This method deprecated because readibility issue")
        fun showView(view: View, show: Boolean) {
            if (show)
                view.visibility = View.VISIBLE
            else
                view.visibility = View.GONE
        }

        fun showView(view: View) {
            view.visibility = View.VISIBLE
        }

        fun hideView(view: View) {
            view.visibility = View.GONE
        }
    }
}
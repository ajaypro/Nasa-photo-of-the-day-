package com.deepak.nasa.utils.common

import android.view.View
import android.view.animation.DecelerateInterpolator
import com.deepak.nasa.databinding.FragmentHomeBinding

private var animated : Boolean = false

fun animateViewsIn(binding: FragmentHomeBinding) {
    for (i in 0 until binding.container.childCount) {
        animateEachViewIn(binding.container.getChildAt(i), i)
    }
    animated = true
}

fun animateEachViewIn(child: View, i: Int) {
    child.animate()
        .translationY(0f)
        .alpha(1f)
        .scaleX(1f).scaleY(1f)
        .setStartDelay(500).setDuration((200 * i).toLong())
        .setInterpolator(DecelerateInterpolator(2f))
        .start()
}





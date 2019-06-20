package com.chenzhang.mvi.util

import android.animation.ValueAnimator
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.recording_item_expanded.view.*


fun LottieAnimationView.animateDownload(view: View) {
    setAnimation("download_progress.json", LottieAnimationView.CacheStrategy.Strong)
    ValueAnimator.ofFloat(0f, 1.0f).setDuration(3000).apply {
        addUpdateListener { valueAnimator ->
            progress = valueAnimator.animatedValue as Float

            if (progress == 1.0f) {
                setAnimation("check_mark.json", LottieAnimationView.CacheStrategy.Strong)
                ValueAnimator.ofFloat(0f, 1.0f).setDuration(2000).apply {
                    addUpdateListener { checkAnimator ->
                        progress = checkAnimator.animatedValue as Float
                        if (checkAnimator.animatedValue as Float == 1.0f) {
                            view.downloadView.visibility = View.GONE
                        }
                    }
                    start()
                }
            }
        }
        start()
    }
}

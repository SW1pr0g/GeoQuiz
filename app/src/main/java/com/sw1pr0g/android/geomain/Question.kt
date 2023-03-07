package com.sw1pr0g.android.geomain

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean) {
}
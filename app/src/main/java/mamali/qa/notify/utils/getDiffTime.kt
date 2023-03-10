package mamali.qa.notify.utils

import android.text.format.DateUtils
import java.util.*

fun Date.getRelativeTime(): String {
    // Get current time
    val now = System.currentTimeMillis()
    // Get difference between current and reference time
    val diff = now - this.time

    return when {
        diff < DateUtils.MINUTE_IN_MILLIS -> return "چند لحظه پیش"
        diff < DateUtils.HOUR_IN_MILLIS -> return "${diff / DateUtils.MINUTE_IN_MILLIS} دقیقه پیش"
        diff < DateUtils.DAY_IN_MILLIS -> return "${diff / DateUtils.HOUR_IN_MILLIS} ساعت پیش"
        diff < DateUtils.WEEK_IN_MILLIS -> return "${diff / DateUtils.DAY_IN_MILLIS} روز پیش"
        diff < DateUtils.WEEK_IN_MILLIS * 4 -> return "${diff / DateUtils.WEEK_IN_MILLIS} هفته پیش"
        else -> this.getFormatted()
    }
}
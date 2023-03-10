package mamali.qa.notify.utils
import java.util.*

    fun Date.getFormatted(): String {
        // Convert Georgian date to Jalali date
        val jalaliDate = DateConverter().apply {
            gregorianToPersian(this@getFormatted)
        }
        // Format as yyyy/MM/dd
        return with(jalaliDate) { "$day $month $year" }
    }
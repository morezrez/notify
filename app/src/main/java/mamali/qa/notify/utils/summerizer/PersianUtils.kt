package mamali.qa.notify.utils.summerizer

object PersianUtils {

    // تبدیل حروف عربی به فارسی و حذف اعراب
    fun normalize(text: String): String {
        var res = text

        // تبدیل ی و ک عربی به فارسی
        res = res.replace("ي", "ی")
        res = res.replace("ك", "ک")

        // حذف همزه و اعراب مزاحم (در صورت نیاز)
        res = res.replace("أ", "ا")
        res = res.replace("إ", "ا")
        res = res.replace("ؤ", "و")

        // استاندارد کردن نیم‌فاصله (همه را به فاصله معمولی تبدیل می‌کنیم تا توکن‌ها جدا شوند)
        // یا می‌توانیم آنها را حذف کنیم، اما تبدیل به فاصله برای شمارش کلمات بهتر است
        res = res.replace("\u200C", " ")

        return res
    }

    // ریشه‌یابی ساده: حذف پسوندهای رایج برای اینکه کلمات مشابه یکی شمرده شوند
    fun stem(word: String): String {
        var res = word.trim()

        // اگر کلمه خیلی کوتاه است (کمتر از ۳ حرف)، دست نزنیم (مثل "پا" که با حذف "ا" خراب نشود)
        if (res.length <= 3) return res

        // حذف پسوندهای جمع
        if (res.endsWith("ها")) return res.removeSuffix("ها").trim()
        if (res.endsWith("ان")) return res.removeSuffix("ان").trim()
        if (res.endsWith("ات")) return res.removeSuffix("ات").trim()

        // حذف پسوندهای صفت
        if (res.endsWith("تر")) return res.removeSuffix("تر").trim()
        if (res.endsWith("ترین")) return res.removeSuffix("ترین").trim()

        // حذف ی نکره یا نسبت (با احتیاط)
        if (res.endsWith("ی") && res.length > 4) return res.removeSuffix("ی").trim()

        return res
    }
}
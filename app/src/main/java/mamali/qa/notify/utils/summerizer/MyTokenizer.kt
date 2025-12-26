package mamali.qa.notify.utils.summerizer

import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MyTokenizer {
    companion object {

        // لیست کلمات توقف فارسی + انگلیسی
        val ALL_STOP_WORDS = PERSIAN_STOP_WORDS + ENGLISH_STOP_WORDS

        fun textToSentences(para: String): Array<String> {
            // اول متن را نرمال می‌کنیم
            val normalizedText = PersianUtils.normalize(para.trim())

            // پترن تشخیص جمله (شامل ؟ فارسی)
            val pattern = Pattern.compile(
                "[^.!?؟\\s][^.!?؟]*(?:[.!?؟](?!['\"]?\\s|$)[^.!?؟]*)*[.!?؟]?['\"]?(?=\\s|$)",
                Pattern.MULTILINE or Pattern.COMMENTS
            )
            val matcher = pattern.matcher(normalizedText)
            val sentences = ArrayList<String>()
            while (matcher.find()) {
                sentences.add(matcher.group())
            }
            return sentences.toTypedArray()
        }

        fun sentenceToTokens(s: String): Array<String> {
            // نرمال‌سازی قبل از توکن‌بندی
            val sentence = PersianUtils.normalize(s).trim()

            // جدا کردن کلمات بر اساس فاصله
            var tokens = sentence.split("\\s+".toRegex())

            // پاکسازی کاراکترهای غیر حرفی/عددی
            // \p{L} حروف تمام زبان‌ها، \p{N} اعداد
            val cleanRegex = Regex("[^\\p{L}\\p{N}]")

            val processedTokens = ArrayList<String>()

            for (t in tokens) {
                // ۱. حذف علائم نگارشی
                var token = cleanRegex.replace(t, "")

                // ۲. اگر خالی شد، رد شو
                if (token.isBlank()) continue

                // ۳. چک کردن Stop Word قبل از ریشه‌یابی
                if (ALL_STOP_WORDS.contains(token)) continue

                // ۴. ریشه‌یابی (Stemming) -> تبدیل "کتاب‌ها" به "کتاب"
                token = PersianUtils.stem(token)

                // ۵. چک کردن مجدد Stop Word (شاید بعد از ریشه‌یابی تبدیل به کلمه توقف شده باشد)
                if (token.isNotBlank() && !ALL_STOP_WORDS.contains(token)) {
                    processedTokens.add(token)
                }
            }

            return processedTokens.toTypedArray()
        }

        // بقیه توابع بدون تغییر...
        fun buildVocab(words: Array<String>): Map<String, Int> {
            val sortedWords = words.toSet()
            val vocab = HashMap<String, Int>()
            for (word in sortedWords) {
                vocab[word] = words.count { it == word }
            }
            return vocab
        }

        fun getWeightedVocab(vocab: Map<String, Int>): Map<String, Float> {
            val maxFreq = vocab.values.maxOrNull()?.toFloat() ?: 1.0f
            val weightedFreqHashMap = HashMap<String, Float>()
            vocab.entries.forEach {
                weightedFreqHashMap[it.key] = it.value.toFloat() / maxFreq
            }
            return weightedFreqHashMap
        }

        fun removeLineBreaks(para: String): String =
            para.replace("\n", " ").replace("\r", " ")

        fun checkRate(rate: Float): Boolean = rate > 0.0 && rate < 1.0

        fun getTopNIndices(
            x: Array<Float>,
            xSorted: Array<Float>,
            N: Int,
        ): Array<Int> {
            val topN = xSorted.take(N)
            val topNIndices = ArrayList<Int>()
            for (i in topN) {
                topNIndices.add(x.indexOf(i))
            }
            topNIndices.sort()
            return topNIndices.distinct().toTypedArray()
        }
    }
}
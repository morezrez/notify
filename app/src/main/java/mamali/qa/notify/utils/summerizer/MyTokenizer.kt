package mamali.qa.notify.utils.summerizer

import java.text.BreakIterator
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MyTokenizer {

    companion object {

        // Persian Stop Words (manually curated; can be replaced with a more comprehensive list)
        private val persianStopWords = arrayOf(
            "و", "در", "به", "از", "که", "این", "را", "با", "برای", "آن", "ها", "می", "شود", "است",
            "بود", "نیز", "هم", "ای", "تا", "کند", "روی", "دیگر", "کرد", "شد", "دهد", "وی", "چه", "هر", "چند", "اگر",
            "اما", "یک", "باید", "همه", "نه", "او", "ما", "من", "شما", "خود", "آنها", "پس", "توسط", "چون", "بین", "کنند"
        )

        // Function to split a paragraph into sentences for Persian text.
        fun paragraphToSentence(para: String): Array<String> {
            val breakIterator = BreakIterator.getSentenceInstance(Locale("fa"))
            breakIterator.setText(para)
            val sentences = ArrayList<String>()
            var start = breakIterator.first()
            var end = breakIterator.next()
            while (end != BreakIterator.DONE) {
                val sentence = para.substring(start, end).trim()
                if (sentence.isNotEmpty()) {
                    sentences.add(sentence)
                }
                start = end
                end = breakIterator.next()
            }
            return sentences.toTypedArray()
        }

        // Function to split a sentence into tokens (words) for Persian text.
        fun sentenceToToken(s: String): Array<String> {
            val sentence = s.trim().lowercase(Locale("fa"))
            val tokens = sentence.split(Regex("[\\s،؛ْ?؟!.]+")) // Persian-aware tokenization
            val filteredTokens = tokens.map { token ->
                Regex("[^آ-ی]").replace(token, "") // Keep only Persian alphabet
            }.filter { token ->
                !persianStopWords.contains(token.trim()) && token.isNotBlank()
            }
            return filteredTokens.toTypedArray()
        }

        // Builds a (word, frequency) HashMap.
        fun buildVocab(words: Array<String>): HashMap<String, Int> {
            val vocab = HashMap<String, Int>()
            val wordSet = words.toSet()
            for (word in wordSet) {
                vocab[word] = words.count { it == word }
            }
            return vocab
        }

        // Builds a (word, weighted_frequency) HashMap.
        fun getWeightedVocab(vocab: HashMap<String, Int>): HashMap<String, Float> {
            val maxFreq = vocab.values.maxOrNull()?.toFloat() ?: 1f
            val weightedFreqHashMap = HashMap<String, Float>()
            vocab.forEach { (word, freq) ->
                weightedFreqHashMap[word] = freq.toFloat() / maxFreq
            }
            return weightedFreqHashMap
        }

        // Removes \n and \r from Persian text.
        fun removeLineBreaks(para: String): String {
            return para.replace("\n", " ").replace("\r", " ")
        }

        // Checks if the compression rate lies in the range (0, 1].
        fun checkRate(rate: Float): Boolean {
            return rate > 0.0 && rate <= 1.0
        }

        // Get the indices of top N maximum values in X.
        fun getTopNIndices(x: Array<Float>, xSorted: Array<Float>, N: Int): Array<Int> {
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
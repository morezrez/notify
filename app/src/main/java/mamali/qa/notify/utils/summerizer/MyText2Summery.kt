package mamali.qa.notify.utils.summerizer

import android.os.AsyncTask
import java.lang.StringBuilder

public class MyText2Summery {
    companion object {
        // Summarizes the given text. Note, this method should be used whe you're dealing with long texts.
        // It performs the summarization on the background thread. Once the process is complete the summary is
        // passed to the SummaryCallback.onSummaryProduced callback.
        @JvmStatic
        suspend fun summarize(
            text: String,
            compressionRate: Float,
        ): String {
            val sentences = MyTokenizer.textToSentences(MyTokenizer.removeLineBreaks(text))
            val tfidfSummarizer = MyTFIDFSummerizer()
            val summarySentenceIndices = tfidfSummarizer.compute(text, compressionRate)
            return buildString(sentences, summarySentenceIndices).trim()
        }

        // Fetchs the sentences from topNValues and concatenates them to produce a complete. String.
        private fun buildString(
            sentences: Array<String>,
            topNValues: Array<Int>,
        ): String {
            var summary = ""
            topNValues.forEach {
                summary += sentences[it] + " "
            }
            return summary
        }
    }
}
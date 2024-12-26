package mamali.qa.notify.utils.summerizer

import android.os.AsyncTask
import java.lang.StringBuilder

public class MyText2Summery {

    companion object {

        // Summarizes the given text.
        @JvmStatic
        fun summarize(text: String, compressionRate: Float): String {
            val sentences = MyTokenizer.paragraphToSentence(MyTokenizer.removeLineBreaks(text))
            val tfidfSummarizer = MyTFIDFSummerizer()
            val p1 = tfidfSummarizer.compute(text, compressionRate)
            return buildString(sentences, p1)
        }

        // Summarizes the given text. Designed for longer texts with async processing.
        @JvmStatic
        fun summarizeAsync(text: String, compressionRate: Float, callback: SummaryCallback) {
            SummaryTask(text, compressionRate, callback).execute()
        }

        private class SummaryTask(
            var text: String, var rate: Float, var callback: SummaryCallback
        ) : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg params: Void?): String {
                val sentences = MyTokenizer.paragraphToSentence(MyTokenizer.removeLineBreaks(text))
                val tfidfSummarizer = MyTFIDFSummerizer()
                val p1 = tfidfSummarizer.compute(text, rate)
                return buildString(sentences, p1)
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                callback.onSummaryProduced(result!!)
            }
        }

        private fun buildString(sentences: Array<String>, topNValues: Array<Int>): String {
            val stringBuilder = StringBuilder()
            for (n in topNValues) {
                stringBuilder.append(sentences[n] + " .")
            }
            return stringBuilder.toString()
        }
    }

    interface SummaryCallback {
        fun onSummaryProduced(summary: String)
    }
}
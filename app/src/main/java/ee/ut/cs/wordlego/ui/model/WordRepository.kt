package ee.ut.cs.wordlego

import android.content.Context
import android.util.Log
import org.json.JSONObject
import kotlin.random.Random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

object WordRepository {
    suspend fun fetchRandomWord(length: Int = 5): String? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("https://random-word-api.herokuapp.com/word?length=$length")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val responseCode = connection.responseCode
                if (responseCode == 200) {
                    val stream = connection.inputStream.bufferedReader().use { it.readText() }
                    val json = JSONArray(stream)
                    json.getString(0).uppercase()
                } else null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
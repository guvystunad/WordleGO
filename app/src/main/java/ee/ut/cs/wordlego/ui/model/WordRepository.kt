package ee.ut.cs.wordlego

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import org.json.JSONObject
import kotlin.random.Random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import org.json.JSONArray
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
@Serializable
data class Sonad(var counter: Int, val words: List<String>)
object WordRepository {

//Save the current counter and date ti SharedPreferences
    fun saveWordState(context: Context, counter: Int, date: String) {
        context.getSharedPreferences("daily_word", Context.MODE_PRIVATE)
            .edit()
            .putInt("counter", counter)
            .putString("date", date)
            .apply()
    }

//Load the counter and date from SharedPreferences
    fun loadWordState(context: Context): Pair<Int, String?> {
        val prefs = context.getSharedPreferences("daily_word", Context.MODE_PRIVATE)
        val counter = prefs.getInt("counter", 0)
        val date = prefs.getString("date", null)
        return counter to date
    }
//Return today's date as a string in "yyyy-MM-dd" format
    fun today(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(java.util.Calendar.getInstance().time)
    }
//Fetch the word for the given location
    suspend fun fetchRandomWord(context: Context, location: String): String? {
        return withContext(Dispatchers.IO) {

            try {
                // read the JSON file from assets
                val jsonString = context.assets.open("$location.json")
                    .bufferedReader(Charsets.UTF_8)
                    .use { it.readText().trim('\uFEFF') }

                val json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
                //Parse JSON into Sonad class
                val sonad = json.decodeFromString<Sonad>(jsonString)

                // Load counter and date
                val (storedCounter, storedDate) = loadWordState(context)
                val today = today()

                // If a new day, change counter
                val finalCounter = if (storedDate != today) {
                    if (storedCounter >= sonad.words.lastIndex) 0 else storedCounter + 1
                } else {
                    storedCounter
                }

                // 4. Save updated counter and date if new day
                if(storedDate!=today) {
                    saveWordState(context, finalCounter, today)
                }
                // 5. return the word of the day
                sonad.words[finalCounter]

            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
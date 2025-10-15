package ee.ut.cs.wordlego

import android.content.Context
import android.util.Log
import org.json.JSONObject
import kotlin.random.Random

object WordRepository {

    fun loadRandomWord(context: Context): String {
        return try {
            val jsonString = context.assets.open("words.json")
                .bufferedReader()
                .use { it.readText() }

            val jsonObject = JSONObject(jsonString)
            val wordsArray = jsonObject.getJSONArray("words")

            if (wordsArray.length() == 0) {
                Log.e("WordRepository", "No words found in JSON file")
                return "SQUAD"
            }

            val randomIndex = Random.nextInt(wordsArray.length())
            wordsArray.getString(randomIndex).uppercase()

        } catch (e: Exception) {
            Log.e("WordRepository", "Error loading random word: ${e.message}")
            "SQUAD"
        }
    }
}
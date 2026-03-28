package shibin.flowplayground.voiceHelper

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale


class VoiceHelper(context: Context) {

    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setSpeechRate(0.9f)
                tts?.setPitch(1.0f)
            }
        }
    }

    fun speak(text: String) {
        tts?.stop() // 🔥 important: stop previous
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun release() {
        tts?.shutdown()
    }
}
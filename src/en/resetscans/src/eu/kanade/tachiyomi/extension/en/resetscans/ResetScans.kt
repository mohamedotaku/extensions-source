package eu.kanade.tachiyomi.extension.en.resetscans
import eu.kanade.tachiyomi.multisrc.madara.Madara
import java.text.SimpleDateFormat
import java.util.Locale

class ResetScans : Madara(
    "Reset Scans",
    "https://resetscan.com",
    "en",
    dateFormat = SimpleDateFormat("MMM dd", Locale("en")),
) {
    override val useLoadMoreRequest = LoadMoreStrategy.Always
    override val useNewChapterEndpoint = true
    override val chapterUrlSelector = ".li__text > a"
}

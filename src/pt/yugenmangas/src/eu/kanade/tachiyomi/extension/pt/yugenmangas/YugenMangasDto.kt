package eu.kanade.tachiyomi.extension.pt.yugenmangas

import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import java.text.SimpleDateFormat
import java.util.Locale

@Serializable
class YugenMangaDto(
    val name: String,
    @JsonNames("capa", "cover") val cover: String,
    val slug: String,
    val author: String? = null,
    val artist: String? = null,
    val genres: List<String> = emptyList(),
    val synopsis: String? = null,
    val status: String? = null,
) {

    fun toSManga(baseUrl: String): SManga = SManga.create().apply {
        title = name
        author = this@YugenMangaDto.author
        artist = this@YugenMangaDto.artist
        description = synopsis
        status = when (this@YugenMangaDto.status) {
            "ongoing" -> SManga.ONGOING
            "completed", "finished" -> SManga.COMPLETED
            else -> SManga.UNKNOWN
        }

        thumbnail_url = when {
            cover.startsWith(listOf("/", "cover")) -> "$baseUrl/media/${cover.removePrefix("/")}"
            else -> cover
        }
        url = "/series/$slug"
    }

    private fun String.startsWith(group: List<String>): Boolean = group.any(::startsWith)
}

@Serializable
class YugenChapterListDto(val chapters: List<YugenChapterDto>)

@Serializable
class YugenChapterDto(
    val name: String,
    val season: Int,
    @SerialName("upload_date") val uploadDate: String,
    val slug: String,
    val group: String,
) {

    fun toSChapter(mangaSlug: String): SChapter = SChapter.create().apply {
        name = this@YugenChapterDto.name
        date_upload = runCatching { DATE_FORMATTER.parse(uploadDate)?.time }
            .getOrNull() ?: 0L
        chapter_number = this@YugenChapterDto.name
            .removePrefix("Capítulo ")
            .substringBefore(" - ")
            .toFloatOrNull() ?: -1f
        scanlator = group.ifEmpty { null }
        url = "/series/$mangaSlug/$slug"
    }

    companion object {
        private val DATE_FORMATTER by lazy {
            SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        }
    }
}

@Serializable
data class YugenGetChaptersBySeriesDto(
    @SerialName("serie_slug") val seriesSlug: String,
)

@Serializable
class YugenPageList(
    @SerialName("chapter_images") val chapterImages: List<String>,
)

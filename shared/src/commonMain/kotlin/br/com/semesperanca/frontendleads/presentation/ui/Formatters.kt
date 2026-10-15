package br.com.semesperanca.frontendleads.presentation.ui

import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.roundToLong

object Formatters {

    fun natureColor(nature: String): Color = when (nature) {
        "Venda" -> AppColors.Green500
        "Aluguel" -> AppColors.Blue500
        "Lançamento" -> AppColors.Purple500
        else -> AppColors.Gray500
    }

    fun formatCurrency(value: Number): String {
        val cents = (value.toDouble() * 100).roundToLong()
        val negative = cents < 0
        val absCents = abs(cents)
        val reais = absCents / 100
        val centavos = absCents % 100
        val grouped = reais.toString().reversed().chunked(3).joinToString(".").reversed()
        val sign = if (negative) "-" else ""
        return "${sign}R$ $grouped,${centavos.toString().padStart(2, '0')}"
    }

    fun formatDate(iso: String?): String {
        if (iso.isNullOrBlank()) return "—"
        val cleaned = iso.removeSuffix("Z")
        return try {
            if (cleaned.length >= 10) {
                "${cleaned.substring(8, 10)}/${cleaned.substring(5, 7)}/${cleaned.substring(0, 4)}"
            } else cleaned
        } catch (_: Exception) {
            iso
        }
    }

    fun formatActivityDate(iso: String): String {
        if (iso.isBlank()) return "—"
        val cleaned = iso.removeSuffix("Z")
        return try {
            val date = if (cleaned.length >= 10) {
                "${cleaned.substring(8, 10)}/${cleaned.substring(5, 7)}"
            } else {
                return cleaned
            }
            var time = ""
            if (cleaned.length >= 16) {
                time = " às ${cleaned.substring(11, 16)}"
            }
            "$date$time"
        } catch (_: Exception) {
            iso
        }
    }

    /** Parseia [iso] (prefixo "yyyy-MM-dd") para millis UTC do dia civil. */
    fun dateToMillis(iso: String?): Long? {
        val s = iso?.trim().orEmpty()
        if (s.length < 10) return null
        val y = s.substring(0, 4).toIntOrNull() ?: return null
        val m = s.substring(5, 7).toIntOrNull() ?: return null
        val d = s.substring(8, 10).toIntOrNull() ?: return null
        if (m !in 1..12) return null
        val daysInMonth = when (m) {
            2 -> if (isLeap(y)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
        if (d !in 1..daysInMonth) return null
        return daysFromCivil(y, m, d) * MILLIS_PER_DAY
    }

    /** Extrai "HH:mm" de [iso] ("yyyy-MM-ddTHH:mm...") em minutos desde a meia-noite. */
    fun timeToMinutes(iso: String?): Int? {
        val s = iso?.trim().orEmpty()
        if (s.length < 16) return null
        val h = s.substring(11, 13).toIntOrNull() ?: return null
        val mi = s.substring(14, 16).toIntOrNull() ?: return null
        if (h !in 0..23 || mi !in 0..59) return null
        return h * 60 + mi
    }

    /** Formata uma data ISO ("yyyy-MM-dd...") como "dd/MM/yyyy". */
    fun formatPickerDate(iso: String?): String {
        val s = iso?.trim().orEmpty()
        if (s.length < 10) return ""
        val d = s.substring(8, 10).toIntOrNull() ?: return ""
        val m = s.substring(5, 7).toIntOrNull() ?: return ""
        val y = s.substring(0, 4).toIntOrNull() ?: return ""
        return "${pad(d)}/${pad(m)}/$y"
    }

    /** Formata uma hora em minutos desde a meia-noite como "HH:mm". */
    fun formatPickerTime(minutes: Int?): String {
        if (minutes == null) return ""
        val h = minutes / 60
        val m = minutes % 60
        return "${pad(h)}:${pad(m)}"
    }

    /** Exibe data/hora de uma atividade como "15/09 às 14:30" (vazio = "Agora"). */
    fun dateTimeDisplay(dateIso: String?, timeMinutes: Int?): String {
        val s = dateIso?.trim().orEmpty()
        val date = if (s.length >= 10)
            "${pad(s.substring(8, 10).toIntOrNull() ?: 0)}/${pad(s.substring(5, 7).toIntOrNull() ?: 0)}"
        else ""
        val time = formatPickerTime(timeMinutes)
        return when {
            date.isEmpty() && time.isNotEmpty() -> time
            date.isNotEmpty() && time.isNotEmpty() -> "$date às $time"
            else -> "Agora"
        }
    }

    /** Converte year/month/day civs em dias desde a Época (algoritmo Rata Die negativo/positivo). */
    private fun daysFromCivil(year: Int, month: Int, day: Int): Long {
        val y = if (month <= 2) year - 1 else year
        val era = if (y >= 0) y / 400 else (y - 399) / 400
        val yoe = y - era * 400
        val mp = (month + 9) % 12
        val doy = (153 * mp + 2) / 5 + day - 1
        val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
        return era * 146097L + doe - 719468
    }

    /** Formata millis UTC (meia-noite do dia) como "yyyy-MM-dd". */
    fun millisToDateIso(millis: Long): String = civilFromDays(millis / MILLIS_PER_DAY)

    /** Converte dias desde a Época em "yyyy-MM-dd" (inverso de [daysFromCivil]). */
    private fun civilFromDays(epochDay: Long): String {
        var z = epochDay + 719468
        val era = if (z >= 0) z else z - 146096
        val eraInt = (era / 146097).toInt()
        val doe = (era - eraInt.toLong() * 146097).toInt()
        val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
        var y = yoe + eraInt * 400
        val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
        val mp = (5 * doy + 2) / 153
        val d = doy - (153 * mp + 2) / 5 + 1
        val m = if (mp < 10) mp + 3 else mp - 9
        if (m <= 2) y += 1
        return "${pad4(y)}-${pad(m)}-${pad(d)}"
    }

    private fun isLeap(year: Int): Boolean =
        (year % 4 == 0 && year % 100 != 0) || year % 400 == 0

    private fun pad(v: Int): String = if (v in 0..9) "0$v" else v.toString()

    private fun pad4(v: Int): String =
        if (v < 0) "-${pad3(-v)}" else pad3(v)

    private fun pad3(v: Int): String = when {
        v < 10 -> "00$v"
        v < 100 -> "0$v"
        else -> v.toString()
    }

    private const val MILLIS_PER_DAY = 86_400_000L
}

package br.com.semesperanca.frontendleads

import br.com.semesperanca.frontendleads.presentation.ui.Formatters
import kotlin.test.Test
import kotlin.test.assertEquals

class FormattersTest {

    @Test
    fun formatCurrencyThousands() {
        assertEquals("R$ 1.234,56", Formatters.formatCurrency(1234.56))
    }

    @Test
    fun formatCurrencyZero() {
        assertEquals("R$ 0,00", Formatters.formatCurrency(0))
    }

    @Test
    fun formatCurrencyMillions() {
        assertEquals("R$ 1.000.000,00", Formatters.formatCurrency(1000000.0))
    }

    @Test
    fun formatCurrencyNegative() {
        assertEquals("-R$ 50,50", Formatters.formatCurrency(-50.5))
    }

    @Test
    fun formatCurrencyCentsOnly() {
        assertEquals("R$ 0,05", Formatters.formatCurrency(0.05))
    }

    @Test
    fun formatDateFromIso() {
        assertEquals("31/08/2026", Formatters.formatDate("2026-08-31T10:30:00Z"))
    }

    @Test
    fun formatDateBlank() {
        assertEquals("—", Formatters.formatDate(""))
    }

    @Test
    fun formatDateNull() {
        assertEquals("—", Formatters.formatDate(null))
    }

    @Test
    fun formatActivityDateWithTime() {
        assertEquals("31/08 às 10:30", Formatters.formatActivityDate("2026-08-31T10:30:00Z"))
    }

    @Test
    fun formatActivityDateNoTime() {
        assertEquals("31/08", Formatters.formatActivityDate("2026-08-31"))
    }

    @Test
    fun dateToMillisRoundTrip() {
        val iso = "2026-09-15"
        assertEquals(iso, Formatters.millisToDateIso(Formatters.dateToMillis(iso)!!))
    }

    @Test
    fun dateToMillisLeapYear() {
        assertEquals(
            "2024-02-29",
            Formatters.millisToDateIso(Formatters.dateToMillis("2024-02-29")!!),
        )
    }

    @Test
    fun dateToMillisRejectsInvalidMonthDay() {
        assertEquals(null, Formatters.dateToMillis("2026-13-01"))
        assertEquals(null, Formatters.dateToMillis("2026-02-30"))
        assertEquals(null, Formatters.dateToMillis("2026"))
        assertEquals(null, Formatters.dateToMillis(null))
    }

    @Test
    fun timeToMinutesParsesIso() {
        assertEquals(14 * 60 + 30, Formatters.timeToMinutes("2026-09-15T14:30"))
        assertEquals(null, Formatters.timeToMinutes("2026-09-15"))
        assertEquals(null, Formatters.timeToMinutes("2026-09-15T25:00"))
    }

    @Test
    fun formatPickerDateAndTime() {
        assertEquals("15/09/2026", Formatters.formatPickerDate("2026-09-15"))
        assertEquals("", Formatters.formatPickerDate(""))
        assertEquals("14:30", Formatters.formatPickerTime(14 * 60 + 30))
        assertEquals("", Formatters.formatPickerTime(null))
    }

    @Test
    fun dateTimeDisplayCombinations() {
        assertEquals("15/09 às 14:30", Formatters.dateTimeDisplay("2026-09-15", 14 * 60 + 30))
        assertEquals("14:30", Formatters.dateTimeDisplay("", 14 * 60 + 30))
        assertEquals("Agora", Formatters.dateTimeDisplay("", null))
    }
}

package br.com.semesperanca.frontendleads

import br.com.semesperanca.frontendleads.data.dto.LeadDto
import br.com.semesperanca.frontendleads.presentation.leads.applyLocalFilters
import br.com.semesperanca.frontendleads.presentation.leads.matchesSearch
import br.com.semesperanca.frontendleads.presentation.leads.toLeadUi
import br.com.semesperanca.frontendleads.presentation.leads.LeadFilter
import kotlin.test.Test
import kotlin.test.assertEquals

class LeadAdapterTest {

    private fun lead(
        id: String = "1",
        name: String = "Maria Silva",
        email: String = "maria@example.com",
        phone: Long? = null,
        transactionType: String? = null,
        status: String? = null,
    ) = LeadDto(
        id = id,
        name = name,
        email = email,
        phone = phone,
        transactionType = transactionType,
        status = status,
    )

    @Test
    fun formatsPhoneWithDdd() {
        val ui = lead(id = "10", phone = 11999998888L, transactionType = "SALE").toLeadUi()
        assertEquals("(11) 99999-8888", ui.phone)
    }

    @Test
    fun mapsSaleNature() {
        val ui = lead(transactionType = "SALE").toLeadUi()
        assertEquals("Venda", ui.nature)
        assertEquals("SALE", ui.transactionType)
    }

    @Test
    fun mapsRentNature() {
        val ui = lead(transactionType = "RENT").toLeadUi()
        assertEquals("Aluguel", ui.nature)
        assertEquals("RENT", ui.transactionType)
    }

    @Test
    fun defaultsToVendaWhenUnknownTransaction() {
        val ui = lead(transactionType = "X").toLeadUi()
        assertEquals("Venda", ui.nature)
    }

    @Test
    fun detectsArchivedStatus() {
        val ui = lead(status = "archived").toLeadUi()
        assertEquals(true, ui.isArchived)
    }

    @Test
    fun mapsPropertyPlaceholderAndEmail() {
        val ui = lead(email = "c@x.com").toLeadUi()
        assertEquals("Imóvel", ui.propertyName)
        assertEquals(0.0, ui.propertyValue)
        assertEquals("c@x.com", ui.email)
    }

    @Test
    fun emptyPhoneStaysEmpty() {
        val ui = lead(phone = null).toLeadUi()
        assertEquals("", ui.phone)
    }

    @Test
    fun matchesSearchByPhone() {
        val ui = lead(phone = 11999998888L, transactionType = "SALE").toLeadUi()
        assertEquals(true, ui.matchesSearch("99999"))
        assertEquals(true, ui.matchesSearch("maria"))
        assertEquals(false, ui.matchesSearch("joao"))
    }

    @Test
    fun filtersFavoritesArchivedAndFuture() {
        val favorite = lead(id = "1").toLeadUi().copy(isFavorite = true)
        val archived = lead(id = "2").toLeadUi().copy(isArchived = true)
        val active = lead(id = "3").toLeadUi()
        val all = listOf(favorite, archived, active)

        assertEquals(listOf(favorite), applyLocalFilters(all, LeadFilter.FAVORITOS))
        assertEquals(listOf(archived), applyLocalFilters(all, LeadFilter.ARQUIVADOS))
        assertEquals(emptyList(), applyLocalFilters(all, LeadFilter.FUTUROS))
        assertEquals(listOf(favorite, active), applyLocalFilters(all, LeadFilter.TODOS))
    }

    @Test
    fun sortsByIdLexicographically() {
        val a = lead(id = "10").toLeadUi()
        val b = lead(id = "2").toLeadUi()
        val c = lead(id = "1").toLeadUi()
        assertEquals(listOf(c, a, b), applyLocalFilters(listOf(a, b, c), LeadFilter.TODOS))
    }
}
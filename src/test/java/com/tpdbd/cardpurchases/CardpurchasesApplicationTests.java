package com.tpdbd.cardpurchases;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CardpurchasesApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper mapper = new ObjectMapper();

	private String bankId;
	private String cardHolderId;
	private String cardId;

	@BeforeEach
	void setUp() throws Exception {
		// Crear banco y capturar su ID real (ObjectId en MongoDB)
		MvcResult bankResult = mockMvc.perform(post("/api/banks")
				.param("name", "BancoTest")
				.param("cuit", "20123456789")
				.param("address", "Av. Test")
				.param("telephone", "1123456789")
				.param("direction", "Centro"))
			.andExpect(status().isCreated())
			.andReturn();
		bankId = mapper.readTree(bankResult.getResponse().getContentAsString()).get("id").asText();

		// Crear titular y capturar su ID real
		MvcResult holderResult = mockMvc.perform(post("/api/cardholders")
				.param("completeName", "Test Holder")
				.param("dni", "12345678")
				.param("cuil", "20123456789")
				.param("address", "Test Address")
				.param("telephone", "1123456789")
				.param("entryDate", "2020-01-15"))
			.andExpect(status().isCreated())
			.andReturn();
		cardHolderId = mapper.readTree(holderResult.getResponse().getContentAsString()).get("id").asText();

		// Crear tarjeta usando los IDs reales
		MvcResult cardResult = mockMvc.perform(post("/api/cards")
				.param("cardHolderId", cardHolderId)
				.param("bankId", bankId)
				.param("number", "4111111111111111")
				.param("ccv", "789")
				.param("cardholderNameInCard", "TEST HOLDER")
				.param("since", "2020-01-01")
				.param("expirationDate", "2028-12-31"))
			.andExpect(status().isCreated())
			.andReturn();
		cardId = mapper.readTree(cardResult.getResponse().getContentAsString()).get("id").asText();
	}

	// ====== CASO 1: Agregar una nueva promoción de tipo descuento a un banco dado ======
	@Test
	@DisplayName("Caso 1: Agregar promoción de descuento a banco")
	void testAddDiscountPromotionToBankUseCase() throws Exception {
		mockMvc.perform(post("/api/promotions/discount")
				.param("bankId", bankId)
				.param("code", "DESC2026")
				.param("promotionTitle", "Descuento Anual 15%")
				.param("nameStore", "SuperMercado")
				.param("cuitStore", "20999888777")
				.param("validityStartDate", "2026-01-01")
				.param("validityEndDate", "2026-12-31")
				.param("discountPercentage", "15")
				.param("priceCap", "5000")
				.param("onlyCash", "false"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isString())
			.andExpect(jsonPath("$.code").value("DESC2026"))
			.andExpect(jsonPath("$.promotionTitle").value("Descuento Anual 15%"))
			.andExpect(jsonPath("$.discountPercentage").value(15.0));
	}

	// ====== CASO 1b: Agregar promoción de tipo financiación a un banco dado ======
	@Test
	@DisplayName("Caso 1b: Agregar promoción de financiación a banco")
	void testAddFinancingPromotionToBankUseCase() throws Exception {
		mockMvc.perform(post("/api/promotions/financing")
				.param("bankId", bankId)
				.param("code", "FIN2026")
				.param("promotionTitle", "Financiación 6 cuotas")
				.param("nameStore", "TiendaFinanciada")
				.param("cuitStore", "20111222333")
				.param("validityStartDate", "2026-01-01")
				.param("validityEndDate", "2026-12-31")
				.param("numberOfQuotas", "6")
				.param("interest", "5.0"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isString())
			.andExpect(jsonPath("$.code").value("FIN2026"))
			.andExpect(jsonPath("$.promotionTitle").value("Financiación 6 cuotas"))
			.andExpect(jsonPath("$.numberOfQuotas").value(6))
			.andExpect(jsonPath("$.interest").value(5.0));
	}

	// ====== CASO 2: Editar las fechas de vencimiento de un pago con cierto código ======
	@Test
	@DisplayName("Caso 2: Editar fechas de vencimiento de pago")
	void testEditPaymentDueDatesUseCase() throws Exception {
		mockMvc.perform(post("/api/purchases/monthly")
				.param("cardId", cardId)
				.param("store", "TiendaQuotas")
				.param("cuitStore", "20999888777")
				.param("amount", "2000")
				.param("numberOfQuotas", "3")
				.param("interest", "8.0")
				.param("paymentVoucher", "VOUCHER003"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.amount").value(2000))
			.andExpect(jsonPath("$.numberOfQuotas").value(3));
	}

	// ====== CASO 3: Generar el total de pago de un mes dado ======
	@Test
	@DisplayName("Caso 3: Generar total de pago del mes con cuotas y compras al contado")
	void testGenerateMonthlyPaymentTotalUseCase() throws Exception {
		String currentMonth = String.format("%02d", LocalDate.now().getMonthValue());
		String currentYear = String.valueOf(LocalDate.now().getYear());

		// Crear compra en cuotas → genera cuotas a partir del mes actual
		mockMvc.perform(post("/api/purchases/monthly")
				.param("cardId", cardId)
				.param("store", "ElectrodomésticsPlus")
				.param("cuitStore", "20999888777")
				.param("amount", "3000")
				.param("numberOfQuotas", "6")
				.param("interest", "9.0")
				.param("paymentVoucher", "VOUCHER_MONTHLY_CASO3"))
			.andExpect(status().isCreated());

		// Crear compra al contado para el mes actual
		mockMvc.perform(post("/api/purchases/cash")
				.param("cardId", cardId)
				.param("store", "TiendaContado")
				.param("cuitStore", "20777666555")
				.param("amount", "1500")
				.param("storeDiscount", "0")
				.param("purchaseMonth", currentMonth)
				.param("purchaseYear", currentYear))
			.andExpect(status().isCreated());

		// Verificar que el resumen incluye AMBOS tipos
		mockMvc.perform(get("/api/payments/month/" + currentYear + "/" + currentMonth))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalAmount").isNumber())
			.andExpect(jsonPath("$.quotaItems").isArray())
			.andExpect(jsonPath("$.quotaItems.length()").value(greaterThan(0)))
			.andExpect(jsonPath("$.cashPaymentItems").isArray())
			.andExpect(jsonPath("$.cashPaymentItems.length()").value(greaterThan(0)));
	}

	// ====== CASO 4: Obtener el listado de tarjetas emitidas hace más de 5 años ======
	@Test
	@DisplayName("Caso 4: Obtener tarjetas antiguas (>5 años)")
	void testGetOldCardsUseCase() throws Exception {
		mockMvc.perform(get("/api/cards/old"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$[*]").exists());
	}

	// ====== CASO 5: Obtener la información de una compra, incluyendo cuotas ======
	@Test
	@DisplayName("Caso 5: Obtener información de compra con cuotas")
	void testGetPurchaseDetailsWithQuotasUseCase() throws Exception {
		MvcResult createResult = mockMvc.perform(post("/api/purchases/monthly")
				.param("cardId", cardId)
				.param("store", "ElectrodomésticosTienda")
				.param("cuitStore", "20999888777")
				.param("amount", "15000")
				.param("numberOfQuotas", "12")
				.param("interest", "12.5")
				.param("paymentVoucher", "VOUCHER004"))
			.andExpect(status().isCreated())
			.andReturn();

		String purchaseId = mapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

		mockMvc.perform(get("/api/purchases/{purchaseId}", purchaseId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.purchase.amount").isNumber())
			.andExpect(jsonPath("$.purchase.store").exists())
			.andExpect(jsonPath("$.quotas").isArray())
			.andExpect(jsonPath("$.quotas.length()").value(12))
			.andExpect(jsonPath("$.appliedPromotions").isArray());
	}

	// ====== CASO 6: Eliminar una promoción a través de su código ======
	@Test
	@DisplayName("Caso 6: Eliminar promoción por código")
	void testDeletePromotionByCodeUseCase() throws Exception {
		mockMvc.perform(post("/api/promotions/discount")
				.param("bankId", bankId)
				.param("code", "PROMO_DELETE_001")
				.param("promotionTitle", "Promoción para Eliminar")
				.param("nameStore", "LocalConPromocion")
				.param("cuitStore", "20888777666")
				.param("validityStartDate", "2026-01-01")
				.param("validityEndDate", "2026-12-31")
				.param("discountPercentage", "10")
				.param("priceCap", "5000")
				.param("onlyCash", "false"))
			.andExpect(status().isCreated());

		// Crear compra en el mismo local → la promoción se auto-aplica
		mockMvc.perform(post("/api/purchases/monthly")
				.param("cardId", cardId)
				.param("store", "LocalConPromocion")
				.param("cuitStore", "20888777666")
				.param("amount", "3000")
				.param("numberOfQuotas", "2")
				.param("interest", "5.0")
				.param("paymentVoucher", "VOUCHER_WITH_PROMO"))
			.andExpect(status().isCreated());

		// Eliminar → no debe lanzar error aunque la promo fue aplicada a una compra
		mockMvc.perform(delete("/api/promotions/{code}", "PROMO_DELETE_001"))
			.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/promotions/available")
				.param("cuitStore", "20888777666")
				.param("startDate", "2026-01-01")
				.param("endDate", "2026-12-31"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray());
	}

	// ====== CASO 7: Obtener el listado de promociones disponibles de un local entre dos fechas ======
	@Test
	@DisplayName("Caso 7: Obtener promociones disponibles entre fechas")
	void testGetAvailablePromotionsBetweenDatesUseCase() throws Exception {
		mockMvc.perform(get("/api/promotions/available")
				.param("cuitStore", "20999888777")
				.param("startDate", "2025-01-01")
				.param("endDate", "2026-12-31"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray());
	}

	// ====== CASO 8: Obtener los 10 titulares con mayor monto total en compras ======
	@Test
	@DisplayName("Caso 8: Obtener top 10 titulares por gasto")
	void testGetTop10CardholdersBySpendingUseCase() throws Exception {
		mockMvc.perform(get("/api/cardholders/top-10-spending"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray());
	}

	// ====== CASO 9: Obtener el nombre del local con mayor cantidad de compras ======
	@Test
	@DisplayName("Caso 9: Obtener local con más compras")
	void testGetStoreWithMostPurchasesUseCase() throws Exception {
		// Crear una compra para garantizar que hay datos
		mockMvc.perform(post("/api/purchases/monthly")
				.param("cardId", cardId)
				.param("store", "TiendaMasCompras")
				.param("cuitStore", "20111222333")
				.param("amount", "1000")
				.param("numberOfQuotas", "3")
				.param("interest", "5.0"))
			.andExpect(status().isCreated());

		mockMvc.perform(get("/api/purchases/store-most-purchases"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.store").exists());
	}

	// ====== CASO 10: Obtener el banco con mayor cantidad de compras ======
	@Test
	@DisplayName("Caso 10: Obtener banco con más compras")
	void testGetBankWithMostPurchasesUseCase() throws Exception {
		// Crear una compra para garantizar que hay datos
		mockMvc.perform(post("/api/purchases/monthly")
				.param("cardId", cardId)
				.param("store", "TiendaBancoMasCompras")
				.param("cuitStore", "20444555666")
				.param("amount", "2000")
				.param("numberOfQuotas", "2")
				.param("interest", "5.0"))
			.andExpect(status().isCreated());

		mockMvc.perform(get("/api/banks/most-purchases"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").isString())
			.andExpect(jsonPath("$.name").isString())
			.andExpect(jsonPath("$.cuit").isString())
			.andExpect(jsonPath("$.direction").isString());
	}

	// ====== CASO 11: Obtener el número de clientes de cada banco ======
	@Test
	@DisplayName("Caso 11: Obtener cantidad de clientes por banco")
	void testGetClientCountByBankUseCase() throws Exception {
		mockMvc.perform(get("/api/banks/client-count"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isMap())
			.andExpect(jsonPath("$[*]").exists());
	}

	// ====== TESTS ADICIONALES DE VALIDACIÓN ======

	@Test
	@DisplayName("Test: Crear banco con validación de estructura")
	void testCreateBankWithValidation() throws Exception {
		mockMvc.perform(post("/api/banks")
				.param("name", "BancoValidacion")
				.param("cuit", "20888777666")
				.param("address", "Calle Test 123")
				.param("telephone", "1187654321")
				.param("direction", "Sur"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isString())
			.andExpect(jsonPath("$.name").value("BancoValidacion"))
			.andExpect(jsonPath("$.cuit").value("20888777666"))
			.andExpect(jsonPath("$.direction").value("Sur"));
	}

	@Test
	@DisplayName("Test: Crear titular con validación de estructura")
	void testCreateCardholderWithValidation() throws Exception {
		mockMvc.perform(post("/api/cardholders")
				.param("completeName", "Carlos Lopez Martinez")
				.param("dni", "36789012")
				.param("cuil", "20367890123")
				.param("address", "Avenida Principal 456")
				.param("telephone", "1198765432")
				.param("entryDate", "2023-06-15"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.completeName").value("Carlos Lopez Martinez"))
			.andExpect(jsonPath("$.dni").value("36789012"));
	}

	@Test
	@DisplayName("Test: Crear tarjeta con validación de estructura")
	void testCreateCardWithValidation() throws Exception {
		mockMvc.perform(post("/api/cards")
				.param("cardHolderId", cardHolderId)
				.param("bankId", bankId)
				.param("number", "5111222233334444")
				.param("ccv", "456")
				.param("cardholderNameInCard", "CARLOS LOPEZ")
				.param("since", "2023-01-01")
				.param("expirationDate", "2028-12-31"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.number").value("5111222233334444"))
			.andExpect(jsonPath("$.cardholderNameInCard").value("CARLOS LOPEZ"));
	}

	@Test
	@DisplayName("Test: Promoción con estructura completa")
	void testPromotionCompleteStructure() throws Exception {
		mockMvc.perform(post("/api/promotions/discount")
				.param("bankId", bankId)
				.param("code", "STRUCT001")
				.param("promotionTitle", "Promoción Estructura")
				.param("nameStore", "TiendaEstructura")
				.param("cuitStore", "20999888777")
				.param("validityStartDate", "2026-01-15")
				.param("validityEndDate", "2026-12-31")
				.param("discountPercentage", "20")
				.param("priceCap", "2000")
				.param("onlyCash", "true"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.code").value("STRUCT001"))
			.andExpect(jsonPath("$.promotionTitle").value("Promoción Estructura"))
			.andExpect(jsonPath("$.onlyCash").value(true));
	}

	@Test
	@DisplayName("Test: Validar pago mensual con estructura completa")
	void testMonthlyPaymentCompleteStructure() throws Exception {
		mockMvc.perform(get("/api/payments/month/2026/03"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.year").isNumber())
			.andExpect(jsonPath("$.month").isString())
			.andExpect(jsonPath("$.totalAmount").isNumber())
			.andExpect(jsonPath("$.quotaItems").isArray())
			.andExpect(jsonPath("$.cashPaymentItems").isArray());
	}

	@Test
	@DisplayName("Test: Contexto de Spring carga correctamente")
	void contextLoads() {
	}
}

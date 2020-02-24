package cz.muni.fi.pa165.currency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyConvertorImplTest {

    private static final Currency CZK = Currency.getInstance("CZK");
    private static final Currency EUR = Currency.getInstance("EUR");

    @Mock
    private ExchangeRateTable exchangeRateTable;

    private CurrencyConvertorImpl currencyConvertor;

    @Before
    public void createCurrencyConvertor(){
        this.currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);
    }

    @Test
    public void assertionsDemonstration() throws ExternalServiceFailureException {

        when(exchangeRateTable.getExchangeRate(CZK,EUR)).thenReturn(new BigDecimal("0.01"));

        BigDecimal actualResult = currencyConvertor.convert(CZK, EUR, new BigDecimal("1.49"));
        BigDecimal expectedResult = new BigDecimal("0.01");

        //assertTrue(actualResult.equals(expectedResult));

        //assertEquals(expectedResult, actualResult);

        // Hamcrest way
        //assertThat(actualResult, equalTo(expectedResult));

        // AssertJ way
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void testConvert() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(CZK,EUR)).thenReturn(new BigDecimal("0.01"));

        assertThat(currencyConvertor.convert(CZK,EUR,new BigDecimal("1.49")))
                .isEqualTo("0.01");
        //???
    }

    @Test
    public void testConvertWithNullSourceCurrency() {
        fail("Test is not implemented yet.");
    }

    @Test
    public void testConvertWithNullTargetCurrency() {
        fail("Test is not implemented yet.");
    }

    @Test
    public void testConvertWithNullSourceAmount() {
        fail("Test is not implemented yet.");
    }

    @Test
    public void testConvertWithUnknownCurrency() {
        fail("Test is not implemented yet.");
    }

    @Test
    public void testConvertWithExternalServiceFailure() throws ExternalServiceFailureException {

        ExternalServiceFailureException exception = new ExternalServiceFailureException("Exchange rate lookup failed for source currency CZK and target currency EUR");
        when(exchangeRateTable.getExchangeRate(CZK,EUR)).thenThrow(exception);

        assertThatExceptionOfType(UnknownExchangeRateException.class)
                .isThrownBy(() -> currencyConvertor.convert(CZK,EUR,BigDecimal.ONE))
                .withCause(exception)
                .withMessage("Exchange rate lookup failed for source currency CZK and target currency EUR");
    }

}

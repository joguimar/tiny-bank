package es.jguimar.tinybankAPI.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class WalletTest {

    @Test
    public void builderOK() {
        Wallet result = Wallet.builder().build();

        assertThat(result).isNotNull();
    }

    @Test
    public void newOK() {
        Wallet result = new Wallet("id", null, Arrays.asList(1.4, 1.6), 3.0);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("id");
        assertThat(result.getUserIds()).isNull();
        assertThat(result.getMovements().get(0)).isEqualTo(1.4);
        assertThat(result.getMovements().get(1)).isEqualTo(1.6);
        assertThat(result.getTotalAmount()).isEqualTo(3.0);
    }

    @Test
    public void updateTotalAmountOK() {
        Wallet result = new Wallet("id", null, Arrays.asList(1.4, 1.6), null);
        result.updateTotalAmount();
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("id");
        assertThat(result.getUserIds()).isNull();
        assertThat(result.getMovements().get(0)).isEqualTo(1.4);
        assertThat(result.getMovements().get(1)).isEqualTo(1.6);
        assertThat(result.getTotalAmount()).isEqualTo(3.0);
    }


}

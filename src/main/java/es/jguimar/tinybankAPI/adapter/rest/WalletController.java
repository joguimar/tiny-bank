package es.jguimar.tinybankAPI.adapter.rest;

import es.jguimar.tinybankAPI.adapter.rest.dto.MoneyRequestDto;
import es.jguimar.tinybankAPI.adapter.rest.dto.TransferRequestDto;
import es.jguimar.tinybankAPI.adapter.rest.dto.WalletRequestDto;
import es.jguimar.tinybankAPI.adapter.rest.dto.WalletResponseDto;
import es.jguimar.tinybankAPI.adapter.rest.tranform.WalletMapper;
import es.jguimar.tinybankAPI.application.port.inbound.WalletWeb;
import es.jguimar.tinybankAPI.application.service.WalletService;
import es.jguimar.tinybankAPI.infrastructure.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@Api(value = "API tiny bank poc", tags = { "Wallet" })
@AllArgsConstructor //Constructor-Based Dependency Injection
public class WalletController implements WalletWeb {

    private final WalletService walletService;

    private final WalletMapper walletMapper;

    @Override
    public WalletResponseDto walletPost(@Valid WalletRequestDto body) {
        return walletMapper.toWalletResponseDto(walletService.create(body.getUserIds()));
    }

    @Override
    public WalletResponseDto moneyToWalletPut(String id, @Valid MoneyRequestDto body) throws ResourceNotFoundException {
        return walletMapper.toWalletResponseDto(
                walletService.putMoney(id, body.getAmount()));
    }

    @Override
    public WalletResponseDto walletGet(String id) throws ResourceNotFoundException {
        return walletMapper.toWalletResponseDto(walletService.showWallet(id));
    }

    @Override
    public WalletResponseDto moneyTransferPost(@Valid TransferRequestDto body) throws ResourceNotFoundException {
        return walletMapper.toWalletResponseDto(
                walletService.transferMoney(body.getWalletSource(), body.getWalletTarget(), body.getAmount()));
    }

}

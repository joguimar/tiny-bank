package es.jguimar.tinybankAPI.adapter.rest;

import es.jguimar.tinybankAPI.adapter.rest.dto.MoneyRequestDto;
import es.jguimar.tinybankAPI.adapter.rest.dto.TransferRequestDto;
import es.jguimar.tinybankAPI.adapter.rest.dto.WalletRequestDto;
import es.jguimar.tinybankAPI.adapter.rest.dto.WalletResponseDto;
import es.jguimar.tinybankAPI.adapter.rest.tranform.WalletMapper;
import es.jguimar.tinybankAPI.application.port.inbound.WalletWeb;
import es.jguimar.tinybankAPI.application.service.WalletService;
import es.jguimar.tinybankAPI.infrastructure.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;


@RestController
@Tag(name = "Wallet")
@AllArgsConstructor //Constructor-Based Dependency Injection
public class WalletController implements WalletWeb {

    private final WalletService walletService;

    private final WalletMapper walletMapper;

    @Override
    public WalletResponseDto walletPost(@Valid WalletRequestDto body) {
        return walletMapper.toWalletResponseDto(walletService.create(body.getUserIds()));
    }

    @Override
    public WalletResponseDto moneyToWalletPut(String id, @Valid MoneyRequestDto body) {
        try {
            return walletMapper.toWalletResponseDto(
                    walletService.putMoney(id, body.getAmount()));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public WalletResponseDto walletGet(String id) {
        try {
            return walletMapper.toWalletResponseDto(walletService.showWallet(id));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public WalletResponseDto moneyTransferPost(@Valid TransferRequestDto body) {
        try {
            return walletMapper.toWalletResponseDto(
                    walletService.transferMoney(body.getWalletSource(), body.getWalletTarget(), body.getAmount()));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}

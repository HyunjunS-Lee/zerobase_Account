package zerobase.AccountAssignment.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zerobase.AccountAssignment.domain.Account;
import zerobase.AccountAssignment.dto.AccountInfo;
import zerobase.AccountAssignment.dto.CreateAccount;
import zerobase.AccountAssignment.dto.DeleteAccount;
import zerobase.AccountAssignment.service.AccountService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/account") //계정 생성
    public CreateAccount.Response createAccount(
            //CreateAccount의 request객체를 통해서 요청이 body로 들어오고
            //그 값을 이용해서 계정을 생성
            @RequestBody @Valid CreateAccount.Request request) {

        return CreateAccount.Response.from(accountService.createAccount(
                request.getUserId(),
                request.getInitialBalance()));
    }

    @DeleteMapping("/account") //계정 삭제
    public DeleteAccount.Response deleteAccount(
            @RequestBody @Valid DeleteAccount.Request request) {

        return DeleteAccount.Response.from(accountService.deleteAccount(
                request.getUserId(),
                request.getAccountNumber()));
    }

    @GetMapping("/account")
    public List<AccountInfo> getAccountsByUserId(
            @RequestParam("user_id") Long userId
    ){
        return accountService.getAccountsByUserId(userId)
                .stream().map(accountDto ->
                        AccountInfo.builder()
                        .accountNumber(accountDto.getAccountNumber())
                        .balance(accountDto.getBalance())
                                .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/account/{id}")
    public Account getAccount(
            @PathVariable Long id){
        return accountService.getAccount(id);
    }
}

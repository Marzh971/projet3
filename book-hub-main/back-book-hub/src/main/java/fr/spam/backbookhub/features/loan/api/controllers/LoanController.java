package fr.spam.backbookhub.features.loan.api.controllers;

import fr.spam.backbookhub.features.category.services.ICategoryService;
import fr.spam.backbookhub.features.copy.services.ICopyService;
import fr.spam.backbookhub.features.loan.api.dtos.LoanRequest;
import fr.spam.backbookhub.features.loan.api.mappers.LoanMapper;
import fr.spam.backbookhub.features.loan.models.Loan;
import fr.spam.backbookhub.features.loan.services.ILoanService;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final ILoanService loanService;
    private final ICopyService copyService;
    private final LoanMapper loanMapper;

    public LoanController(ILoanService loanService, ICopyService copyService, LoanMapper loanMapper) {
        this.loanService = loanService;
        this.copyService = copyService;
        this.loanMapper = loanMapper;
    }

    @GetMapping("/{id}")
    public ServiceResponse<Loan> findById(@PathVariable Long id) {
        return loanService.findById(id);
    }

    @GetMapping("/retrieved")
    public ServiceResponse<Loan> findByDateRetrieved(
            @RequestParam LocalDateTime dateRetrieved) {
        return loanService.findByDateRetrieved(dateRetrieved);
    }

    @GetMapping("/returned")
    public ServiceResponse<Loan> findByDateReturned(
            @RequestParam LocalDateTime dateReturned) {
        return loanService.findByDateReturned(dateReturned);
    }

    @GetMapping("")
    public ServiceResponse<List<Loan>> findAll() {
        return loanService.findAll();
    }

    @PostMapping("")
    public ResponseEntity<ServiceResponse<Loan>> create(@RequestBody LoanRequest loanRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(loanService.createWithLink(loanMapper.toLoan(loanRequest)));
    }


    @PutMapping("")
    public ServiceResponse<Loan> update(@RequestBody Loan loan) {
        return loanService.update(loan);
    }

    @DeleteMapping("/{id}")
    public ServiceResponse<Void> delete(@PathVariable Long id) {
        return loanService.delete(id);
    }
}
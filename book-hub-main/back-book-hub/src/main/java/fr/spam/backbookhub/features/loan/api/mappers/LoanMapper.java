package fr.spam.backbookhub.features.loan.api.mappers;

import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.loan.api.dtos.LoanListResponse;
import fr.spam.backbookhub.features.loan.api.dtos.LoanRequest;
import fr.spam.backbookhub.features.loan.api.dtos.LoanResponse;
import fr.spam.backbookhub.features.loan.models.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "copy.book.isbn", source = "bookIsbn")
    @Mapping(target = "user", expression = "java(toUserRef(loanRequest.userId()))")
    Loan toLoan(LoanRequest loanRequest);

    default User toUserRef(String userId) {
        User user = new User();
        user.setId(UUID.fromString(userId));
        return user;
    }

    LoanResponse toLoanResponse(Loan loan);
    LoanListResponse toLoanListResponse(Loan loan);
}
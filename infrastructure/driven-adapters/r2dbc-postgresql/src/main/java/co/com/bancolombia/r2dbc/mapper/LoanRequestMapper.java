package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.loanrequest.LoanRequest;
import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.r2dbc.entity.LoanRequestEntity;

public class LoanRequestMapper {

    public static LoanRequestEntity toEntity(LoanRequest loanRequest) {
        return LoanRequestEntity.builder()
                .idLoanRequest(loanRequest.getIdLoanRequest())
                .userId(loanRequest.getUser().getIdUser())
                .amount(loanRequest.getAmount())
                .term(loanRequest.getTerm())
                .email(loanRequest.getUser().getEmail())
                .statusId(loanRequest.getStatus() != null ? loanRequest.getStatus().getIdStatus() : null)
                .loanTypeId(loanRequest.getLoanType() != null ? loanRequest.getLoanType().getIdLoanType() : null)
                .build();
    }

    public static LoanRequest toDomain(LoanRequestEntity entity, Status status, LoanType loanType, User user) {
        return LoanRequest.builder()
                .idLoanRequest(entity.getIdLoanRequest())
                .user(user)
                .amount(entity.getAmount())
                .term(entity.getTerm())
                .email(entity.getEmail())
                .status(status)
                .loanType(loanType)
                .build();
    }


    public static LoanRequest toDomain(LoanRequestEntity entity) {
        return LoanRequest.builder()
                .idLoanRequest(entity.getIdLoanRequest())
                .user(User.builder()
                        .idUser(entity.getUserId())
                        .build())
                .amount(entity.getAmount())
                .term(entity.getTerm())
                .email(entity.getEmail())
                .status(Status.builder()
                        .idStatus(entity.getStatusId())
                        .build())
                .loanType(LoanType.builder()
                        .idLoanType(entity.getLoanTypeId())
                        .build())
                .build();
    }
}

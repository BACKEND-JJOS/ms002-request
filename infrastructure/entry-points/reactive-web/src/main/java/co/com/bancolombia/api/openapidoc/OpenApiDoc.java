package co.com.bancolombia.api.openapidoc;

import co.com.bancolombia.api.response.ApiResponse;
import co.com.bancolombia.model.loanrequest.LoanRequest;
import lombok.experimental.UtilityClass;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

@UtilityClass
public class OpenApiDoc {

    private static  final  String MEDIA_TYPE_APPLICATION_JSON = "application/json";

    private static final String TAG_USER = "LOAN_REQUEST";

    public Builder createLoanRequest(Builder builder){
        return builder.operationId("loanRequest")
                .description("Created a new loan request")
                .requestBody(
                        requestBodyBuilder()
                                .required(true)
                                .content(
                                        contentBuilder()
                                                .mediaType(MEDIA_TYPE_APPLICATION_JSON)
                                                .schema(schemaBuilder().implementation(LoanRequest.class))
                                )
                )
                .response(
                        responseBuilder()
                                .responseCode(HttpStatus.CREATED.toString())
                                .description("Loan Request created succesfully")
                                .content(
                                        contentBuilder()
                                                .mediaType(MEDIA_TYPE_APPLICATION_JSON)
                                                .schema(schemaBuilder().implementation(ApiResponse.class))
                                )

                )
                .tag(TAG_USER);
    }
}

package org.appsdeveloperblog.estore.UserService.query;

import com.appsdeveloperblog.estore.Core.model.PaymentDetails;
import com.appsdeveloperblog.estore.Core.model.User;
import com.appsdeveloperblog.estore.Core.query.FetchUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

@Service
public class UserEventsHandlerImpl implements UserEvents {

    @QueryHandler
    public User on(FetchUserPaymentDetailsQuery query) {
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cardNumber("123Card")
                .cvv("123")
                .name("SERGEY KARGOPOLOV")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();

        User user = User.builder()
                .firstName("Sergey")
                .lastName("Kargopolov")
                .userId(query.getUserId())
                .paymentDetails(paymentDetails)
                .build();
        return user;
    }
}

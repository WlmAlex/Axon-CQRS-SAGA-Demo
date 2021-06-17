package org.appsdeveloperblog.estore.UserService.query;

import com.appsdeveloperblog.estore.Core.model.User;
import com.appsdeveloperblog.estore.Core.query.FetchUserPaymentDetailsQuery;

public interface UserEvents {

    User on(FetchUserPaymentDetailsQuery query);

}

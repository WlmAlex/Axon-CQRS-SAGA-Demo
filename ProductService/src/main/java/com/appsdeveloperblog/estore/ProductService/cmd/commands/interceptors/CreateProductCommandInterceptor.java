package com.appsdeveloperblog.estore.ProductService.cmd.commands.interceptors;

import com.appsdeveloperblog.estore.ProductService.cmd.commands.CreateProductCommand;
import com.appsdeveloperblog.estore.ProductService.core.data.ProductEntity;
import com.appsdeveloperblog.estore.ProductService.core.data.ProductLookupEntity;
import com.appsdeveloperblog.estore.ProductService.core.data.ProductLookupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookupRepository productLookupRepository;

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> list) {
        return (index, command) -> {
            log.info("Intercepted command: {}", command.getPayloadType());
            if (CreateProductCommand.class.equals(command.getPayloadType())) {
                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
                if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("Price can not be equal or less than zero");
                }

                if (createProductCommand.getQuantity() <= 0) {
                    throw new IllegalArgumentException("Quantity must be greater than zero");
                }

                String title = createProductCommand.getTitle();
                if (StringUtils.isBlank(title)) {
                    throw new IllegalArgumentException("Title must have a valid value");
                }
                String productId = createProductCommand.getProductId();
                ProductLookupEntity productLookupEntity = productLookupRepository.findByProductIdOrTitle(productId, title);
                if (productLookupEntity != null) {
                    throw new IllegalArgumentException(String.format(
                            "Product with id %s or title %s already exists!!!", productId, title));
                }
            }
            return command;
        };
    }
}

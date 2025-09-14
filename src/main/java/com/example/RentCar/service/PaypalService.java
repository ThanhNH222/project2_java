package com.example.RentCar.service;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class PaypalService {
    private final PayPalHttpClient payPalClient;

    public PaypalService(PayPalHttpClient payPalClient) {
        this.payPalClient = payPalClient;
    }

    public String createOrder(String amount, String currency, String returnUrl, String cancelUrl) throws IOException {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl);
        orderRequest.applicationContext(applicationContext);

        List<PurchaseUnitRequest> purchaseUnits = Arrays.asList(
                new PurchaseUnitRequest()
                        .amountWithBreakdown(new AmountWithBreakdown().currencyCode(currency).value(amount))
        );
        orderRequest.purchaseUnits(purchaseUnits);

        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        HttpResponse<Order> response = payPalClient.execute(request);
        if (response.statusCode() == 201) {
            for (LinkDescription link : response.result().links()) {
                if ("approve".equals(link.rel())) {
                    return link.href(); // URL để redirect user qua PayPal
                }
            }
        }
        return null;
    }

    public HttpResponse<Order> captureOrder(String orderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        return payPalClient.execute(request);
    }
}
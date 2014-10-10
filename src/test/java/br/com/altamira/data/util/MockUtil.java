/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.util;

import br.com.altamira.data.model.sales.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.MessageFormat;

/**
 *
 * @author alessandro.holanda
 */
public class MockUtil {
    
    public static Order getMockupOrder(Long number) throws IOException {
        Order order = new Order();
        
        ObjectMapper mapper = new ObjectMapper();
        String jsonFile = MessageFormat.format("/order_{0}.json", number.toString());
        return mapper.readValue(order.getClass().getResourceAsStream(jsonFile), Order.class);
    }
}

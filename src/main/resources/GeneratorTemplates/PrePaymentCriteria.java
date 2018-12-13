package com.paratera.core.prepayment.service.dto.query;

import com.paratera.query.BigDecimalFilter;
import com.paratera.query.StringFilter;
import com.paratera.query.ZonedDateTimeFilter;
import lombok.Data;

import java.io.Serializable;

@Data
public class PrePaymentCriteria implements Serializable{

    private StringFilter name;

    private StringFilter userId;

    private BigDecimalFilter prePaymentLimit;

    private ZonedDateTimeFilter createAt;

    private ZonedDateTimeFilter updateAt;
    <% if(item.type == "String"){ %>
    private StringFilter ${item.name};

    <%}else if(item.type == "Integer") {%>
    private IntegerFilter ${item.name};

    <%}else if(item.type == "Boolean") {%>
    private BooleanFilter ${item.name};

    <%}else if(item.type == "BigDecimal") {%>
    private BigDecimalFilter ${item.name};

    <%}else if(item.type == "Instant") {%>
    private InstantFilter ${item.name};

    <%}else if(item.type == "ZonedDateTime") {%>
    private ZonedDateTimeFilter ${item.name};

    <%}else{%>
        private String ${item.name};
    <%}}%>

}

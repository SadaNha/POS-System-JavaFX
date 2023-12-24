package dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class OrderDetailDto {
    private String orderId;
    private String itemCode;
    private int qty;
    private double unitPrice;
}

package entity;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Orders {
    private String orderId;
    private  String date;
    private String customerId;
}

package dto;

import com.jfoenix.controls.JFXButton;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ItemDto {

    private String code;
    private String desc;
    private Double unitPrice;
    private int qty;
    private JFXButton btn;

    public ItemDto(String code, String desc, Double unitPrice, int qty) {
        this.code = code;
        this.desc = desc;
        this.unitPrice = unitPrice;
        this.qty = qty;
    }
}

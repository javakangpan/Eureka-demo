package demo.model;

import lombok.Data;
import org.joda.money.Money;

import java.util.Date;

@Data
public class Coffee {
    private Long id;
    private String name;
    private Money price;
    private int count;
    private Date createTime;
    private Date updateTime;

}

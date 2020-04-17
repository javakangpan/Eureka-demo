package demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoffeeRequest {
    private String name;
    private int count;
    private boolean flag;
}

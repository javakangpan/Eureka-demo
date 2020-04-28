package demo.mapStruct;

import demo.model.Coffee;
import demo.model.MapStructStuCoffee;
import demo.model.Student;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-04-28T18:12:02+0800",
    comments = "version: 1.2.0.CR1, compiler: javac, environment: Java 1.8.0_191 (Oracle Corporation)"
)
@Component
public class MapStructStuCoffeeMapperImpl implements MapStructStuCoffeeMapper {

    @Override
    public MapStructStuCoffee from(Student student, Coffee coffee) {
        if ( student == null && coffee == null ) {
            return null;
        }

        MapStructStuCoffee mapStructStuCoffee = new MapStructStuCoffee();

        if ( student != null ) {
            mapStructStuCoffee.setName( student.getName() );
        }
        if ( coffee != null ) {
            mapStructStuCoffee.setCoffeeName( coffee.getName() );
        }

        return mapStructStuCoffee;
    }
}

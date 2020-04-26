package demo.mapStruct;

import demo.model.Coffee;
import demo.model.MapStructStuCoffee;
import demo.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * arget/generated-sources/annotations目录下查看对应Mapper实现类
 */
@Mapper(componentModel = "spring")
public interface MapStructStuCoffeeMapper {
   public static MapStructStuCoffeeMapper MAPPER = Mappers.getMapper(MapStructStuCoffeeMapper.class);
    @Mappings({
            @Mapping(source = "student.name",target = "name"),
            @Mapping(source = "coffee.name",target = "coffeeName")
    })
    public MapStructStuCoffee from(Student student, Coffee coffee);
}

package demo.mapper;

import demo.model.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudentMapper {

    public Student getStudentById(int id);
    /*对象传参法 #{}里面的名称对应的是User类里面的成员属性*/
    public int addStudent(Student student);
    /*@Param注解传参法 #{}里面的名称对应的是注解@Param括号里面修饰的名称*/
    public int updateStudentName(@Param("name") String name, @Param("id") int id);

    public Student getStudentByIdWithClassInfo(int id);

    public List<Student> listUserLikeUsername(@Param("name") String name);
    /*顺序传参法 #{}里面的数字代表传入参数的顺序*/
    public int updateStudentName2(String name,int id);

    /*Map传参法 #{}里面的名称对应的是Map里面的key名称*/
    public Student getStudent(Map<String, Object> params);
}

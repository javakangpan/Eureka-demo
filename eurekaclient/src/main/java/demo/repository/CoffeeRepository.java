package demo.repository;

import demo.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
    Coffee findByName(String name);
    @Transactional
    @Modifying
    /**
     * nativeQuery = true 原生sql语句 字段对于数据库的字段 表对于数据库的表
     * nativeQuery = false 不是原生sql语句 字段对应实体的属性 表对于实体的类
     */
    @Query(value = "update t_coffee set count=:count where name=:name",nativeQuery = true)
    void updateCoffeeCount(@Param("name")String name, @Param("count")int count);

}


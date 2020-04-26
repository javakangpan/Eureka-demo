package demo.test.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Logs implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 操作用户 */
    private String username;

    /** 描述 */
    private String description;

    /** 方法名 */
    private String method;

    /** 参数 */
    @Column(columnDefinition = "text")
    private String params;

    /** 日志类型 */
    @Column(name = "log_type")
    private String logType;

    /** 请求ip */
    @Column(name = "request_ip")
    private String requestIp;

    /** 地址 */
    @Column(name = "address")
    private String address;

    /** 浏览器  */
    private String browser;

    /** 请求耗时 */
    private Long time;

    /** 异常详细  */
    @Column(name = "exception_detail", columnDefinition = "text")
    private String exceptionDetail;

    /** 创建日期 */
    @CreationTimestamp
    @Column(name = "create_time")
    private Timestamp createTime;

    /** 返回值 */
    @Column(columnDefinition = "text",name = "return_value")
    private String returnValue;
}

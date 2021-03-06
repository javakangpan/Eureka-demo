package demoTest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import java.io.File;

/**
 * ip查询处理
 *
 * @author videomonster
 */
@Slf4j
public class Ip2regionTest {

    private static final String UNKOWN_ADDRESS = "未知位置";

    /**
     * 根据IP获取地址
     *
     * @return 国家|区域|省份|城市|ISP
     */
    public static String getAddress(String ip) {
        return getAddress(ip, DbSearcher.BTREE_ALGORITHM);
    }

    /**
     * 根据IP获取地址
     *
     * @param ip
     * @param algorithm 查询算法
     * @return 国家|区域|省份|城市|ISP
     * @see DbSearcher
     * DbSearcher.BTREE_ALGORITHM; //B-tree
     * DbSearcher.BINARY_ALGORITHM //Binary
     * DbSearcher.MEMORY_ALGORITYM //Memory
     */
    @SneakyThrows
    public static String getAddress(String ip, int algorithm) {
        if (!Util.isIpAddress(ip)) {
            log.error("错误格式的ip地址: {}", ip);
            return UNKOWN_ADDRESS;
        }
        String dbPath ="../ip2region.db";
        File file = new File(dbPath);
        if (!file.exists()) {
            log.error("地址库文件不存在");
            return UNKOWN_ADDRESS;
        }
        DbSearcher searcher = new DbSearcher(new DbConfig(), dbPath);
        DataBlock dataBlock;
        switch (algorithm) {
            case DbSearcher.BTREE_ALGORITHM:
                dataBlock = searcher.btreeSearch(ip);
                break;
            case DbSearcher.BINARY_ALGORITHM:
                dataBlock = searcher.binarySearch(ip);
                break;
            case DbSearcher.MEMORY_ALGORITYM:
                dataBlock = searcher.memorySearch(ip);
                break;
            default:
                log.error("未传入正确的查询算法");
                return UNKOWN_ADDRESS;
        }
        return dataBlock.getRegion();
    }
    @Test
    public void test() {
        log.info("开始查询");
        System.out.println(Ip2regionTest.getAddress("106.35.112.88"));
        System.out.println(Ip2regionTest.getAddress("222.190.125.42"));
        System.out.println(Ip2regionTest.getAddress("206.77.131.86"));
        System.out.println(Ip2regionTest.getAddress("116.37.161.86"));
        System.out.println(Ip2regionTest.getAddress("136.27.231.86"));
        System.out.println(Ip2regionTest.getAddress("127.0.0.1"));
        System.out.println(Ip2regionTest.getAddress("112.17.236.511"));
        System.out.println(Ip2regionTest.getAddress("192.168.0.128"));
        log.info("查询完成");
    }
}

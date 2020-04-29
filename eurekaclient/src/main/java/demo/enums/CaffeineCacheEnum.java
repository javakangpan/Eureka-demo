package demo.enums;

public enum  CaffeineCacheEnum {

    COFFEE3(30,100),
    COFFEE4(60,100);

    private int maxSize = 1000;    //最大数量
    private int ttl = 1;        //过期时间（秒）
    CaffeineCacheEnum(){}
    CaffeineCacheEnum(int ttl) {
        this.ttl = ttl;
    }
    CaffeineCacheEnum(int ttl, int maxSize) {
        this.ttl = ttl;
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getTtl() {
        return ttl;
    }
}

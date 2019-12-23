package com.myjdbc.redis.util;

import org.springframework.stereotype.Component;


@Component
public class RedisUtil {
//    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
//
//    private static RedisUtil redisUtil;
//
//    @Autowired
//    private RedisBaseServiceImpl redisService;
//
//    @Autowired
//    private BaseService baseService;
//
//    @PostConstruct
//    public void init() {
//        redisUtil = this;
//        redisUtil.redisService = this.redisService;
//        redisUtil.baseService = this.baseService;
//    }
//
//    /**
//     * @Author 陈文
//     * @Date 2019/12/11  10:50
//     * @Description 获取缓存中的实体
//     */
//    public static <T> T getPojo(Class<T> cls, String id) {
//        Map<String, Object> map = getPojoMap(cls, id);
//        if (map != null) {
//            T t = BeanUtil.mapToPrjo(cls, map);
//            return t;
//        }
//        return null;
//    }
//
//    /**
//     * @Author 陈文
//     * @Date 2019/12/11  16:29
//     * @Description 通过缓存存储实体
//     */
//    public static void setPojo(Object obj) {
//        try {
//            redisUtil.baseService.save(obj);
//            setPojoMap(obj.getClass(), getKey(obj), BeanUtil.pojoToMap(obj, true));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @Author 陈文
//     * @Date 2019/12/11  16:29
//     * @Description 通过缓存存储实体
//     */
//    public static void deletePojo(Object obj) {
//        redisUtil.baseService.delete(obj);
//        redisUtil.redisService.delete(getKey(obj));
//    }
//
//    /**
//     * @Author 陈文
//     * @Date 2019/12/11  10:50
//     * @Description 获取缓存中的实体
//     */
//    public static Map<String, Object> getPojoMap(Class<? extends Object> cls, String id) {
//        String key = getKey(cls, id);
//        Map<String, Object> map = redisUtil.redisService.getMap(key);
//        //如果存在，直接返回
//        if (map != null && map.size() > 0) {
//            return map;
//        }
//
//        try {
//            //从数据库查找实体
//            map = redisUtil.baseService.findMapById(cls, id);
//            setPojoMap(cls, key, map);
//            return map;
//        } catch (SQLException e) {
//            logger.error("缓存工具->获取实体故障：" + e.getMessage());
//            return null;
//        }
//    }
//
//    public static void setPojoMap(Class cls, String key, Map<String, Object> map) {
//        redisUtil.redisService.setMap(key, map);
//        //同步分开保存缓存实体的字典字段
////        setPojoMapDictionary(cls, map);
//    }
//
//    private static String getKey(Class<? extends Object> cls, String id) {
//        String key = BeanUtil.getTableName(cls) + id;
//        return key;
//    }
//
//    private static String getKey(Object t) {
//        String key = BeanUtil.getTableName(t.getClass()) + BeanUtil.getId(t);
//        return key;
//    }
//
//    /**
//     * @Author 陈文
//     * @Date 2019/12/10  22:17
//     * @Description 字典匹配
//     */
//    public static Object getDictionaryValue(String tableName, String fieldName, String replaceName, String value) {
//        String key = tableName + fieldName + value;
//        String obj = redisUtil.redisService.get(key);
//        //如果缓存里有，则取出
//        if (obj != null) {
//            return obj;
//        }
//        //从数据库中查找
//        obj = findDictionaryValue(tableName, fieldName, replaceName, value);
//        //存储
//        setDictionaryValue(key, obj);
//        return obj;
//    }
//
//    public static void setDictionaryValue(String key, String value) {
//        redisUtil.redisService.set(key, value);
//    }
//
//    private static String findDictionaryValue(String tableName, String fieldName, String replaceName, String value) {
//        try {
//            StringBuffer sql = new StringBuffer(" SELECT ").append(replaceName).append(" FROM ").
//                    append(tableName).append(" WHERE ").append(fieldName).
//                    append("='").append(value).append("'");
//            List<Object[]> list = redisUtil.baseService.findAll(sql + "");
//            if (list != null && list.size() == 1) {
//                return list.get(0)[0] + "";
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}

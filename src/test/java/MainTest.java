import dao.PCodeDao;
import entity.po.PCode;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTest {
    PCodeDao pCodeDao = PCodeDao.getInstance();

    @Test
    public void test1() {
        for (int x = 0; x < 5; x++) {
            PCode[] list = new PCode[20000];
            for (int i = 0; i < 20000; i++) {
                PCode pCode = new PCode();
                pCode.setShortId(new BigInteger("25"));
                pCode.setCodeType("9876543");
                pCode.setValue(x + i + "");
                pCode.setCodeNane("测试" + x + i);
                list[i] = pCode;
            }
            long startTime = System.nanoTime();   //获取开始时间
            save(list);
            long endTime = System.nanoTime(); //获取结束时间
            System.out.println("程序运行时间： " + (endTime - startTime) / 1000 / 1000 + "ms");
        }
    }

    public boolean save2(PCode[] pCodes) {
        for (PCode pCode : pCodes) {
            pCodeDao.save(pCode);
        }
        return true;
    }


    @Test
    public void delete() {
        Map<String, Object> map = new HashMap<>();
        map.put("codeType", "9876543");
        List<PCode> list = findAll(map);
        System.out.println(list.size());
        List<Object> ids = new ArrayList<>();
        for (PCode pCode : list) {
            ids.add(pCode.getCodeId());
        }
        long startTime = System.nanoTime();   //获取开始时间
        delete2(ids.toArray());
        long endTime = System.nanoTime(); //获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) / 1000 / 1000 + "ms");
    }

    public boolean delete2(Object[] ids) {
        for (Object id : ids) {
            pCodeDao.delete(id);
        }
        return true;
    }

    public boolean delete(Object... ids) {
        if (ids.length == 1) {
            return pCodeDao.delete(ids[0]);
        } else {
            return pCodeDao.deletes(ids);
        }
    }

    public boolean save(PCode... pCodes) {
        if (pCodes.length == 1) {
            return pCodeDao.save(pCodes[0]);
        } else {
            return pCodeDao.saves(pCodes);
        }
    }

    public PCode findById(String value) {
        PCodeDao pCodeDao = PCodeDao.getInstance();
        PCode pCode = pCodeDao.findById(value);
        return pCode;
    }

    public List<PCode> findAll(PCode po) {
        PCodeDao pCodeDao = PCodeDao.getInstance();
        List<PCode> list = pCodeDao.findAll(po);
        return list;
    }

    public List<PCode> findAll(Map map) {
        PCodeDao pCodeDao = PCodeDao.getInstance();
        List<PCode> list = pCodeDao.findAll(map);
        return list;
    }

    public List<PCode> findAll(String sql, Object... objs) {
        PCodeDao pCodeDao = PCodeDao.getInstance();
        List<PCode> list = pCodeDao.findAll(sql, objs);
        return list;
    }

    public List<Map<String, Object>> findAllMap(String sql, Object... objs) {
        PCodeDao pCodeDao = PCodeDao.getInstance();
        List<Map<String, Object>> list = pCodeDao.findAllMap(sql, objs);
        return list;
    }

    public List<Object[]> findAllObjects(String sql, Object... objs) {
        PCodeDao pCodeDao = PCodeDao.getInstance();
        List<Object[]> list = pCodeDao.findAllObjects(sql, objs);
        return list;
    }

}

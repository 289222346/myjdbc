package dao;

import com.myjdbc.sql.impl.BaseDaoMysql;
import com.myjdbc.util.PoolConnection;
import entity.po.PCode;

public class PCodeDao extends BaseDaoMysql<PCode> {



    private PCodeDao() {
        super(PCode.class, "codeId");
    }

    private PCodeDao(PoolConnection conn) {
        super(PCode.class, "codeId", conn);
    }

    private static volatile PCodeDao pCodeDao;

    public static PCodeDao getInstance() {
        if (pCodeDao == null) {
            synchronized (PCodeDao.class) {
                if (pCodeDao == null) {
                    pCodeDao = new PCodeDao();
                }
            }
        }
        return pCodeDao;
    }

}

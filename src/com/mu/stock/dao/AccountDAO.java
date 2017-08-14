package com.mu.stock.dao;

import com.mu.db.jpa.criteria.Filter;
import com.mu.db.jpa.criteria.Sequence;
import com.mu.db.jpa.dao.BaseEmbeddedDAO;
import com.mu.stock.entity.Account;
import com.mu.stock.entity.AccountLog;
import com.mu.stock.entity.TxnLog;
import com.mu.util.DateUtil;
import com.mu.util.RandomUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class AccountDAO extends BaseEmbeddedDAO
{
    static public Account createAcct(String name, Float capital, String dateFrom, String dateTo, String strategy)
    {
        Account re = new Account();
        re.setName(name);
        re.setCapital(capital);
        re.setCash(capital);
        re.setCreateTime(new Date());
        re.setStrategy(strategy);
        Date d = RandomUtil.pickDate(DateUtil.toDate(dateFrom), DateUtil.toDate(dateTo));
        re.setStartDate(d);
        re.setCurrentDate(d);
        re.setDateDrift(RandomUtil.pickInt(-2000, 2000));
        re.setPriceDrift(RandomUtil.pickFloat(0F, 3F));
        insert(re);
        return re;
    }

    static public List<Account> getAll()
    {
        Filter f = new Filter("Account");
        f.addOrderBy(new Sequence("createTime"));
        return (List<Account>)get(f);
    }
    static public List<Account> getByName(String name)
    {
        Filter f = new Filter("Account");
        f.addEqual("name", name);
        f.addOrderBy(new Sequence("createTime"));
        return (List<Account>)get(f);
    }
    static public void main(String[] argu)
    {
        AccountDAO.createAcct("test", 100000F, "1999-01-01", "2010-01-01", "");
    }
    static void remove(Account c)
    {
        BaseEmbeddedDAO.remove(c);
    }
    static public List<TxnLog> getTxnLog(Account c)
    {
        List<TxnLog> re = new ArrayList<TxnLog>();
        for(AccountLog al : c.getAcctLogs())
        {
            re.addAll(al.getTxns());
        }
        re.addAll(c.getSettledTxn());
        re.addAll(c.getPendingTxn());
        Collections.reverse(re);
        return re;
    }


}

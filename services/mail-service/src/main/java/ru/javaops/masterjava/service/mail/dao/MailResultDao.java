package ru.javaops.masterjava.service.mail.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.AbstractDao;
import ru.javaops.masterjava.service.mail.model.SendResult;

import java.util.List;

/**
 * @author Dmitriy Panfilov
 * 16.02.2021
 */
@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailResultDao implements AbstractDao {
    @Override
    @SqlUpdate("TRUNCATE mailsender")
    public abstract void clean() ;

    public SendResult insert(SendResult sendResult) {
        if (sendResult.isNew()) {
            int id = insertGeneratedId(sendResult);
            sendResult.setId(id);
        } else {
            insertWitId(sendResult);
        }
        return sendResult;
    }

    @SqlQuery("SELECT nextval('common_seq')")
    abstract int getNextVal();

    @Transaction
    public int getSeqAndSkip(int step) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("SELECT setval('common_seq', " + (id + step - 1) + ")"));
        return id;
    }

    @SqlUpdate("INSERT INTO mailsender (date, address, is_send) VALUES (:dateTime, :address, :result) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean SendResult sendResult);

    @SqlUpdate("INSERT INTO mailsender (id, date, address, is_send) VALUES (:id, :dateTime, :address, :result) ")
    abstract void insertWitId(@BindBean SendResult sendResult);

    @SqlQuery("SELECT * FROM mailsender ORDER BY full_name, email LIMIT :it")
    public abstract List<SendResult> getWithLimit(@Bind int limit);

    //    https://habrahabr.ru/post/264281/
    @SqlBatch("INSERT INTO mailsender (date, address, is_send) VALUES (:dateTime, :address, :result)" )
    public abstract int[] insertBatch(@BindBean List<SendResult> sendResults, @BatchChunkSize int chunkSize);

}

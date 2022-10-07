package com.app.habit.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.app.habit.data.model.Usage;

import java.util.Date;
import java.util.List;

@Dao
public interface UsagesDao {

    @Query("SELECT * FROM usages ORDER BY usage_day")
    List<Usage> getAll();

    @Query("SELECT * FROM usages WHERE usage_day = :usageDay")
    Usage get(Date usageDay);

    @Query("SELECT * FROM usages WHERE usage_day >= :startDay AND usage_day <= :endDay ORDER BY usage_day")
    List<Usage> getInInterval(Date startDay, Date endDay);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUsageReplace(Usage usage);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertUsage(Usage usage);

    @Update
    int updateUsage(Usage usage);

    @Transaction
    default int insertOrUpdate(Usage usage) {

        long result = insertUsage(usage);
        if (result == -1L)
            return updateUsage(usage);

        return (int) result;
    }

    @Transaction
    default int insertOrUpdateActions(Usage usage) {

        Usage usageResult = get(usage.usageDay);
        if (usageResult == null)
            return (int) insertUsage(usage);

        usage.instagram = usageResult.instagram;
        usage.facebook = usageResult.facebook;
        usage.youtube = usageResult.youtube;
        usage.linkedin = usageResult.linkedin;
        usage.pinterest = usageResult.pinterest;
        usage.twitter = usageResult.twitter;

        return updateUsage(usage);
    }

    @Transaction
    default int insertOrUpdateApps(Usage usage) {

        Usage usageResult = get(usage.usageDay);
        if (usageResult == null)
            return (int) insertUsage(usage);

        usage.driving = usageResult.driving;
        usage.moving = usageResult.moving;
        usage.beSit = usageResult.beSit;

        return updateUsage(usage);
    }

    @Query("DELETE FROM usages")
    void deleteAll();
}

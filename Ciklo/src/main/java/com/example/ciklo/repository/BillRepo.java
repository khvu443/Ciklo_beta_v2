package com.example.ciklo.repository;

import com.example.ciklo.model.Bill;
import com.example.ciklo.model.Customer;
import com.example.ciklo.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface BillRepo<T> extends JpaRepository<Bill, Integer> {

    List<Bill> findBillsByCus(Customer cus);

    //------------------------------------------------------------------------------------------------------------------
    //Driver

    List<Bill> findBillsByDriver(Driver driver);
    @Query(value = "select * \n" +
            "from _bill\n" +
            "where driver_driver_id = :driverId\n" +
            "and cast(to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM') as DATE) = cast(now() as date)", nativeQuery = true)
    List<Bill> getBillsTodayByDriverId(@Param("driverId") int driverId);

    @Query(
            value = "select cast(sum(total) as decimal(10,1))\n" +
                    "from _bill\n" +
                    "where driver_driver_id = :driverId\n" +
                    "  and cast(to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM') as DATE) = cast(now() as date)", nativeQuery = true
    )
    Double getTotalBillsToday(@Param("driverId") int driverId);

    @Query(
            value = "select *\n" +
                    "from _bill\n" +
                    "where driver_driver_id = :driverId\n" +
                    "and EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = extract(month from now())", nativeQuery = true
    )
    List<Bill> getBillsMonthByDriverId(@Param("driverId") int driverId);

    @Query(
            value = "select cast (sum(total) as decimal(10,1))" +
                    "from _bill " +
                    "where driver_driver_id = :driverId " +
                    "and EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = extract(month from now())", nativeQuery = true
    )
    Double getTotalBillsMonth(@Param("driverId") int driverId);

    @Query(value = "select\n" +
            "sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 1 then total else 0 end) as JAN,\n" +
            "sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 2 then total else 0 end) as FEB,\n" +
            "sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 3 then total else 0 end) as MAR,\n" +
            "sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 4 then total else 0 end) as APR,\n" +
            "sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 5 then total else 0 end) as MAY,\n" +
            "sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 6 then total else 0 end) as JUN,\n" +
            "sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 7 then total else 0 end) as JUL,\n" +
            "sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 8 then total else 0 end) as AUG,\n" +
            "sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 9 then total else 0 end) as SEP,\n" +
            "sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 10 then total else 0 end) as OCT,\n" +
            "sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 11 then total else 0 end) as NOV,\n" +
            "sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 12 then total else 0 end) as DEC\n" +
            "from _bill\n" +
            "where driver_driver_id = :driverId\n" +
            "and to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM') >= to_timestamp( cast (EXTRACT(YEAR from now()) as varchar), 'YYYY  HH12:MI:SS PM')\n" +
            "and to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM') <= to_timestamp(cast (EXTRACT(YEAR from now()) as varchar), 'YYYY  HH12:MI:SS PM') + interval '1 year'\n" +
            "GROUP BY  EXTRACT(YEAR FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM'));", nativeQuery = true)
    public T getTotalBillAllMonthsByDriverId(@Param("driverId") int driverId);

    @Query(value = "select\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 1 then date_trip else null end) as JAN,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 2 then date_trip else null end) as FEB,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 3 then date_trip else null end) as MAR,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 4 then date_trip else null end) as APR,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 5 then date_trip else null end) as MAY,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 6 then date_trip else null end) as JUN,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 7 then date_trip else null end) as JUL,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 8 then date_trip else null end) as AUG,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 9 then date_trip else null end) as SEP,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 10 then date_trip else null end) as OCT,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 11 then date_trip else null end) as NOV,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 12 then date_trip else null end) as DEC\n" +
            "from _bill\n" +
            "where driver_driver_id = :driverId\n" +
            "and to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM') >= to_timestamp( cast (EXTRACT(YEAR from now()) as varchar), 'YYYY  HH12:MI:SS PM')\n" +
            "and to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM') <= to_timestamp(cast (EXTRACT(YEAR from now()) as varchar), 'YYYY  HH12:MI:SS PM') + interval '1 year'\n" +
            "GROUP BY  EXTRACT(YEAR FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM'));", nativeQuery = true)
    public T getNumberTripsAllMonthsByDriverId(@Param("driverId") int driverId);

    //----------------------------------------------------------------------------------------------------------------
    //Admin
    @Query(value = "select * \n" +
            "from _bill\n" +
            "where  cast(to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM') as DATE) = cast(now() as date)", nativeQuery = true)
    List<Bill> getAllBillsToday();

    @Query(
            value = "select CAST(sum(total) as Decimal(10, 1))\n" +
                    "from _bill\n" +
                    "where cast(to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM') as DATE) = cast(now() as date)", nativeQuery = true
    )
    Double getTotalAllBillsToday();

    @Query(
            value = "select *\n" +
                    "from _bill\n" +
                    "where EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = extract(month from now())", nativeQuery = true
    )
    List<Bill> getBillsMonth();

    @Query(
            value = "select CAST(sum(total) as Decimal(10,1)) " +
                    "from _bill " +
                    "where EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = extract(month from now())", nativeQuery = true
    )
    Double getTotalAllBillsMonth();


    @Query(value = "select\n" +
            "    sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 1 then total else 0 end) as JAN,\n" +
            "    sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 2 then total else 0 end) as FEB,\n" +
            "    sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 3 then total else 0 end) as MAR,\n" +
            "    sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 4 then total else 0 end) as APR,\n" +
            "    sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 5 then total else 0 end) as MAY,\n" +
            "    sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 6 then total else 0 end) as JUN,\n" +
            "    sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 7 then total else 0 end) as JUL,\n" +
            "    sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 8 then total else 0 end) as AUG,\n" +
            "    sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 9 then total else 0 end) as SEP,\n" +
            "    sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 10 then total else 0 end) as OCT,\n" +
            "    sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 11 then total else 0 end) as NOV,\n" +
            "    sum(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 12 then total else 0 end) as DEC\n" +
            "from _bill\n" +
            "where "+
            "to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM') >= to_timestamp( cast (EXTRACT(YEAR from now()) as varchar), 'YYYY  HH12:MI:SS PM')\n" +
            "and to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM') <= to_timestamp(cast (EXTRACT(YEAR from now()) as varchar), 'YYYY  HH12:MI:SS PM') + interval '1 year'\n" +
            "GROUP BY  EXTRACT(YEAR FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM'));", nativeQuery = true)
    public T getTotalBillAllMonths();

    @Query(value = "select\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 1 then date_trip else null end) as JAN,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 2 then date_trip else null end) as FEB,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 3 then date_trip else null end) as MAR,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 4 then date_trip else null end) as APR,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 5 then date_trip else null end) as MAY,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 6 then date_trip else null end) as JUN,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 7 then date_trip else null end) as JUL,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 8 then date_trip else null end) as AUG,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 9 then date_trip else null end) as SEP,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 10 then date_trip else null end) as OCT,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 11 then date_trip else null end) as NOV,\n" +
            "    count(case when EXTRACT(MONTH FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM')) = 12 then date_trip else null end) as DEC\n" +
            "from _bill\n" +
            "where to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM') >= to_timestamp( cast (EXTRACT(YEAR from now()) as varchar), 'YYYY  HH12:MI:SS PM')\n" +
            "and to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM') <= to_timestamp(cast (EXTRACT(YEAR from now()) as varchar), 'YYYY  HH12:MI:SS PM') + interval '1 year'\n" +
            "GROUP BY  EXTRACT(YEAR FROM to_timestamp(date_trip, 'MM/DD/YYYY  HH12:MI:SS PM'));", nativeQuery = true)
    public T getNumberTripsAllMonths();


}

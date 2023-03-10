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
            "where driver_driver_id = 1\n" +
            "  and Cast (CONVERT(datetime2, date_trip, 101) as DATE) = Cast (DATEADD(day, 0, GETDATE()) as date)", nativeQuery = true)
    List<Bill> getBillsTodayByDriverId(@Param("driverId") int driverId);

    @Query(
            value = "select CAST(sum(total) as Decimal(10, 1))\n" +
                    "from _bill\n" +
                    "where driver_driver_id = 1\n" +
                    "  and Cast (CONVERT(datetime2, date_trip, 101) as DATE) = cast(DATEADD(day, 0, GETDATE()) as date)", nativeQuery = true
    )
    Double getTotalBillsToday(@Param("driverId") int driverId);

    @Query(
            value = "select *\n" +
                    "from _bill\n" +
                    "where driver_driver_id = :driverId\n" +
                    "  and MONTH(date_trip) = MONTH((getdate()))", nativeQuery = true
    )
    List<Bill> getBillsMonthByDriverId(@Param("driverId") int driverId);

    @Query(
            value = "select CAST(sum(total) as Decimal(10,1)) " +
                    "from _bill " +
                    "where driver_driver_id = :driverId " +
                    "and MONTH(date_trip) = MONTH((getdate()))", nativeQuery = true
    )
    Double getTotalBillsMonth(@Param("driverId") int driverId);

    @Query(value = "select\n" +
            "    cast(sum(IIF(month(date_trip) = 1, total, 0)) as decimal(10,2)) as 'JAN',\n" +
            "    cast(sum(IIF(month(date_trip) = 2, total, 0))as decimal(10,2))  as 'FEB',\n" +
            "    cast(sum(IIF(month(date_trip) = 3, total, 0))as decimal(10,2))  as 'MAR',\n" +
            "    cast(sum(IIF(month(date_trip) = 4, total, 0)) as decimal(10,2)) as 'APR',\n" +
            "    cast(sum(IIF(month(date_trip) = 5, total, 0))as decimal(10,2))  as 'MAY',\n" +
            "    cast(sum(IIF(month(date_trip) = 6, total, 0))as decimal(10,2))  as 'JUN',\n" +
            "    cast(sum(IIF(month(date_trip) = 7, total, 0))as decimal(10,2))  as 'JUL',\n" +
            "    cast(sum(IIF(month(date_trip) = 8, total, 0))as decimal(10,2))  as 'AUG',\n" +
            "    cast(sum(IIF(month(date_trip) = 9, total, 0))as decimal(10,2))  as 'SEP',\n" +
            "    cast(sum(IIF(month(date_trip) = 10, total, 0))as decimal(10,2))  as 'OCT',\n" +
            "    cast(sum(IIF(month(date_trip) = 11, total, 0))as decimal(10,2))  as 'NOV',\n" +
            "    cast(sum(IIF(month(date_trip) = 12, total, 0))as decimal(10,2))  as 'DEC'\n" +
            "from _bill\n" +
            "where driver_driver_id = :driverId\n" +
            "  and Cast(CONVERT(datetime2, date_trip, 101) as DATE) >=\n" +
            "      cast(DATEADD(month, 0, '1/1/' + cast(Year(GETDATE()) as varchar)) as date)\n" +
            "  and Cast(CONVERT(datetime2, date_trip, 101) as date) <=\n" +
            "      cast(DATEADD(month, 12, '12/31/' + cast(Year(GETDATE()) as varchar)) as date)\n" +
            "group by YEAR(date_trip)\n", nativeQuery = true)
    public T getTotalBillAllMonthsByDriverId(@Param("driverId") int driverId);

    @Query(value = "select\n" +
            "    count(iif(MONTH(date_trip) = 1, date_trip, null)) as 'JAN',\n" +
            "    count(iif(MONTH(date_trip) = 2, date_trip, null)) as 'FEB',\n" +
            "    count(iif(MONTH(date_trip) = 3, date_trip, null)) as 'MAR',\n" +
            "    count(iif(MONTH(date_trip) = 4, date_trip, null)) as 'APR',\n" +
            "    count(iif(MONTH(date_trip) = 5, date_trip, null)) as 'MAY',\n" +
            "    count(iif(MONTH(date_trip) = 6, date_trip, null)) as 'JUN',\n" +
            "    count(iif(MONTH(date_trip) = 7, date_trip, null)) as 'JUL',\n" +
            "    count(iif(MONTH(date_trip) = 8, date_trip, null)) as 'AUG',\n" +
            "    count(iif(MONTH(date_trip) = 9, date_trip, null)) as 'SEP',\n" +
            "    count(iif(MONTH(date_trip) = 10, date_trip, null)) as 'OCT',\n" +
            "    count(iif(MONTH(date_trip) = 11, date_trip, null)) as 'NOV',\n" +
            "    count(iif(MONTH(date_trip) = 12, date_trip, null)) as 'DEC'\n" +
            "\n" +
            "from _bill\n" +
            "where driver_driver_id = 1\n" +
            "  and Cast(CONVERT(datetime2, date_trip, 101) as DATE) >=\n" +
            "      cast(DATEADD(month, 0, '1/1/' + cast(Year(GETDATE()) as varchar)) as date)\n" +
            "  and Cast(CONVERT(datetime2, date_trip, 101) as date) <=\n" +
            "      cast(DATEADD(month, 12, '12/31/' + cast(Year(GETDATE()) as varchar)) as date)", nativeQuery = true)
    public T getNumberTripsAllMonthsByDriverId(@Param("driverId") int driverId);

    //----------------------------------------------------------------------------------------------------------------
    //Admin
    @Query(value = "select * \n" +
            "from _bill\n" +
            "where Cast (CONVERT(datetime2, date_trip, 101) as DATE) = Cast (DATEADD(day, 0, GETDATE()) as date)", nativeQuery = true)
    List<Bill> getAllBillsToday();

    @Query(
            value = "select CAST(sum(total) as Decimal(10, 1))\n" +
                    "from _bill\n" +
                    "where Cast (CONVERT(datetime2, date_trip, 101) as DATE) = cast(DATEADD(day, 0, GETDATE()) as date)", nativeQuery = true
    )
    Double getTotalAllBillsToday();

    @Query(
            value = "select *\n" +
                    "from _bill\n" +
                    "where MONTH(date_trip) = MONTH((getdate()))", nativeQuery = true
    )
    List<Bill> getBillsMonth();

    @Query(
            value = "select CAST(sum(total) as Decimal(10,1)) " +
                    "from _bill " +
                    "where MONTH(date_trip) = MONTH((getdate()))", nativeQuery = true
    )
    Double getTotalAllBillsMonth();


    @Query(value = "select\n" +
            "    cast(sum(IIF(month(date_trip) = 1, total, 0)) as decimal(10,2)) as 'JAN',\n" +
            "    cast(sum(IIF(month(date_trip) = 2, total, 0))as decimal(10,2))  as 'FEB',\n" +
            "    cast(sum(IIF(month(date_trip) = 3, total, 0))as decimal(10,2))  as 'MAR',\n" +
            "    cast(sum(IIF(month(date_trip) = 4, total, 0)) as decimal(10,2)) as 'APR',\n" +
            "    cast(sum(IIF(month(date_trip) = 5, total, 0))as decimal(10,2))  as 'MAY',\n" +
            "    cast(sum(IIF(month(date_trip) = 6, total, 0))as decimal(10,2))  as 'JUN',\n" +
            "    cast(sum(IIF(month(date_trip) = 7, total, 0))as decimal(10,2))  as 'JUL',\n" +
            "    cast(sum(IIF(month(date_trip) = 8, total, 0))as decimal(10,2))  as 'AUG',\n" +
            "    cast(sum(IIF(month(date_trip) = 9, total, 0))as decimal(10,2))  as 'SEP',\n" +
            "    cast(sum(IIF(month(date_trip) = 10, total, 0))as decimal(10,2))  as 'OCT',\n" +
            "    cast(sum(IIF(month(date_trip) = 11, total, 0))as decimal(10,2))  as 'NOV',\n" +
            "    cast(sum(IIF(month(date_trip) = 12, total, 0))as decimal(10,2))  as 'DEC'\n" +
            "from _bill\n" +
            "where Cast(CONVERT(datetime2, date_trip, 101) as DATE) >=\n" +
            "      cast(DATEADD(month, 0, '1/1/' + cast(Year(GETDATE()) as varchar)) as date)\n" +
            "  and Cast(CONVERT(datetime2, date_trip, 101) as date) <=\n" +
            "      cast(DATEADD(month, 12, '12/31/' + cast(Year(GETDATE()) as varchar)) as date)\n" +
            "group by YEAR(date_trip)\n", nativeQuery = true)
    public T getTotalBillAllMonths();

    @Query(value = "select\n" +
            "    count(iif(MONTH(date_trip) = 1, date_trip, null)) as 'JAN',\n" +
            "    count(iif(MONTH(date_trip) = 2, date_trip, null)) as 'FEB',\n" +
            "    count(iif(MONTH(date_trip) = 3, date_trip, null)) as 'MAR',\n" +
            "    count(iif(MONTH(date_trip) = 4, date_trip, null)) as 'APR',\n" +
            "    count(iif(MONTH(date_trip) = 5, date_trip, null)) as 'MAY',\n" +
            "    count(iif(MONTH(date_trip) = 6, date_trip, null)) as 'JUN',\n" +
            "    count(iif(MONTH(date_trip) = 7, date_trip, null)) as 'JUL',\n" +
            "    count(iif(MONTH(date_trip) = 8, date_trip, null)) as 'AUG',\n" +
            "    count(iif(MONTH(date_trip) = 9, date_trip, null)) as 'SEP',\n" +
            "    count(iif(MONTH(date_trip) = 10, date_trip, null)) as 'OCT',\n" +
            "    count(iif(MONTH(date_trip) = 11, date_trip, null)) as 'NOV',\n" +
            "    count(iif(MONTH(date_trip) = 12, date_trip, null)) as 'DEC'\n" +
            "\n" +
            "from _bill\n" +
            "where Cast(CONVERT(datetime2, date_trip, 101) as DATE) >=\n" +
            "      cast(DATEADD(month, 0, '1/1/' + cast(Year(GETDATE()) as varchar)) as date)\n" +
            "  and Cast(CONVERT(datetime2, date_trip, 101) as date) <=\n" +
            "      cast(DATEADD(month, 12, '12/31/' + cast(Year(GETDATE()) as varchar)) as date)", nativeQuery = true)
    public T getNumberTripsAllMonths();


}

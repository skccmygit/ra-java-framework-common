package kr.co.skcc.oss.com.common.repository;

import jakarta.persistence.QueryHint;
import kr.co.skcc.oss.com.common.domain.dept.Dept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface DeptRepository extends JpaRepository<Dept, String>{

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    List<Dept> findByUseYn(String useYn, Sort sort);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT t " +
                "FROM Dept t " +
                "WHERE ('' = :deptcd OR t.deptcd = :deptcd ) ")
    Dept findByDeptcd(String deptcd);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 천지은"))
    @Query(value= "SELECT t \n" +
                  "  FROM Dept t \n" +
                  " WHERE ('' = :bssmacd OR t.bssmacd = :bssmacd ) \n" +
                  "   AND (t.deptNm LIKE CONCAT('%',:deptNm,'%') )" +
                  "   AND ('' = :useYn OR t.useYn = :useYn ) \n")
    Page<Dept> searchDeptPage(String bssmacd, String deptNm, String useYn, Pageable pageable);

}
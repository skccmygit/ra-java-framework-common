package kr.co.skcc.oss.com.common.repository;

import jakarta.persistence.QueryHint;
import kr.co.skcc.oss.com.common.domain.attachFile.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Modifying
    @Query(value="UPDATE AttachFile t SET t.atacFileStsCd = :flag \n" +
                 " WHERE t.atacFileNo IN (:list) \n" +
                 "   AND t.atacFileStsCd <> 'X' ")
    void updateAtacFileStsCd(List<Long> list, String flag);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value="SELECT t \n" +
                 "  FROM AttachFile t \n" +
                 " WHERE t.atacFileNo IN (:list) \n" +
                 "   AND t.atacFileStsCd <> 'X' ")
    List<AttachFile> findFileList(List<Long> list);
}

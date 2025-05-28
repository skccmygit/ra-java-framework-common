package com.skcc.ra.common.repository;

import com.skcc.ra.common.api.dto.domainDto.BoardFileDto;
import com.skcc.ra.common.domain.board.BoardFile;
import com.skcc.ra.common.domain.board.pk.BoardFilePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import jakarta.persistence.QueryHint;
import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface BoardFileRepository extends JpaRepository<BoardFile, BoardFilePK>, JpaSpecificationExecutor<BoardFile> {
    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT new com.skcc.ra.common.api.dto.domainDto.BoardFileDto(boardFile, attachFile.atacFileNm, attachFile.atacFileTypeCd, attachFile.atacFileSize) " +
                    "FROM BoardFile boardFile " +
                    "LEFT OUTER JOIN AttachFile attachFile ON attachFile.atacFileNo = boardFile.atacFileNo " +
                   "WHERE boardFile.annceNo = :annceNo " +
                   "  AND boardFile.delYn = 'N' ")
    List<BoardFileDto> findByAnnceNo(Long annceNo);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    BoardFile findByAtacFileNo(Long atacFileNo);
}

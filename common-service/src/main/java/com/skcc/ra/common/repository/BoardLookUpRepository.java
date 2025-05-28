package com.skcc.ra.common.repository;

import com.skcc.ra.common.domain.board.BoardLookUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardLookUpRepository extends JpaRepository<BoardLookUp, Long> {

}

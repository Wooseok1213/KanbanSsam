package com.sparta.kanbanssam.column.repository;

import com.sparta.kanbanssam.column.entity.Columns;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ColumnRepository extends JpaRepository<Columns, Long> {



    Long countAllByBoardId(Long boardId);

    boolean existsByBoardIdAndName(Long id, String name);

    // 쓰기 잠금(x-lock)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    //Column 테이블에서 param으로 받은 columnId와 동일한 칼럼 선택
    @Query("SELECT c FROM Columns c WHERE c.id = :columnId")
    Optional<Columns> findByIdWithPessimisticLock(@Param("columnId") Long columnId);
}

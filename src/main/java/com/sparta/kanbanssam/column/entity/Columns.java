package com.sparta.kanbanssam.column.entity;

import com.sparta.kanbanssam.board.entity.Board;
import com.sparta.kanbanssam.card.entity.Card;
import com.sparta.kanbanssam.column.dto.ColumnRequestDto;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "Columns")
@NoArgsConstructor
public class Columns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long orders;

    @OneToMany(mappedBy = "columns",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cardList;

    @Builder
    public Columns(Long id, Board board, String name, Long orders) {
        this.id = id;
        this.board = board;
        this.name=name;
        this.orders = orders;
    }

    /**
     * 컬럼 수정
     * @param requestDto 컬럼 수정 정보
     */
    public void update(ColumnRequestDto requestDto) {
        this.name = requestDto.getName();
    }

    /**
     * 컬럼 순서 이동
     * @param orders
     * @param board
     */
    public void updateOrders(Long orders, Board board) {
        this.board = board;
        this.orders = orders;
        // 로그 추가
        System.out.println("Updated column: " + this.id + ", new order: " + this.orders);
    }
}

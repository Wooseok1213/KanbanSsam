package com.sparta.kanbanssam.card.service;

import com.sparta.kanbanssam.card.entity.Card;
import com.sparta.kanbanssam.card.repository.CardRepository;
import com.sparta.kanbanssam.column.entity.Columns;
import com.sparta.kanbanssam.column.repository.ColumnRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private ColumnRepository columnRepository;

    @Test
    void 카드_순서_변경_테스트() {

        //Given

        //칼럼 생성
        Columns columns1 = new Columns();
        Columns columns2 = new Columns();

        //칼럼 id 설정
        ReflectionTestUtils.setField(columns1, "id", 1L);
        ReflectionTestUtils.setField(columns2, "id", 2L);

        //카드 생성
        Card card1 = new Card();
        Card card2 = new Card();
        Card card3 = new Card();

        //카드 id, 칼럼 설정
        //카드 1: 1(id), column1, 1(orders)
        //카드 2: 2(id), column1, 2(orders)
        //카드 3: 3(id), column2, 1(orders)
        ReflectionTestUtils.setField(card1, "id", 1L);
        ReflectionTestUtils.setField(card2, "id", 2L);
        ReflectionTestUtils.setField(card3, "id", 3L);

        ReflectionTestUtils.setField(card1, "orders", 1L);
        ReflectionTestUtils.setField(card2, "orders", 2L);
        ReflectionTestUtils.setField(card3, "orders", 1L);

        ReflectionTestUtils.setField(card1, "columns", columns1);
        ReflectionTestUtils.setField(card2, "columns", columns1);
        ReflectionTestUtils.setField(card3, "columns", columns2);


        // Mock 설정
        given(columnRepository.findByIdWithPessimisticLock(columns1.getId())).willReturn(Optional.of(columns1));
        given(cardRepository.findByIdWithPessimisticLock(card1.getId())).willReturn(Optional.of(card1));
        given(cardRepository.findByIdWithPessimisticLock(card2.getId())).willReturn(Optional.of(card2));
        given(cardRepository.findByIdWithPessimisticLock(card3.getId())).willReturn(Optional.of(card3));

        //카드 3을 칼럼 1로 이동시키고, card1, card2, card3 순으로 순서를 변경
        List<Long> cardIdList = Arrays.asList(1L, 3L, 2L);

        //When
        cardService.updateCardOrders(1L, cardIdList);

        //Then
        assertEquals(card3.getOrders(), 2L);
        assertEquals(card2.getOrders(), 3L);
    }

    @Test
    void 카드_순서_변경_동시성_테스트() throws InterruptedException {
        //Given
        Columns columns1 = new Columns();
        Columns columns2 = new Columns();

        ReflectionTestUtils.setField(columns1, "id", 1L);
        ReflectionTestUtils.setField(columns2, "id", 2L);

        Card card1 = new Card();
        Card card2 = new Card();
        Card card3 = new Card();

        ReflectionTestUtils.setField(card1, "id", 1L);
        ReflectionTestUtils.setField(card2, "id", 2L);
        ReflectionTestUtils.setField(card3, "id", 3L);

        ReflectionTestUtils.setField(card1, "orders", 1L);
        ReflectionTestUtils.setField(card2, "orders", 2L);
        ReflectionTestUtils.setField(card3, "orders", 1L);

        ReflectionTestUtils.setField(card1, "columns", columns1);
        ReflectionTestUtils.setField(card2, "columns", columns1);
        ReflectionTestUtils.setField(card3, "columns", columns2);

        given(columnRepository.findByIdWithPessimisticLock(columns1.getId())).willReturn(Optional.of(columns1));
        given(cardRepository.findByIdWithPessimisticLock(card1.getId())).willReturn(Optional.of(card1));
        given(cardRepository.findByIdWithPessimisticLock(card2.getId())).willReturn(Optional.of(card2));
        given(cardRepository.findByIdWithPessimisticLock(card3.getId())).willReturn(Optional.of(card3));

        List<Long> cardIdList = Arrays.asList(1L, 3L, 2L);

        //동시성 테스트를 위한 스레드 풀과 CountDownLatch 설정

        //동시에 실행할 스레드의 수를 2로 설정
        int threadCount = 2;
        //스레드 개수가 2인 고정된 크기의 스레드 풀 생성
        //ExecutorService을 통해 2개의 스레드가 동시에 작업을 수행하게 된다.
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        //하나 이상의 스레드가 다른 스레드의 작업이 완료될 때까지 기다리도록 도와주는 동기화 도구
        CountDownLatch latch = new CountDownLatch(threadCount);

        Runnable task = () -> {
            try {
                cardService.updateCardOrders(1L, cardIdList);
            } finally {
                latch.countDown();
                // 현재 스레드가 작업을 완료했음을 알림
                // 메소드 호출될 때마다 카운트 1씩 감소
            }
        };

        //When
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(task); //task 수행(updateCardOrders 수행)
        }

        latch.await(); //카운트 0될때까지 기다림
        executorService.shutdown(); // 종료

        //Then
        assertEquals(card3.getOrders(), 2L);
        assertEquals(card2.getOrders(), 3L);
    }

}
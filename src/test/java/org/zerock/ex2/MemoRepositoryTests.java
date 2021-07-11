package org.zerock.ex2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.zerock.ex2.entity.Memo;
import org.zerock.ex2.repository.MemoRepository;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
        //MemoRepository 인터페이스 타입의 실제 객체가 어떤 것인지 확인
    }

    @Test
    public void testInsertDummies() {
        IntStream.range(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..."+i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect() {
        //데이터베이스에 존재하는 mno
        Long mno = 1L;

        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("==========================");
        if(result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Test
    @Transactional
    public void testSelect2(){
        Long mno = 2L;

        Memo result = memoRepository.getById(mno);
        System.out.println("==========================");
        System.out.println(result);
    }

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("update Text2").build();
        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long mno = 100L;
        memoRepository.deleteById(mno);
    }
}

package org.zerock.ex2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.zerock.ex2.entity.Memo;
import org.zerock.ex2.repository.MemoRepository;

import java.util.List;
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

    @Test
    public void testPageDefault() {
        //1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);
        System.out.println("===================================");
        System.out.println("Total Pages: "+result.getTotalPages()); //총 몇 페이지
        System.out.println("Total Count: "+result.getTotalElements()); //전체 개수
        System.out.println("Page Number: "+result.getNumber()); //현재 페이지 번호(0부터 시작)
        System.out.println("Page Size: "+result.getSize()); //페이지당 데이터 개수
        System.out.println("has next page?: "+result.hasNext()); //다음 페이지 존재 여부
        System.out.println("is first page?: "+result.isFirst()); //시작 페이지(0) 여부

        result.getContent().forEach(System.out::println);
    }

    @Test
    public void testSort() {
        Sort sort1 = Sort.by("mno").descending();
        Pageable pageable = PageRequest.of(0, 10, sort1);
        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(System.out::println);
    }

    @Test
    public void testSort2() {
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText");
        Sort sortAll = sort1.and(sort2);

        Pageable pageable = PageRequest.of(0, 10, sortAll);
        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(System.out::println);
    }

    @Test
    public void testQueryMethods() {
        List<Memo> result = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);
        for (Memo memo : result) {
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethods2() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);
        for (Memo memo : result) {
            System.out.println(memo);
        }
    }

    @Test
    @Transactional
    @Commit
    public void testDeleteQueryMethods() {
        memoRepository.deleteMemoByMnoLessThan(10L);
    }
}

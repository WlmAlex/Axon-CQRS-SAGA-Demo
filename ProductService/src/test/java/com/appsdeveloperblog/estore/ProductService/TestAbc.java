package com.appsdeveloperblog.estore.ProductService;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TestAbc {

    public static void main(String[] args) {

        Student student = Student.builder().books(Collections.emptyList()).build();
        System.out.println(student.getBooks());
        List<Book> bookList = Optional.ofNullable(student.getBooks()).orElseGet(null);
        if (CollectionUtils.isEmpty(bookList)) {
            System.err.println("abc");
        }

    }
}

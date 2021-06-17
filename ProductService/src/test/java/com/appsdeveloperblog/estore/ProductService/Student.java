package com.appsdeveloperblog.estore.ProductService;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Student {

  private List<Book> books = new ArrayList<>();
}
